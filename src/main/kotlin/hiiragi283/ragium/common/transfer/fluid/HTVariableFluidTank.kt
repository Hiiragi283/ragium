package hiiragi283.ragium.common.transfer.fluid

import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.HTTransferPredicates
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import net.neoforged.neoforge.transfer.fluid.FluidResource
import java.util.function.BiPredicate
import java.util.function.IntSupplier
import java.util.function.Predicate

class HTVariableFluidTank(
    private val capacitySupplier: IntSupplier,
    canExtract: BiPredicate<FluidResource, HTTransferAccess>,
    canInsert: BiPredicate<FluidResource, HTTransferAccess>,
    filter: Predicate<FluidResource>,
    listener: Runnable?
) : HTBasicFluidTank(capacitySupplier.asInt, canExtract, canInsert, filter, listener) {
    companion object {
        @JvmStatic
        fun create(
            capacity: IntSupplier,
            listener: Runnable?,
            canExtract: BiPredicate<FluidResource, HTTransferAccess> = HTTransferPredicates.alwaysTrueBi(),
            canInsert: BiPredicate<FluidResource, HTTransferAccess> = HTTransferPredicates.alwaysTrueBi(),
            filter: Predicate<FluidResource> = HTTransferPredicates.alwaysTrue()
        ): HTVariableFluidTank = HTVariableFluidTank(capacity, canExtract, canInsert, filter, listener)

        @JvmStatic
        fun input(
            capacity: IntSupplier,
            listener: Runnable?,
            canInsert: Predicate<FluidResource> = HTTransferPredicates.alwaysTrue(),
            filter: Predicate<FluidResource> = canInsert
        ): HTVariableFluidTank = create(
            capacity,
            listener,
            HTTransferPredicates.notExternal(),
            { resource: FluidResource, _ -> canInsert.test(resource) },
            filter
        )

        @JvmStatic
        fun output(capacity: IntSupplier, listener: Runnable?): HTVariableFluidTank =
            create(capacity, listener, canInsert = HTTransferPredicates.internalOnly())
    }

    override fun getCapacity(resource: FluidResource): Int = capacitySupplier.asInt
}
