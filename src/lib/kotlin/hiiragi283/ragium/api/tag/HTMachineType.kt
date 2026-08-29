package hiiragi283.ragium.api.tag

import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix

/**
 * Ragiumで追加される機械のカテゴリを表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
enum class HTMachineType : HTMaterialLike {
    MECHANICAL,
    HEAT,
    CHEMICAL,
    BIO,
    ELECTRONICS,
    ARCANE,
    ;

    companion object {
        @JvmField
        val PREFIX = HTTagPrefix("machines", "machines/%s")
    }

    override val materialName: String get() = name.lowercase()
}
