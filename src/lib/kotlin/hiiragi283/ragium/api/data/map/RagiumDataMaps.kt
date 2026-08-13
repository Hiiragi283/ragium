package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.datamaps.DataMapType

data object RagiumDataMaps {

    @JvmStatic
    fun <R : Any, T : Any> register(name: String, registryKey: ResourceKey<Registry<R>>, codec: Codec<T>): DataMapType<R, T> = DataMapType.builder(RagiumAPI.id(name), registryKey, codec).synced(codec, false).build()
}
