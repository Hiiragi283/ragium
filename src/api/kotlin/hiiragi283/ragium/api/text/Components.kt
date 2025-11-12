package hiiragi283.ragium.api.text

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.experience.HTExperienceTank
import hiiragi283.ragium.api.storage.fluid.HTFluidView
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentUtils
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.neoforged.fml.ModList
import net.neoforged.neoforge.common.extensions.ILevelExtension
import net.neoforged.neoforgespi.language.IModInfo
import java.text.NumberFormat
import java.util.function.Consumer

//    Text    //

fun bracketText(text: Component): MutableComponent = Component.literal("[").append(text).append("]")

fun joinedText(vararg texts: Component): Component = ComponentUtils.formatList(listOf(*texts), Component.literal(", "))

/**
 * フォーマットされた[Int]の[Component]を返します。
 */
fun intText(value: Int): MutableComponent = longText(value.toLong())

/**
 * フォーマットされた[Long]の[Component]を返します。
 */
fun longText(value: Long): MutableComponent = Component.literal(NumberFormat.getIntegerInstance().format(value))

/**
 * フォーマットされた[Float]の[Component]を返します。
 */
fun floatText(value: Float): MutableComponent = doubleText(value.toDouble())

/**
 * フォーマットされた[Double]の[Component]を返します。
 */
fun doubleText(value: Double): MutableComponent = Component.literal(NumberFormat.getNumberInstance().format(value))

/**
 * フォーマットされた[Boolean]の[Component]を返します。
 */
fun boolText(value: Boolean): MutableComponent = stringText(value)

/**
 * 指定された[value]を[Any.toString]に基づいて[Component]に変換します。
 */
fun stringText(value: Any?): MutableComponent = Component.literal(value.toString())

fun blockPosText(value: BlockPos): MutableComponent = bracketText(joinedText(intText(value.x), intText(value.y), intText(value.z)))

fun levelText(key: ResourceKey<Level>): MutableComponent {
    val location: ResourceLocation = key.location()
    return Component.translatable(
        location.toLanguageKey(ILevelExtension.TRANSLATION_PREFIX),
        location.toString(),
    )
}

private fun energyText(amount: Int, capacity: Int): MutableComponent =
    RagiumTranslation.TOOLTIP_ENERGY_PERCENTAGE.translate(intText(amount), intText(capacity))

fun energyText(battery: HTEnergyBattery): MutableComponent = energyText(battery.getAmount(), battery.getCapacity())

fun addEnergyTooltip(battery: HTEnergyBattery, consumer: Consumer<Component>) {
    battery.let(::energyText).let(consumer::accept)
}

private fun experienceText(amount: Long, capacity: Long): MutableComponent =
    RagiumTranslation.TOOLTIP_EXP_PERCENTAGE.translate(longText(amount), longText(capacity))

fun experienceText(tank: HTExperienceTank): MutableComponent = experienceText(tank.getAmount(), tank.getCapacity())

fun addExperienceTooltip(tank: HTExperienceTank, consumer: Consumer<Component>) {
    tank.let(::experienceText).let(consumer::accept)
}

/**
 * 指定した[stack]からツールチップを生成します
 * @param consumer 生成したツールチップを受けとるブロック
 */
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
        consumer.accept(Component.literal(stack.holder().registeredName).withStyle(ChatFormatting.DARK_GRAY))
    }
    // Mod Name
    val firstMod: IModInfo = ModList
        .get()
        .getModFileById(stack.getId().namespace)
        .mods
        .firstOrNull() ?: return
    consumer.accept(Component.literal(firstMod.displayName).withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC))
}

fun addFluidTooltip(views: Iterable<HTFluidView>, consumer: Consumer<Component>, flag: TooltipFlag) {
    for (view: HTFluidView in views) {
        addFluidTooltip(view.getStack(), consumer, flag, false)
    }
}

fun addDescription(translation: HTTranslation, tooltips: Consumer<Component>, flag: TooltipFlag) {
    if (flag.hasShiftDown()) {
        tooltips.accept(translation.translate())
    } else {
        tooltips.accept(RagiumTranslation.TOOLTIP_SHOW_DESCRIPTION.translateColored(ChatFormatting.YELLOW))
    }
}
