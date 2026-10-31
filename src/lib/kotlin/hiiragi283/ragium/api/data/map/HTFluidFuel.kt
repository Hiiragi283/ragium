package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import hiiragi283.lib.serialization.codec.HTCodecs

@JvmRecord
data class HTFluidFuel(val tickPerMb: Int) {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidFuel> = HTCodecs.record { instance ->
            instance.group(HTCodecs.POSITIVE_INT.fieldOf("tick/mb").forGetter(HTFluidFuel::tickPerMb)).apply(instance, ::HTFluidFuel)
        }
    }
}
