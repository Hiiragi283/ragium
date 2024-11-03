package hiiragi283.ragium.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.extension.isClientEnv
import hiiragi283.ragium.api.machine.HTClientMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTypeKey
import hiiragi283.ragium.api.model.HTAliasedModel
import hiiragi283.ragium.api.model.HTDefaultProcessorModel
import hiiragi283.ragium.api.renderer.HTMultiblockPreviewRenderer
import hiiragi283.ragium.common.init.RagiumMachineTypes

object RagiumDefaultClientPlugin : RagiumPlugin {
    override val priority: Int = -100

    override fun afterRagiumInit() {}

    override fun shouldLoad(): Boolean = isClientEnv()

    override fun setupClientMachineProperties(helper: RagiumPlugin.PropertyHelper) {
        helper.modify(HTMachineTypeKey::isProcessor) {
            set(HTClientMachinePropertyKeys.STATIC_RENDERER, HTDefaultProcessorModel)
        }
        helper.modify(RagiumMachineTypes.BLAST_FURNACE) {
            set(HTClientMachinePropertyKeys.DYNAMIC_RENDERER, HTMultiblockPreviewRenderer)
        }
        helper.modify(RagiumMachineTypes.DISTILLATION_TOWER) {
            set(HTClientMachinePropertyKeys.DYNAMIC_RENDERER, HTMultiblockPreviewRenderer)
        }
        helper.modify(RagiumMachineTypes.SAW_MILL) {
            set(HTClientMachinePropertyKeys.DYNAMIC_RENDERER, HTMultiblockPreviewRenderer)
        }

        helper.modify(HTMachineTypeKey::isGenerator) {
            set(HTClientMachinePropertyKeys.STATIC_RENDERER, HTAliasedModel(RagiumAPI.id("block/generator")))
        }
        helper.modify(RagiumMachineTypes.Generator.STEAM) {
            set(HTClientMachinePropertyKeys.STATIC_RENDERER, HTDefaultProcessorModel)
        }
        helper.modify(RagiumMachineTypes.Generator.SOLAR) {
            set(HTClientMachinePropertyKeys.STATIC_RENDERER, HTAliasedModel(RagiumAPI.id("block/solar_generator")))
        }
    }
}
