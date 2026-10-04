package hiiragi283.ragium.api.data.chemical

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.ragium.api.data.element.HTElement
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

/**
 * 単体を表す[HTChemical]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@JvmRecord
data class HTSimpleChemical(val element: Holder<HTElement>, val weight: Int) : HTChemical {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTSimpleChemical> = HTCodecs.recordMap { instance ->
            instance.group(
                HTElement.HOLDER_CODEC.fieldOf(HTConstants.ELEMENT).forGetter(HTSimpleChemical::element),
                HTChemical.WEIGHT_CODEC.forGetter(HTSimpleChemical::weight)
            ).apply(instance, ::HTSimpleChemical)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTSimpleChemical> = StreamCodec.composite(
            HTElement.HOLDER_STREAM_CODEC,
            HTSimpleChemical::element,
            ByteBufCodecs.VAR_INT,
            HTSimpleChemical::weight,
            ::HTSimpleChemical
        )

        @JvmField
        val TYPE: HTChemical.Type<HTSimpleChemical> = HTChemical.Type(CODEC, STREAM_CODEC)
    }

    override fun type(): HTChemical.Type<out HTChemical> = TYPE

    override fun getChemicalFormula(): String = "${element.value().symbol}${HTChemical.format(weight)}"
}
