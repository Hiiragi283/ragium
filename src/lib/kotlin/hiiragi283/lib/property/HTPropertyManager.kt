package hiiragi283.lib.property

/**
 * キーに対応する[HTPropertyGetter]を管理するクラスです。
 * @param K キーのクラス
 * @param E [HTPropertyManager.Entry]を実装したクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
class HTPropertyManager<K : Any, E : HTPropertyManager.Entry<K>>(private val map: Map<K, E>) : Iterable<E> {
    operator fun contains(key: K): Boolean = key in map

    operator fun get(key: K): E? = map[key]

    fun getOrThrow(key: K): E = get(key) ?: error("Missing entry: $key")

    val keys: Set<K> get() = map.keys

    val entries: Collection<E> = map.values

    override fun iterator(): Iterator<E> = entries.iterator()

    /**
     * [HTPropertyManager]の値に実装するインターフェースです。
     * @author Hiiragi Tsubasa
     * @since 26.1.2
     */
    interface Entry<K : Any> : HTPropertyGetter {
        /**
         * 対応するキー
         */
        val key: K
    }
}
