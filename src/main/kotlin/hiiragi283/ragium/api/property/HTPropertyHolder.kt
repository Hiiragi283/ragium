package hiiragi283.ragium.api.property

interface HTPropertyHolder {
    companion object {
        @JvmField
        val EMPTY: HTPropertyHolder = Empty

        @JvmStatic
        fun create(map: MutableMap<HTPropertyKey<*>, Any> = mutableMapOf(), builderAction: Mutable.() -> Unit = {}): HTPropertyHolder =
            Builder(map).apply(builderAction)
    }

    operator fun <T : Any> get(id: HTPropertyKey<T>): T?

    @Throws(AssertionError::class)
    fun <T : Any> getOrDefault(id: HTPropertyKey<T>): T = get(id) ?: id.getDefaultValue()

    operator fun contains(id: HTPropertyKey<*>): Boolean

    fun forEachProperties(action: (HTPropertyKey<*>, Any) -> Unit)

    //    Mutable    //

    interface Mutable : HTPropertyHolder {
        operator fun <T : Any> set(id: HTPropertyKey<T>, value: T)

        fun remove(id: HTPropertyKey<*>)
    }

    //    Empty    //

    private object Empty : HTPropertyHolder {
        override fun <T : Any> get(id: HTPropertyKey<T>): T? = null

        override fun contains(id: HTPropertyKey<*>): Boolean = false

        override fun forEachProperties(action: (HTPropertyKey<*>, Any) -> Unit) = Unit
    }

    //    Builder    //

    private class Builder(private val map: MutableMap<HTPropertyKey<*>, Any>) : Mutable {
        override fun <T : Any> get(id: HTPropertyKey<T>): T? = id.cast(map[id])

        override fun contains(id: HTPropertyKey<*>): Boolean = id in map

        override fun forEachProperties(action: (HTPropertyKey<*>, Any) -> Unit) {
            map.forEach(action)
        }

        override fun <T : Any> set(id: HTPropertyKey<T>, value: T) {
            map[id] = value
        }

        override fun remove(id: HTPropertyKey<*>) {
            map.remove(id)
        }
    }
}
