package hiiragi283.lib.collection

/**
 * [MutableSet]に基づいた[AbstractMultiMapBuilder]の実装クラスです。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
class SetMultiMapBuilder<K, V>(
    destination: MutableMap<K, MutableSet<V>>,
    private val collectionFactory: () -> MutableSet<V> = ::mutableSetOf
) : AbstractMultiMapBuilder<K, V, MutableSet<V>>(destination) {
    override fun emptyCollection(): MutableSet<V> = collectionFactory()

    override fun build(): Map<K, Set<V>> = destination
}
