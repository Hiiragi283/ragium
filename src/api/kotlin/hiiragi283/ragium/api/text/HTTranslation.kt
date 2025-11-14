package hiiragi283.ragium.api.text

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.MutableComponent

/**
 * 翻訳を保持するインターフェース
 * @see mekanism.api.text.ILangEntry
 */
interface HTTranslation : HTHasTranslationKey {
    fun translate(): MutableComponent = translatableText(this.translationKey)

    fun translate(vararg args: Any?): MutableComponent = HTTextUtil.smartTranslate(this.translationKey, *args)

    fun translateColored(color: ChatFormatting): MutableComponent = translate().withStyle(color)

    fun translateColored(color: ChatFormatting, vararg args: Any?): MutableComponent = translate(*args).withStyle(color)
}
