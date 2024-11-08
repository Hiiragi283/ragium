package hiiragi283.ragium.api.property

class HTPropertyHolderBuilder(private val map: MutableMap<HTPropertyKey<*>, Any> = mutableMapOf()) : HTMutablePropertyHolder {
    override fun <T : Any> set(key: HTPropertyKey<T>, value: T) {
        map[key] = value
    }

    override fun remove(id: HTPropertyKey<*>) {
        map.remove(id)
    }

    override fun <T : Any> get(key: HTPropertyKey<T>): T? = key.cast(map[key])

    override fun contains(key: HTPropertyKey<*>): Boolean = key in map

    override fun iterator(): Iterator<Pair<HTPropertyKey<*>, Any>> = map.toList().iterator()
}
