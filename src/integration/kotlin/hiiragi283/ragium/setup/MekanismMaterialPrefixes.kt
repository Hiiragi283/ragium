package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.material.HTMaterialPrefix
import hiiragi283.ragium.api.registry.impl.HTDeferredMaterialPrefixRegister

object MekanismMaterialPrefixes {
    @JvmField
    val REGISTER = HTDeferredMaterialPrefixRegister()

    @JvmField
    val DIRTY_DUST: HTMaterialPrefix = REGISTER.register("dirty_dust")

    @JvmField
    val CLUMP: HTMaterialPrefix = REGISTER.register("clump")

    @JvmField
    val SHARD: HTMaterialPrefix = REGISTER.register("shard")

    @JvmField
    val CRYSTAL: HTMaterialPrefix = REGISTER.register("crystal")

    @JvmField
    val PELLET: HTMaterialPrefix = REGISTER.register("pellet")

    @JvmField
    val ENRICHED: HTMaterialPrefix =
        REGISTER.register("enriched", "${RagiumConst.MEKANISM}:enriched", "${RagiumAPI.MOD_ID}:enriched/%s")
}
