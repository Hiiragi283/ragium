package hiiragi283.ragium.api.material

/**
 * 素材のタイプを管理するクラス
 */
enum class HTMaterialType(val validPrefixes: List<HTTagPrefix>) {
    ALLOY(HTTagPrefix.DUST, HTTagPrefix.GEAR, HTTagPrefix.INGOT, HTTagPrefix.PLATE, HTTagPrefix.STORAGE_BLOCK),
    DUST(HTTagPrefix.DUST),
    GEM(
        HTTagPrefix.DUST,
        HTTagPrefix.GEAR,
        HTTagPrefix.GEM,
        HTTagPrefix.ORE,
        HTTagPrefix.STORAGE_BLOCK,
        HTTagPrefix.WIRE,
    ),
    METAL(
        HTTagPrefix.DUST,
        HTTagPrefix.GEAR,
        HTTagPrefix.INGOT,
        HTTagPrefix.ORE,
        HTTagPrefix.PLATE,
        HTTagPrefix.RAW_MATERIAL,
        HTTagPrefix.ROD,
        HTTagPrefix.STORAGE_BLOCK,
        HTTagPrefix.WIRE,
    ),
    MINERAL(
        HTTagPrefix.DUST,
        HTTagPrefix.ORE,
        HTTagPrefix.RAW_MATERIAL,
    ),
    PLATE(HTTagPrefix.DUST, HTTagPrefix.PLATE),
    ;

    constructor(vararg prefixed: HTTagPrefix) : this(prefixed.toList())

    fun isValidPrefix(prefix: HTTagPrefix): Boolean = prefix in validPrefixes

    /**
     * 指定した[HTTagPrefix]をインゴットや宝石といったメインの[HTTagPrefix]に変換します。
     * @see [hiiragi283.ragium.api.RagiumPlugin.RecipeHelper.useItemFromMainPrefix]
     * @return 対応する値がなければnull
     */
    fun getMainPrefix(): HTTagPrefix? = when (this) {
        ALLOY -> HTTagPrefix.INGOT
        DUST -> null
        GEM -> HTTagPrefix.GEM
        METAL -> HTTagPrefix.INGOT
        MINERAL -> null
        PLATE -> null
    }

    /**
     * 指定した[HTTagPrefix]を原石や宝石といった鉱石ドロップの[HTTagPrefix]に変換します。
     * @see [hiiragi283.ragium.api.RagiumPlugin.RecipeHelper.useItemFromRawPrefix]
     * @return 対応する値がなければnull
     */
    fun getRawPrefix(): HTTagPrefix? = when (this) {
        ALLOY -> null
        DUST -> null
        GEM -> HTTagPrefix.GEM
        METAL -> HTTagPrefix.RAW_MATERIAL
        MINERAL -> HTTagPrefix.RAW_MATERIAL
        PLATE -> null
    }
}
