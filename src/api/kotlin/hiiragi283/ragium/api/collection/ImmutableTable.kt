package hiiragi283.ragium.api.collection

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table

/**
 * [Table]のイミュータブルなラッパー
 */
@JvmInline
value class ImmutableTable<R : Any, C : Any, V : Any>(private val table: Table<R, C, V>) {
    fun contains(row: R, column: C): Boolean = table.contains(row, column)

    fun containsRow(row: R): Boolean = table.containsRow(row)

    fun containsColumn(column: C): Boolean = table.containsColumn(column)

    fun containsValue(value: V): Boolean = table.containsValue(value)

    operator fun get(row: R, column: C): V? = table.get(row, column)

    fun isEmpty(): Boolean = table.isEmpty

    val size: Int get() = table.size()

    fun row(row: R): Map<C, V> = table.row(row)

    fun column(column: C): Map<R, V> = table.column(column)

    val rowKeys: Set<R> get() = table.rowKeySet()

    val columnKeys: Set<C> get() = table.columnKeySet()

    val values: Collection<V> get() = table.values()

    val entries: Set<Triple<R, C, V>> get() = table.cellSet().map { Triple(it.rowKey, it.columnKey, it.value) }.toSet()

    val rowMap: Map<R, Map<C, V>> get() = table.rowMap()

    val columnMap: Map<C, Map<R, V>> get() = table.columnMap()

    //    Extensions    //

    fun rowValues(row: R): Collection<V> = row(row).values

    fun columnValues(column: C): Collection<V> = column(column).values

    inline fun forEach(action: (Triple<R, C, V>) -> Unit) {
        entries.forEach(action)
    }

    //    Builder    //

    class Builder<R : Any, C : Any, V : Any>(initialRow: Int = 10, initialColumn: Int = 10) {
        private val values: Table<R, C, V> = HashBasedTable.create<R, C, V>(initialRow, initialColumn)

        fun put(row: R, column: C, value: V): Builder<R, C, V> {
            values.put(row, column, value)
            return this
        }

        operator fun set(row: R, column: C, value: V) {
            put(row, column, value)
        }

        fun putAll(table: ImmutableTable<R, C, V>): Builder<R, C, V> {
            table.forEach { (r: R, c: C, v: V) -> this.values.put(r, c, v) }
            return this
        }

        fun build(): ImmutableTable<R, C, V> = ImmutableTable(values)
    }
}
