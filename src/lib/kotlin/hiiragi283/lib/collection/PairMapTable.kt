package hiiragi283.lib.collection

/**
 * 行と列を[Pair]で束ねて管理する[Table]の実装クラスです。
 * @param R 行のクラス
 * @param C 列のクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmInline
value class PairMapTable<R, C, out V> private constructor(private val map: Map<Pair<R, C>, V>) : Table<R, C, V> {
    override fun contains(row: R, column: C): Boolean = (row to column) in map

    override fun containsRow(row: R): Boolean = map.any { (key: Pair<R, C>, _) -> key.first == row }

    override fun containsColumn(column: C): Boolean = map.any { (key: Pair<R, C>, _) -> key.second == column }

    override fun containsValue(value: @UnsafeVariance V): Boolean = map.containsValue(value)

    override fun get(row: R, column: C): V? = map[row to column]

    override val size: Int get() = map.size
    override val isEmpty: Boolean get() = map.isEmpty()

    override fun row(row: R): Map<C, V> = map.filterKeys { (rowIn: R, _) -> rowIn == row }.mapKeys { (key: Pair<R, C>, _) -> key.second }

    override fun column(column: C): Map<R, V> = map.filterKeys { (_, columnIn: C) -> columnIn == column }.mapKeys { (key: Pair<R, C>, _) -> key.first }

    override val rowKeys: Set<R> get() = map.keys.mapTo(mutableSetOf()) { it.first }
    override val columnKeys: Set<C> get() = map.keys.mapTo(mutableSetOf()) { it.second }
    override val values: Collection<V> get() = map.values
    override val entries: Set<Triple<R, C, V>> get() = map.entries.mapTo(mutableSetOf()) { (key: Pair<R, C>, value: V) -> Triple(key.first, key.second, value) }

    /**
     * [PairMapTable]向けの[Table.Builder]の抽象クラスです。
     * @param R 行のクラス
     * @param C 列のクラス
     * @param V 値のクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    class Builder<R, C, V>(private val map: MutableMap<Pair<R, C>, V>) : Table.Builder<R, C, V> {
        constructor(initialCapacity: Int = 10) : this(LinkedHashMap(initialCapacity))

        constructor(other: Table<R, C, V>) : this() {
            other.forEach(this::put)
        }

        override fun put(row: R, column: C, value: V): V? = map.put(row to column, value)

        override fun build(): Table<R, C, V> = when {
            map.isEmpty() -> emptyTableOf()
            else -> PairMapTable(map)
        }
    }
}
