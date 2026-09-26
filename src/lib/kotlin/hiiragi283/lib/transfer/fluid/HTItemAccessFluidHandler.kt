package hiiragi283.lib.transfer.fluid

import hiiragi283.lib.transfer.ExtendedResourceHandler
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.HTTransferPredicates
import hiiragi283.ragium.api.data.RagiumDataComponents
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.fluid.ItemAccessFluidHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * Ragiumで使用される[ItemAccessFluidHandler]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
class HTItemAccessFluidHandler(
    itemAccess: ItemAccess,
    capacity: Int,
    private val canExtract: BiPredicate<FluidResource, HTTransferAccess>,
    private val canInsert: BiPredicate<FluidResource, HTTransferAccess>,
    private val filter: Predicate<FluidResource>
) : ItemAccessFluidHandler(itemAccess, RagiumDataComponents.FLUID, capacity),
    ExtendedResourceHandler<FluidResource> {
    companion object {
        @JvmStatic
        fun create(
            itemAccess: ItemAccess,
            capacity: Int,
            canExtract: BiPredicate<FluidResource, HTTransferAccess> = HTTransferPredicates.alwaysTrueBi(),
            canInsert: BiPredicate<FluidResource, HTTransferAccess> = HTTransferPredicates.alwaysTrueBi(),
            filter: Predicate<FluidResource> = HTTransferPredicates.alwaysTrue()
        ): HTItemAccessFluidHandler = HTItemAccessFluidHandler(itemAccess, capacity, canExtract, canInsert, filter)

        @JvmStatic
        fun input(
            itemAccess: ItemAccess,
            capacity: Int,
            canInsert: Predicate<FluidResource> = HTTransferPredicates.alwaysTrue(),
            filter: Predicate<FluidResource> = canInsert
        ): HTItemAccessFluidHandler = create(
            itemAccess,
            capacity,
            HTTransferPredicates.notExternal(),
            { resource: FluidResource, _ -> canInsert.test(resource) },
            filter
        )

        @JvmStatic
        fun output(itemAccess: ItemAccess, capacity: Int): HTItemAccessFluidHandler =
            create(itemAccess, capacity, canInsert = HTTransferPredicates.internalOnly())
    }

    override fun insert(
        index: Int,
        resource: FluidResource,
        amount: Int,
        transaction: TransactionContext,
        access: HTTransferAccess
    ): Int = when {
        canInsert.test(resource, access) -> super<ItemAccessFluidHandler>.insert(index, resource, amount, transaction)
        else -> 0
    }

    override fun extract(
        index: Int,
        resource: FluidResource,
        amount: Int,
        transaction: TransactionContext,
        access: HTTransferAccess
    ): Int = when {
        canExtract.test(resource, access) -> super<ItemAccessFluidHandler>.extract(index, resource, amount, transaction)
        else -> 0
    }

    override fun isValid(index: Int, resource: FluidResource): Boolean =
        super.isValid(index, resource) && filter.test(resource)

    override fun insert(index: Int, resource: FluidResource, amount: Int, transaction: TransactionContext): Int =
        this.insert(index, resource, amount, transaction, HTTransferAccess.EXTERNAL)

    override fun extract(index: Int, resource: FluidResource, amount: Int, transaction: TransactionContext): Int =
        this.extract(index, resource, amount, transaction, HTTransferAccess.EXTERNAL)
}
