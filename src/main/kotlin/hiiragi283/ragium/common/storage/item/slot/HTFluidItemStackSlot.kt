package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.inventory.HTContainerItemSlot
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.storage.capability.tankRange
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.getFluidStack
import hiiragi283.ragium.api.storage.fluid.insertFluid
import hiiragi283.ragium.api.storage.item.HTFluidItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import java.util.function.Predicate

/**
 * @see [mekanism.common.inventory.slot.FluidInventorySlot]
 */
open class HTFluidItemStackSlot protected constructor(
    protected val tank: HTFluidTank,
    canExtract: Predicate<ImmutableItemStack>,
    canInsert: Predicate<ImmutableItemStack>,
    listener: HTContentListener?,
    x: Int,
    y: Int,
) : HTItemStackSlot(
        RagiumConst.ABSOLUTE_MAX_STACK_SIZE,
        canExtract,
        canInsert,
        ImmutableStack.alwaysTrue(),
        listener,
        x,
        y,
        HTContainerItemSlot.Type.BOTH,
    ),
    HTFluidItemSlot {
    companion object {
        @JvmStatic
        fun fillPredicate(tank: HTFluidTank): Predicate<ImmutableItemStack> = Predicate { stack: ImmutableItemStack ->
            val handler: IFluidHandlerItem = tryGetHandler(stack) ?: return@Predicate false
            for (i: Int in handler.tankRange) {
                val stackIn: FluidStack = handler.getFluidInTank(i)
                val bool1: Boolean = !stackIn.isEmpty
                val bool2: Boolean =
                    tank.insertFluid(stackIn, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL).amount < stack.amountAsInt()
                if (bool1 && bool2) {
                    return@Predicate true
                }
            }
            false
        }

        @JvmStatic
        fun tryGetHandler(stack: ImmutableItemStack): IFluidHandlerItem? {
            if (stack.amountAsInt() > 1 && RagiumCapabilities.FLUID.getCapability(stack) == null) {
                return null
            }
            val stack1: ImmutableItemStack = when (stack.amountAsInt()) {
                1 -> stack.copyWithAmount(1)
                else -> stack
            }
            return RagiumCapabilities.FLUID.getCapability(stack1)
        }

        @JvmStatic
        fun fill(
            tank: HTFluidTank,
            listener: HTContentListener?,
            x: Int,
            y: Int,
        ): HTFluidItemStackSlot = create(tank, listener, x, y, ImmutableStack.alwaysFalse(), fillPredicate(tank))

        @JvmStatic
        fun drain(
            tank: HTFluidTank,
            listener: HTContentListener?,
            x: Int,
            y: Int,
        ): HTFluidItemStackSlot = create(tank, listener, x, y, ImmutableStack.alwaysFalse()) { stack: ImmutableItemStack ->
            val handler: IFluidHandlerItem = RagiumCapabilities.FLUID.getCapability(stack) ?: return@create false
            if (tank.isEmpty()) {
                for (i: Int in handler.tankRange) {
                    if (handler.getFluidInTank(i).amount < handler.getTankCapacity(i)) {
                        return@create true
                    }
                }
            }
            handler.fill(tank.getFluidStack(), HTStorageAction.SIMULATE.toFluid()) > 0
        }

        @JvmStatic
        private fun create(
            tank: HTFluidTank,
            listener: HTContentListener?,
            x: Int,
            y: Int,
            canExtract: Predicate<ImmutableItemStack>,
            canInsert: Predicate<ImmutableItemStack>,
        ): HTFluidItemStackSlot = HTFluidItemStackSlot(tank, canExtract, canInsert, listener, x, y)
    }

    final override fun getFluidTank(): HTFluidTank = tank
}
