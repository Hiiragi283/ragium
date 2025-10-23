package hiiragi283.ragium.api.function

import net.minecraft.resources.ResourceLocation
import java.util.function.Function

typealias IdToFunction<R> = Function<ResourceLocation, R>

fun <IP, R> (() -> IP).andThen(f: (IP) -> R): () -> R = { this().let(f) }

fun <P1, IP, R> ((P1) -> IP).andThen(f: (IP) -> R): (P1) -> R = { p1: P1 -> this(p1).let(f) }

fun <P1, P2, IP, R> ((P1, P2) -> IP).andThen(f: (IP) -> R): (P1, P2) -> R = { p1: P1, p2: P2 -> this(p1, p2).let(f) }

fun <P1, IP, R> ((IP) -> R).compose(f: (P1) -> IP): (P1) -> R = { p1: P1 -> f(p1).let(this) }

fun <P1, R> ((P1) -> R).partially1(p1: P1): () -> R = { this(p1) }

fun <P1, P2, R> ((P1, P2) -> R).partially1(p1: P1): (P2) -> R = { p2: P2 -> this(p1, p2) }

fun <P1, P2, P3, R> ((P1, P2, P3) -> R).partially1(p1: P1): (P2, P3) -> R = { p2: P2, p3: P3 -> this(p1, p2, p3) }

fun <P1, P2, P3, P4, R> ((P1, P2, P3, P4) -> R).partially1(p1: P1): (P2, P3, P4) -> R = { p2: P2, p3: P3, p4: P4 -> this(p1, p2, p3, p4) }

fun <P1, P2, R> ((P1, P2) -> R).partially2(p1: P1, p2: P2): () -> R = { this(p1, p2) }

fun <P1, P2, P3, R> ((P1, P2, P3) -> R).partially2(p1: P1, p2: P2): (P3) -> R = { p3: P3 -> this(p1, p2, p3) }

fun (() -> Boolean).negate(): () -> Boolean = { !this() }

fun <P1> ((P1) -> Boolean).negate(): (P1) -> Boolean = { p1: P1 -> !this(p1) }

fun <P1, P2> ((P1, P2) -> Boolean).negate(): (P1, P2) -> Boolean = { p1: P1, p2: P2 -> !this(p1, p2) }

fun <P1, P2, P3> ((P1, P2, P3) -> Boolean).negate(): (P1, P2, P3) -> Boolean = { p1: P1, p2: P2, p3: P3 -> !this(p1, p2, p3) }
