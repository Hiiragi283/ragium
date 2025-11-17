package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTCapturerBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity.Tickable(blockHolder, pos, state) {
    lateinit var inputSlot: HTItemStackSlot
        private set
    lateinit var outputSlots: List<HTItemStackSlot>
        private set

    final override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTItemStackSlot.input(
                listener,
                HTSlotHelper.getSlotPosX(2),
                HTSlotHelper.getSlotPosY(1),
                filter = ::inputFilter,
            ),
        )
        // outputs
        outputSlots = (0..<15).map { i: Int ->
            builder.addSlot(
                HTSlotInfo.OUTPUT,
                HTOutputItemStackSlot.create(
                    listener,
                    HTSlotHelper.getSlotPosX(4 + i % 5),
                    HTSlotHelper.getSlotPosY(i / 5),
                ),
            )
        }
    }

    protected abstract fun inputFilter(stack: ImmutableItemStack): Boolean
}
