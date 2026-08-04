@file:Suppress("DEPRECATION")

package hiiragi283.lib.text

import hiiragi283.lib.HTConstants
import java.text.NumberFormat
import net.minecraft.ChatFormatting
import net.minecraft.core.Direction
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import net.minecraft.network.chat.contents.TranslatableContents
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModList
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforgespi.language.IModInfo
import org.apache.commons.lang3.math.Fraction
import org.apache.commons.lang3.text.WordUtils

/**
 * [テキスト][Text]に関するメソッドを集めたクラスです。
 *
 * 参照 : [Mekanism - TextComponentUtil](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/text/TextComponentUtil.java)
 *       [Mekanism - TextUtils](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/util/text/TextUtils.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
object HTTextUtil {
    /**
     * 引数が`null`の時に置き換えられる[テキスト][Text]
     */
    @JvmStatic
    private val TEXT_NULL: Text = "null".toText()

    @JvmStatic
    private val INT_FORMAT: NumberFormat = NumberFormat.getIntegerInstance()

    @JvmStatic
    private val DOUBLE_FORMAT: NumberFormat = NumberFormat.getNumberInstance()

    /**
     * 指定した[MOD ID][modId]からMOD名を取得します。
     * @return [MOD ID][modId]が["common"][HTConstants.COMMON]の場合は`Common`，それ以外の場合は登録されたMODから取得した値
     */
    @JvmStatic
    fun getModName(modId: String): String = when (modId) {
        HTConstants.COMMON -> "Common"
        else ->
            ModList
                .get()
                .getModContainerById(modId)
                .map(ModContainer::getModInfo)
                .map(IModInfo::getDisplayName)
                .orElse(WordUtils.capitalizeFully(modId.replace(oldChar = '_', newChar = ' ')))
    }

    @JvmStatic
    fun getModNameText(modId: String): MutableText = getModName(modId).toText()

    /**
     * 指定した[翻訳キー][key]と[引数][args]をいい感じにして[テキスト][MutableText]に変換します。
     * @return いい感じになった[テキスト][MutableText]
     */
    @JvmStatic
    fun smartTranslate(key: String, vararg args: Any?): MutableText {
        if (args.isEmpty()) {
            return translatableText(key)
        } else {
            val formattedArgs: MutableList<Any> = mutableListOf()
            var cachedStyle: Style = Style.EMPTY
            for (arg: Any? in args) {
                if (arg == null) {
                    formattedArgs += TEXT_NULL
                    cachedStyle = Style.EMPTY
                    continue
                }
                var current: MutableText? = null
                when (arg) {
                    is Text -> current = arg.copy()
                    // Ragium
                    is HTHasText -> current = arg.getText().copy()
                    is HTHasTranslationKey -> current = translatableText(arg.translationKey)
                    // Vanilla
                    is Block -> current = arg.name.copy()
                    is EntityType<*> -> current = arg.description.copy()
                    is Fluid -> current = arg.fluidType.description.copy()
                    is FluidStack -> current = arg.hoverName.copy()
                    is Direction -> current = directionText(arg)
                    is Item -> current = ItemStack(arg).hoverName.copy()
                    is ItemStack -> current = arg.hoverName.copy()
                    is Level -> current = arg.description.copy()
                    // Primitive
                    is Int -> current = INT_FORMAT.format(arg.toLong()).toText()
                    is Long -> current = INT_FORMAT.format(arg).toText()
                    is Float -> current = DOUBLE_FORMAT.format(arg.toDouble()).toText()
                    is Double -> current = DOUBLE_FORMAT.format(arg).toText()
                    is Boolean -> current = boolText(arg)
                    is Fraction -> current = DOUBLE_FORMAT.format(arg.toDouble()).toText()
                    // Formatting
                    is TextColor -> {
                        if (cachedStyle.color == null) {
                            cachedStyle = cachedStyle.withColor(arg)
                            continue
                        }
                    }

                    is ChatFormatting -> {
                        if (!hasStyle(cachedStyle, arg)) {
                            cachedStyle = cachedStyle.applyFormat(arg)
                            continue
                        }
                    }

                    is ClickEvent -> {
                        if (cachedStyle.clickEvent == null) {
                            cachedStyle = cachedStyle.withClickEvent(arg)
                            continue
                        }
                    }

                    is HoverEvent -> {
                        if (cachedStyle.hoverEvent == null) {
                            cachedStyle = cachedStyle.withHoverEvent(arg)
                            continue
                        }
                    }
                    // Other
                    is String -> current = arg.toText()
                    else -> if (!TranslatableContents.isAllowedPrimitiveArgument(arg)) {
                        current = arg.toString().toText()
                    }
                }

                if (!cachedStyle.isEmpty) {
                    if (current == null) {
                        current = arg.toString().toText()
                    }
                    formattedArgs += current.setStyle(cachedStyle)
                    cachedStyle = Style.EMPTY
                } else {
                    formattedArgs += (current ?: arg)
                }
            }

            if (!cachedStyle.isEmpty) {
                val lastArg: Any? = args.lastOrNull()
                formattedArgs += when {
                    lastArg == null -> TEXT_NULL
                    lastArg is Text || TranslatableContents.isAllowedPrimitiveArgument(lastArg) -> lastArg
                    else -> lastArg.toString()
                }
            }

            return translatableText(key, *formattedArgs.toTypedArray())
        }
    }

    @JvmStatic
    private fun hasStyle(style: Style, formatting: ChatFormatting): Boolean = when (formatting) {
        ChatFormatting.OBFUSCATED -> style.isObfuscated
        ChatFormatting.BOLD -> style.isBold
        ChatFormatting.STRIKETHROUGH -> style.isStrikethrough
        ChatFormatting.UNDERLINE -> style.isUnderlined
        ChatFormatting.ITALIC -> style.isItalic
        ChatFormatting.RESET -> style.isEmpty
        else -> style.color != null
    }
}
