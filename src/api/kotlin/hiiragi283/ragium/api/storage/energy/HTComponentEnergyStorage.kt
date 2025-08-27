package hiiragi283.ragium.api.storage.energy

import hiiragi283.ragium.api.RagiumAPI
import net.neoforged.neoforge.common.MutableDataComponentHolder
import net.neoforged.neoforge.energy.ComponentEnergyStorage

/**
 * [ComponentEnergyStorage.getEnergyStored]が`0`の時にコンポーネントを消す[ComponentEnergyStorage]
 */
open class HTComponentEnergyStorage(
    parent: MutableDataComponentHolder,
    capacity: Int,
    maxReceive: Int = capacity,
    maxExtract: Int = maxReceive,
) : ComponentEnergyStorage(parent, RagiumAPI.getInstance().getEnergyComponent(), capacity, maxReceive, maxExtract) {
    override fun setEnergy(energy: Int) {
        super.setEnergy(energy)
        if (parent.getOrDefault(energyComponent, 0) <= 0) {
            parent.remove(energyComponent)
        }
    }
}
