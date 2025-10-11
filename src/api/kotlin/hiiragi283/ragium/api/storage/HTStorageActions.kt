package hiiragi283.ragium.api.storage

import net.neoforged.neoforge.fluids.capability.IFluidHandler

fun IFluidHandler.FluidAction.toAction(): HTStorageAction = HTStorageAction.of(this.simulate())

fun HTStorageAction.toFluid(): IFluidHandler.FluidAction = when (this.execute) {
    true -> IFluidHandler.FluidAction.EXECUTE
    false -> IFluidHandler.FluidAction.SIMULATE
}
