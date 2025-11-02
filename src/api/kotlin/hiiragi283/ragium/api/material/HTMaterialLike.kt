package hiiragi283.ragium.api.material

fun interface HTMaterialLike {
    fun asMaterialKey(): HTMaterialKey

    fun asMaterialName(): String = asMaterialKey().name
}
