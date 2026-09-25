package hiiragi283.lib.collection

import com.google.common.collect.HashBasedTable
import com.google.common.collect.ImmutableTable

/**
 * [Table]を実装した[com.google.common.collect.Table]のラッパークラスです。
 * @param R 行のクラス
 * @param C 列のクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
class DelegatingTable<R : Any, C : Any, out V : Any>(private val delegate: com.google.common.collect.Table<R, C, V>) :
    Table<R, C, V> {
    override val size: Int get() = delegate.size()
    override val isEmpty: Boolean get() = delegate.isEmpty

    override fun contains(row: R, column: C): Boolean = delegate.contains(row, column)

    override fun containsRow(row: R): Boolean = delegate.containsRow(row)

    override fun containsColumn(column: C): Boolean = delegate.containsColumn(column)

    override fun containsValue(value: @UnsafeVariance V): Boolean = delegate.containsValue(value)

    override fun get(row: R, column: C): V? = delegate[row, column]

    override fun row(row: R): Map<C, V> = delegate.row(row)

    override fun column(column: C): Map<R, V> = delegate.column(column)

    override val rowKeys: Set<R> get() = delegate.rowKeySet()
    override val columnKeys: Set<C> get() = delegate.columnKeySet()
    override val values: Collection<V> get() = delegate.values()
    override val entries: Set<Triple<R, C, V>> get() = delegate.cellSet().mapTo(mutableSetOf()) {
        Triple(it.rowKey, it.columnKey, it.value)
    }

    /**
     * [DelegatingTable]向けの[Table.Builder]の抽象クラスです。
     * @param R 行のクラス
     * @param C 列のクラス
     * @param V 値のクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.7
     */
    class Builder<R : Any, C : Any, V : Any>(private val builder: com.google.common.collect.Table<R, C, V>) :
        Table.Builder<R, C, V> {
        constructor() : this(HashBasedTable.create())

        constructor(initialCapacity: Int = 10) : this(HashBasedTable.create(initialCapacity, initialCapacity))
        override fun get(row: R, column: C): V? = builder[row, column]

        override fun put(row: R, column: C, value: V): V? = builder.put(row, column, value)

        override fun build(): DelegatingTable<R, C, V> = ImmutableTable.copyOf(builder).let(::DelegatingTable)
    }
}
