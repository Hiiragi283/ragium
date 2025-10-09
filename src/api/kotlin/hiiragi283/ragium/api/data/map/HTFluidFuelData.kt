package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.ExtraCodecs

@JvmRecord
data class HTFluidFuelData(val amount: Int) {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidFuelData> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(HTFluidFuelData::amount),
                ).apply(instance, ::HTFluidFuelData)
        }
    }
}
