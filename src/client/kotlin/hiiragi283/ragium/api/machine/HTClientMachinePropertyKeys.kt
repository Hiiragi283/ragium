package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTPropertyKey
import hiiragi283.ragium.api.renderer.HTDynamicMachineRenderer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Environment(EnvType.CLIENT)
object HTClientMachinePropertyKeys {
    @JvmField
    val DYNAMIC_RENDERER: HTPropertyKey.Simple<HTDynamicMachineRenderer> =
        HTPropertyKey.ofSimple(RagiumAPI.id("dynamic_renderer"))
}
