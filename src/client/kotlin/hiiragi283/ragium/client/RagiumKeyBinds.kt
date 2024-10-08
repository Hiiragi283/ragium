package hiiragi283.ragium.client

import hiiragi283.ragium.api.RagiumAPI
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import org.lwjgl.glfw.GLFW

object RagiumKeyBinds {
    const val CATEGORY: String = "category.${RagiumAPI.MOD_ID}.${RagiumAPI.MOD_ID}"

    @JvmField
    val OPEN_BACKPACK: KeyBinding = KeyBindingHelper.registerKeyBinding(
        KeyBinding(keyId("open_backpack"), GLFW.GLFW_KEY_O, CATEGORY),
    )

    @JvmStatic
    private fun keyId(name: String): String = "key.${RagiumAPI.MOD_ID}.$name"
}
