package hiiragi283.lib.text

import hiiragi283.lib.color.HTDefaultColor
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.extensions.ILevelExtension

//    Text    //

/**
 * [Component]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias Text = Component

/**
 * [MutableComponent]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias MutableText = MutableComponent

/**
 * 指定した[文字列][this]を[テキスト][MutableText]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun String.toText(): MutableText = Text.literal(this)

/**
 * 指定した[文字列][value]を翻訳された[テキスト][MutableText]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun translatableText(value: String): MutableText = Text.translatable(value)

/**
 * 指定した[文字列][value]と[引数][args]を翻訳された[テキスト][MutableText]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun translatableText(value: String, vararg args: Any): MutableText = Text.translatable(value, *args)

/**
 * 指定した[Boolean]を翻訳された[テキスト][MutableText]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun boolText(value: Boolean): MutableText = when (value) {
    true -> HTCommonTranslation.TRUE
    false -> HTCommonTranslation.FALSE
}.translate()

/**
 * 指定した[Direction]を翻訳された[テキスト][MutableText]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun directionText(direction: Direction): MutableText = when (direction) {
    Direction.DOWN -> HTCommonTranslation.DOWN
    Direction.UP -> HTCommonTranslation.UP
    Direction.NORTH -> HTCommonTranslation.NORTH
    Direction.SOUTH -> HTCommonTranslation.SOUTH
    Direction.WEST -> HTCommonTranslation.WEST
    Direction.EAST -> HTCommonTranslation.EAST
}.translate()

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 * @see ILevelExtension.getDescription
 */
fun levelText(key: ResourceKey<Level>): MutableText {
    val location: Identifier = key.identifier()
    return translatableText(location.toLanguageKey(ILevelExtension.TRANSLATION_PREFIX), location.toString())
}

/**
 * 指定した[色][color]を適応します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun MutableText.withStyle(color: TextColor): MutableText = this.withStyle { style: Style -> style.withColor(color) }

/**
 * 指定した[色][color]を適応します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun MutableText.withStyle(color: HTDefaultColor): MutableText = this.withStyle(color.textColor)
