package hiiragi283.ragium.common.material

import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.ragium.api.RagiumAPI

object RagiumMaterialKeys {
    //    Minerals    //

    @JvmStatic
    val RAGINITE: HTMaterialKey = HTMaterialKey.of(RagiumAPI.id("raginite"))

    //    Gems    //

    @JvmStatic
    val RAGI_CRYSTAL: HTMaterialKey = HTMaterialKey.of(RagiumAPI.id("ragi_crystal"))

    //    Alloys    //

    @JvmStatic
    val RAGI_ALLOY: HTMaterialKey = HTMaterialKey.of(RagiumAPI.id("ragi_alloy"))

    @JvmStatic
    val ADVANCED_RAGI_ALLOY: HTMaterialKey = HTMaterialKey.of(RagiumAPI.id("advanced_ragi_alloy"))

    //    Others    //

    @JvmStatic
    val MEAT: HTMaterialKey = HTMaterialKey.of(RagiumAPI.id("meat"))

    @JvmStatic
    val COOKED_MEAT: HTMaterialKey = HTMaterialKey.of(RagiumAPI.id("cooked_meat"))
}
