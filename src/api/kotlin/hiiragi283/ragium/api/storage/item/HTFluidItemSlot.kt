package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStackView
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

/**
 * @see [mekanism.common.inventory.slot.IFluidHandlerSlot]
 * @see [me.desht.pneumaticcraft.common.block.entity.AbstractPneumaticCraftBlockEntity.processFluidItem]
 */
interface HTFluidItemSlot :
    HTItemSlot,
    HTStackView.Mutable<ImmutableItemStack> {
    fun getFluidTank(): HTFluidTank

    companion object {
        fun <INPUT> moveFluid(
            tank: HTFluidTank,
            moveFrom: INPUT,
            moveTo: HTItemSlot.Mutable,
        ): Boolean where INPUT : HTItemSlot, INPUT : HTStackView.Mutable<ImmutableItemStack> {
            val stackIn: ImmutableItemStack = moveFrom.getStack()
            val handler: HTFluidHandler = tank.toSingleHandler()

            val handlerIn: IFluidHandlerItem = RagiumCapabilities.FLUID.getCapability(stackIn) ?: return false
            var containerIn: ImmutableItemStack = handlerIn.container.toImmutable()
            // 最初に移動できる液体を取得
            val firstStackIn: FluidStack = handlerIn.drain(Int.MAX_VALUE, HTStorageAction.SIMULATE.toFluid())
            // 液体が有効，かつ搬出先が受け入れ可能な場合
            if (!firstStackIn.isEmpty && (moveTo.isEmpty() || moveTo.isSameStack(containerIn))) {
                // 現在のコンテナの個数が1の場合だけ処理を行う
                if (stackIn.amountAsInt() != 1) return false
                val transferred: FluidStack = FluidUtil.tryFluidTransfer(
                    handler,
                    handlerIn,
                    firstStackIn.amount,
                    false,
                )
                // 移動可能な量が最初に取り出した値と同じ場合
                if (transferred.amount == firstStackIn.amount) {
                    handlerIn.drain(transferred.amount, HTStorageAction.EXECUTE.toFluid())
                    containerIn = handlerIn.container.toImmutable()
                    val excessStack: ImmutableItemStack = moveTo.insert(containerIn, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
                    if (excessStack.isEmpty()) {
                        moveFrom.shrinkStack(1, HTStorageAction.EXECUTE)
                        moveTo.insert(containerIn, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                        tank.insert(transferred.toImmutable(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                        return true
                    }
                } else if (!transferred.isEmpty) {
                    moveFrom.shrinkStack(1, HTStorageAction.EXECUTE)
                    moveTo.insert(handlerIn.container.toImmutable().copy(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                    return true
                }
            } else if (moveTo.isEmpty()) {
                val stackInCopied: ImmutableItemStack = stackIn.copyWithAmount(1)
                RagiumCapabilities.FLUID.getCapability(stackInCopied)?.let { handlerItem: IFluidHandlerItem ->
                    val transferred: FluidStack = FluidUtil.tryFluidTransfer(handlerItem, handler, Int.MAX_VALUE, true)
                    if (!transferred.isEmpty) {
                        moveFrom.shrinkStack(1, HTStorageAction.EXECUTE)
                        moveTo.insertItem(handlerItem.container, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                    }
                }
            }
            return false
        }
    }
}
