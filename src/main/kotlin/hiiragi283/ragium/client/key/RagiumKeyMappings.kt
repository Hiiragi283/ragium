package hiiragi283.ragium.client.key

import hiiragi283.ragium.common.text.RagiumCommonTranslation
import net.minecraft.client.KeyMapping
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.lwjgl.glfw.GLFW

@OnlyIn(Dist.CLIENT)
object RagiumKeyMappings {
    @JvmField
    val OPEN_UNIVERSAL_BUNDLE: KeyMapping = HTKeyMappingBuilder()
        .description(RagiumCommonTranslation.KEY_OPEN_UNIVERSAL_BUNDLE)
        .conflictInGame()
        .keyCode(GLFW.GLFW_KEY_U)
        .build()
}
