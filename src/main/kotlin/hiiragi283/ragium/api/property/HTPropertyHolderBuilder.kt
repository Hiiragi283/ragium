package hiiragi283.ragium.api.property

class HTPropertyHolderBuilder(private val map: MutableMap<HTPropertyKey<*>, Any> = mutableMapOf()) : HTMutablePropertyHolder {
    override fun <T : Any> set(id: HTPropertyKey<T>, value: T) {
        map[id] = value
    }

    override fun remove(id: HTPropertyKey<*>) {
        map.remove(id)
    }

    override fun <T : Any> get(id: HTPropertyKey<T>): T? = id.cast(map[id])

    override fun contains(id: HTPropertyKey<*>): Boolean = id in map

    override fun iterator(): Iterator<Pair<HTPropertyKey<*>, Any>> = map.toList().iterator()
}
