package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTPropertyKey
import hiiragi283.ragium.api.renderer.HTMachineEntityRenderer
import hiiragi283.ragium.client.model.HTProcessorMachineModel
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
object HTClientMachinePropertyKeys {
    @JvmField
    val DYNAMIC_RENDERER: HTPropertyKey.Simple<HTMachineEntityRenderer> =
        HTPropertyKey.Simple(RagiumAPI.id("dynamic_renderer"))

    @JvmField
    val MODEL_ID: HTPropertyKey.Defaulted<Identifier> =
        HTPropertyKey.Defaulted(RagiumAPI.id("model_id")) { HTProcessorMachineModel.MODEL_ID }
}
