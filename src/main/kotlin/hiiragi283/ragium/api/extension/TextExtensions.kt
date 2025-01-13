package hiiragi283.ragium.api.extension

import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.core.HolderSet
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentUtils
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.level.material.Fluid
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

fun fluidFilterText(entryList: HolderSet<Fluid>): MutableComponent = Component
    .translatable(
        RagiumTranslationKeys.EXPORTER_FLUID_FILTER,
        entryList.asText(Fluid::name).withStyle(ChatFormatting.DARK_AQUA),
    ).withStyle(ChatFormatting.GRAY)

fun itemFilterText(entryList: HolderSet<Item>): MutableComponent = Component
    .translatable(
        RagiumTranslationKeys.EXPORTER_ITEM_FILTER,
        entryList.asText(Item::getName).withStyle(ChatFormatting.GOLD),
    ).withStyle(ChatFormatting.GRAY)
