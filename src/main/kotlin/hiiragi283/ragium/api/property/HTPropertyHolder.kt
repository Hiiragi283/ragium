package hiiragi283.ragium.api.property

/**
 * Holder for various typed values, like non-serializable [net.minecraft.component.ComponentHolder]
 * @see [HTMutablePropertyHolder]
 * @see [HTPropertyHolderBuilder]
 */
interface HTPropertyHolder : Iterable<Pair<HTPropertyKey<*>, Any>> {
    operator fun <T : Any> get(key: HTPropertyKey<T>): T?

    fun <T : Any> getOrDefault(key: HTPropertyKey.Defaulted<T>): T = get(key) ?: key.getDefaultValue()

    fun <T : Any> getOrThrow(key: HTPropertyKey<T>): T = checkNotNull(get(key)) { "Unknown property key: $key" }

    operator fun contains(key: HTPropertyKey<*>): Boolean

    /**
     * Transform stored value bound with [key], or null if not stored
     */
    fun <T : Any, R : Any> map(key: HTPropertyKey<T>, transform: (T) -> R): R? = get(key)?.let(transform)

    /**
     * Run [action] if there is a value bound with [key]
     */
    fun <T : Any> ifPresent(key: HTPropertyKey<T>, action: (T) -> Unit) {
        map(key, action)
    }

    //    Empty    //

    /**
     * Empty, Unmodifiable implementation for [HTPropertyHolder]
     */
    object Empty : HTPropertyHolder {
        override fun <T : Any> get(key: HTPropertyKey<T>): T? = null

        override fun contains(key: HTPropertyKey<*>): Boolean = false

        override fun iterator(): Iterator<Pair<HTPropertyKey<*>, Any>> = listOf<Pair<HTPropertyKey<*>, Any>>().iterator()
    }
}
