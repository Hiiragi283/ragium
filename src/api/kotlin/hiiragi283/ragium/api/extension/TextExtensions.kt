package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.util.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentUtils
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import java.text.NumberFormat
import java.util.function.Consumer

//    Text    //

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
fun boolText(value: Boolean): MutableComponent = Component.literal(value.toString())

fun blockPosText(value: BlockPos): MutableComponent = Component.literal("[${value.x}, ${value.y}, ${value.z}]")

fun globalPosText(value: GlobalPos): MutableComponent = Component
    .literal("[")
    .append(
        ComponentUtils.formatList(
            listOf(
                worldText(value.dimension),
                intText(value.pos.x),
                intText(value.pos.y),
                intText(value.pos.z),
            ),
            Component.literal(", "),
        ),
    ).append("]")

fun worldText(value: ResourceKey<Level>): MutableComponent = Component.translatable(Util.makeDescriptionId("world", value.location()))

/**
 * 指定した[stack]からツールチップを生成します
 * @param consumer 生成したツールチップを受けとるブロック
 */
fun addFluidTooltip(stack: ItemStack, consumer: Consumer<Component>) {
    val fluidHandler: IFluidHandlerItem = stack.getCapability(Capabilities.FluidHandler.ITEM) ?: return
    for (i: Int in fluidHandler.tankRange) {
        addFluidTooltip(fluidHandler.getFluidInTank(i), consumer)
    }
}

/**
 * 指定した[stack]からツールチップを生成します
 * @param consumer 生成したツールチップを受けとるブロック
 */
fun addFluidTooltip(stack: FluidStack, consumer: Consumer<Component>) {
    if (stack.isEmpty) return
    // Name - Amount
    consumer.accept(
        Component
            .translatable(
                RagiumTranslationKeys.TEXT_FLUID_NAME,
                stack.hoverName.copy().withStyle(ChatFormatting.AQUA),
                intText(stack.amount).withStyle(ChatFormatting.GRAY),
            ).withStyle(ChatFormatting.GRAY),
    )
}

/**
 * フォーマットされた容量の[Component]を返します。
 */
fun fluidCapacityText(value: Int): MutableComponent = Component
    .translatable(
        RagiumTranslationKeys.TEXT_FLUID_CAPACITY,
        intText(value).withStyle(ChatFormatting.YELLOW),
    ).withStyle(ChatFormatting.GRAY)
