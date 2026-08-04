package hiiragi283.lib.collection

import hiiragi283.lib.util.HTBuilderMarker

/**
 * [Map]に基づいた[MultiMap]の抽象クラスです。
 * @param K キーのクラス
 * @param V 値のクラス
 * @param C コレクションのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class AbstractMultiMap<K, out V, out C : Collection<V>>(protected val map: Map<K, C>) : MultiMap<K, V> {
    companion object {
        protected fun Map<*, Collection<*>>.isDeepEmpty(): Boolean = this.isEmpty() || this.values.all(Collection<*>::isEmpty)
    }

    override val size: Int get() = map.size

    override val isEmpty: Boolean get() = map.isEmpty()

    override fun containsKey(key: K): Boolean = key in map

    override fun containsValue(value: @UnsafeVariance V): Boolean = map.any { (_, values: C) -> value in values }

    override fun get(key: K): C = map[key] ?: emptyCollection()

    /**
     * 空のコレクションを生成します。
     */
    protected abstract fun emptyCollection(): C

    override val keys: Set<K> get() = map.keys
    override val values: Collection<V> get() = map.values.flatten()
    override val entries: Set<Map.Entry<K, Collection<V>>> get() = map.entries

    override fun asMap(): Map<K, C> = map

    /**
     * [MutableMap]に基づいた[MultiMap.Builder]の抽象クラスです。
     * @param K キーのクラス
     * @param V 値のクラス
     * @param C コレクションのクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.1
     */
    @HTBuilderMarker
    abstract class Builder<K, V, out C : MutableCollection<V>>(protected val map: MutableMap<K, @UnsafeVariance C>) : MultiMap.Builder<K, V> {
        constructor(initialCapacity: Int = 10) : this(LinkedHashMap(initialCapacity))

        constructor(other: MultiMap<K, V>) : this() {
            other.asMap().forEach(this::putAll)
        }

        /**
         * 指定したキーから値の一覧を取得します。
         * @return 対応する一覧がない場合は[emptyCollection]で初期化
         */
        protected fun get(key: K): C = map.getOrPut(key, ::emptyCollection)

        /**
         * 空のコレクションを生成します。
         */
        protected abstract fun emptyCollection(): C

        override fun put(key: K, value: V): Boolean = this.get(key).add(value)

        override fun putAll(key: K, values: Iterable<V>): Boolean = this.get(key).addAll(values)

        override fun putAll(key: K, vararg values: V): Boolean = this.get(key).addAll(values)

        override fun putAll(key: K, values: Sequence<V>): Boolean = this.get(key).addAll(values)
    }
}
