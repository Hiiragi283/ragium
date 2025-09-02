package hiiragi283.ragium.api.util.translate

import net.minecraft.network.chat.Component

/**
 * @see [mekanism.api.text.IHasTextComponent]
 */
fun interface HTHasComponent {
    fun getComponent(): Component
}
