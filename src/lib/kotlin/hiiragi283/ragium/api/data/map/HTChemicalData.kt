package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.ragium.api.data.chemical.HTChemical
import net.minecraft.core.Holder
import net.minecraft.util.ExtraCodecs

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
@JvmRecord
data class HTChemicalData(val chemical: Holder<HTChemical>, val scale: Float) {
    companion object {
        @JvmField
        val CODEC: Codec<HTChemicalData> = HTCodecs.record { instance ->
            instance.group(
                HTChemical.HOLDER_CODEC.fieldOf(HTConstants.CHEMICAL).forGetter(HTChemicalData::chemical),
                ExtraCodecs.POSITIVE_FLOAT.fieldOf("scale").forGetter(HTChemicalData::scale)
            ).apply(instance, ::HTChemicalData)
        }
    }

    constructor(chemical: Holder<HTChemical>, scale: Int) : this(chemical, scale.toFloat())
}
