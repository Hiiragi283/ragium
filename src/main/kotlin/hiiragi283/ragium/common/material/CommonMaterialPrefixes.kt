package hiiragi283.ragium.common.material

import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.material.prefix.HTPrefixLike

enum class CommonMaterialPrefixes : HTPrefixLike {
    // Block
    ORE,
    GLASS_BLOCK,
    STORAGE_BLOCK,
    RAW_STORAGE_BLOCK {
        override val prefix: HTMaterialPrefix = HTMaterialPrefix("raw_storage_block", "c:storage_blocks", "c:storage_blocks/raw_%s")
    },

    // Item
    CROP,
    DUST,
    FOOD,
    GEM,
    GEAR,
    INGOT,
    NUGGET,
    PLATE,
    RAW_MATERIAL,
    ROD,

    // Item - Custom
    DOUGH,
    FLOUR,
    FUEL,
    JAM,
    RAW_MATERIAL_DYE {
        override val prefix: HTMaterialPrefix = HTMaterialPrefix("raw_materials/dye")
    },
    SCRAP,
    ;

    protected open val prefix: HTMaterialPrefix = HTMaterialPrefix(name.lowercase())

    override fun asMaterialPrefix(): HTMaterialPrefix = prefix
}
