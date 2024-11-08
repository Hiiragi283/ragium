package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.init.*
import net.fabricmc.api.ModInitializer

object RagiumCommon : ModInitializer {
    override fun onInitialize() {
        RagiumAPI.log { info("Registering game objects...") }
        RagiumConfig.init()

        RagiumComponentTypes

        RagiumAdvancementCriteria
        RagiumArmorMaterials
        RagiumBlockEntityTypes
        RagiumBlocks
        RagiumEntityTypes
        RagiumItems
        RagiumRecipeSerializers
        RagiumRecipeTypes

        RagiumContentRegister.registerContents()
        InternalRagiumAPI.registerMachines()

        RagiumBlockEntityTypes.init()
        RagiumCommands.init()
        RagiumEventHandlers.init()
        RagiumFeatures.init()
        RagiumItemGroup.init()
        RagiumNetworks

        RagiumContentRegister.initRegistry()

        RagiumAPI.getPlugins().forEach { it.afterRagiumInit(RagiumAPI.getInstance()) }

        RagiumAPI.log { info("Ragium initialized!") }
    }
}
