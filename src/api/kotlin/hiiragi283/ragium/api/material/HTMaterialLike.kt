package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.data.lang.HTLangName

/**
 * [HTMaterialKey]を保持するインターフェース
 */
fun interface HTMaterialLike {
    fun asMaterialKey(): HTMaterialKey

    fun asMaterialName(): String = asMaterialKey().name

    fun isOf(other: HTMaterialLike): Boolean = this.asMaterialKey() == other.asMaterialKey()

    interface Translatable :
        HTMaterialLike,
        HTLangName
}
