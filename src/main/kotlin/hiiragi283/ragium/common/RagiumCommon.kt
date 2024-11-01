package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.trade.HTTradeOfferRegistry
import hiiragi283.ragium.common.init.*
import net.fabricmc.api.ModInitializer

object RagiumCommon : ModInitializer {
    override fun onInitialize() {
        RagiumAPI.log { info("Registering game objects...") }
        RagiumConfig.init()

        RagiumComponentTypes

        RagiumAdvancementCriteria
        RagiumBlockEntityTypes
        RagiumBlocks
        RagiumEntityTypes
        RagiumRecipeSerializers
        RagiumRecipeTypes

        InternalRagiumAPI.registerMachines()
        InternalRagiumAPI.registerProperties()
        RagiumContentRegister.registerContents()

        HTTradeOfferRegistry.init()
        RagiumBlockEntityTypes.init()
        RagiumCommands.init()
        RagiumEventHandlers.init()
        RagiumFeatures.init()
        RagiumItemGroup.init()
        RagiumMaterialItemRecipes.init()
        RagiumNetworks

        RagiumContentRegister.initRegistry()

        RagiumAPI.getPlugins().forEach(RagiumPlugin::afterRagiumInit)

        RagiumAPI.log { info("Ragium initialized!") }
    }
}
