package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.setup.RagiumDataComponents
import net.neoforged.neoforge.common.MutableDataComponentHolder
import net.neoforged.neoforge.energy.ComponentEnergyStorage

/**
 * [getEnergyStored]が`0`の時にコンポーネントを消す[net.neoforged.neoforge.energy.ComponentEnergyStorage]
 */
open class HTComponentEnergyStorage(
    parent: MutableDataComponentHolder,
    capacity: Int,
    maxReceive: Int = capacity,
    maxExtract: Int = maxReceive,
) : ComponentEnergyStorage(parent, RagiumDataComponents.ENERGY.get(), capacity, maxReceive, maxExtract) {
    override fun setEnergy(energy: Int) {
        super.setEnergy(energy)
        if (parent.getOrDefault(energyComponent, 0) <= 0) {
            parent.remove(energyComponent)
        }
    }
}
