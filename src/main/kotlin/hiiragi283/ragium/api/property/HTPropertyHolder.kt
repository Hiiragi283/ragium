package hiiragi283.ragium.api.property

interface HTPropertyHolder : Iterable<Pair<HTPropertyKey<*>, Any>> {
    operator fun <T : Any> get(id: HTPropertyKey<T>): T?

    fun <T : Any> getOrDefault(id: HTPropertyKey.Defaulted<T>): T = get(id) ?: id.getDefaultValue()

    fun <T : Any> getOrThrow(id: HTPropertyKey<T>): T = get(id) ?: throw IllegalStateException("")

    operator fun contains(id: HTPropertyKey<*>): Boolean

    fun <T : Any> ifPresent(id: HTPropertyKey<T>, action: (T) -> Unit) {
        get(id)?.let(action)
    }

    interface Delegated : HTPropertyHolder {
        val delegated: HTPropertyHolder

        override fun <T : Any> get(id: HTPropertyKey<T>): T? = delegated[id]

        override fun contains(id: HTPropertyKey<*>): Boolean = id in delegated

        override fun iterator(): Iterator<Pair<HTPropertyKey<*>, Any>> = delegated.iterator()
    }

    interface Mutable : HTPropertyHolder {
        operator fun <T : Any> set(id: HTPropertyKey<T>, value: T)

        fun <T : Any> setIfNonNull(id: HTPropertyKey<T>, value: T?) {
            value?.let { set(id, it) }
        }

        fun remove(id: HTPropertyKey<*>)

        fun <T : Any> removeIf(id: HTPropertyKey<T>, filter: (T) -> Boolean) {
            val existValue: T = get(id) ?: return
            if (filter(existValue)) {
                remove(id)
            }
        }

        fun <T : Any> removeIfNull(id: HTPropertyKey<T>, mapping: (T) -> Any?) {
            val existValue: T = get(id) ?: return
            if (mapping(existValue) == null) {
                remove(id)
            }
        }

        fun <T : Any> computeIfAbsent(id: HTPropertyKey<T>, mapping: () -> T): T {
            val value: T? = get(id)
            if (value == null) {
                val newValue: T = mapping()
                set(id, newValue)
                return newValue
            } else {
                return value
            }
        }
    }

    companion object {
        @JvmField
        val EMPTY: HTPropertyHolder = Empty

        @JvmStatic
        fun create(map: MutableMap<HTPropertyKey<*>, Any> = mutableMapOf(), builderAction: Mutable.() -> Unit = {}): HTPropertyHolder =
            create(map, HTPropertyHolder::Impl, builderAction)

        @JvmStatic
        fun <T : Any> create(
            map: MutableMap<HTPropertyKey<*>, Any> = mutableMapOf(),
            build: (HTPropertyHolder) -> T,
            builderAction: Mutable.() -> Unit = {},
        ): T = Builder(map).apply(builderAction).let(build)

        @JvmStatic
        fun builder(): Builder = Builder()

        @JvmStatic
        fun builder(map: MutableMap<HTPropertyKey<*>, Any>): Mutable = Builder(map)

        @JvmStatic
        fun builder(parent: HTPropertyHolder): Mutable = Builder(parent.toMap().toMutableMap())
    }

    //    Empty    //

    private object Empty : HTPropertyHolder {
        override fun <T : Any> get(id: HTPropertyKey<T>): T? = null

        override fun contains(id: HTPropertyKey<*>): Boolean = false

        override fun iterator(): Iterator<Pair<HTPropertyKey<*>, Any>> = listOf<Pair<HTPropertyKey<*>, Any>>().iterator()
    }

    //    Impl    //

    private class Impl(delegate: HTPropertyHolder) : HTPropertyHolder by delegate

    //    Builder    //

    class Builder(private val map: MutableMap<HTPropertyKey<*>, Any> = mutableMapOf()) : Mutable {
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
