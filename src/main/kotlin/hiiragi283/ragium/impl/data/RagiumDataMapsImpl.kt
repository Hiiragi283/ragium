package hiiragi283.ragium.impl.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.data.HTFluidFuelData
import hiiragi283.ragium.api.data.HTSolarPower
import hiiragi283.ragium.api.data.RagiumDataMaps
import hiiragi283.ragium.api.extension.RegistryKey
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
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
        private val SOLAR_POWER: DataMapType<Block, HTSolarPower> = create("solar_power", Registries.BLOCK, HTSolarPower.CODEC)

        @JvmStatic
        private fun <T : Any, R : Any> create(
            path: String,
            registryKey: ResourceKey<Registry<R>>,
            codec: BiCodec<*, T>,
        ): DataMapType<R, T> = DataMapType
            .builder(RagiumAPI.id(path), registryKey, codec.codec)
            .synced(codec.codec, false)
            .build()

        @JvmStatic
        private fun createFuel(path: String): DataMapType<Fluid, HTFluidFuelData> =
            create("fuel/$path", Registries.FLUID, HTFluidFuelData.CODEC)
    }

    override val thermalFuelType: DataMapType<Fluid, HTFluidFuelData> = THERMAL_FUEL
    override val combustionFuelType: DataMapType<Fluid, HTFluidFuelData> = COMBUSTION_FUEL
    override val solarPowerType: DataMapType<Block, HTSolarPower> = SOLAR_POWER

    override fun <TYPE : Any, DATA : Any> getData(
        access: RegistryAccess,
        registryKey: RegistryKey<TYPE>,
        holder: Holder<TYPE>,
        type: DataMapType<TYPE, DATA>,
    ): DATA? {
        if (holder.kind() == Holder.Kind.REFERENCE) {
            return holder.getData(type)
        } else {
            val registry: Registry<TYPE> = access.registry(registryKey).getOrNull() ?: return null
            return registry.wrapAsHolder(holder.value()).getData(type)
        }
    }
}
