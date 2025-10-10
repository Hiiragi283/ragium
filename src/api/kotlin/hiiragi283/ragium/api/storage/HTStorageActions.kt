package hiiragi283.ragium.api.storage

import net.neoforged.neoforge.fluids.capability.IFluidHandler

fun IFluidHandler.FluidAction.wrapAction(): HTStorageAction = HTStorageAction.of(this.simulate())
