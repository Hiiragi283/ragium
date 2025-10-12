package hiiragi283.ragium.api.serialization.codec

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
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
