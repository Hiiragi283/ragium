package hiiragi283.ragium.api.property

interface HTPropertyHolder : Iterable<Pair<HTPropertyKey<*>, Any>> {
    operator fun <T : Any> get(key: HTPropertyKey<T>): T?

    fun <T : Any> getOrDefault(key: HTPropertyKey.Defaulted<T>): T = get(key) ?: key.getDefaultValue()

    fun <T : Any> getOrThrow(key: HTPropertyKey<T>): T = get(key) ?: throw IllegalStateException("")

    operator fun contains(key: HTPropertyKey<*>): Boolean

    fun <T : Any, R : Any> map(key: HTPropertyKey<T>, transform: (T) -> R): R? = get(key)?.let(transform)

    fun <T : Any> ifPresent(key: HTPropertyKey<T>, action: (T) -> Unit) {
        map(key, action)
    }

    //    Empty    //

    object Empty : HTPropertyHolder {
        override fun <T : Any> get(key: HTPropertyKey<T>): T? = null

        override fun contains(key: HTPropertyKey<*>): Boolean = false

        override fun iterator(): Iterator<Pair<HTPropertyKey<*>, Any>> = listOf<Pair<HTPropertyKey<*>, Any>>().iterator()
    }
}
