package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.storage.HTAmountView
import java.util.function.BiFunction

fun interface HTAmountViewProvider<HANDLER : Any, C : Any, N> :
    BiFunction<HANDLER, C?, List<HTAmountView<N>>> where N : Number, N : Comparable<N> {
    abstract override fun apply(handler: HANDLER, context: C?): List<HTAmountView<N>>
}
