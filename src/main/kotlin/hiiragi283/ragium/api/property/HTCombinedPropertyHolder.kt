package hiiragi283.ragium.api.property

interface HTCombinedPropertyHolder : HTPropertyHolder {
    val properties: List<HTPropertyHolder>

    override fun <T : Any> get(id: HTPropertyKey<T>): T? = properties.firstNotNullOfOrNull { it[id] }

    override fun contains(id: HTPropertyKey<*>): Boolean = properties.any { it.contains(id) }

    override fun iterator(): Iterator<Pair<HTPropertyKey<*>, Any>> = iterator {
        properties.forEach { holder: HTPropertyHolder -> yieldAll(holder.iterator()) }
    }
}
