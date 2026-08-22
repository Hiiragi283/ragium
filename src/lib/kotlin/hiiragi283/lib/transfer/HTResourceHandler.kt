package hiiragi283.lib.transfer

import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTResourceHandler<T : Resource>(val slots: List<HTResourceSlot<T>>) : ResourceHandler<T> {
    fun getSlot(index: Int): HTResourceSlot<T> = slots[index]

    override fun size(): Int = slots.size

    override fun getResource(index: Int): T = getSlot(index).resource

    override fun getAmountAsLong(index: Int): Long = getAmountAsInt(index).toLong()

    @Suppress("NonExtendableApiUsage")
    override fun getAmountAsInt(index: Int): Int = getSlot(index).amount

    override fun getCapacityAsLong(index: Int, resource: T): Long = getCapacityAsInt(index, resource).toLong()

    @Suppress("NonExtendableApiUsage")
    override fun getCapacityAsInt(index: Int, resource: T): Int = getSlot(index).getCapacity(resource)

    override fun isValid(index: Int, resource: T): Boolean = getSlot(index).isValid(resource)

    override fun insert(index: Int, resource: T, amount: Int, transaction: TransactionContext): Int = getSlot(index).insert(resource, amount, transaction, HTTransferAccess.EXTERNAL)

    override fun extract(index: Int, resource: T, amount: Int, transaction: TransactionContext): Int = getSlot(index).extract(resource, amount, transaction, HTTransferAccess.EXTERNAL)
}
