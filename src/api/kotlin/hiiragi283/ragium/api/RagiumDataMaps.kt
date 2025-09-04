package hiiragi283.ragium.api

import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.data.HTFluidFuelData
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.DataMapType

/**
 * @see [net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps]
 */
object RagiumDataMaps {
    @JvmField
    val THERMAL_FUEL: DataMapType<Fluid, HTFluidFuelData> = createFuel("thermal")

    @JvmField
    val COMBUSTION_FUEL: DataMapType<Fluid, HTFluidFuelData> = createFuel("combustion")

    @JvmField
    val FUELS: List<DataMapType<Fluid, HTFluidFuelData>> = listOf(
        THERMAL_FUEL,
        COMBUSTION_FUEL,
    )

    @JvmStatic
    private fun <T : Any, R : Any> create(path: String, registryKey: ResourceKey<Registry<R>>, codec: BiCodec<*, T>): DataMapType<R, T> =
        DataMapType
            .builder(RagiumAPI.id(path), registryKey, codec.codec)
            .synced(codec.codec, false)
            .build()

    @JvmStatic
    private fun createFuel(path: String): DataMapType<Fluid, HTFluidFuelData> =
        create("fuel/$path", Registries.FLUID, HTFluidFuelData.CODEC)
}
