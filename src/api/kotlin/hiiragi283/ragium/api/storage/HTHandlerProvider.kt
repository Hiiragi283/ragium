package hiiragi283.ragium.api.storage

import hiiragi283.ragium.api.storage.experience.IExperienceHandler
import net.minecraft.core.Direction
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler

interface HTHandlerProvider {
    /**
     * 指定した[direction]から[IItemHandler]を返します。
     */
    fun getItemHandler(direction: Direction?): IItemHandler?

    /**
     * 指定した[direction]から[IExperienceHandler]を返します。
     */
    fun getExperienceHandler(direction: Direction?): IExperienceHandler?

    /**
     * 指定した[direction]から[IFluidHandler]を返します。
     */
    fun getFluidHandler(direction: Direction?): IFluidHandler?

    /**
     * 指定した[direction]から[IEnergyStorage]を返します。
     */
    fun getEnergyStorage(direction: Direction?): IEnergyStorage?
}
