package hiiragi283.ragium.common.transfer.fluid

import hiiragi283.lib.HTConstants
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.fluid.HTFluidTank
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * @see net.neoforged.neoforge.transfer.InfiniteResourceHandler
 */
class HTCreativeFluidTank : HTFluidTank {
    override fun isValid(resource: FluidResource): Boolean = resource == this.resource

    override fun insert(
        resource: FluidResource,
        amount: Int,
        transaction: TransactionContext,
        access: HTTransferAccess
    ): Int {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
        return when {
            this.resource.isEmpty -> {
                this.resource = resource
                amount
            }

            this.resource == resource -> amount

            else -> 0
        }
    }

    override fun extract(
        resource: FluidResource,
        amount: Int,
        transaction: TransactionContext,
        access: HTTransferAccess
    ): Int {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
        return when (resource) {
            this.resource -> amount
            else -> 0
        }
    }

    override var resource: FluidResource = FluidResource.EMPTY

    override val amount: Int get() = when (resource.isEmpty) {
        true -> 0
        false -> Int.MAX_VALUE
    }

    override fun getCapacity(resource: FluidResource): Int = Int.MAX_VALUE

    override fun serialize(output: ValueOutput) {
        output.store(HTConstants.FLUID, FluidResource.OPTIONAL_CODEC, this.resource)
    }

    override fun deserialize(input: ValueInput) {
        input.read(HTConstants.FLUID, FluidResource.OPTIONAL_CODEC).ifPresent(this::resource::set)
    }
}
