package hiiragi283.ragium.api.property

/**
 * [HTMutablePropertyHolder]のシンプルな実装
 */
class HTPropertyHolderBuilder(private val map: MutableMap<HTPropertyKey<*>, Any> = mutableMapOf()) : HTMutablePropertyHolder {
    override fun <T : Any> set(key: HTPropertyKey<T>, value: T) {
        map[key] = value
    }

    override fun remove(key: HTPropertyKey<*>) {
        map.remove(key)
    }

    override fun <T : Any> get(key: HTPropertyKey<T>): T? = key.cast(map[key])

    override fun contains(key: HTPropertyKey<*>): Boolean = key in map

    override fun iterator(): Iterator<Pair<HTPropertyKey<*>, Any>> = map.toList().iterator()
}
