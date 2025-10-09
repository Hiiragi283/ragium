package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.ExtraCodecs

@JvmRecord
data class HTSolarPower(val multiplier: Float) {
    companion object {
        @JvmField
        val CODEC: Codec<HTSolarPower> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("multiplier").forGetter(HTSolarPower::multiplier),
                ).apply(instance, ::HTSolarPower)
        }
    }
}
