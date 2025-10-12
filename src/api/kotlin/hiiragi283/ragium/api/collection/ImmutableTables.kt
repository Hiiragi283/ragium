package hiiragi283.ragium.api.collection

inline fun <R : Any, C : Any, V : Any> buildTable(
    initialRow: Int = 10,
    initialColumn: Int = 10,
    builderAction: ImmutableTable.Builder<R, C, V>.() -> Unit,
): ImmutableTable<R, C, V> = ImmutableTable.Builder<R, C, V>(initialRow, initialColumn).apply(builderAction).build()
