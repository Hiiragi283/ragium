package hiiragi283.ragium.common.storage.fluid.tank

import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.util.HTContentListener
import java.util.function.BiPredicate
import java.util.function.IntSupplier
import java.util.function.Predicate

/**
 * @see mekanism.common.capabilities.fluid.VariableCapacityFluidTank
 */
class HTVariableFluidTank(
    private val capacitySupplier: IntSupplier,
    canExtract: BiPredicate<ImmutableFluidStack, HTStorageAccess>,
    canInsert: BiPredicate<ImmutableFluidStack, HTStorageAccess>,
    filter: Predicate<ImmutableFluidStack>,
    listener: HTContentListener?,
) : HTBasicFluidTank(capacitySupplier.asInt, canExtract, canInsert, filter, listener) {
    companion object {
        @JvmStatic
        fun create(listener: HTContentListener?, capacity: IntSupplier): HTBasicFluidTank = HTVariableFluidTank(
            capacity,
            HTPredicates.alwaysTrueBi(),
            HTPredicates.alwaysTrueBi(),
            HTPredicates.alwaysTrue(),
            listener,
        )

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            capacity: IntSupplier,
            canInsert: Predicate<ImmutableFluidStack> = HTPredicates.alwaysTrue(),
            filter: Predicate<ImmutableFluidStack> = canInsert,
        ): HTBasicFluidTank = HTVariableFluidTank(
            capacity,
            HTPredicates.notExternal(),
            { stack: ImmutableFluidStack, _ -> canInsert.test(stack) },
            filter,
            listener,
        )

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: IntSupplier): HTBasicFluidTank = HTVariableFluidTank(
            capacity,
            HTPredicates.alwaysTrueBi(),
            HTPredicates.internalOnly(),
            HTPredicates.alwaysTrue(),
            listener,
        )
    }

    override fun getCapacity(stack: ImmutableFluidStack?): Int = capacitySupplier.asInt
}
