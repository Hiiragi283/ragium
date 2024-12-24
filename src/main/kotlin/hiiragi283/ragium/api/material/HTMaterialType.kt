package hiiragi283.ragium.api.material

/**
 * Represent material type
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
    ),
    METAL(
        HTTagPrefix.DUST,
        HTTagPrefix.GEAR,
        HTTagPrefix.INGOT,
        HTTagPrefix.ORE,
        HTTagPrefix.PLATE,
        HTTagPrefix.RAW_MATERIAL,
        HTTagPrefix.STORAGE_BLOCK,
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
     * Transform main prefix
     * @see [hiiragi283.ragium.api.RagiumPlugin.RecipeHelper.useItemFromMainPrefix]
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
     * Transform raw material prefix
     * @see [hiiragi283.ragium.api.RagiumPlugin.RecipeHelper.useItemFromRawPrefix]
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
