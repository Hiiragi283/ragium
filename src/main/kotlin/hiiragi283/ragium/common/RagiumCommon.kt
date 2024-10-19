package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.api.recipe.machine.HTRecipeComponentTypes
import hiiragi283.ragium.api.trade.HTTradeOfferRegistry
import hiiragi283.ragium.common.data.HTHardModeResourceCondition
import hiiragi283.ragium.common.init.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.registry.DynamicRegistries

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

        InternalRagiumAPI.initMachineType()
        RagiumContents
        RagiumContentRegister.registerContents()

        HTHardModeResourceCondition.init()
        RagiumBlockEntityTypes.init()
        RagiumCauldronBehaviors.init()
        RagiumCommands.init()
        RagiumEventHandlers.init()
        RagiumItemGroup.init()
        RagiumNetworks.init()
        RagiumFeatures.init()
        HTTradeOfferRegistry.init()

        RagiumApiLookupInit.init()
        RagiumMaterialItemRecipes.init()

        RagiumAPI.log { info("Ragium initialized!") }
    }
}
