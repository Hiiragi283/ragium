package hiiragi283.ragium.api.text

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.extensions.ILevelExtension
import java.text.NumberFormat

//    Text    //

fun literalText(value: String): MutableComponent = Component.literal(value)

fun translatableText(value: String): MutableComponent = Component.translatable(value)

fun translatableText(value: String, vararg args: Any): MutableComponent = Component.translatable(value, *args)

/**
 * フォーマットされた[Int]の[Component]を返します。
 */
fun intText(value: Int): MutableComponent = longText(value.toLong())

/**
 * フォーマットされた[Long]の[Component]を返します。
 */
fun longText(value: Long): MutableComponent = literalText(NumberFormat.getIntegerInstance().format(value))

/**
 * フォーマットされた[Float]の[Component]を返します。
 */
fun floatText(value: Float): MutableComponent = doubleText(value.toDouble())

/**
 * フォーマットされた[Double]の[Component]を返します。
 */
fun doubleText(value: Double): MutableComponent = literalText(NumberFormat.getNumberInstance().format(value))

/**
 * フォーマットされた[Boolean]の[Component]を返します。
 */
fun boolText(value: Boolean): MutableComponent = when (value) {
    true -> RagiumTranslation.TRUE
    false -> RagiumTranslation.FALSE
}.translate()

/**
 * @see ILevelExtension.getDescription
 */
fun levelText(key: ResourceKey<Level>): MutableComponent {
    val location: ResourceLocation = key.location()
    return translatableText(location.toLanguageKey(ILevelExtension.TRANSLATION_PREFIX), location.toString())
}
