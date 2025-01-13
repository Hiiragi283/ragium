package hiiragi283.ragium.api.fluid

import hiiragi283.ragium.api.machine.HTMachineTierUpgradable
import net.neoforged.neoforge.fluids.capability.IFluidHandler

interface HTTieredFluidHandler :
    IFluidHandler,
    HTMachineTierUpgradable
