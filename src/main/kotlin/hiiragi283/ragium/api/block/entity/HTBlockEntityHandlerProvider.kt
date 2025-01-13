package hiiragi283.ragium.api.block.entity

import net.minecraft.core.Direction
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler

interface HTBlockEntityHandlerProvider {
    fun getItemHandler(direction: Direction?): IItemHandler? = null

    fun getFluidHandler(direction: Direction?): IFluidHandler? = null

    fun getEnergyStorage(direction: Direction?): IEnergyStorage? = null
}
