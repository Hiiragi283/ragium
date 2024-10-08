package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTypeRegistry
import hiiragi283.ragium.common.data.HTHardModeResourceCondition
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.integration.accessories.RagiumAccessoriesInit
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
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
        registerModifications()

        InternalRagiumAPI.initMachineType()
        RagiumContents.init()

        RagiumBlockEntityTypes.init()
        RagiumCauldronBehaviors.init()
        RagiumCommands.init()
        RagiumEnergyProviders.init()
        RagiumEventHandlers.init()
        RagiumItemGroup.init()
        HTHardModeResourceCondition.init()

        RagiumAccessoriesInit.init()

        RagiumAPI.log { info("Ragium initialized!") }
    }

    private fun registerModifications() {
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, RagiumAPI.id("ore_raginite")),
        )
    }
}
