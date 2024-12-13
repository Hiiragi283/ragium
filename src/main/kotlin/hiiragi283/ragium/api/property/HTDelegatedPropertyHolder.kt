package hiiragi283.ragium.api.property

/**
 * A simple implementation for [HTPropertyHolder]
 */
interface HTDelegatedPropertyHolder : HTPropertyHolder {
    val delegated: HTPropertyHolder

    override fun <T : Any> get(key: HTPropertyKey<T>): T? = delegated[key]

    override fun contains(key: HTPropertyKey<*>): Boolean = key in delegated

    override fun iterator(): Iterator<Pair<HTPropertyKey<*>, Any>> = delegated.iterator()
}
