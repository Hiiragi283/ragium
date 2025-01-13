package hiiragi283.ragium.common.fluid

import hiiragi283.ragium.api.fluid.HTTieredFluidHandler
import hiiragi283.ragium.api.machine.HTMachineTier
import net.neoforged.neoforge.fluids.capability.templates.FluidTank

class HTTieredSingleFluidHandler(override var tier: HTMachineTier) :
    FluidTank(tier.tankCapacity),
    HTTieredFluidHandler {
    override fun onUpdateTier(oldTier: HTMachineTier, newTier: HTMachineTier) {
        this.tier = newTier
        this.capacity = newTier.tankCapacity
    }
}
