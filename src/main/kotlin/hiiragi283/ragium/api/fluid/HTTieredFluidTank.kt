package hiiragi283.ragium.api.fluid

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierUpgradable
import net.neoforged.neoforge.fluids.capability.templates.FluidTank

class HTTieredFluidTank(override var machineTier: HTMachineTier) :
    FluidTank(machineTier.tankCapacity),
    HTMachineTierUpgradable {
    override fun onUpdateTier(oldTier: HTMachineTier, newTier: HTMachineTier) {
        this.machineTier = newTier
        this.capacity = newTier.tankCapacity
    }
}
