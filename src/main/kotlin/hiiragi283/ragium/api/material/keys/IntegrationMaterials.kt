package hiiragi283.ragium.api.material.keys

import hiiragi283.ragium.api.material.HTMaterialKey

/**
 * 連携mod向けの素材の一覧
 */
object IntegrationMaterials {
    //    Actually Additions    //

    @JvmField
    val BLACK_QUARTZ: HTMaterialKey = HTMaterialKey.of("black_quartz")

    //    Evil Craft    //

    @JvmField
    val DARK_GEM: HTMaterialKey = HTMaterialKey.of("dark")

    //    Mekanism    //

    @JvmField
    val REFINED_GLOWSTONE: HTMaterialKey = HTMaterialKey.of("refined_glowstone")

    @JvmField
    val REFINED_OBSIDIAN: HTMaterialKey = HTMaterialKey.of("refined_obsidian")
}
