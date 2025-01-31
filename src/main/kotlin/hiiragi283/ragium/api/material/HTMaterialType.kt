package hiiragi283.ragium.api.material

/**
 * 素材のタイプを管理するクラス
 */
enum class HTMaterialType(val validPrefixes: Set<HTTagPrefix>) {
    ALLOY(HTTagPrefix.METAL_PARTS),
    DUST(HTTagPrefix.DUST),
    GEM(
        HTTagPrefix.DUST,
        HTTagPrefix.GEAR,
        HTTagPrefix.GEM,
        HTTagPrefix.ORE,
        HTTagPrefix.STORAGE_BLOCK,
    ),
    METAL(HTTagPrefix.METAL_PARTS, HTTagPrefix.ORE_PARTS, HTTagPrefix.MEKANISM_PARTS),
    MINERAL(listOf(HTTagPrefix.DUST), HTTagPrefix.ORE_PARTS, HTTagPrefix.MEKANISM_PARTS),
    ;

    constructor(vararg prefixed: HTTagPrefix) : this(prefixed.toSortedSet())

    constructor(vararg lists: List<HTTagPrefix>) : this(lists.toList().flatten().toSortedSet())

    fun isValidPrefix(prefix: HTTagPrefix): Boolean = prefix in validPrefixes

    /**
     * 指定した[HTTagPrefix]をインゴットや宝石といったメインの[HTTagPrefix]に変換します。
     * @return 対応する値がなければ`null`
     */
    fun getMainPrefix(): HTTagPrefix? = when (this) {
        ALLOY -> HTTagPrefix.INGOT
        DUST -> null
        GEM -> HTTagPrefix.GEM
        METAL -> HTTagPrefix.INGOT
        MINERAL -> null
    }

    /**
     * 指定した[HTTagPrefix]を原石や宝石といった鉱石ドロップの[HTTagPrefix]に変換します。
     * @return 対応する値がなければ`null`
     */
    fun getRawPrefix(): HTTagPrefix? = when (this) {
        ALLOY -> null
        DUST -> null
        GEM -> HTTagPrefix.GEM
        METAL -> HTTagPrefix.RAW_MATERIAL
        MINERAL -> HTTagPrefix.RAW_MATERIAL
    }
}
