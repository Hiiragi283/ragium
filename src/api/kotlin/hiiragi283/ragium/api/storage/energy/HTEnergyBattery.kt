package hiiragi283.ragium.api.storage.energy

import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.value.HTValueSerializable

interface HTEnergyBattery :
    HTValueSerializable,
    HTContentListener {
    fun getAmount(): Int

    fun setAmount(amount: Int)

    fun getCapacity(): Int

    fun insertEnergy(amount: Int, simulate: Boolean, access: HTStorageAccess): Int

    fun extractEnergy(amount: Int, simulate: Boolean, access: HTStorageAccess): Int

    fun getNeeded(): Int = getCapacity() - getAmount()

    val isEmpty: Boolean get() = getAmount() <= 0

    fun setEmpty() {
        setAmount(0)
    }
}
