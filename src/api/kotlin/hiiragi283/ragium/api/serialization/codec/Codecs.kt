package hiiragi283.ragium.api.serialization.codec

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import io.netty.buffer.ByteBuf
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

//    BiCodec    //

/**
 * 指定された[BiCodec]を，別の[BiCodec]に変換します。
 * @param X 変換後のコーデックの対象となるクラス
 * @param V [X]を継承したクラス
 * @return [X]を対象とした[BiCodec]
 */
inline fun <B : ByteBuf, reified X : Any, reified V : X> BiCodec<B, V>.downCast(): BiCodec<B, X> = this.flatXmap({ it as X }, { it as V })

fun <T : Any> Result<T>.toData(): DataResult<T> = fold(DataResult<T>::success) { throwable: Throwable ->
    DataResult.error { throwable.message ?: "Thrown exception" }
}
