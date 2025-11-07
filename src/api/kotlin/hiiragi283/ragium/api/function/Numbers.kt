package hiiragi283.ragium.api.function

fun <N> N.clamp(range: ClosedRange<N>): N where N : Number, N : Comparable<N> = minOf(maxOf(this, range.start), range.endInclusive)
