package hiiragi283.ragium.api.serialization.codec

import net.minecraft.network.codec.StreamCodec
import java.util.function.Function

//    StreamCodec    //

fun <B : Any, V : Any, O : Any> StreamCodec<B, V>.comapFlatMap(to: Function<V, Result<O>>, from: Function<O, V>): StreamCodec<B, O> =
    map(to.andThen(throwResult()), from)

fun <B : Any, V : Any, O : Any> StreamCodec<B, V>.flatComapMap(to: Function<V, O>, from: Function<O, Result<V>>): StreamCodec<B, O> =
    map(to, from.andThen(throwResult()))

fun <B : Any, V : Any, O : Any> StreamCodec<B, V>.flatMap(to: Function<V, Result<O>>, from: Function<O, Result<V>>): StreamCodec<B, O> =
    map(to.andThen(throwResult()), from.andThen(throwResult()))

private fun <T : Any> throwResult(): (Result<T>) -> T = Result<T>::getOrThrow
