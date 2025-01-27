package hiiragi283.ragium.api

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.property.HTMachineFuel
import hiiragi283.ragium.api.material.HTMaterialDefinition
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.multiblock.HTControllerHolder
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import hiiragi283.ragium.api.util.DisableOverwriteMerger
import hiiragi283.ragium.api.util.HTTemperatureInfo
import hiiragi283.ragium.common.internal.HTMachineRegistryImpl
import hiiragi283.ragium.common.internal.HTMaterialRegistryImpl
import net.minecraft.core.Direction
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.registries.RegistryBuilder
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType
import net.neoforged.neoforge.registries.datamaps.DataMapType
import net.minecraft.core.registries.Registries as MCRegistries

/**
 * RagiumのAPI
 */
@Suppress("DEPRECATION")
data object RagiumAPI {
    const val MOD_ID = "ragium"
    const val MOD_NAME = "Ragium"

    /**
     * 名前空間が`ragium`となる[ResourceLocation]を返します。
     */
    @JvmStatic
    fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)

    /**
     * 指定した[id]の名前空間を`ragium`に変えます。
     */
    @JvmStatic
    fun wrapId(id: ResourceLocation): ResourceLocation = id(id.path)

    /**
     * 機械レジストリのインスタンスです。
     */
    @JvmField
    val machineRegistry: HTMachineRegistry = HTMachineRegistryImpl

    /**
     * 素材レジストリのインスタンスです。
     */
    @JvmField
    val materialRegistry: HTMaterialRegistry = HTMaterialRegistryImpl

    object BlockCapabilities {
        @JvmField
        val CONTROLLER_HOLDER: BlockCapability<HTControllerHolder, Direction?> =
            BlockCapability.createSided(id("controller_holder"), HTControllerHolder::class.java)

        @JvmField
        val MACHINE_TIER: BlockCapability<HTMachineTier, Void?> =
            BlockCapability.createVoid(id("machine_tier"), HTMachineTier::class.java)
    }

    object DataMapTypes {
        @JvmField
        val MACHINE_KEY: DataMapType<Item, HTMachineKey> = createItem("machine", HTMachineKey.CODEC)

        @JvmField
        val MACHINE_TIER: DataMapType<Item, HTMachineTier> = createItem("machine_tier", HTMachineTier.CODEC)

        @JvmField
        val MACHINE_FUEL: DataMapType<Fluid, Map<HTMachineKey, Int>> =
            AdvancedDataMapType
                .builder(id("machine_fuel"), MCRegistries.FLUID, HTMachineFuel.CODEC)
                .synced(HTMachineFuel.CODEC, false)
                .merger(HTMachineFuel.Merger)
                .remover(HTMachineFuel.Remover.CODEC)
                .build()

        @JvmField
        val MATERIAL: DataMapType<Item, HTMaterialDefinition> = createItem("material", HTMaterialDefinition.CODEC)

        @JvmField
        val TEMP_TIER: DataMapType<Block, HTTemperatureInfo> = createBlock("temperature", HTTemperatureInfo.CODEC)

        @JvmStatic
        private fun <T : Any> createBlock(path: String, codec: Codec<T>): DataMapType<Block, T> = AdvancedDataMapType
            .builder(id(path), MCRegistries.BLOCK, codec)
            .synced(codec, false)
            .merger(DisableOverwriteMerger())
            .build()

        @JvmStatic
        private fun <T : Any> createItem(path: String, codec: Codec<T>): DataMapType<Item, T> = AdvancedDataMapType
            .builder(id(path), MCRegistries.ITEM, codec)
            .synced(codec, false)
            .merger(DisableOverwriteMerger())
            .build()
    }

    object Registries {
        @JvmField
        val MULTIBLOCK_COMPONENT_TYPE: Registry<HTMultiblockComponent.Type<*>> =
            RegistryBuilder(RegistryKeys.MULTIBLOCK_COMPONENT_TYPE).sync(true).create()

        @JvmField
        val RECIPE_CONDITION: Registry<MapCodec<out HTMachineRecipeCondition>> =
            RegistryBuilder(RegistryKeys.RECIPE_CONDITION).sync(true).create()
    }

    object RegistryKeys {
        @JvmField
        val MULTIBLOCK_COMPONENT_TYPE: ResourceKey<Registry<HTMultiblockComponent.Type<*>>> =
            ResourceKey.createRegistryKey<HTMultiblockComponent.Type<*>>(id("multiblock_component_type"))

        @JvmField
        val RECIPE_CONDITION: ResourceKey<Registry<MapCodec<out HTMachineRecipeCondition>>> =
            ResourceKey.createRegistryKey<MapCodec<out HTMachineRecipeCondition>>(id("recipe_condition"))
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
