package hiiragi283.ragium.api.text

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

/**
 * @see [mekanism.api.text.ILangEntry]
 */
interface HTTranslation : HTHasTranslationKey {
    fun getComponent(): MutableComponent = Component.translatable(translationKey)

    fun getComponent(vararg args: Any): MutableComponent = Component.translatable(translationKey, *args)

    fun getColoredComponent(color: ChatFormatting): MutableComponent = getComponent().withStyle(color)

    fun getColoredComponent(color: ChatFormatting, vararg args: Any): MutableComponent = getComponent(*args).withStyle(color)
}
