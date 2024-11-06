package hiiragi283.ragium.api.util

import com.google.common.collect.Table

interface HTTable<R : Any, C : Any, V : Any> {
    fun contains(row: R, column: C): Boolean

    fun containsRow(row: R): Boolean

    fun containsColumn(column: C): Boolean

    fun containsValue(value: V): Boolean

    fun get(row: R, column: C): V?

    fun isEmpty(): Boolean

    val size: Int

    fun row(row: R): Map<C, V>

    fun column(column: C): Map<R, V>

    val rowKeys: Set<R>

    val columnKeys: Set<C>

    val values: Collection<V>

    val entries: Set<Triple<R, C, V>>

    val rowMap: Map<R, Map<C, V>>

    val columnMap: Map<C, Map<R, V>>

    interface Mutable<R : Any, C : Any, V : Any> : HTTable<R, C, V> {
        fun clear()

        fun put(row: R, column: C, value: V): V?

        fun putAll(other: Table<R, C, V>)

        fun remove(row: R, column: C): V?

        fun computeIfAbsent(row: R, column: C, mapper: (R, C) -> V): V? {
            val value: V? = get(row, column)
            if (value == null) {
                val newValue: V = mapper(row, column)
                put(row, column, newValue)
                return newValue
            }
            return value
        }
    }
}
