package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.extension.getRangedAABB
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTDeviceVariant
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.items.ItemHandlerHelper

class HTItemBufferBlockEntity(pos: BlockPos, state: BlockState) : HTDeviceBlockEntity(HTDeviceVariant.ITEM_BUFFER, pos, state) {
    private lateinit var slots: List<HTItemSlot>

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        slots = (0..8).map { index: Int ->
            HTItemStackSlot.create(listener, HTSlotHelper.getSlotPosX(3 + index % 3), HTSlotHelper.getSlotPosY(index / 3))
        }
        return HTSimpleItemSlotHolder(null, slots, listOf())
    }

    override fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult {
        if (isClientSide) RagiumMenuTypes.ITEM_BUFFER.openMenu(player, name, this, ::writeExtraContainerData)
        return InteractionResult.sidedSuccess(isClientSide)
    }

    override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        ItemHandlerHelper.calcRedstoneFromInventory(getItemHandler(null))

    //    Ticking    //

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // アップグレードにマグネットが入っている場合のみ機能する
        // if (!upgrades.hasStack { stack: ItemStack -> stack.`is`(RagiumItems.RAGI_MAGNET) }) return TriState.FALSE
        // 範囲内のItem Entityを取得する
        val itemEntities: List<ItemEntity> = level.getEntitiesOfClass(
            ItemEntity::class.java,
            blockPos.getRangedAABB(RagiumConfig.CONFIG.deviceCollectorEntityRange.asDouble),
        )
        if (itemEntities.isEmpty()) return false
        // それぞれのItem Entityに対して回収を行う
        for (entity: ItemEntity in itemEntities) {
            // IEのコンベヤ上にいるアイテムは無視する
            if (entity.persistentData.getBoolean("PreventRemoteMovement")) continue
            // 回収までのディレイが残っている場合はスキップ
            if (entity.hasPickUpDelay()) continue
            // 各スロットに対して搬入操作を行う
            val previous: ItemStack = entity.item.copy()
            var remainder: ItemStack = previous
            for (slot: HTItemSlot in slots) {
                remainder = slot.insertItem(remainder, false, HTStorageAccess.INTERNAl)
                if (remainder.isEmpty) {
                    entity.discard()
                    break
                }
            }
            if (remainder != previous) {
                entity.item = remainder
            }
        }
        return true
    }
}
