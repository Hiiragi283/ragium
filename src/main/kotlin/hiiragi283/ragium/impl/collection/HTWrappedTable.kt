package hiiragi283.ragium.impl.collection

import com.google.common.collect.Table
import hiiragi283.ragium.api.collection.HTTable
import hiiragi283.ragium.api.extension.forEach

internal open class HTWrappedTable<R : Any, C : Any, V : Any>(protected val delegated: Table<R, C, V>) : HTTable<R, C, V> {
    override fun contains(row: R, column: C): Boolean = delegated.contains(row, column)

    override fun containsRow(row: R): Boolean = delegated.containsRow(row)

    override fun containsColumn(column: C): Boolean = delegated.containsColumn(column)

    override fun containsValue(value: V): Boolean = delegated.containsValue(value)

    override fun get(row: R, column: C): V? = delegated.get(row, column)

    override fun isEmpty(): Boolean = delegated.isEmpty

    override val size: Int get() = delegated.size()
    override val rowKeys: Set<R> get() = delegated.rowKeySet()
    override val columnKeys: Set<C> get() = delegated.columnKeySet()
    override val values: Collection<V> get() = delegated.values()
    override val entries: Set<Triple<R, C, V>> get() = delegated.cellSet().map { Triple(it.rowKey, it.columnKey, it.value) }.toSet()
    override val rowMap: Map<R, Map<C, V>> get() = delegated.rowMap()
    override val columnMap: Map<C, Map<R, V>> get() = delegated.columnMap()

    override fun column(column: C): Map<R, V> = delegated.column(column)

    override fun row(row: R): Map<C, V> = delegated.row(row)

    //    Mutable    //

    class Mutable<R : Any, C : Any, V : Any>(delegated: Table<R, C, V>) :
        HTWrappedTable<R, C, V>(delegated),
        HTTable.Mutable<R, C, V> {
        override fun clear() = delegated.clear()

        override fun remove(row: R, column: C): V? = delegated.remove(row, column)

        override fun putAll(other: HTTable<out R, out C, out V>) {
            other.forEach { (row: R, column: C, value: V) ->
                delegated.put(row, column, value)
            }
        }

        override fun put(row: R, column: C, value: V): V? = delegated.put(row, column, value)
    }
}
