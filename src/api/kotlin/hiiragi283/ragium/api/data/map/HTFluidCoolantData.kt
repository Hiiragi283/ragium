package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.ExtraCodecs

@JvmInline
value class HTFluidCoolantData(val amount: Int) {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidCoolantData> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(HTFluidCoolantData::amount),
                ).apply(instance, ::HTFluidCoolantData)
        }
    }
}
