package hiiragi283.ragium.api.text

import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.ImmutableFluidStack
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
import net.neoforged.neoforge.energy.IEnergyStorage
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

private fun energyText(amount: Long, capacity: Long): MutableComponent =
    RagiumTranslation.TOOLTIP_ENERGY_PERCENTAGE.getComponent(longText(amount), longText(capacity))

fun energyText(storage: IEnergyStorage): MutableComponent = energyText(storage.energyStored, storage.maxEnergyStored)

fun energyText(battery: HTEnergyBattery): MutableComponent = energyText(battery.getAmountAsLong(), battery.getCapacityAsLong())

fun addEnergyTooltip(battery: HTEnergyBattery, consumer: Consumer<Component>) {
    battery.let(::energyText).let(consumer::accept)
}

/**
 * 指定した[stack]からツールチップを生成します
 * @param consumer 生成したツールチップを受けとるブロック
 */
fun addFluidTooltip(
    stack: ImmutableFluidStack,
    consumer: Consumer<Component>,
    flag: TooltipFlag,
    inGui: Boolean,
) {
    // Empty name if stack is empty
    if (stack.isEmpty()) {
        consumer.accept(RagiumTranslation.TOOLTIP_FLUID_NAME_EMPTY.getComponent())
        return
    }
    // Fluid Name and Amount
    consumer.accept(
        RagiumTranslation.TOOLTIP_FLUID_NAME.getComponent(
            stack.getText(),
            longText(stack.amountAsLong()),
        ),
    )
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

fun addFluidTooltip(handler: HTFluidHandler, consumer: Consumer<Component>, flag: TooltipFlag) {
    for (tank: HTFluidTank in handler.getFluidTanks(handler.getFluidSideFor())) {
        addFluidTooltip(tank.getStack(), consumer, flag, false)
    }
}
