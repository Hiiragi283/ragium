package hiiragi283.ragium.api

import hiiragi283.ragium.api.data.BiCodec
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
    val THERMAL_FUEL: DataMapType<Fluid, HTFluidFuelData> =
        register("fuel/thermal", Registries.FLUID, HTFluidFuelData.CODEC)

    @JvmField
    val BURNING_FUEL: DataMapType<Fluid, HTFluidFuelData> =
        register("fuel/burning", Registries.FLUID, HTFluidFuelData.CODEC)

    @JvmStatic
    private fun <T : Any, R : Any> register(path: String, registryKey: ResourceKey<Registry<R>>, codec: BiCodec<*, T>): DataMapType<R, T> =
        DataMapType
            .builder(RagiumAPI.id(path), registryKey, codec.codec)
            .synced(codec.codec, false)
            .build()
}
