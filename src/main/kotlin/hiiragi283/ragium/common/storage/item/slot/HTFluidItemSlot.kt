package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.HTFluidCapabilities
import hiiragi283.ragium.api.storage.capability.tankRange
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.inventory.HTContainerItemSlot
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import java.util.function.Predicate

/**
 * @see mekanism.common.inventory.slot.FluidInventorySlot
 * @see mekanism.common.inventory.slot.IFluidHandlerSlot
 */
open class HTFluidItemSlot protected constructor(
    protected val tank: HTFluidTank,
    canExtract: Predicate<ImmutableItemStack>,
    canInsert: Predicate<ImmutableItemStack>,
    listener: HTContentListener?,
    x: Int,
    y: Int,
) : HTBasicItemSlot(
        RagiumConst.ABSOLUTE_MAX_STACK_SIZE,
        canExtract,
        canInsert,
        HTPredicates.alwaysTrue(),
        listener,
        x,
        y,
        HTContainerItemSlot.Type.BOTH,
    ) {
    companion object {
        @JvmStatic
        fun fillPredicate(tank: HTFluidTank): Predicate<ImmutableItemStack> = Predicate { stack: ImmutableItemStack ->
            val handler: IFluidHandlerItem = tryGetHandler(stack) ?: return@Predicate false
            for (i: Int in handler.tankRange) {
                val stackIn: ImmutableFluidStack? = handler.getFluidInTank(i).toImmutable()
                if (stackIn != null) {
                    val remainder: Int =
                        tank.insert(stackIn, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)?.amount() ?: 0
                    if (remainder < stackIn.amount()) {
                        return@Predicate true
                    }
                }
            }
            false
        }

        @JvmStatic
        fun tryGetHandler(stack: ImmutableItemStack): IFluidHandlerItem? {
            if (stack.amount() > 1 && HTFluidCapabilities.getCapability(stack) == null) {
                return null
            }
            val stack1: ImmutableItemStack = when (stack.amount()) {
                1 -> stack.copyWithAmount(1)
                else -> stack
            } ?: return null
            return HTFluidCapabilities.getCapability(stack1)
        }

        @JvmStatic
        fun create(
            tank: HTFluidTank,
            listener: HTContentListener?,
            x: Int,
            y: Int,
        ): HTFluidItemSlot = create(tank, listener, x, y, HTPredicates.alwaysFalse(), HTFluidCapabilities::hasCapability)

        @JvmStatic
        private fun create(
            tank: HTFluidTank,
            listener: HTContentListener?,
            x: Int,
            y: Int,
            canExtract: Predicate<ImmutableItemStack>,
            canInsert: Predicate<ImmutableItemStack>,
        ): HTFluidItemSlot = HTFluidItemSlot(tank, canExtract, canInsert, listener, x, y)
    }
}
