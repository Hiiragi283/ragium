package hiiragi283.ragium.api.text

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.energy.HTEnergyStorage
import hiiragi283.ragium.api.storage.experience.HTExperienceStorage
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
fun longText(value: Long): MutableComponent = Component.literal(NumberFormat.getNumberInstance().format(value))

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
    RagiumTranslation.TOOLTIP_ENERGY_PERCENTAGE.getComponent(intText(amount), intText(capacity))

fun energyText(storage: HTEnergyStorage): MutableComponent = energyText(storage.getAmount(), storage.getCapacity())

fun addEnergyTooltip(storage: HTEnergyStorage, consumer: Consumer<Component>) {
    storage.let(::energyText).let(consumer::accept)
}

private fun experienceText(amount: Long, capacity: Long): MutableComponent =
    RagiumTranslation.TOOLTIP_EXP_PERCENTAGE.getComponent(longText(amount), longText(capacity))

fun experienceText(storage: HTExperienceStorage): MutableComponent = experienceText(storage.getAmount(), storage.getCapacity())

fun addExperienceTooltip(storage: HTExperienceStorage, consumer: Consumer<Component>) {
    storage.let(::experienceText).let(consumer::accept)
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
        consumer.accept(RagiumTranslation.TOOLTIP_FLUID_NAME_EMPTY.getComponent())
        return
    }
    // Fluid Name and Amount
    consumer.accept(RagiumTranslation.TOOLTIP_FLUID_NAME.getComponent(stack.getText(), intText(stack.amount())))
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

fun addFluidTooltip(stacks: Iterable<ImmutableFluidStack?>, consumer: Consumer<Component>, flag: TooltipFlag) {
    for (stack: ImmutableFluidStack? in stacks) {
        addFluidTooltip(stack, consumer, flag, false)
    }
}
