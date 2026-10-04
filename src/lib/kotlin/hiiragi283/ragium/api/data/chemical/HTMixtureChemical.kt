package hiiragi283.ragium.api.data.chemical

import com.mojang.serialization.MapCodec
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.nelOf
import hiiragi283.ragium.api.data.element.HTElement
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

/**
 * 混合物を表す[HTChemical]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@JvmRecord
data class HTMixtureChemical(val elements: Nel<Holder<HTElement>>) : HTChemical {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMixtureChemical> = HTElement.HOLDER_CODEC
            .compactNelFieldOf("element", "elements")
            .xmap(::HTMixtureChemical, HTMixtureChemical::elements)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTMixtureChemical> =
            HTElement.HOLDER_STREAM_CODEC.nelOf().map(::HTMixtureChemical, HTMixtureChemical::elements)

        @JvmField
        val TYPE: HTChemical.Type<HTMixtureChemical> = HTChemical.Type(CODEC, STREAM_CODEC)
    }

    override fun type(): HTChemical.Type<out HTChemical> = TYPE

    override fun getChemicalFormula(): String = elements.joinToString(prefix = "(", postfix = ")")
}
