package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.storage.energy.HTEnergyHandler
import net.minecraft.core.Direction
import net.neoforged.neoforge.energy.IEnergyStorage

class HTEnergyNetworkWrapper(private val delegate: () -> IEnergyStorage?) : HTEnergyHandler {
    override fun getEnergyHandler(side: Direction?): IEnergyStorage? = delegate()

    override fun onContentsChanged() {}
}
