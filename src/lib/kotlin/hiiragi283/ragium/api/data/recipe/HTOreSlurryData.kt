package hiiragi283.ragium.api.data.recipe

import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.text.Text
import hiiragi283.lib.util.toOptional
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStackTemplate
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
@JvmRecord
data class HTOreSlurryData @JvmOverloads constructor(
    val title: Text,
    val result: HolderSet<Item>,
    val fallback: Optional<Holder<Item>>,
    val count: Int = 1
) {
    companion object {
        @JvmField
        val CODEC: Codec<HTOreSlurryData> = HTCodecs.record { instance ->
            instance.group(
                HTCodecs.TEXT.fieldOf("title").forGetter(HTOreSlurryData::title),
                HTCodecs.holderSet(Registries.ITEM).fieldOf(HTConstants.RESULT).forGetter(HTOreSlurryData::result),
                Item.CODEC.optionalFieldOf("fallback").forGetter(HTOreSlurryData::fallback),
                HTCodecs.POSITIVE_INT.optionalFieldOf(HTConstants.COUNT, 1).forGetter(HTOreSlurryData::count)
            ).apply(instance, ::HTOreSlurryData)
        }
    }

    @JvmOverloads constructor(title: Text, result: HolderSet<Item>, fallback: Holder<Item>?, count: Int = 1) : this(
        title,
        result,
        fallback.toOptional(),
        count
    )

    fun getFirstHolder(): Holder<Item>? = result
        .asSequence()
        .sortedWith(HTItemResult.TagEntry.HOLDER_COMPARATOR)
        .firstOrNull()
        ?: fallback.getOrNull()

    fun createTemplate(): ItemStackTemplate? = getFirstHolder()?.let { ItemStackTemplate(it, count) }
}
