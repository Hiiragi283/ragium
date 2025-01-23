package hiiragi283.ragium.api

import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.multiblock.HTControllerHolder
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.internal.HTMachineRegistryImpl
import hiiragi283.ragium.common.internal.HTMaterialRegistryImpl
import net.minecraft.core.Direction
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.registries.RegistryBuilder

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
            BlockCapability.createSided(RagiumAPI.id("controller_holder"), HTControllerHolder::class.java)

        @JvmField
        val MACHINE_TIER: BlockCapability<HTMachineTier, Void?> =
            BlockCapability.createVoid(RagiumAPI.id("machine_tier"), HTMachineTier::class.java)
    }

    object Registries {
        @JvmField
        val MULTIBLOCK_COMPONENT_TYPE: Registry<HTMultiblockComponent.Type<*>> =
            RegistryBuilder(RegistryKeys.MULTIBLOCK_COMPONENT_TYPE).sync(true).create()
    }

    object RegistryKeys {
        @JvmField
        val MULTIBLOCK_COMPONENT_TYPE: ResourceKey<Registry<HTMultiblockComponent.Type<*>>> =
            ResourceKey.createRegistryKey<HTMultiblockComponent.Type<*>>(RagiumAPI.id("multiblock_component_type"))
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
