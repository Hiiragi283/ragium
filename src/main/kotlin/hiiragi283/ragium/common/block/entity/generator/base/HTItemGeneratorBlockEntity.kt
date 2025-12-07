package hiiragi283.ragium.common.block.entity.generator.base

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemSlot
import hiiragi283.ragium.common.util.HTItemDropHelper
import hiiragi283.ragium.common.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTItemGeneratorBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTFuelGeneratorBlockEntity(blockHolder, pos, state) {
    lateinit var inputSlot: HTBasicItemSlot
        private set
    lateinit var remainderSlot: HTBasicItemSlot
        private set

    final override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot =
            singleInput(
                builder,
                listener,
                canInsert = ::canInsertFuel,
            )
        // output
        remainderSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemSlot.create(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)),
        )
    }

    protected abstract fun canInsertFuel(stack: ImmutableItemStack): Boolean

    final override fun onFuelUpdated(level: ServerLevel, pos: BlockPos, isSucceeded: Boolean) {
        if (isSucceeded) {
            // インプットを減らす, 返却物がある場合は移動
            consumeFuel(level, pos)
            // SEを鳴らす
            playSound(level, pos)
        }
    }

    private fun consumeFuel(level: ServerLevel, pos: BlockPos) {
        HTStackSlotHelper.shrinkItemStack(
            inputSlot,
            ::getRemainder,
            { stack: ImmutableItemStack ->
                val remainder: ImmutableItemStack? = remainderSlot.insert(stack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                HTItemDropHelper.dropStackAt(level, pos, remainder)
            },
            { 1 },
            HTStorageAction.EXECUTE,
        )
    }
    
    protected abstract fun getRemainder(stack: ImmutableItemStack): ItemStack

    protected abstract fun playSound(level: ServerLevel, pos: BlockPos)
}
