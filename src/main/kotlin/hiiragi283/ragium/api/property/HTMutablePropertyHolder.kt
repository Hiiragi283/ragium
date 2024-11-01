package hiiragi283.ragium.api.property

interface HTMutablePropertyHolder : HTPropertyHolder {
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
