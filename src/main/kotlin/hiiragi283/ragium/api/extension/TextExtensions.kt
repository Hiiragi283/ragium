package hiiragi283.ragium.api.extension

import net.minecraft.registry.RegistryKey
import net.minecraft.text.*
import net.minecraft.util.Language
import net.minecraft.util.Util
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.GlobalPos
import net.minecraft.world.World
import java.text.NumberFormat

//    Text    //

fun intText(value: Int): MutableText = longText(value.toLong())

fun longText(value: Long): MutableText = Text.literal(NumberFormat.getNumberInstance().format(value))

fun floatText(value: Float): MutableText = doubleText(value.toDouble())

fun doubleText(value: Double): MutableText = Text.literal(NumberFormat.getNumberInstance().format(value))

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

fun Text.hasValidTranslation(): Boolean = (this.content as? TranslatableTextContent)
    ?.let(TranslatableTextContent::getKey)
    ?.let(Language.getInstance()::hasTranslation) == true

inline fun buildStyle(builderAction: Style.() -> Unit): Style = Style.EMPTY.apply(builderAction)
