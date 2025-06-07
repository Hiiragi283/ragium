package hiiragi283.ragium.api.extension

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import java.util.function.Function

//    Codec    //

fun <A : Any> Codec<A>.listOrElement(): Codec<List<A>> = Codec.either(this.listOf(), this).xmap(
    { either: Either<List<A>, A> -> either.map(Function.identity(), ::listOf) },
    { list: List<A> -> if (list.size == 1) Either.right(list[0]) else Either.left(list) },
)
