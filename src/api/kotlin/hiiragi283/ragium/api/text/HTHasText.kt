package hiiragi283.ragium.api.text

import net.minecraft.network.chat.Component

/**
 * @see [mekanism.api.text.IHasTextComponent]
 */
fun interface HTHasText {
    fun getText(): Component
}
