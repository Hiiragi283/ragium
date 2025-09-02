package hiiragi283.ragium.common.storage.holder

import hiiragi283.ragium.api.storage.HTTransferIO
import hiiragi283.ragium.api.storage.holder.HTEnergyStorageHolder
import net.minecraft.core.Direction
import net.neoforged.neoforge.energy.IEnergyStorage

class HTSimpleEnergyStorageHolder(
    transferProvider: HTTransferIO.Provider?,
    private val storage: IEnergyStorage?,
    private val canInsert: Boolean,
    private val canExtract: Boolean,
) : HTSimpleCapabilityHolder(transferProvider),
    HTEnergyStorageHolder {
    companion object {
        @JvmStatic
        fun input(transferProvider: HTTransferIO.Provider?, storage: IEnergyStorage): HTSimpleEnergyStorageHolder =
            HTSimpleEnergyStorageHolder(transferProvider, storage, canInsert = true, canExtract = false)

        @JvmStatic
        fun output(transferProvider: HTTransferIO.Provider?, storage: IEnergyStorage): HTSimpleEnergyStorageHolder =
            HTSimpleEnergyStorageHolder(transferProvider, storage, canInsert = false, canExtract = true)

        @JvmStatic
        fun generic(transferProvider: HTTransferIO.Provider?, storage: IEnergyStorage): HTSimpleEnergyStorageHolder =
            HTSimpleEnergyStorageHolder(transferProvider, storage, canInsert = true, canExtract = true)
    }

    override fun getEnergyHandler(side: Direction?): IEnergyStorage? = when {
        side == null -> storage
        canInsert(side) && canInsert -> storage
        canExtract(side) && canExtract -> storage
        else -> null
    }
}
