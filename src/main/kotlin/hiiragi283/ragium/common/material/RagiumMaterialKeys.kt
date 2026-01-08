package hiiragi283.ragium.common.material

import hiiragi283.core.api.material.HTMaterialKey

object RagiumMaterialKeys {
    //    Minerals    //

    @JvmStatic
    val RAGINITE: HTMaterialKey = HTMaterialKey.of("raginite")

    //    Gems    //

    @JvmStatic
    val RAGI_CRYSTAL: HTMaterialKey = HTMaterialKey.of("ragi_crystal")

    //    Alloys    //

    @JvmStatic
    val RAGI_ALLOY: HTMaterialKey = HTMaterialKey.of("ragi_alloy")

    @JvmStatic
    val ADVANCED_RAGI_ALLOY: HTMaterialKey = HTMaterialKey.of("advanced_ragi_alloy")

    //    Others    //

    @JvmStatic
    val MEAT: HTMaterialKey = HTMaterialKey.of("meat")

    @JvmStatic
    val COOKED_MEAT: HTMaterialKey = HTMaterialKey.of("cooked_meat")
}
