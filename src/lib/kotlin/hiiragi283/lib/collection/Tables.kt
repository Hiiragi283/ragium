@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.collection

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 空の[Table]のインスタンスを返します。
 * @param R 行のクラス
 * @param C 列のクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@Suppress("UNCHECKED_CAST")
fun <R, C, V> emptyTableOf(): Table<R, C, V> = EmptyTable as Table<R, C, V>

private data object EmptyTable : Table<Nothing, Nothing, Nothing> {
    override val size: Int = 0
    override val isEmpty: Boolean = true

    override fun contains(row: Nothing, column: Nothing): Boolean = false

    override fun containsRow(row: Nothing): Boolean = false

    override fun containsColumn(column: Nothing): Boolean = false

    override fun containsValue(value: Nothing): Boolean = false

    override fun get(row: Nothing, column: Nothing): Nothing? = null

    override fun row(row: Nothing): Map<Nothing, Nothing> = emptyMap()

    override fun column(column: Nothing): Map<Nothing, Nothing> = emptyMap()

    override val rowKeys: Set<Nothing> = emptySet()
    override val columnKeys: Set<Nothing> = emptySet()
    override val values: Collection<Nothing> = emptySet()
    override val entries: Set<Triple<Nothing, Nothing, Nothing>> = emptySet()
}

/**
 * 新しい[PairMapTable]のインスタンスを作成します。
 * @param R 行のクラス
 * @param C 列のクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <R, C, V> buildTable(initialCapacity: Int = 10, builderAction: Table.Builder<R, C, V>.() -> Unit): Table<R, C, V> {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return PairMapTable.Builder<R, C, V>(initialCapacity).apply(builderAction).build()
}

/**
 * 各要素に対して[action]を行います。
 * @param R 行のクラス
 * @param C 列のクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <R, C, V> Table<R, C, V>.forEach(action: (Triple<R, C, V>) -> Unit) {
    this.entries.forEach(action)
}

/**
 * 要素の一覧を[Sequence]に変換します。
 * @param R 行のクラス
 * @param C 列のクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <R, C, V> Table<R, C, V>.asSequence(): Sequence<Triple<R, C, V>> = this.entries.asSequence()
