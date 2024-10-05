package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTypeRegistry
import hiiragi283.ragium.api.recipe.machine.HTFluidPumpEntry
import hiiragi283.ragium.common.data.HTFluidPumpEntryLoader
import hiiragi283.ragium.common.data.HTHardModeResourceCondition
import hiiragi283.ragium.common.init.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.fabricmc.fabric.api.event.registry.DynamicRegistries
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.resource.ResourceType
import net.minecraft.world.gen.GenerationStep

object RagiumCommon : ModInitializer {
    override fun onInitialize() {
        RagiumAPI.log { info("Registering game objects...") }
        RagiumConfig.init()

        RagiumComponentTypes

        RagiumAdvancementCriteria
        RagiumBlockEntityTypes
        RagiumEntityTypes.init()
        RagiumNetworks
        RagiumRecipeSerializers
        RagiumRecipeTypes
        registerDynamics()
        registerModifications()

        HTMachineTypeRegistry.init()
        RagiumContents.init()

        RagiumBlockEntityTypes.init()
        RagiumCauldronBehaviors.init()
        RagiumCommands.init()
        RagiumEnergyProviders.init()
        RagiumEventHandlers.init()
        RagiumItemGroup.init()
        HTHardModeResourceCondition.init()

        RagiumAPI.log { info("Ragium initialized!") }
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
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, RagiumAPI.id("ore_raginite")),
        )
    }
}
