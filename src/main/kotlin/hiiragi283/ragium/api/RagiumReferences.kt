package hiiragi283.ragium.api

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeCondition
import hiiragi283.ragium.api.util.DisableOverwriteMerger
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.RegistryBuilder
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType
import net.neoforged.neoforge.registries.datamaps.DataMapType
import net.minecraft.core.registries.Registries as MCRegistries

/**
 * RagiumのAPI
 */
@Suppress("DEPRECATION")
data object RagiumReferences {
    /**
     * Ragiumが追加する[DataMapType]
     */
    object DataMapTypes {
        //    Item    //

        /**
         * [HTMachineKey]を返す[DataMapType]
         */
        @JvmField
        val MACHINE_KEY: DataMapType<Item, HTMachineKey> = createItem("machine", HTMachineKey.CODEC)

        /**
         * [HTMachineTier]を返す[DataMapType]
         */
        @JvmField
        val MACHINE_TIER: DataMapType<Item, HTMachineTier> = createItem("machine_tier", HTMachineTier.CODEC)

        @JvmStatic
        private fun <T : Any> createItem(path: String, codec: Codec<T>): DataMapType<Item, T> = AdvancedDataMapType
            .builder(RagiumAPI.id(path), MCRegistries.ITEM, codec)
            .synced(codec, false)
            .merger(DisableOverwriteMerger())
            .build()
    }

    /**
     * Ragiumが追加する[Registry]
     */
    object Registries {
        /**
         * [HTMultiblockComponent.Type]の[Registry]
         */
        @JvmField
        val MULTIBLOCK_COMPONENT_TYPE: Registry<HTMultiblockComponent.Type<*>> =
            RegistryBuilder(RegistryKeys.MULTIBLOCK_COMPONENT_TYPE).sync(true).create()

        /**
         * [HTMachineRecipeCondition]の[MapCodec]の[Registry]
         */
        @JvmField
        val RECIPE_CONDITION: Registry<MapCodec<out HTMachineRecipeCondition>> =
            RegistryBuilder(RegistryKeys.RECIPE_CONDITION).sync(true).create()
    }

    /**
     * Ragiumが追加する[Registry]の[ResourceKey]
     */
    object RegistryKeys {
        @JvmField
        val MULTIBLOCK_COMPONENT_TYPE: ResourceKey<Registry<HTMultiblockComponent.Type<*>>> =
            ResourceKey.createRegistryKey<HTMultiblockComponent.Type<*>>(RagiumAPI.id("multiblock_component_type"))

        @JvmField
        val RECIPE_CONDITION: ResourceKey<Registry<MapCodec<out HTMachineRecipeCondition>>> =
            ResourceKey.createRegistryKey<MapCodec<out HTMachineRecipeCondition>>(RagiumAPI.id("recipe_condition"))
    }
}

/*fun collectPlugins() {
    LOGGER.info("Collecting RagiumPlugin ...")
    this.plugins =
        buildList<RagiumPlugin> {
            ModList.get().forEachModContainer { modId: String, container: ModContainer ->
                container
                    .getCustomExtension(RagiumPlugin.Provider::class.java)
                    .map(RagiumPlugin.Provider::getPlugins)
                    .ifPresent(this::addAll)
            }
        }.sortedWith(compareBy(RagiumPlugin::priority).thenBy { it::class.java.canonicalName })
            .filter(RagiumPlugin::shouldLoad)
    LOGGER.info("RagiumPlugin collected!")

    LOGGER.info("=== Loaded Ragium Plugins ===")
    plugins.forEach { plugin: RagiumPlugin ->
        LOGGER.info("- Priority : ${plugin.priority} ... ${plugin.javaClass.canonicalName}")
    }
    LOGGER.info("=============================")
}*/
