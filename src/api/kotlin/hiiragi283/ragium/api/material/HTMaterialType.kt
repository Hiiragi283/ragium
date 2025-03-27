package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes

/**
 * 素材のタイプを管理するクラス
 */
enum class HTMaterialType {
    DEFAULT,
    ALLOY,
    GEM,
    INGOT_LIKE,
    METAL,
    MINERAL,
    ;

    /**
     * 指定した[HTTagPrefix]をインゴットや宝石といったメインの[HTTagPrefix]に変換します。
     * @return 対応する値がなければ`null`
     */
    fun getMainPrefix(): HTTagPrefix? = when (this) {
        DEFAULT -> null
        ALLOY -> HTTagPrefixes.INGOT
        GEM -> HTTagPrefixes.GEM
        INGOT_LIKE -> HTTagPrefixes.INGOT
        METAL -> HTTagPrefixes.INGOT
        MINERAL -> null
    }

    /**
     * 指定した[HTTagPrefix]を原石や宝石といった鉱石ドロップの[HTTagPrefix]に変換します。
     * @return 対応する値がなければ`null`
     */
    fun getOreResultPrefix(): HTTagPrefix? = when (this) {
        DEFAULT -> null
        ALLOY -> null
        GEM -> HTTagPrefixes.GEM
        INGOT_LIKE -> HTTagPrefixes.DUST
        METAL -> HTTagPrefixes.DUST
        MINERAL -> HTTagPrefixes.DUST
    }
}
