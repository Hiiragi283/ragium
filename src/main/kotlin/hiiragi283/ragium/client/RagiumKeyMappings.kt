package hiiragi283.ragium.client

import com.mojang.blaze3d.platform.InputConstants
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.settings.KeyConflictContext
import org.lwjgl.glfw.GLFW

@OnlyIn(Dist.CLIENT)
object RagiumKeyMappings {
    @JvmField
    val OPEN_UNIVERSAL_BUNDLE: HTKeyMapping = createKey("open_universal_bundle", GLFW.GLFW_KEY_U)

    @JvmField
    val KEYS: List<HTKeyMapping> = listOf(
        OPEN_UNIVERSAL_BUNDLE,
    )

    @JvmStatic
    private fun createKey(name: String, keyCode: Int, conflict: KeyConflictContext = KeyConflictContext.IN_GAME): HTKeyMapping =
        HTKeyMapping(name, conflict, InputConstants.Type.KEYSYM, keyCode)
}
