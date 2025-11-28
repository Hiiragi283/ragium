package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.world.getRangedAABB
import hiiragi283.ragium.common.entity.HTThrownCaptureEgg
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemEntitySlot
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
import hiiragi283.ragium.common.util.HTItemDropHelper
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.FluidTags
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.Vec3

class HTItemCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity.Tickable(RagiumBlocks.ITEM_COLLECTOR, pos, state) {
    lateinit var inputSlot: HTItemStackSlot
        private set
    lateinit var outputSlots: List<HTItemStackSlot>
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = builder.addSlot(
            HTSlotInfo.CATALYST,
            HTItemStackSlot.create(
                listener,
                HTSlotHelper.getSlotPosX(1.5),
                HTSlotHelper.getSlotPosY(1),
                canExtract = HTPredicates.notExternal(),
                canInsert = HTPredicates.notExternal(),
            ),
        )
        // outputs
        outputSlots = (0..<12).map { i: Int ->
            builder.addSlot(
                HTSlotInfo.OUTPUT,
                HTOutputItemStackSlot.create(
                    listener,
                    HTSlotHelper.getSlotPosX(3 + i % 4),
                    HTSlotHelper.getSlotPosY(i / 4),
                ),
            )
        }
    }

    //    Ticking    //

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = when {
        hasUpgrade(RagiumItems.FISHING_UPGRADE) -> collectFish(level, pos)
        hasUpgrade(RagiumItems.MOB_CAPTURE_UPGRADE) -> collectMobs(level, pos)
        else -> collectItem(level, pos)
    }

    private fun collectFish(level: ServerLevel, pos: BlockPos): Boolean {
        val fishingRod: ItemStack = inputSlot.getStack()?.unwrap() ?: ItemStack(Items.FISHING_ROD)
        val checkWater: Boolean = BlockPos
            .betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 0, 1))
            .all { it == pos || level.getFluidState(it).`is`(FluidTags.WATER) }
        if (!checkWater) return false
        // 釣りの結果を生成する
        val owner: ServerPlayer? = getOwnerPlayer(level)
        val params: LootParams = LootParams
            .Builder(level)
            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
            .withParameter(LootContextParams.TOOL, fishingRod)
            .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, owner)
            .withLuck(owner?.luck ?: 0f)
            .create(LootContextParamSets.FISHING)
        val lootTable: LootTable = level.server.reloadableRegistries().getLootTable(BuiltInLootTables.FISHING)
        val drops: List<ItemStack> = lootTable.getRandomItems(params)
        // 釣りの結果をスロットに入れる
        for (stack: ItemStack in drops) {
            val remainder: ImmutableItemStack? = HTStackSlotHelper.insertStacks(outputSlots, stack.toImmutable(), HTStorageAction.EXECUTE)
            HTItemDropHelper.dropStackAt(level, pos.above(), remainder)
            fishingRod.hurtAndBreak(1, level, null as LivingEntity?) { inputSlot.setStackUnchecked(null) }
            inputSlot.setStackUnchecked(fishingRod.toImmutable())
        }
        level.playSound(null, pos, SoundEvents.FISHING_BOBBER_SPLASH, SoundSource.BLOCKS, 0.5f, 1.0f)
        return true
    }

    private fun collectMobs(level: ServerLevel, pos: BlockPos): Boolean {
        if (!RagiumItems.ELDRITCH_EGG.isOf(inputSlot.getStack())) return false
        // 範囲内のエンティティを取得する
        val entities: List<LivingEntity> = level.getEntitiesOfClass(
            LivingEntity::class.java,
            pos.center.getRangedAABB(RagiumConfig.COMMON.deviceCollectorEntityRange.asDouble),
        )
        if (entities.isEmpty()) return false
        // それぞれのエンティティについて捕獲を行う
        for (entity: LivingEntity in entities) {
            val eggStack: ImmutableItemStack = HTThrownCaptureEgg.getCapturedStack(entity) ?: continue
            for (slot: HTItemStackSlot in outputSlots) {
                if (slot.insert(eggStack, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) == null) {
                    // スポーンエッグをスロットに入れる
                    slot.insert(eggStack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                    // 対象を消す
                    entity.discard()
                    // Capture Eggを減らす
                    inputSlot.extract(1, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                    return true
                }
            }
        }
        return false
    }

    private fun collectItem(level: ServerLevel, pos: BlockPos): Boolean {
        // アップグレードにマグネットが入っている場合のみ機能する
        // if (!upgrades.hasStack { stack: ItemStack -> stack.`is`(RagiumItems.RAGI_MAGNET) }) return TriState.FALSE
        // 範囲内のItem Entityを取得する
        val itemEntities: List<ItemEntity> = level.getEntities(
            EntityType.ITEM,
            pos.center.getRangedAABB(RagiumConfig.COMMON.deviceCollectorEntityRange.asDouble),
            EntitySelector.NO_SPECTATORS,
        )
        if (itemEntities.isEmpty()) return false
        // それぞれのItem Entityに対して回収を行う
        itemEntities
            .asSequence()
            .filter(ItemEntity::isAlive)
            .filterNot { entity: ItemEntity -> entity.persistentData.getBoolean("PreventRemoteMovement") }
            .filterNot(ItemEntity::hasPickUpDelay)
            .map(::HTItemEntitySlot)
            .forEach { entitySlot: HTItemSlot ->
                for (slot: HTItemSlot in outputSlots) {
                    HTStackSlotHelper.moveStack(entitySlot, slot)
                }
            }
        return true
    }
}
