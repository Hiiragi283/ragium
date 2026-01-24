package hiiragi283.ragium.common.storge.fluid

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import java.util.function.BiPredicate
import java.util.function.IntSupplier
import java.util.function.Predicate

/**
 * @see mekanism.common.capabilities.fluid.VariableCapacityFluidTank
 */
class HTVariableFluidTank(
    private val capacitySupplier: IntSupplier,
    canExtract: BiPredicate<HTFluidResourceType, HTStorageAccess>,
    canInsert: BiPredicate<HTFluidResourceType, HTStorageAccess>,
    filter: Predicate<HTFluidResourceType>,
    listener: HTContentListener?,
) : HTBasicFluidTank(capacitySupplier.asInt, canExtract, canInsert, filter, listener) {
    companion object {
        @JvmStatic
        fun create(listener: HTContentListener?, capacity: IntSupplier): HTBasicFluidTank = HTVariableFluidTank(
            capacity,
            HTStoragePredicates.alwaysTrueBi(),
            HTStoragePredicates.alwaysTrueBi(),
            HTStoragePredicates.alwaysTrue(),
            listener,
        )

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            capacity: IntSupplier,
            canInsert: Predicate<HTFluidResourceType> = HTStoragePredicates.alwaysTrue(),
            filter: Predicate<HTFluidResourceType> = canInsert,
        ): HTBasicFluidTank = HTVariableFluidTank(
            capacity,
            HTStoragePredicates.notExternal(),
            { stack: HTFluidResourceType, _ -> canInsert.test(stack) },
            filter,
            listener,
        )

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: IntSupplier): HTBasicFluidTank = HTVariableFluidTank(
            capacity,
            HTStoragePredicates.alwaysTrueBi(),
            HTStoragePredicates.internalOnly(),
            HTStoragePredicates.alwaysTrue(),
            listener,
        )
    }

    override fun getCapacity(resource: HTFluidResourceType?): Int = capacitySupplier.asInt
}
