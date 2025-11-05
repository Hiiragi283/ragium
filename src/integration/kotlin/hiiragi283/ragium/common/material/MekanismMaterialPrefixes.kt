package hiiragi283.ragium.common.material

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.material.prefix.HTPrefixLike

enum class MekanismMaterialPrefixes : HTPrefixLike {
    DIRTY_DUST,
    CLUMP,
    SHARD,
    CRYSTAL,
    PELLET,
    ENRICHED {
        override val prefix: HTMaterialPrefix = HTMaterialPrefix(
            "enriched",
            "${RagiumConst.MEKANISM}:enriched",
            "${RagiumAPI.MOD_ID}:enriched/%s",
        )
    },
    ;

    protected open val prefix: HTMaterialPrefix = HTMaterialPrefix(name.lowercase())

    override fun asMaterialPrefix(): HTMaterialPrefix = prefix
}
