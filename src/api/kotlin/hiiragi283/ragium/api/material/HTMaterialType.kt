package hiiragi283.ragium.api.material

/**
 * 素材のタイプを管理するクラス
 */
enum class HTMaterialType {
    ALLOY,
    DUST,
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
        ALLOY -> HTTagPrefix.INGOT
        DUST -> null
        GEM -> HTTagPrefix.GEM
        INGOT_LIKE -> HTTagPrefix.INGOT
        METAL -> HTTagPrefix.INGOT
        MINERAL -> null
    }

    /**
     * 指定した[HTTagPrefix]を原石や宝石といった鉱石ドロップの[HTTagPrefix]に変換します。
     * @return 対応する値がなければ`null`
     */
    fun getOreResultPrefix(): HTTagPrefix? = when (this) {
        ALLOY -> null
        DUST -> null
        GEM -> HTTagPrefix.GEM
        INGOT_LIKE -> HTTagPrefix.DUST
        METAL -> HTTagPrefix.DUST
        MINERAL -> HTTagPrefix.DUST
    }
}
