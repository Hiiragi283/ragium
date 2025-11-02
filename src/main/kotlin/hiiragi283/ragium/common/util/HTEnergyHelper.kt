package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery

object HTEnergyHelper {
    @JvmStatic
    fun moveEnergy(
        from: HTEnergyBattery?,
        to: HTEnergyBattery?,
        amount: Int = from?.getAmount() ?: 0,
        access: HTStorageAccess = HTStorageAccess.INTERNAL,
    ): Int? {
        if (from == null || to == null || amount <= 0) return null
        val simulatedExtracted: Int = from.extract(amount, HTStorageAction.SIMULATE, access)
        val simulatedInserted: Int = to.insert(simulatedExtracted, HTStorageAction.EXECUTE, access)
        return from.extract(simulatedInserted, HTStorageAction.EXECUTE, access)
    }
}
