package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.ExtraCodecs

@JvmInline
value class HTFluidFuelData(val time: Int) {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidFuelData> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("time").forGetter(HTFluidFuelData::time),
                ).apply(instance, ::HTFluidFuelData)
        }
    }
}
