package hiiragi283.ragium.api.property

interface HTDelegatedPropertyHolder : HTPropertyHolder {
    val delegated: HTPropertyHolder

    override fun <T : Any> get(id: HTPropertyKey<T>): T? = delegated[id]

    override fun contains(id: HTPropertyKey<*>): Boolean = id in delegated

    override fun iterator(): Iterator<Pair<HTPropertyKey<*>, Any>> = delegated.iterator()
}
