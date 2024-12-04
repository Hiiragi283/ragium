package hiiragi283.ragium.api.storage

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.ExtractionOnlyStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import kotlin.math.min

interface HTCreativeStorage<T : TransferVariant<*>> :
    SingleSlotStorage<T>,
    ExtractionOnlyStorage<T> {
    fun setResource(resource: T)

    //    SingleSlotStorage    //

    override fun extract(resource: T, maxAmount: Long, transaction: TransactionContext): Long {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount)
        return min(amount, maxAmount)
    }

    override fun isResourceBlank(): Boolean = resource.isBlank

    override fun getAmount(): Long = when (isResourceBlank) {
        true -> 0
        false -> Long.MAX_VALUE
    }

    override fun getCapacity(): Long = Long.MAX_VALUE

    companion object {
        @JvmStatic
        fun ofItem(): HTCreativeStorage<ItemVariant> = object : HTCreativeStorage<ItemVariant> {
            private var cache: ItemVariant = ItemVariant.blank()

            override fun setResource(resource: ItemVariant) {
                cache = resource
            }

            override fun getResource(): ItemVariant = cache
        }

        @JvmStatic
        fun ofFluid(): HTCreativeStorage<FluidVariant> = object : HTCreativeStorage<FluidVariant> {
            private var cache: FluidVariant = FluidVariant.blank()

            override fun setResource(resource: FluidVariant) {
                cache = resource
            }

            override fun getResource(): FluidVariant = cache
        }
    }
}
