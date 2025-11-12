package hiiragi283.ragium.api.text

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import net.minecraft.network.chat.contents.TranslatableContents
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

object HTTextUtil {
    @JvmStatic
    private val TEXT_NULL: Component = Component.literal("null")

    /**
     * @see mekanism.api.text.TextComponentUtil.smartTranslate
     */
    @JvmStatic
    fun smartTranslate(key: String, vararg args: Any?): MutableComponent {
        if (args.isEmpty()) {
            return Component.translatable(key)
        } else {
            val formattedArgs: MutableList<Any> = mutableListOf()
            var cachedStyle: Style = Style.EMPTY
            for (arg: Any? in args) {
                if (arg == null) {
                    formattedArgs += TEXT_NULL
                    cachedStyle = Style.EMPTY
                    continue
                }
                var current: MutableComponent? = null
                when (arg) {
                    is Component -> current = arg.copy()
                    // Ragium
                    is HTHasText -> current = arg.getText().copy()
                    is HTHasTranslationKey -> current = Component.translatable(arg.translationKey)
                    // Vanilla
                    is Block -> current = arg.name.copy()
                    is EntityType<*> -> current = arg.description.copy()
                    is Fluid -> current = arg.fluidType.description.copy()
                    is FluidStack -> current = arg.hoverName.copy()
                    is Item -> current = arg.description.copy()
                    is ItemStack -> current = arg.hoverName.copy()
                    is Level -> current = arg.description.copy()
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
                    is String -> current = Component.literal(arg)
                    else -> if (!TranslatableContents.isAllowedPrimitiveArgument(arg)) {
                        current = Component.literal(arg.toString())
                    }
                }

                if (!cachedStyle.isEmpty) {
                    if (current == null) {
                        current = Component.literal(arg.toString())
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
                    lastArg is Component || TranslatableContents.isAllowedPrimitiveArgument(lastArg) -> lastArg
                    else -> lastArg.toString()
                }
            }

            return Component.translatable(key, *formattedArgs.toTypedArray())
        }
    }

    /**
     * @see mekanism.api.text.TextComponentUtil.hasStyleType
     */
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
