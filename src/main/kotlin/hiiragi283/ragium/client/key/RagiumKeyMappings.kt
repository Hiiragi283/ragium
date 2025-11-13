package hiiragi283.ragium.client.key

import hiiragi283.ragium.client.text.RagiumClientTranslation
import net.minecraft.client.KeyMapping
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.lwjgl.glfw.GLFW

@OnlyIn(Dist.CLIENT)
object RagiumKeyMappings {
    @JvmField
    val OPEN_UNIVERSAL_BUNDLE: KeyMapping = HTKeyMappingBuilder()
        .description(RagiumClientTranslation.KEY_OPEN_UNIVERSAL_BUNDLE)
        .conflictInGame()
        .keyCode(GLFW.GLFW_KEY_U)
        .build()
}
