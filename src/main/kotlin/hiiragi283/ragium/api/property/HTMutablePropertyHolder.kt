package hiiragi283.ragium.api.property

/**
 * Mutable [HTPropertyHolder]
 */
interface HTMutablePropertyHolder : HTPropertyHolder {
    operator fun <T : Any> set(key: HTPropertyKey<T>, value: T)

    fun add(key: HTPropertyKey<Unit>) {
        set(key, Unit)
    }

    /**
     * Set [value] if not null
     */
    fun <T : Any> setIfNonNull(key: HTPropertyKey<T>, value: T?) {
        value?.let { set(key, it) }
    }

    fun remove(id: HTPropertyKey<*>)

    fun <T : Any> removeIf(key: HTPropertyKey<T>, filter: (T) -> Boolean) {
        val existValue: T = get(key) ?: return
        if (filter(existValue)) {
            remove(key)
        }
    }

    /**
     * @see [MutableMap.computeIfAbsent]
     */
    fun <T : Any> computeIfAbsent(key: HTPropertyKey<T>, mapping: () -> T): T = get(key) ?: mapping().apply { set(key, this) }
}
