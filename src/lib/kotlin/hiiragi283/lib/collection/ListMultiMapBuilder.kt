package hiiragi283.lib.collection

/**
 * [MutableList]に基づいた[AbstractMultiMapBuilder]の実装クラスです。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
class ListMultiMapBuilder<K, V>(
    destination: MutableMap<K, MutableList<V>>,
    private val collectionFactory: () -> MutableList<V> = ::mutableListOf
) : AbstractMultiMapBuilder<K, V, MutableList<V>>(destination) {
    override fun emptyCollection(): MutableList<V> = collectionFactory()

    override fun build(): Map<K, List<V>> = destination
}
