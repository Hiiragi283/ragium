package hiiragi283.ragium.config

import hiiragi283.ragium.api.config.HTDoubleConfigValue
import hiiragi283.ragium.api.config.definePositiveDouble
import net.neoforged.neoforge.common.ModConfigSpec

class RagiumClientConfig(builder: ModConfigSpec.Builder) {
    // Renderer
    @JvmField
    val gogglesRendererHeight: HTDoubleConfigValue

    init {
        // Renderer
        builder.push("renderer")
        gogglesRendererHeight = builder.definePositiveDouble("gogglesRendererHeight", -0.25, -1.0, 1.0)
        builder.pop()
    }
}
