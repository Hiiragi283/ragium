package hiiragi283.ragium.common

import hiiragi283.ragium.common.data.HTFluidPumpEntryLoader
import hiiragi283.ragium.common.data.HTHardModeResourceCondition
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.recipe.HTFluidPumpEntry
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.fabricmc.fabric.api.event.registry.DynamicRegistries
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier
import net.minecraft.world.gen.GenerationStep
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Ragium : ModInitializer {
    const val MOD_ID = "ragium"
    const val MOD_NAME = "Ragium"

    @JvmStatic
    fun id(path: String): Identifier = Identifier.of(MOD_ID, path)

    @JvmField
    val logger: Logger = LoggerFactory.getLogger(MOD_NAME)

    @JvmStatic
    inline fun log(action: Logger.() -> Unit) {
        logger.action()
    }

    override fun onInitialize() {
        log { info("Registering game objects...") }
        RagiumConfig.init()

        RagiumComponentTypes

        RagiumAdvancementCriteria
        RagiumBlockEntityTypes
        RagiumMachineTypes.init()
        RagiumNetworks
        RagiumRecipeSerializers
        RagiumRecipeTypes
        registerDynamics()
        registerModifications()

        RagiumContents.init()

        RagiumBlockEntityTypes.init()
        RagiumCauldronBehaviors.init()
        RagiumCommands.init()
        RagiumEnergyProviders.init()
        RagiumEventHandlers.init()
        RagiumItemGroup.init()
        HTHardModeResourceCondition.init()

        log { info("Ragium initialized!") }
    }

    private fun registerDynamics() {
        DynamicRegistries.registerSynced(
            HTFluidPumpEntry.REGISTRY_KEY,
            HTFluidPumpEntry.CODEC,
            DynamicRegistries.SyncOption.SKIP_WHEN_EMPTY,
        )
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(HTFluidPumpEntryLoader)
    }

    private fun registerModifications() {
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, id("ore_raginite")),
        )
    }
}
