package hiiragi283.ragium.api.text

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.experience.HTExperienceTank
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
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.fml.ModList
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforgespi.language.IModInfo
import java.util.function.Consumer

/**
 * @see mekanism.api.text.TextComponentUtil
 * @see mekanism.common.util.text.TextUtils
 */
object HTTextUtil {
    @JvmStatic
    private val TEXT_NULL: Component = literalText("null")

    @JvmStatic
    fun addEnergyTooltip(battery: HTEnergyBattery, consumer: Consumer<Component>) {
        battery.let(::energyText).let(consumer::accept)
    }

    @JvmStatic
    fun addExperienceTooltip(tank: HTExperienceTank, consumer: Consumer<Component>) {
        tank.let(::experienceText).let(consumer::accept)
    }

    @JvmStatic
    fun addFluidTooltip(
        stack: ImmutableFluidStack?,
        consumer: Consumer<Component>,
        flag: TooltipFlag,
        inGui: Boolean,
    ) {
        // Empty name if stack is empty
        if (stack == null) {
            consumer.accept(RagiumTranslation.EMPTY.translate())
            return
        }
        // Fluid Name and Amount
        consumer.accept(RagiumTranslation.STORED_MB.translate(stack, intText(stack.amount())))
        if (!inGui) return
        // Fluid id if advanced
        if (flag.isAdvanced) {
            consumer.accept(literalText(stack.holder().registeredName).withStyle(ChatFormatting.DARK_GRAY))
        }
        // Mod Name
        val firstMod: IModInfo = ModList
            .get()
            .getModFileById(stack.getId().namespace)
            .mods
            .firstOrNull() ?: return
        consumer.accept(literalText(firstMod.displayName).withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC))
    }

    /**
     * @see mekanism.api.text.TextComponentUtil.smartTranslate
     */
    @JvmStatic
    fun smartTranslate(key: String, vararg args: Any?): MutableComponent {
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
                var current: MutableComponent? = null
                when (arg) {
                    is Component -> current = arg.copy()
                    // Ragium
                    is HTHasText -> current = arg.getText().copy()
                    is HTHasTranslationKey -> current = translatableText(arg.translationKey)
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
                    is String -> current = literalText(arg)
                    else -> if (!TranslatableContents.isAllowedPrimitiveArgument(arg)) {
                        current = literalText(arg.toString())
                    }
                }

                if (!cachedStyle.isEmpty) {
                    if (current == null) {
                        current = literalText(arg.toString())
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

            return translatableText(key, *formattedArgs.toTypedArray())
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
