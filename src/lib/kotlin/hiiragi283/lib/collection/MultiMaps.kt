@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.collection

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 新しい[Map]のインスタンスを作成します。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <K, V> buildListMultiMap(
    builderAction: ListMultiMapBuilder<K, V>.() -> Unit,
    noinline collectionFactory: () -> MutableList<V> = ::mutableListOf
): Map<K, List<V>> = buildListMultiMap(Object2ObjectLinkedOpenHashMap(), builderAction, collectionFactory)

/**
 * 新しい[Map]のインスタンスを作成します。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <K, V> buildListMultiMap(
    map: MutableMap<K, MutableList<V>>,
    builderAction: ListMultiMapBuilder<K, V>.() -> Unit,
    noinline collectionFactory: () -> MutableList<V> = ::mutableListOf
): Map<K, List<V>> {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return ListMultiMapBuilder(map, collectionFactory).apply(builderAction).build()
}

/**
 * 新しい[Map]のインスタンスを作成します。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <K, V> buildSetMultiMap(
    builderAction: SetMultiMapBuilder<K, V>.() -> Unit,
    noinline collectionFactory: () -> MutableSet<V> = ::mutableSetOf
): Map<K, Set<V>> = buildSetMultiMap(Object2ObjectLinkedOpenHashMap(), builderAction, collectionFactory)

/**
 * 新しい[Map]のインスタンスを作成します。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <K, V> buildSetMultiMap(
    map: MutableMap<K, MutableSet<V>>,
    builderAction: SetMultiMapBuilder<K, V>.() -> Unit,
    noinline collectionFactory: () -> MutableSet<V> = ::mutableSetOf
): Map<K, Set<V>> {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return SetMultiMapBuilder(map, collectionFactory).apply(builderAction).build()
}

//    Table    //

/**
 * [Table]に変換します。
 * @param K [Map]のキーのクラス
 * @param V [Map]の値のクラス
 * @param R [Table]の行のクラス
 * @param C [Table]の列のクラス
 * @param W [Table]の値のクラス
 * @param transform [Map]の要素の組を[Triple]の一覧に変換するブロック
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <K, V, R : Any, C : Any, W : Any> Map<K, Collection<V>>.flatMapTable(
    transform: (Map.Entry<K, Collection<V>>) -> Iterable<Triple<R, C, W>>
): Table<R, C, W> = this.flatMapTableTo(DelegatingTable.Builder(), transform)

/**
 * [Table]に変換します。
 * @param K [Map]のキーのクラス
 * @param V [Map]の値のクラス
 * @param R [Table]の行のクラス
 * @param C [Table]の列のクラス
 * @param W [Table]の値のクラス
 * @param builder テーブルの値の受取先
 * @param transform [Map]の要素の組を[Triple]の一覧に変換するブロック
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <K, V, R : Any, C : Any, W : Any, D : Table.Builder<R, C, W>> Map<K, Collection<V>>.flatMapTableTo(
    builder: D,
    transform: (Map.Entry<K, Collection<V>>) -> Iterable<Triple<R, C, W>>
): Table<R, C, W> {
    this.entries.flatMap(transform).forEach(builder::put)
    return builder.build()
}
