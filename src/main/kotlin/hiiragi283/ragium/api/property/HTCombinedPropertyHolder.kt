package hiiragi283.ragium.api.property

interface HTCombinedPropertyHolder : HTPropertyHolder {
    val properties: List<HTPropertyHolder>

    override fun <T : Any> get(key: HTPropertyKey<T>): T? = properties.firstNotNullOfOrNull { it[key] }

    override fun contains(key: HTPropertyKey<*>): Boolean = properties.any { it.contains(key) }

    override fun iterator(): Iterator<Pair<HTPropertyKey<*>, Any>> = iterator {
        properties.forEach { holder: HTPropertyHolder -> yieldAll(holder.iterator()) }
    }
}
