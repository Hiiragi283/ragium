package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.api.recipe.machine.HTRecipeComponentTypes
import hiiragi283.ragium.common.accessories.RagiumAccessoriesInit
import hiiragi283.ragium.common.data.HTHardModeResourceCondition
import hiiragi283.ragium.common.init.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.fabricmc.fabric.api.event.registry.DynamicRegistries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.gen.GenerationStep

object RagiumCommon : ModInitializer {
    override fun onInitialize() {
        RagiumAPI.log { info("Registering game objects...") }
        RagiumConfig.init()

        DynamicRegistries.registerSynced(HTMultiblockPattern.REGISTRY_KEY, HTMultiblockPattern.CODEC)

        HTRecipeComponentTypes
        RagiumComponentTypes

        RagiumAdvancementCriteria
        RagiumBlockEntityTypes
        RagiumBlocks
        RagiumEntityTypes.init()
        RagiumFluids.init()
        RagiumRecipeSerializers
        RagiumRecipeTypes

        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, RagiumAPI.id("ore_raginite")),
        )

        InternalRagiumAPI.initMachineType()
        RagiumContents
        RagiumContentRegister.registerContents()

        RagiumBlockEntityTypes.init()
        RagiumCauldronBehaviors.init()
        RagiumCommands.init()
        RagiumApiLookupInit.init()
        RagiumEventHandlers.init()
        RagiumItemGroup.init()
        HTHardModeResourceCondition.init()
        RagiumNetworks.init()

        RagiumAccessoriesInit.init()

        RagiumAPI.log { info("Ragium initialized!") }
    }
}
