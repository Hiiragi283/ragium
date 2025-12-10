package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.util.HTItemDropHelper
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTOpenCrateBlockEntity(pos: BlockPos, state: BlockState) : HTConfigurableBlockEntity(RagiumBlocks.OPEN_CRATE, pos, state) {
    private lateinit var slot: HTItemSlot

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        slot = builder.addSlot(HTSlotInfo.INPUT, OpenItemSlot())
    }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = false

    private inner class OpenItemSlot :
        HTItemSlot,
        HTContentListener.Empty,
        HTValueSerializable.Empty {
        override fun isValid(stack: ImmutableItemStack): Boolean = true

        override fun insert(stack: ImmutableItemStack?, action: HTStorageAction, access: HTStorageAccess): ImmutableItemStack? {
            val level: Level = this@HTOpenCrateBlockEntity.level ?: return stack
            val pos: BlockPos = this@HTOpenCrateBlockEntity.blockPos
            if (action.execute) {
                HTItemDropHelper.dropStackAt(level, pos.below(), stack)
            }
            return null
        }

        // 搬出はサポートしない
        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): ImmutableItemStack? = null

        override fun getStack(): ImmutableItemStack? = null

        override fun getCapacity(stack: ImmutableItemStack?): Int = Int.MAX_VALUE
    }
}
