package hiiragi283.ragium.common.material

import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.material.prefix.HTPrefixLike

enum class CommonMaterialPrefixes : HTPrefixLike {
    // Block
    ORE,
    GLASS_BLOCK,
    GLASS_BLOCK_TINTED {
        override val prefix: HTMaterialPrefix = HTMaterialPrefix("tinted_glass_block", "c:glass_blocks/tinted")
    },
    STORAGE_BLOCK,
    RAW_STORAGE_BLOCK {
        override val prefix: HTMaterialPrefix = HTMaterialPrefix("raw_storage_block", "c:storage_blocks", "c:storage_blocks/raw_%s")
    },

    // Item
    DUST,
    GEM,
    GEAR,
    INGOT,
    NUGGET,
    PLATE,
    RAW_MATERIAL,
    ROD,
    CIRCUIT,
    FUEL,
    SCRAP,
    ;

    protected open val prefix: HTMaterialPrefix = HTMaterialPrefix(name.lowercase())

    override fun asMaterialPrefix(): HTMaterialPrefix = prefix
}
