package hiiragi283.ragium.impl.data.map

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.map.HTBrewingEffect
import hiiragi283.ragium.api.data.map.HTFluidFuelData
import hiiragi283.ragium.api.data.map.HTSolarPower
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.extension.RegistryKey
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.DataMapType
import kotlin.jvm.optionals.getOrNull

class RagiumDataMapsImpl : RagiumDataMaps {
    companion object {
        @JvmStatic
        private val THERMAL_FUEL: DataMapType<Fluid, HTFluidFuelData> = createFuel("thermal")

        @JvmStatic
        private val COMBUSTION_FUEL: DataMapType<Fluid, HTFluidFuelData> = createFuel("combustion")

        @JvmStatic
        private val NUCLEAR_FUEL: DataMapType<Fluid, HTFluidFuelData> = createFuel("nuclear")

        @JvmStatic
        private val SOLAR_POWER: DataMapType<Block, HTSolarPower> = create("solar_power", Registries.BLOCK, HTSolarPower.CODEC)

        @JvmStatic
        private val BREWING_EFFECT: DataMapType<Item, HTBrewingEffect> = create("brewing/effect", Registries.ITEM, HTBrewingEffect.CODEC)

        @JvmStatic
        private fun <T : Any, R : Any> create(path: String, registryKey: ResourceKey<Registry<R>>, codec: Codec<T>): DataMapType<R, T> =
            DataMapType
                .builder(RagiumAPI.id(path), registryKey, codec)
                .synced(codec, false)
                .build()

        @JvmStatic
        private fun createFuel(path: String): DataMapType<Fluid, HTFluidFuelData> =
            create("fuel/$path", Registries.FLUID, HTFluidFuelData.CODEC)
    }

    override val thermalFuelType: DataMapType<Fluid, HTFluidFuelData> = THERMAL_FUEL
    override val combustionFuelType: DataMapType<Fluid, HTFluidFuelData> = COMBUSTION_FUEL
    override val nuclearFuelType: DataMapType<Fluid, HTFluidFuelData> = NUCLEAR_FUEL
    override val solarPowerType: DataMapType<Block, HTSolarPower> = SOLAR_POWER

    override val brewingEffectType: DataMapType<Item, HTBrewingEffect> = BREWING_EFFECT

    override fun <TYPE : Any, DATA : Any> getData(
        access: RegistryAccess,
        registryKey: RegistryKey<TYPE>,
        holder: Holder<TYPE>,
        type: DataMapType<TYPE, DATA>,
    ): DATA? = when (Holder.Kind.REFERENCE) {
        holder.kind() -> holder.getData(type)
        else ->
            access
                .registry(registryKey)
                .getOrNull()
                ?.wrapAsHolder(holder.value())
                ?.getData(type)
    }
}
