package hiiragi283.ragium.api.extension

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.codec.BiCodec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import java.util.function.Function
import kotlin.collections.List

//    Codec    //

fun <A : Any> Codec<A>.listOrElement(): Codec<List<A>> = Codec.either(this.listOf(), this).xmap(
    { either: Either<List<A>, A> -> either.map(Function.identity(), ::listOf) },
    { list: List<A> -> if (list.size == 1) Either.right(list[0]) else Either.left(list) },
)

fun <A : Any> Codec<A>.listOrElement(min: Int, max: Int): Codec<List<A>> = Codec.either(this.listOf(min, max), this).xmap(
    { either: Either<List<A>, A> -> either.map(Function.identity(), ::listOf) },
    { list: List<A> -> if (list.size == 1) Either.right(list[0]) else Either.left(list) },
)

fun <E : Any, MAP : MapCodec<out E>> Codec<MAP>.dispatchSelf(type: Function<E, MAP>): Codec<E> =
    this.dispatch(type, Function.identity<MAP>())

//    StreamCodec    //

fun <B : Any, V : Any, O : Any> StreamCodec<B, V>.comapFlatMap(to: Function<V, Result<O>>, from: Function<O, V>): StreamCodec<B, O> =
    map(to.andThen(throwResult()), from)

fun <B : Any, V : Any, O : Any> StreamCodec<B, V>.flatComapMap(to: Function<V, O>, from: Function<O, Result<V>>): StreamCodec<B, O> =
    map(to, from.andThen(throwResult()))

fun <B : Any, V : Any, O : Any> StreamCodec<B, V>.flatMap(to: Function<V, Result<O>>, from: Function<O, Result<V>>): StreamCodec<B, O> =
    map(to.andThen(throwResult()), from.andThen(throwResult()))

private fun <T : Any> throwResult(): (Result<T>) -> T = Result<T>::getOrThrow

//    BiCodec    //

/**
 * 指定された[BiCodec]を，別の[BiCodec]に変換します。
 * @param N [Number]を継承したクラス
 * @param range 値の範囲
 * @return [range]に範囲を制限された[BiCodec]
 */
fun <B : ByteBuf, N> BiCodec<B, N>.ranged(range: ClosedRange<N>): BiCodec<B, N> where N : Number, N : Comparable<N> {
    val range1: (N) -> Result<N> = { value: N ->
        runCatching {
            check(value in range) { "Value $value outside of range [$range]" }
            value
        }
    }
    return this.flatXmap(range1, range1)
}

/**
 * 指定された[BiCodec]を，別の[BiCodec]に変換します。
 * @param X 変換後のコーデックの対象となるクラス
 * @param V [X]を継承したクラス
 * @return [X]を対象とした[BiCodec]
 */
inline fun <B : ByteBuf, reified X : Any, reified V : X> BiCodec<B, V>.downCast(): BiCodec<B, X> =
    this.flatComapMap({ it as X }, { runCatching { it as V } })
