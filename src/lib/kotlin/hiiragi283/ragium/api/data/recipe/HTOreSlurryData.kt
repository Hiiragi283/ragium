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
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
@JvmRecord
data class HTOreSlurryData(val title: Text, val result: HolderSet<Item>, val fallback: Optional<Holder<Item>>) {
    companion object {
        @JvmField
        val CODEC: Codec<HTOreSlurryData> = HTCodecs.record { instance ->
            instance.group(
                HTCodecs.TEXT.fieldOf("title").forGetter(HTOreSlurryData::title),
                HTCodecs.holderSet(Registries.ITEM).fieldOf(HTConstants.RESULT).forGetter(HTOreSlurryData::result),
                HTCodecs.holder(Registries.ITEM).optionalFieldOf("fallback").forGetter(HTOreSlurryData::fallback)
            ).apply(instance, ::HTOreSlurryData)
        }
    }

    constructor(title: Text, result: HolderSet<Item>, fallback: Holder<Item>?) : this(
        title,
        result,
        fallback.toOptional()
    )

    fun getFirstHolder(): Holder<Item>? = result
        .asSequence()
        .sortedWith(HTItemResult.TagEntry.HOLDER_COMPARATOR)
        .firstOrNull()
        ?: fallback.getOrNull()
}
