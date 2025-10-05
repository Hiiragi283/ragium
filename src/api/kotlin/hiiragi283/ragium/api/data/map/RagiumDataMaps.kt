package hiiragi283.ragium.api.data.map

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.RegistryKey
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
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
    val nuclearFuelType: DataMapType<Fluid, HTFluidFuelData>
    val solarPowerType: DataMapType<Block, HTSolarPower>

    val brewingEffectType: DataMapType<Item, HTBrewingEffect>

    fun <TYPE : Any, DATA : Any> getData(
        access: RegistryAccess,
        registryKey: RegistryKey<TYPE>,
        holder: Holder<TYPE>,
        type: DataMapType<TYPE, DATA>,
    ): DATA?

    fun getThermalFuel(access: RegistryAccess, holder: Holder<Fluid>): Int =
        getData(access, Registries.FLUID, holder, thermalFuelType)?.amount ?: 0

    fun getCombustionFuel(access: RegistryAccess, holder: Holder<Fluid>): Int =
        getData(access, Registries.FLUID, holder, combustionFuelType)?.amount ?: 0

    fun getNuclearFuel(access: RegistryAccess, holder: Holder<Fluid>): Int =
        getData(access, Registries.FLUID, holder, nuclearFuelType)?.amount ?: 0

    fun getSolarPower(access: RegistryAccess, holder: Holder<Block>): Float? =
        getData(access, Registries.BLOCK, holder, solarPowerType)?.multiplier

    fun getBrewingEffect(access: RegistryAccess, holder: Holder<Item>): HTBrewingEffect? =
        getData(access, Registries.ITEM, holder, brewingEffectType)
}
