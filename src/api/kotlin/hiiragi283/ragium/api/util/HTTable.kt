package hiiragi283.ragium.api.util

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

        fun putAll(other: HTTable<out R, out C, out V>)

        fun remove(row: R, column: C): V?
    }
}
