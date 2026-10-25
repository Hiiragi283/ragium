package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import hiiragi283.lib.serialization.codec.HTCodecs

@JvmRecord
data class HTFluidFuel(val tickPerMb: Int) {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidFuel> = HTCodecs.POSITIVE_INT.xmap(::HTFluidFuel, HTFluidFuel::tickPerMb)
    }
}
