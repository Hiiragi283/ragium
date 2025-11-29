package hiiragi283.ragium.api.item.alchemy

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs.holder
import hiiragi283.ragium.api.util.wrapOptional
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * @see PotionContents
 */
@JvmRecord
data class HTPotionContents(private val potion: Optional<Holder<Potion>>, private val instances: List<HTMobEffectInstance>) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTPotionContents> = BiCodec.composite(
            holder(Registries.POTION).optionalFieldOf("potion"),
            HTPotionContents::potion,
            HTMobEffectInstance.CODEC.listOf().optionalFieldOf("custom_effects", listOf()),
            HTPotionContents::instances,
            ::HTPotionContents,
        )

        @JvmStatic
        fun fromVanilla(contents: PotionContents): HTPotionContents {
            val instances: List<HTMobEffectInstance> = contents.customEffects().map(::HTMobEffectInstance)
            return HTPotionContents(contents.potion(), instances)
        }
    }

    constructor(potion: Holder<Potion>) : this(potion.wrapOptional(), listOf())

    constructor(instances: List<HTMobEffectInstance>) : this(Optional.empty(), instances)

    constructor(vararg instances: HTMobEffectInstance) : this(listOf(*instances))

    fun isEmpty(): Boolean = potion.isEmpty && instances.isEmpty()

    fun isOf(contents: PotionContents): Boolean = contents == this.toVanilla()

    fun toVanilla(): PotionContents = HTPotionHelper.contents(potion.getOrNull(), null, instances)
}
