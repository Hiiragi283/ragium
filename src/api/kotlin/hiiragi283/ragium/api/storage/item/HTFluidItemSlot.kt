package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.HTFluidCapabilities
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

/**
 * @see mekanism.common.inventory.slot.IFluidHandlerSlot
 * @see me.desht.pneumaticcraft.common.block.entity.AbstractPneumaticCraftBlockEntity.processFluidItem
 */
interface HTFluidItemSlot : HTItemSlot {
    fun getFluidTank(): HTFluidTank

    companion object {
        fun moveFluid(tank: HTFluidTank, moveFrom: HTItemSlot, moveTo: HTItemSlot): Boolean {
            val stackIn: ImmutableItemStack = moveFrom.getStack() ?: return false
            val handler = HTFluidHandler { listOf(tank) }

            val handlerIn: IFluidHandlerItem = HTFluidCapabilities.getCapability(stackIn) ?: return false
            var containerIn: ImmutableItemStack? = handlerIn.container.toImmutable()
            // 最初に移動できる液体を取得
            val firstStackIn: FluidStack = handlerIn.drain(Int.MAX_VALUE, HTStorageAction.SIMULATE.toFluid())
            // 液体が有効，かつ搬出先が受け入れ可能な場合
            if (!firstStackIn.isEmpty && (moveTo.getStack() == null || moveTo.isSameStack(containerIn))) {
                // 現在のコンテナの個数が1の場合だけ処理を行う
                if (stackIn.amount() != 1) return false
                val transferred: ImmutableFluidStack = FluidUtil
                    .tryFluidTransfer(
                        handler,
                        handlerIn,
                        firstStackIn.amount,
                        false,
                    ).toImmutable() ?: return false
                // 移動可能な量が最初に取り出した値と同じ場合
                if (transferred.amount() == firstStackIn.amount) {
                    handlerIn.drain(transferred.amount(), HTStorageAction.EXECUTE.toFluid())
                    containerIn = handlerIn.container.toImmutable()
                    if (containerIn != null) {
                        val excessStack: ImmutableItemStack? = moveTo.insert(
                            containerIn,
                            HTStorageAction.SIMULATE,
                            HTStorageAccess.INTERNAL,
                        )
                        if (excessStack == null) {
                            moveFrom.extract(1, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                            moveTo.insert(containerIn, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                            tank.insert(transferred, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                            return true
                        }
                    }
                } else {
                    containerIn = handlerIn.container.toImmutable()?.copy()
                    if (containerIn != null) {
                        moveFrom.extract(1, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                        moveTo.insert(containerIn, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                        return true
                    }
                }
            } else if (moveTo.getStack() == null) {
                val stackInCopied: ImmutableItemStack = stackIn.copyWithAmount(1) ?: return false
                HTFluidCapabilities.getCapability(stackInCopied)?.let { handlerItem: IFluidHandlerItem ->
                    val transferred: FluidStack = FluidUtil.tryFluidTransfer(handlerItem, handler, Int.MAX_VALUE, true)
                    if (!transferred.isEmpty) {
                        moveFrom.extract(1, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                        moveTo.insertItem(handlerItem.container, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                    }
                }
            }
            return false
        }
    }
}
