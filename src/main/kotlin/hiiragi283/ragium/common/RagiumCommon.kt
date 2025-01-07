package hiiragi283.ragium.common

import hiiragi283.ragium.api.block.HTBlockRotationHandler
import hiiragi283.ragium.api.util.DelegatedLogger
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.internal.InternalRagiumAPI
import hiiragi283.ragium.common.internal.RagiumContentRegister
import hiiragi283.ragium.common.resource.HTHardModeResourceCondition
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger

object RagiumCommon : ModInitializer {
    @JvmStatic
    private val logger: Logger by DelegatedLogger()

    override fun onInitialize() {
        HTHardModeResourceCondition

        logger.info("Registering game objects...")

        HTBlockRotationHandler
        RagiumComponentTypes

        RagiumAdvancementCriteria
        RagiumArmorMaterials
        RagiumBlockEntityTypes
        RagiumBlocks
        RagiumEntityTypes
        RagiumRecipeSerializers
        RagiumRecipeTypes

        RagiumBlocks.register()
        RagiumItems.register()
        RagiumFluids.register()
        InternalRagiumAPI.registerMachines()

        RagiumBlockEntityTypes.init()
        RagiumCommands.init()
        RagiumFeatures.init()
        RagiumItemGroup.init()
        RagiumNetworks

        RagiumContentRegister.initEvents()
        RagiumContentRegister.initRegistry()

        logger.info("Ragium initialized!")
    }
}
