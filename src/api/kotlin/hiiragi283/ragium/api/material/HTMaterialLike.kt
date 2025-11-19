package hiiragi283.ragium.api.material

/**
 * [HTMaterialKey]を保持するインターフェース
 */
fun interface HTMaterialLike {
    fun asMaterialKey(): HTMaterialKey

    fun asMaterialName(): String = asMaterialKey().name

    fun isOf(other: HTMaterialLike): Boolean = this.asMaterialKey() == other.asMaterialKey()
}
