package hiiragi283.ragium.api.tag

import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix

/**
 * Ragiumで追加される機械のカテゴリを表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
enum class HTMachineType(langName: HTLangName) :
    HTMaterialLike,
    HTLangName by langName {
    MECHANICAL("Mechanical", "機械加工"),
    HEAT("Heat", "熱"),
    CHEMICAL("Chemical", "化学"),
    BIO("Bio", "生体"),
    ELECTRONICS("Electronics", "電子"),
    ARCANE("Arcane", "神秘")
    ;

    constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

    companion object {
        @JvmField
        val PREFIX = HTTagPrefix("machines", "machines/%s")
    }

    override val materialName: String get() = name.lowercase()
}
