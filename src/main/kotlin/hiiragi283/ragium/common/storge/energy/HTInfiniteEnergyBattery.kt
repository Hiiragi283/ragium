package hiiragi283.ragium.common.storge.energy

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.energy.HTEnergyBattery

data object HTInfiniteEnergyBattery : HTEnergyBattery, HTContentListener.Empty, HTValueSerializable.Empty {
    override fun insert(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = 0

    override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = amount

    override fun getAmount(): Int = Int.MAX_VALUE

    override fun getCapacity(): Int = Int.MAX_VALUE
}
