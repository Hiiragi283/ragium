package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.extension.getRangedAABB
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemEntitySlot
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTItemBufferBlockEntity(pos: BlockPos, state: BlockState) : HTDeviceBlockEntity.Tickable(RagiumBlocks.ITEM_BUFFER, pos, state) {
    private lateinit var slots: List<HTItemSlot>

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        slots = (0..8).map { index: Int ->
            builder.addSlot(
                HTSlotInfo.BOTH,
                HTItemStackSlot.create(listener, HTSlotHelper.getSlotPosX(3 + index % 3), HTSlotHelper.getSlotPosY(index / 3)),
            )
        }
    }

    override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        HTStackSlotHelper.calculateRedstoneLevel(getItemSlots(getItemSideFor()))

    //    Ticking    //

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
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
                for (slot: HTItemSlot in slots) {
                    HTStackSlotHelper.moveStack(entitySlot, slot)
                }
            }
        return true
    }
}
