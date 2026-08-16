package hiiragi283.lib.transfer

import net.neoforged.neoforge.transfer.resource.Resource

interface HTResourceView<T : Resource> {
    val resource: T
    val amount: Int

    val isEmpty: Boolean get() = resource.isEmpty || amount <= 0

    fun getCapacity(resource: T): Int

    fun getNeeded(resource: T): Int = getCapacity(resource) - amount

    fun getFilledLevel(resource: T): Float = amount.toFloat() / getCapacity(resource)

    val currentCapacity: Int get() = getCapacity(this.resource)
    val currentNeeded: Int get() = getNeeded(this.resource)
    val currentFilledLevel: Float get() = getFilledLevel(this.resource)
}
