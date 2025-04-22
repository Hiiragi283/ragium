package hiiragi283.ragium.api.material.keys

import hiiragi283.ragium.api.material.HTMaterialKey

/**
 * Ragiumで使用されている素材の一覧
 */
object RagiumMaterials {
    //    Gem    //

    @JvmField
    val CRIMSON_CRYSTAL: HTMaterialKey = HTMaterialKey.of("crimson_crystal")

    @JvmField
    val WARPED_CRYSTAL: HTMaterialKey = HTMaterialKey.of("warped_crystal")

    @JvmField
    val RAGI_CRYSTAL: HTMaterialKey = HTMaterialKey.of("ragi_crystal")

    //    Metal    //

    @JvmField
    val RAGI_ALLOY: HTMaterialKey = HTMaterialKey.of("ragi_alloy")

    @JvmField
    val AZURE_STEEL: HTMaterialKey = HTMaterialKey.of("azure_steel")

    @JvmField
    val ADVANCED_RAGI_ALLOY: HTMaterialKey = HTMaterialKey.of("advanced_ragi_alloy")

    @JvmField
    val DEEP_STEEL: HTMaterialKey = HTMaterialKey.of("deep_steel")

    //    Mineral    //

    @JvmField
    val RAGINITE: HTMaterialKey = HTMaterialKey.of("raginite")
}
