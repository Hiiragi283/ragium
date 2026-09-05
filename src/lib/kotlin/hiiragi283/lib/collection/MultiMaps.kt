@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.collection

import hiiragi283.lib.util.identity
import java.util.TreeSet
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 空の[MultiMap]のインスタンスを返します。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@Suppress("UNCHECKED_CAST")
fun <K, V> emptyMultiMapOf(): MultiMap<K, V> = EmptyMultiMap as MultiMap<K, V>

private data object EmptyMultiMap : MultiMap<Any?, Nothing> {
    override val size: Int = 0

    override val isEmpty: Boolean = true

    override fun containsKey(key: Any?): Boolean = false

    override fun containsValue(value: Nothing): Boolean = false

    override fun get(key: Any?): Collection<Nothing> = setOf()

    override val keys: Set<Any?> = setOf()
    override val values: Collection<Nothing> = setOf()
    override val entries: Set<Map.Entry<Any?, Collection<Nothing>>> = setOf()

    override fun asMap(): Map<Any?, Collection<Nothing>> = mapOf()
}

/**
 * 新しい[ListMultiMap]のインスタンスを作成します。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <K, V> buildListMultiMap(
    initialCapacity: Int = 10,
    builderAction: ListMultiMap.Builder<K, V>.() -> Unit
): ListMultiMap<K, V> {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return ListMultiMap.Builder<K, V>(initialCapacity).apply(builderAction).build()
}

/**
 * 新しい[ListMultiMap]のインスタンスを作成します。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <K, V> buildListMultiMap(
    map: MutableMap<K, MutableList<V>>,
    builderAction: ListMultiMap.Builder<K, V>.() -> Unit
): ListMultiMap<K, V> {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return ListMultiMap.Builder(map).apply(builderAction).build()
}

/**
 * 新しい[SetMultiMap]のインスタンスを作成します。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <K, V> buildSetMultiMap(
    initialCapacity: Int = 10,
    builderAction: SetMultiMap.Builder<K, V>.() -> Unit
): SetMultiMap<K, V> {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return SetMultiMap.Builder<K, V>(initialCapacity).apply(builderAction).build()
}

/**
 * 新しい[SetMultiMap]のインスタンスを作成します。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <K, V> buildSetMultiMap(
    map: MutableMap<K, MutableSet<V>>,
    builderAction: SetMultiMap.Builder<K, V>.() -> Unit
): SetMultiMap<K, V> {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return SetMultiMap.Builder(map).apply(builderAction).build()
}

/**
 * 新しい[SetMultiMap]のインスタンスを作成します。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
inline fun <K, V : Comparable<V>> buildSortedSetMultiMap(
    initialCapacity: Int = 10,
    builderAction: SetMultiMap.SortedBuilder<K, V>.() -> Unit
): SetMultiMap<K, V> {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return SetMultiMap.SortedBuilder<K, V>(compareBy(identity()), initialCapacity).apply(builderAction).build()
}

/**
 * 新しい[SetMultiMap]のインスタンスを作成します。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
inline fun <K, V : Comparable<V>> buildSortedSetMultiMap(
    map: MutableMap<K, TreeSet<V>>,
    builderAction: SetMultiMap.SortedBuilder<K, V>.() -> Unit
): SetMultiMap<K, V> {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return SetMultiMap.SortedBuilder(compareBy(identity()), map).apply(builderAction).build()
}

/**
 * 新しい[SetMultiMap]のインスタンスを作成します。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
inline fun <K, V> buildSortedSetMultiMap(
    comparator: Comparator<V>,
    initialCapacity: Int = 10,
    builderAction: SetMultiMap.SortedBuilder<K, V>.() -> Unit
): SetMultiMap<K, V> {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return SetMultiMap.SortedBuilder<K, V>(comparator, initialCapacity).apply(builderAction).build()
}

/**
 * 新しい[SetMultiMap]のインスタンスを作成します。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
inline fun <K, V> buildSortedSetMultiMap(
    comparator: Comparator<V>,
    map: MutableMap<K, TreeSet<V>>,
    builderAction: SetMultiMap.SortedBuilder<K, V>.() -> Unit
): SetMultiMap<K, V> {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return SetMultiMap.SortedBuilder(comparator, map).apply(builderAction).build()
}

//    Table    //

/**
 * [Table]に変換します。
 * @param K [MultiMap]のキーのクラス
 * @param V [MultiMap]の値のクラス
 * @param R [Table]の行のクラス
 * @param C [Table]の列のクラス
 * @param W [Table]の値のクラス
 * @param transform [MultiMap]の要素の組を[Triple]の一覧に変換するブロック
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <K, V, R, C, W> MultiMap<K, V>.flatMapTable(
    transform: (Map.Entry<K, Collection<V>>) -> Iterable<Triple<R, C, W>>
): Table<R, C, W> = this.flatMapTableTo(PairMapTable.Builder(), transform)

/**
 * [Table]に変換します。
 * @param K [MultiMap]のキーのクラス
 * @param V [MultiMap]の値のクラス
 * @param R [Table]の行のクラス
 * @param C [Table]の列のクラス
 * @param W [Table]の値のクラス
 * @param builder テーブルの値の受取先
 * @param transform [MultiMap]の要素の組を[Triple]の一覧に変換するブロック
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <K, V, R, C, W, D : Table.Builder<R, C, W>> MultiMap<K, V>.flatMapTableTo(
    builder: D,
    transform: (Map.Entry<K, Collection<V>>) -> Iterable<Triple<R, C, W>>
): Table<R, C, W> {
    this.entries.flatMap(transform).forEach(builder::put)
    return builder.build()
}
