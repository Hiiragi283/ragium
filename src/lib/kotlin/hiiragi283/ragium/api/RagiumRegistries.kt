package hiiragi283.ragium.api

import hiiragi283.lib.recipe.result.HTItemResultType
import hiiragi283.lib.resource.toId
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegistryBuilder

@EventBusSubscriber
data object RagiumRegistries {
    @JvmField
    val ITEM_RESULT_TYPE: Registry<HTItemResultType<*>> = createRegistry(Keys.ITEM_RESULT_TYPE)

    @SubscribeEvent
    fun registerNewRegistry(event: NewRegistryEvent) {
        event.register(ITEM_RESULT_TYPE)
    }

    @JvmStatic
    private fun <T : Any> createRegistry(key: ResourceKey<Registry<T>>): Registry<T> = RegistryBuilder(key)
        .sync(true)
        .create()

    data object Keys {
        @JvmField
        val ITEM_RESULT_TYPE: ResourceKey<Registry<HTItemResultType<*>>> = createKey("item_result_type")

        @JvmStatic
        private fun <T : Any> createKey(path: String): ResourceKey<Registry<T>> = ResourceKey.createRegistryKey(RagiumAPI.MOD_ID.toId(path))
    }
}
