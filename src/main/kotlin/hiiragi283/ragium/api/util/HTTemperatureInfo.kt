package hiiragi283.ragium.api.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.machine.HTMachineTier

data class HTTemperatureInfo(val type: HTTemperatureType, val tier: HTMachineTier) {
    companion object {
        @JvmField
        val CODEC: Codec<HTTemperatureInfo> = RecordCodecBuilder.create { instance ->
            instance.group(
                HTTemperatureType.CODEC.fieldOf("type").forGetter(HTTemperatureInfo::type),
                HTMachineTier.FIELD_CODEC.forGetter(HTTemperatureInfo::tier)
            ).apply(instance, ::HTTemperatureInfo)
        }
        
        @JvmStatic
        fun heating(tier: HTMachineTier): HTTemperatureInfo = HTTemperatureInfo(HTTemperatureType.HEATING, tier)

        @JvmStatic
        fun cooling(tier: HTMachineTier): HTTemperatureInfo = HTTemperatureInfo(HTTemperatureType.COOLING, tier)
    }
}
