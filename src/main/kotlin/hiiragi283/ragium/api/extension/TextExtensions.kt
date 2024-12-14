package hiiragi283.ragium.api.extension

import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.text.*
import net.minecraft.util.Language
import net.minecraft.util.Util
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.GlobalPos
import net.minecraft.world.World
import java.text.NumberFormat

//    Text    //

/**
 * Create a new [MutableText] instance with formatted [value] by [NumberFormat.format]
 */
fun intText(value: Int): MutableText = longText(value.toLong())

/**
 * Create a new [MutableText] instance with formatted [value] by [NumberFormat.format]
 */
fun longText(value: Long): MutableText = Text.literal(NumberFormat.getNumberInstance().format(value))

/**
 * Create a new [MutableText] instance with formatted [value] by [NumberFormat.format]
 */
fun floatText(value: Float): MutableText = doubleText(value.toDouble())

/**
 * Create a new [MutableText] instance with formatted [value] by [NumberFormat.format]
 */
fun doubleText(value: Double): MutableText = Text.literal(NumberFormat.getNumberInstance().format(value))

/**
 * Create a new [MutableText] instance by [Boolean.toString]
 */
fun boolText(value: Boolean): MutableText = Text.literal(value.toString())

fun blockPosText(value: BlockPos): MutableText = Text.literal("[${value.x}, ${value.y}, ${value.z}]")

fun globalPosText(value: GlobalPos): MutableText = Text
    .literal("[")
    .append(
        Texts.join(
            listOf<Text>(
                worldText(value.dimension),
                intText(value.pos.x),
                intText(value.pos.y),
                intText(value.pos.z),
            ),
            Text.literal(", "),
        ),
    ).append("]")

fun worldText(value: RegistryKey<World>): MutableText = Text.translatable(Util.createTranslationKey("world", value.value))

/**
 * Create a new [MutableText] instance with the format: "Amount: x B, y Units"
 */
fun fluidAmountText(value: Long): MutableText = Text.translatable(
    RagiumTranslationKeys.MACHINE_FLUID_AMOUNT,
    NumberFormat.getNumberInstance().format(value / FluidConstants.BUCKET),
    NumberFormat.getNumberInstance().format(value % FluidConstants.BUCKET),
)

fun fluidFilterText(entryList: RegistryEntryList<Fluid>): MutableText = Text.translatable(
    RagiumTranslationKeys.EXPORTER_FLUID_FILTER,
    entryList.asText(Fluid::name),
)

fun itemFilterText(entryList: RegistryEntryList<Item>): MutableText = Text.translatable(
    RagiumTranslationKeys.EXPORTER_ITEM_FILTER,
    entryList.asText(Item::getName),
)

fun Text.hasValidTranslation(): Boolean = (this.content as? TranslatableTextContent)
    ?.let(TranslatableTextContent::getKey)
    ?.let(Language.getInstance()::hasTranslation) == true

inline fun buildStyle(builderAction: Style.() -> Unit): Style = Style.EMPTY.apply(builderAction)
