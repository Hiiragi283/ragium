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

    inline fun forEach(action: (Triple<R, C, V>) -> Unit) {
        entries.forEach(action)
    }

    //    Builder    //

    class Builder<R : Any, C : Any, V : Any>(initialRow: Int = 10, initialColumn: Int = 10) {
        private val values: Table<R, C, V> = HashBasedTable.create<R, C, V>(initialRow, initialColumn)

        fun put(row: R, column: C, value: V): V? = values.put(row, column, value)

        operator fun set(row: R, column: C, value: V) {
            put(row, column, value)
        }

        fun putAll(table: ImmutableTable<out R, out C, out V>): Builder<R, C, V> {
            table.forEach { (r: R, c: C, v: V) -> this.values.put(r, c, v) }
            return this
        }

        /**
         * @see MutableMap.compute
         */
        fun compute(row: R, column: C, mapping: (R, C, V?) -> V?): V? {
            val oldValue: V? = values.get(row, column)
            val newValue: V? = mapping(row, column, oldValue)
            values.put(row, column, newValue)
            return newValue
        }

        /**
         * @see MutableMap.computeIfPresent
         */
        fun computeIfPresent(row: R, column: C, mapping: (R, C, V) -> V?): V? {
            val oldValue: V = values.get(row, column) ?: return null
            val newValue: V? = mapping(row, column, oldValue)
            values.put(row, column, newValue)
            return newValue
        }

        /**
         * @see MutableMap.computeIfAbsent
         */
        fun computeIfAbsent(row: R, column: C, mapping: (R, C) -> V): V {
            val oldValue: V? = values.get(row, column)
            if (oldValue == null) {
                val newValue: V = mapping(row, column)
                values.put(row, column, newValue)
                return newValue
            } else {
                return oldValue
            }
        }

        fun build(): ImmutableTable<R, C, V> = ImmutableTable(values)
    }
}
