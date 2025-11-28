package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.ExtraCodecs

@JvmInline
value class HTFluidFuelData(val energy: Int) {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidFuelData> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("energy").forGetter(HTFluidFuelData::energy),
                ).apply(instance, ::HTFluidFuelData)
        }
    }
}
