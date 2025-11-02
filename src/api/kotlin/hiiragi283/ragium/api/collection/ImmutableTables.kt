package hiiragi283.ragium.api.collection

import com.google.common.collect.HashBasedTable

fun <R : Any, C : Any, V : Any> immutableTableOf(): ImmutableTable<R, C, V> = ImmutableTable(HashBasedTable.create())

inline fun <R : Any, C : Any, V : Any> buildTable(
    initialRow: Int = 10,
    initialColumn: Int = 10,
    builderAction: ImmutableTable.Builder<R, C, V>.() -> Unit,
): ImmutableTable<R, C, V> = ImmutableTable.Builder<R, C, V>(initialRow, initialColumn).apply(builderAction).build()
