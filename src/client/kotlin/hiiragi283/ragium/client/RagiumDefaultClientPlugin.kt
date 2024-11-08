package hiiragi283.ragium.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.extension.isClientEnv
import hiiragi283.ragium.api.machine.HTClientMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.renderer.HTMultiblockPreviewRenderer
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Environment(EnvType.CLIENT)
object RagiumDefaultClientPlugin : RagiumPlugin {
    override val priority: Int = -100

    override fun afterRagiumInit(instance: RagiumAPI) {}

    override fun shouldLoad(): Boolean = isClientEnv()

    override fun setupClientMachineProperties(helper: RagiumPlugin.PropertyHelper<HTMachineKey>) {
        helper.modify(RagiumMachineKeys.BLAST_FURNACE) {
            set(HTClientMachinePropertyKeys.DYNAMIC_RENDERER, HTMultiblockPreviewRenderer)
        }
        helper.modify(RagiumMachineKeys.DISTILLATION_TOWER) {
            set(HTClientMachinePropertyKeys.DYNAMIC_RENDERER, HTMultiblockPreviewRenderer)
        }
        helper.modify(RagiumMachineKeys.FLUID_DRILL) {
            set(HTClientMachinePropertyKeys.DYNAMIC_RENDERER, HTMultiblockPreviewRenderer)
        }
        helper.modify(RagiumMachineKeys.SAW_MILL) {
            set(HTClientMachinePropertyKeys.DYNAMIC_RENDERER, HTMultiblockPreviewRenderer)
        }
    }
}
