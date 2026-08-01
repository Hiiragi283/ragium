package hiiragi283.ragium.api

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HiiragiCoreAPI.id
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.RegistryBuilder

/**
 * @see hiiragi283.core.api.HCRegistries
 */
data object RagiumRegistries {
    @JvmStatic
    private fun <T : Any> createRegistry(key: ResourceKey<Registry<T>>): Registry<T> = RegistryBuilder<T>(key)
        .sync(true)
        .create()

    //    Keys    //

    data object Keys {
        @JvmStatic
        private fun <T : Any> createKey(path: String): ResourceKey<Registry<T>> = ResourceKey.createRegistryKey(id(path))

        @JvmStatic
        private fun <T : Any> createCodecKey(path: String): ResourceKey<Registry<MapCodec<out T>>> = ResourceKey.createRegistryKey(id(path))
    }
}
