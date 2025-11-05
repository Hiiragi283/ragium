package hiiragi283.ragium.api.text

import net.minecraft.network.chat.Component

/**
 * [Component]を保持するインターフェース
 * @see mekanism.api.text.IHasTextComponent
 */
fun interface HTHasText {
    fun getText(): Component
}
