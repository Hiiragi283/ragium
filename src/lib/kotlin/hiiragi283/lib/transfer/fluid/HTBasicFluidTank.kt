package hiiragi283.lib.transfer.fluid

import hiiragi283.lib.HTConstants
import hiiragi283.lib.transfer.HTSnapshotStackResourceSlot
import hiiragi283.lib.transfer.HTStackResourceHelper
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.HTTransferPredicates
import hiiragi283.lib.transfer.HTTransferValidators
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * [液体][FluidResource]向けの[HTSnapshotStackResourceSlot]の実装クラスです。
 *
 * 参照 : [Mekanism - BasicFluidTank](https://github.com/mekanism/Mekanism/blob/26.3/src/main/java/mekanism/common/capabilities/fluid/BasicFluidTank.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
open class HTBasicFluidTank(
    capacity: Int,
    canExtract: BiPredicate<FluidResource, HTTransferAccess>,
    canInsert: BiPredicate<FluidResource, HTTransferAccess>,
    filter: Predicate<FluidResource>,
    listener: Runnable?,
) : HTSnapshotStackResourceSlot.Basic<FluidStack, FluidResource>(capacity, canExtract, canInsert, filter, listener),
    HTFluidStackTank {
    companion object {
        @JvmStatic
        fun create(
            capacity: Int,
            listener: Runnable?,
            canExtract: BiPredicate<FluidResource, HTTransferAccess> = HTTransferPredicates.alwaysTrueBi(),
            canInsert: BiPredicate<FluidResource, HTTransferAccess> = HTTransferPredicates.alwaysTrueBi(),
            filter: Predicate<FluidResource> = HTTransferPredicates.alwaysTrue(),
        ): HTBasicFluidTank = HTBasicFluidTank(HTTransferValidators.validateCapacity(capacity), canExtract, canInsert, filter, listener)

        @JvmStatic
        fun input(
            capacity: Int,
            listener: Runnable?,
            canInsert: Predicate<FluidResource> = HTTransferPredicates.alwaysTrue(),
            filter: Predicate<FluidResource> = canInsert,
        ): HTBasicFluidTank = create(
            capacity,
            listener,
            HTTransferPredicates.notExternal(),
            { resource: FluidResource, _ -> canInsert.test(resource) },
            filter,
        )

        @JvmStatic
        fun output(capacity: Int, listener: Runnable?): HTBasicFluidTank = create(capacity, listener, canInsert = HTTransferPredicates.internalOnly())
    }

    private var stackIn: FluidStack = FluidStack.EMPTY

    override fun getStackCopy(): FluidStack = stackIn.copy()

    override fun setStack(stack: FluidStack) {
        setStackUnchecked(stack, true)
    }

    override fun setStackInternal(stack: FluidStack) {
        setStackUnchecked(stack, false)
    }

    private fun setStackUnchecked(other: FluidStack, validate: Boolean) {
        val resource: FluidResource = getHelper().getResourceFrom(other)
        if (resource.isEmpty) {
            if (this.stackIn.isEmpty) return
            this.stackIn = FluidStack.EMPTY
        } else if (!validate || isValid(resource)) {
            this.stackIn = other
        } else {
            error("Invalid stack for tank: $other")
        }
        onRootCommit(getStackCopy())
    }

    override fun getHelper(): HTStackResourceHelper.Fluid = HTStackResourceHelper.Fluid

    //    ValueIOSerializable    //

    override fun serialize(output: ValueOutput) {
        output.store(HTConstants.FLUID, FluidStack.OPTIONAL_CODEC, getStackCopy())
    }

    override fun deserialize(input: ValueInput) {
        input.read(HTConstants.FLUID, FluidStack.OPTIONAL_CODEC).ifPresent(::setStackInternal)
    }
}
