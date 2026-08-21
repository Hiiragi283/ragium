package hiiragi283.ragium.api

import hiiragi283.lib.gui.sync.HTSyncablePayload
import hiiragi283.lib.gui.widget.HTWidgetType
import hiiragi283.lib.recipe.result.HTFluidResultType
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
    val FLUID_RESULT_TYPE: Registry<HTFluidResultType<*>> = createRegistry(Keys.FLUID_RESULT_TYPE)

    @JvmField
    val ITEM_RESULT_TYPE: Registry<HTItemResultType<*>> = createRegistry(Keys.ITEM_RESULT_TYPE)

    @JvmField
    val SYNCABLE_SLOT_TYPE: Registry<HTSyncablePayload.Type<*>> = createRegistry(Keys.SYNCABLE_SLOT_TYPE)

    @JvmField
    val WIDGET_TYPE: Registry<HTWidgetType<*>> = createRegistry(Keys.WIDGET_TYPE)

    @SubscribeEvent
    fun registerNewRegistry(event: NewRegistryEvent) {
        event.register(FLUID_RESULT_TYPE)
        event.register(ITEM_RESULT_TYPE)
        event.register(SYNCABLE_SLOT_TYPE)
        event.register(WIDGET_TYPE)
    }

    @JvmStatic
    private fun <T : Any> createRegistry(key: ResourceKey<Registry<T>>): Registry<T> = RegistryBuilder(key)
        .sync(true)
        .create()

    data object Keys {
        @JvmField
        val FLUID_RESULT_TYPE: ResourceKey<Registry<HTFluidResultType<*>>> = createKey("fluid_result_type")

        @JvmField
        val ITEM_RESULT_TYPE: ResourceKey<Registry<HTItemResultType<*>>> = createKey("item_result_type")

        @JvmField
        val SYNCABLE_SLOT_TYPE: ResourceKey<Registry<HTSyncablePayload.Type<*>>> = createKey("syncable_payload_type")

        @JvmField
        val WIDGET_TYPE: ResourceKey<Registry<HTWidgetType<*>>> = createKey("widget_type")

        @JvmStatic
        private fun <T : Any> createKey(path: String): ResourceKey<Registry<T>> = ResourceKey.createRegistryKey(RagiumAPI.MOD_ID.toId(path))
    }
}
