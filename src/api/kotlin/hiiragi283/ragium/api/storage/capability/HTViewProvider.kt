package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.storage.HTStackView
import java.util.function.BiFunction

fun interface HTViewProvider<HANDLER : Any, C : Any, STACK : ImmutableStack<*, STACK>> : BiFunction<HANDLER, C?, List<HTStackView<STACK>>> {
    abstract override fun apply(handler: HANDLER, context: C?): List<HTStackView<STACK>>
}
