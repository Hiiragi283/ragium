package hiiragi283.ragium.api

import hiiragi283.core.api.HiiragiCoreAPI.id
import hiiragi283.ragium.api.recipe.HTDuplicationModifier
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.RegistryBuilder

/**
 * @see hiiragi283.core.api.HCRegistries
 */
object RagiumRegistries {
    @JvmField
    val DUPLICATION_MODIFIER: Registry<HTDuplicationModifier> = createRegistry(Keys.DUPLICATION_MODIFIER)

    @JvmStatic
    private fun <T : Any> createRegistry(key: ResourceKey<Registry<T>>): Registry<T> = RegistryBuilder<T>(key)
        .sync(true)
        .create()

    //    Keys    //

    object Keys {
        @JvmField
        val DUPLICATION_MODIFIER: ResourceKey<Registry<HTDuplicationModifier>> = createKey("duplication_modifier")

        @JvmStatic
        private fun <T : Any> createKey(path: String): ResourceKey<Registry<T>> = ResourceKey.createRegistryKey(id(path))
    }
}
