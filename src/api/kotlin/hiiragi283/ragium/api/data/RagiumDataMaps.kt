package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.DataMapType

/**
 * @see [mekanism.api.datamaps.IMekanismDataMapTypes]
 */
interface RagiumDataMaps {
    companion object {
        @JvmField
        val INSTANCE: RagiumDataMaps = RagiumAPI.getService()
    }

    val thermalFuelType: DataMapType<Fluid, HTFluidFuelData>
    val combustionFuelType: DataMapType<Fluid, HTFluidFuelData>
    val solarPowerType: DataMapType<Block, HTSolarPower>

    fun <TYPE : Any, DATA : Any> getData(
        access: RegistryAccess,
        registryKey: ResourceKey<out Registry<TYPE>>,
        holder: Holder<TYPE>,
        type: DataMapType<TYPE, DATA>,
    ): DATA?

    fun getThermalFuel(access: RegistryAccess, holder: Holder<Fluid>): HTFluidFuelData? =
        getData(access, Registries.FLUID, holder, thermalFuelType)

    fun getCombustionFuel(access: RegistryAccess, holder: Holder<Fluid>): HTFluidFuelData? =
        getData(access, Registries.FLUID, holder, combustionFuelType)

    fun getSolarPower(access: RegistryAccess, holder: Holder<Block>): HTSolarPower? =
        getData(access, Registries.BLOCK, holder, solarPowerType)
}
