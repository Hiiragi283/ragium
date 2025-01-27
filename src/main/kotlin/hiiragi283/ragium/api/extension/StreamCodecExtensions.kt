package hiiragi283.ragium.api.extension

import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable

//    StreamCodec    //

/**
 * [List]の[StreamCodec]に変換します。
 */
fun <B : ByteBuf, V : Any> StreamCodec<B, V>.toList(): StreamCodec<B, List<V>> = apply(ByteBufCodecs.list())

/**
 * [Set]の[StreamCodec]に変換します。
 */
fun <B : ByteBuf, V : Any> StreamCodec<B, V>.toSet(): StreamCodec<B, Set<V>> =
    apply { codec: StreamCodec<B, V> -> ByteBufCodecs.collection(::HashSet, codec) }

/**
 * 指定した[entries]から[StreamCodec]を返します。
 * @param T [StringRepresentable]を継承したクラス
 * @return [stringCodec]をベースに変換された[StreamCodec]
 */
fun <T : StringRepresentable> stringStreamCodec(entries: Iterable<T>): StreamCodec<ByteBuf, T> =
    ByteBufCodecs.fromCodec(stringCodec(entries))
