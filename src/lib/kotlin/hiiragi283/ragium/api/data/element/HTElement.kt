package hiiragi283.ragium.api.data.element

import com.mojang.serialization.Codec
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.ragium.api.RagiumRegistries
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ExtraCodecs

/**
 * 元素を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@JvmRecord
data class HTElement(val symbol: String) {
    companion object {
        @JvmField
        val DIRECT_CODEC: Codec<HTElement> = HTCodecs.record { instance ->
            instance.group(
                ExtraCodecs.NON_EMPTY_STRING.fieldOf("symbol").forGetter(HTElement::symbol)
            ).apply(instance, ::HTElement)
        }

        @JvmField
        val HOLDER_CODEC: Codec<Holder<HTElement>> = HTCodecs.holder(RagiumRegistries.Keys.ELEMENT, DIRECT_CODEC)

        @JvmField
        val DIRECT_STREAM_CODEC: StreamCodec<ByteBuf, HTElement> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTElement::symbol,
            ::HTElement
        )

        @JvmField
        val HOLDER_STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Holder<HTElement>> =
            HTStreamCodecs.holder(RagiumRegistries.Keys.ELEMENT, DIRECT_STREAM_CODEC)
    }
}
