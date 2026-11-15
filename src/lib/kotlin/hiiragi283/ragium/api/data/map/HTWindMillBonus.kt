package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import hiiragi283.lib.serialization.codec.HTCodecs

@JvmRecord
data class HTWindMillBonus(val multiplier: Int) {
    companion object {
        @JvmField
        val CODEC: Codec<HTWindMillBonus> = HTCodecs.record { instance ->
            instance.group(
                HTCodecs.NON_NEGATIVE_INT.fieldOf("multiplier").forGetter(HTWindMillBonus::multiplier),
            ).apply(instance, ::HTWindMillBonus)
        }
    }
}
