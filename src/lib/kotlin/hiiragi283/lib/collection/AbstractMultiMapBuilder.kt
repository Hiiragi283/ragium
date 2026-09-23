package hiiragi283.lib.collection

/**
 * [MutableMap]に基づいた[MultiMapBuilder]の抽象クラスです。
 * @param K キーのクラス
 * @param V 値のクラス
 * @param C コレクションのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
abstract class AbstractMultiMapBuilder<K, V, C : MutableCollection<V>>(protected val destination: MutableMap<K, C>) :
    MultiMapBuilder<K, V> {
    /**
     * 空のコレクションを生成します。
     */
    protected abstract fun emptyCollection(): C

    /**
     * 指定したキーから値の一覧を取得します。
     * @return 対応する一覧がない場合は[emptyCollection]で初期化
     */
    protected fun get(key: K): C = destination.getOrPut(key, ::emptyCollection)

    override fun put(key: K, value: V): Boolean = this.get(key).add(value)

    override fun putAll(key: K, values: Iterable<V>): Boolean = this.get(key).addAll(values)

    override fun putAll(key: K, vararg values: V): Boolean = this.get(key).addAll(values)

    override fun putAll(key: K, values: Sequence<V>): Boolean = this.get(key).addAll(values)

    override fun remove(key: K, value: V): Boolean = destination[key]?.remove(value) ?: false

    override fun removeAll(key: K): Collection<V>? = destination.remove(key)
}
