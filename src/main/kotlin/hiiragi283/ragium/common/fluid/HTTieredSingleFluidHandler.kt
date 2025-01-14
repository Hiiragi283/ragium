package hiiragi283.ragium.common.fluid

import hiiragi283.ragium.api.fluid.HTTieredFluidHandler
import hiiragi283.ragium.api.machine.HTMachineTier
import net.neoforged.neoforge.fluids.capability.templates.FluidTank

class HTTieredSingleFluidHandler(override var machineTier: HTMachineTier) :
    FluidTank(machineTier.tankCapacity),
    HTTieredFluidHandler {
    override fun onUpdateTier(oldTier: HTMachineTier, newTier: HTMachineTier) {
        this.machineTier = newTier
        this.capacity = newTier.tankCapacity
    }
}
