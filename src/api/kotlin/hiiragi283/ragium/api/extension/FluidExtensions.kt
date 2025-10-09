package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.storage.HTStorageAction
import net.neoforged.neoforge.fluids.capability.IFluidHandler

fun IFluidHandler.FluidAction.wrapAction(): HTStorageAction = HTStorageAction.of(this.simulate())
