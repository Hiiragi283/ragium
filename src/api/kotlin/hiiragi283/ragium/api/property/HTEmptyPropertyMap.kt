package hiiragi283.ragium.api.property

object HTEmptyPropertyMap : HTPropertyMap {
    override fun <T : Any> get(key: HTPropertyKey<T>): T? = null
}
