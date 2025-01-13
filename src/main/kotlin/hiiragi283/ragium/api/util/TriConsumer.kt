package hiiragi283.ragium.api.util

fun interface TriConsumer<T : Any, U : Any, V : Any> {
    fun accept(t: T, u: U, v: V)
}
