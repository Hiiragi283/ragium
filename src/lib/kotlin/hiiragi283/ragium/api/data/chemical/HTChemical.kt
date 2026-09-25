package hiiragi283.ragium.api.data.chemical

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.ragium.api.RagiumRegistries
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

/**
 * 化学物質を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
interface HTChemical {
    companion object {
        @JvmField
        val DISPATCH_CODEC: Codec<HTChemical> =
            RagiumRegistries.CHEMICAL_TYPE.byNameCodec().dispatch(HTChemical::type, Type<*>::codec)

        @JvmField
        val HOLDER_CODEC: Codec<Holder<HTChemical>> = HTCodecs.holder(RagiumRegistries.Keys.CHEMICAL, DISPATCH_CODEC)

        @JvmField
        val DISPATCH_STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTChemical> =
            ByteBufCodecs.registry(RagiumRegistries.Keys.CHEMICAL_TYPE).dispatch(HTChemical::type, Type<*>::streamCodec)

        @JvmField
        val HOLDER_STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Holder<HTChemical>> =
            HTStreamCodecs.holder(RagiumRegistries.Keys.CHEMICAL, DISPATCH_STREAM_CODEC)

        @JvmField
        val WEIGHT_CODEC: MapCodec<Int> = Codec.intRange(1, 99).optionalFieldOf("weight", 1)

        @JvmStatic
        fun format(weight: Int): String = when {
            //値が1の場合はパス
            weight <= 1 -> ""

            else -> buildString {
                //化学式の下付き数字の桁数調整
                val subscript1: Char = '\u2080' + (weight % 10)
                val subscript10: Char = '\u2080' + (weight / 10)
                //2桁目が0でない場合，下付き数字を2桁にする
                this.append(
                    buildString {
                        if (subscript10 != '\u2080') this.append(subscript10)
                        this.append(subscript1)
                    }
                )
            }
        }
    }

    fun type(): Type<out HTChemical>

    fun getChemicalFormula(): String

    @JvmRecord
    data class Type<T : HTChemical>(val codec: MapCodec<T>, val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>)
}
