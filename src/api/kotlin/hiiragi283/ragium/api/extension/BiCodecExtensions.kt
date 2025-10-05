package hiiragi283.ragium.api.extension

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.codec.BiCodec
import io.netty.buffer.ByteBuf
import java.util.function.Function

/**
 * 指定された[BiCodec]を，別の[BiCodec]に変換します。
 * @param N [Number]を継承したクラス
 * @param min 範囲の最小値
 * @param max 範囲の最大値
 * @return [min]と[max]に範囲を制限された[BiCodec]
 */
fun <B : ByteBuf, N> BiCodec<B, N>.ranged(min: N, max: N): BiCodec<B, N> where N : Number, N : Comparable<N> {
    val range: Function<N, DataResult<N>> = Codec.checkRange(min, max)
    return this.flatXmap(range, range)
}

/**
 * 指定された[BiCodec]を，別の[BiCodec]に変換します。
 * @param X 変換後のコーデックの対象となるクラス
 * @param V [X]を継承したクラス
 * @return [X]を対象とした[BiCodec]
 */
inline fun <B : ByteBuf, reified X : Any, reified V : X> BiCodec<B, V>.downCast(): BiCodec<B, X> =
    this.flatXmap(DataResult<X>::success) { value: X ->
        (value as? V).wrapDataResult("Value $value cannot cast to ${X::class.java.canonicalName}")
    }
