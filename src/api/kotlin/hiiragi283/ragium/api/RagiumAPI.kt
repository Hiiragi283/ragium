package hiiragi283.ragium.api

import hiiragi283.core.api.resource.toId
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.RegistryBuilder

object RagiumAPI {
    const val MOD_ID = "ragium"

    @JvmStatic
    fun id(path: String): ResourceLocation = MOD_ID.toId(path)

    @JvmStatic
    fun id(vararg path: String): ResourceLocation = MOD_ID.toId(*path)

    //    Registry    //

    @JvmStatic
    private fun <T : Any> createKey(path: String): ResourceKey<Registry<T>> = ResourceKey.createRegistryKey(id(path))

    @JvmStatic
    private fun <T : Any> createRegistry(key: ResourceKey<Registry<T>>): Registry<T> = RegistryBuilder<T>(key)
        .sync(true)
        .create()
}
