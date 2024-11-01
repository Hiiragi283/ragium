package hiiragi283.ragium.api.property

interface HTPropertyHolder : Iterable<Pair<HTPropertyKey<*>, Any>> {
    operator fun <T : Any> get(id: HTPropertyKey<T>): T?

    fun <T : Any> getOrDefault(id: HTPropertyKey.Defaulted<T>): T = get(id) ?: id.getDefaultValue()

    fun <T : Any> getOrThrow(id: HTPropertyKey<T>): T = get(id) ?: throw IllegalStateException("")

    operator fun contains(id: HTPropertyKey<*>): Boolean

    fun <T : Any> ifPresent(id: HTPropertyKey<T>, action: (T) -> Unit) {
        get(id)?.let(action)
    }

    companion object {
        @JvmField
        val EMPTY: HTPropertyHolder = Empty

        @JvmStatic
        fun create(
            map: MutableMap<HTPropertyKey<*>, Any> = mutableMapOf(),
            builderAction: HTMutablePropertyHolder.() -> Unit = {},
        ): HTPropertyHolder = create(map, HTPropertyHolder::Impl, builderAction)

        @JvmStatic
        fun <T : Any> create(
            map: MutableMap<HTPropertyKey<*>, Any> = mutableMapOf(),
            build: (HTPropertyHolder) -> T,
            builderAction: HTMutablePropertyHolder.() -> Unit = {},
        ): T = Builder(map).apply(builderAction).let(build)

        @JvmStatic
        fun builder(): HTMutablePropertyHolder = Builder()

        @JvmStatic
        fun builder(parent: HTPropertyHolder): HTMutablePropertyHolder = Builder(parent.toMap().toMutableMap())
    }

    //    Empty    //

    private object Empty : HTPropertyHolder {
        override fun <T : Any> get(id: HTPropertyKey<T>): T? = null

        override fun contains(id: HTPropertyKey<*>): Boolean = false

        override fun iterator(): Iterator<Pair<HTPropertyKey<*>, Any>> = listOf<Pair<HTPropertyKey<*>, Any>>().iterator()
    }

    //    Impl    //

    private class Impl(override val delegated: HTPropertyHolder) : HTDelegatedPropertyHolder

    //    Builder    //

    private class Builder(private val map: MutableMap<HTPropertyKey<*>, Any> = mutableMapOf()) : HTMutablePropertyHolder {
        override fun <T : Any> get(id: HTPropertyKey<T>): T? = id.cast(map[id])

        override fun contains(id: HTPropertyKey<*>): Boolean = id in map

        override fun <T : Any> set(id: HTPropertyKey<T>, value: T) {
            map[id] = value
        }

        override fun remove(id: HTPropertyKey<*>) {
            map.remove(id)
        }

        override fun iterator(): Iterator<Pair<HTPropertyKey<*>, Any>> = map.toList().iterator()
    }
}
