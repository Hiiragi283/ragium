package hiiragi283.ragium.api.property

interface HTPropertyMap {
    operator fun <T : Any> get(key: HTPropertyKey<T>): T?

    fun <T : Any> getOrDefault(key: HTPropertyKey<T>, default: T): T = get(key) ?: default

    fun <T : Any> getOrDefault(key: HTPropertyKey.Defaulted<T>): T = get(key) ?: key.getDefaultValue()

    fun <T : Any> getOrThrow(key: HTPropertyKey<T>): T = get(key) ?: error("Unknown property with $key")
}
