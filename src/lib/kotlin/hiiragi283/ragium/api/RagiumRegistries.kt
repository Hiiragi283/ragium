package hiiragi283.ragium.api

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.sync.HTSyncablePayload
import hiiragi283.lib.gui.widget.HTWidgetType
import hiiragi283.lib.recipe.result.HTFluidResultType
import hiiragi283.lib.recipe.result.HTItemResultType
import hiiragi283.lib.resource.toId
import hiiragi283.ragium.api.data.chemical.HTChemical
import hiiragi283.ragium.api.data.element.HTElement
import hiiragi283.ragium.api.data.oreSlurry.HTOreSlurryData
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.DataPackRegistryEvent
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegistryBuilder

/**
 * Ragiumで追加されるレジストリをまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@EventBusSubscriber
data object RagiumRegistries {
    /**
     * @since 26.1.8
     */
    @JvmField
    val CHEMICAL_TYPE: Registry<HTChemical.Type<*>> = createRegistry(Keys.CHEMICAL_TYPE)

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
        event.register(CHEMICAL_TYPE)
        event.register(FLUID_RESULT_TYPE)
        event.register(ITEM_RESULT_TYPE)
        event.register(SYNCABLE_SLOT_TYPE)
        event.register(WIDGET_TYPE)
    }

    @SubscribeEvent
    fun registerDynamicRegistries(event: DataPackRegistryEvent.NewRegistry) {
        event.dataPackRegistry(
            Keys.CHEMICAL,
            HTChemical.DISPATCH_CODEC,
            HTChemical.DISPATCH_CODEC
        )
        event.dataPackRegistry(
            Keys.ELEMENT,
            HTElement.DIRECT_CODEC,
            HTElement.DIRECT_CODEC
        )
        event.dataPackRegistry(
            Keys.ORE_SLURRY_DATA,
            HTOreSlurryData.DIRECT_CODEC,
            HTOreSlurryData.DIRECT_CODEC
        )
    }

    @JvmStatic
    private fun <T : Any> createRegistry(key: ResourceKey<Registry<T>>): Registry<T> = RegistryBuilder(key)
        .sync(true)
        .create()

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    data object Keys {
        // Static
        /**
         * @since 26.1.8
         */
        @JvmField
        val CHEMICAL_TYPE: ResourceKey<Registry<HTChemical.Type<*>>> = createKey("chemical_type")

        @JvmField
        val FLUID_RESULT_TYPE: ResourceKey<Registry<HTFluidResultType<*>>> = createKey("fluid_result_type")

        @JvmField
        val ITEM_RESULT_TYPE: ResourceKey<Registry<HTItemResultType<*>>> = createKey("item_result_type")

        @JvmField
        val SYNCABLE_SLOT_TYPE: ResourceKey<Registry<HTSyncablePayload.Type<*>>> = createKey("syncable_payload_type")

        @JvmField
        val WIDGET_TYPE: ResourceKey<Registry<HTWidgetType<*>>> = createKey("widget_type")

        // Dynamic

        /**
         * @since 26.1.8
         */
        @JvmField
        val CHEMICAL: ResourceKey<Registry<HTChemical>> = createKey(HTConstants.CHEMICAL)

        /**
         * @since 26.1.8
         */
        @JvmField
        val ELEMENT: ResourceKey<Registry<HTElement>> = createKey(HTConstants.ELEMENT)

        /**
         * @since 26.1.5
         */
        @JvmField
        val ORE_SLURRY_DATA: ResourceKey<Registry<HTOreSlurryData>> = createKey("ore_slurry_data")

        @JvmStatic
        private fun <T : Any> createKey(path: String): ResourceKey<Registry<T>> =
            ResourceKey.createRegistryKey(RagiumAPI.MOD_ID.toId(path))
    }
}
