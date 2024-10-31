package hiiragi283.ragium.client.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTPropertyKey
import hiiragi283.ragium.client.renderer.HTMachineEntityRenderer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel

@Environment(EnvType.CLIENT)
object HTClientMachinePropertyKeys {
    @JvmField
    val DYNAMIC_RENDERER: HTPropertyKey.Simple<HTMachineEntityRenderer> =
        HTPropertyKey.Simple(RagiumAPI.id("dynamic_renderer"))

    @JvmField
    val STATIC_RENDERER: HTPropertyKey.Simple<FabricBakedModel> =
        HTPropertyKey.Simple(RagiumAPI.id("static_renderer"))
}
