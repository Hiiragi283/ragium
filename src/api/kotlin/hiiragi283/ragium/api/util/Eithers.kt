package hiiragi283.ragium.api.util

import com.mojang.datafixers.util.Either

fun <U> unwrapEither(either: Either<out U, out U>): U = Either.unwrap(either)
