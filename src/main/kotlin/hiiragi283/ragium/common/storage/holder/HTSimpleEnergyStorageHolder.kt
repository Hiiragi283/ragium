package hiiragi283.ragium.common.storage.holder

import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.holder.HTEnergyStorageHolder
import hiiragi283.ragium.api.util.access.HTAccessConfigGetter
import net.minecraft.core.Direction

class HTSimpleEnergyStorageHolder(
    transferProvider: HTAccessConfigGetter?,
    private val storage: HTEnergyBattery?,
    private val canInsert: Boolean,
    private val canExtract: Boolean,
) : HTSimpleCapabilityHolder(transferProvider),
    HTEnergyStorageHolder {
    companion object {
        @JvmStatic
        fun input(transferProvider: HTAccessConfigGetter?, storage: HTEnergyBattery): HTSimpleEnergyStorageHolder =
            HTSimpleEnergyStorageHolder(transferProvider, storage, canInsert = true, canExtract = false)

        @JvmStatic
        fun output(transferProvider: HTAccessConfigGetter?, storage: HTEnergyBattery): HTSimpleEnergyStorageHolder =
            HTSimpleEnergyStorageHolder(transferProvider, storage, canInsert = false, canExtract = true)

        @JvmStatic
        fun generic(transferProvider: HTAccessConfigGetter?, storage: HTEnergyBattery): HTSimpleEnergyStorageHolder =
            HTSimpleEnergyStorageHolder(transferProvider, storage, canInsert = true, canExtract = true)
    }

    override fun getEnergyBattery(side: Direction?): HTEnergyBattery? = when {
        side == null -> storage
        canInsert(side) && canInsert -> storage
        canExtract(side) && canExtract -> storage
        else -> null
    }
}
