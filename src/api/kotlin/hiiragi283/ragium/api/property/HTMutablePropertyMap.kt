package hiiragi283.ragium.api.property

interface HTMutablePropertyMap : HTPropertyMap {
    operator fun <T : Any> set(key: HTPropertyKey<T>, value: T): T?

    fun <T : Any> computeIfAbsent(key: HTPropertyKey<T>, mapper: () -> T): T {
        val oldValue: T? = get(key)
        if (oldValue == null) {
            val newValue: T = mapper()
            set(key, newValue)
            return newValue
        } else {
            return oldValue
        }
    }

    fun <T : Any> computeIfAbsent(key: HTPropertyKey.Defaulted<T>): T = computeIfAbsent(key, key::getDefaultValue)
}
