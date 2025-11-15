package hiiragi283.ragium.api.util

import com.mojang.datafixers.util.Either

fun <A, B> Either<A, B>.toIor(): Ior<A, B> = this.map({ Ior.Left(it) }, { Ior.Right(it) })

fun <A : Any, B : Any> Pair<A?, B?>?.toIor(): Ior<A, B>? {
    val (first: A?, second: B?) = this ?: return null
    return when {
        first != null -> when {
            second != null -> Ior.Both(first, second)
            else -> Ior.Left(first)
        }
        else -> when {
            second != null -> Ior.Right(second)
            else -> null
        }
    }
}
