package hiiragi283.ragium.api.extension

import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentUtils
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidType
import java.text.NumberFormat

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
 * フォーマットされた液体量の[Component]を返します。
 *
 * フォーマットは次に従います: `Amount: x B, y mb`
 */
fun fluidAmountText(value: Long): MutableComponent = Component.translatable(
    RagiumTranslationKeys.FORMATTED_FLUID,
    NumberFormat.getNumberInstance().format(value / FluidType.BUCKET_VOLUME),
    NumberFormat.getNumberInstance().format(value % FluidType.BUCKET_VOLUME),
)
