package hiiragi283.ragium.api.property

/**
 * [HTMutablePropertyHolder]向けのビルダー
 */
class HTPropertyHolderBuilder(
    private val map: MutableMap<HTPropertyKey<*>, Any> = mutableMapOf(),
) {
    fun <T : Any> put(
        key: HTPropertyKey<T>,
        value: T?,
    ): HTPropertyHolderBuilder =
        apply {
            if (value != null) map[key] = value
        }

    fun add(vararg keys: HTPropertyKey<Unit>): HTPropertyHolderBuilder =
        apply {
            keys.forEach { put(it, Unit) }
        }

    fun remove(key: HTPropertyKey<*>): HTPropertyHolderBuilder =
        apply {
            map.remove(key)
        }

    fun build(): HTPropertyHolder =
        object : HTPropertyHolder {
            override fun <T : Any> get(key: HTPropertyKey<T>): T? = key.cast(map[key])

            override fun contains(key: HTPropertyKey<*>): Boolean = key in map

            override fun iterator(): Iterator<Pair<HTPropertyKey<*>, Any>> = map.toList().iterator()
        }
}
