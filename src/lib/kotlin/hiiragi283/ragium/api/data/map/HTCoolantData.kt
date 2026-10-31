package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import hiiragi283.lib.serialization.codec.HTCodecs

@JvmRecord
data class HTCoolantData(val mbPerTick: Int) {
    companion object {
        @JvmField
        val CODEC: Codec<HTCoolantData> = HTCodecs.record { instance ->
            instance.group(HTCodecs.POSITIVE_INT.fieldOf("mb/tick").forGetter(HTCoolantData::mbPerTick)).apply(instance, ::HTCoolantData)
        }
    }
}
