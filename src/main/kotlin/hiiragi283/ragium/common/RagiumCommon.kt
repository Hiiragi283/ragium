package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTBlockRotationHandler
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.internal.InternalRagiumAPI
import hiiragi283.ragium.common.internal.RagiumCompatRegister
import hiiragi283.ragium.common.internal.RagiumContentRegister
import hiiragi283.ragium.common.resource.HTHardModeResourceCondition
import net.fabricmc.api.ModInitializer

object RagiumCommon : ModInitializer {
    override fun onInitialize() {
        RagiumAPI.log {
            InternalRagiumAPI.config
            HTHardModeResourceCondition

            RagiumAPI.LOGGER.info("Registering game objects...")

            HTBlockRotationHandler
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

            RagiumCompatRegister.initRegistry()

            RagiumAPI.LOGGER.info("Ragium initialized!")
        }
    }
}
