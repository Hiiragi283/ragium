package hiiragi283.ragium.common.material

import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.ragium.api.RagiumAPI

object RagiumMaterialKeys {
    @JvmStatic
    private fun create(path: String): HTMaterialKey = HTMaterialKey.of((RagiumAPI.id(path)))

    //    Minerals    //

    @JvmStatic
    val RAGINITE: HTMaterialKey = create("raginite")

    //    Gems    //

    @JvmStatic
    val RAGI_CRYSTAL: HTMaterialKey = create("ragi_crystal")

    //    Alloys    //

    @JvmStatic
    val RAGI_ALLOY: HTMaterialKey = create("ragi_alloy")

    @JvmStatic
    val ADVANCED_RAGI_ALLOY: HTMaterialKey = create("advanced_ragi_alloy")

    @JvmStatic
    val STAINLESS_STEEL: HTMaterialKey = create("stainless_steel")

    //    Others    //

    @JvmStatic
    val MEAT: HTMaterialKey = create("meat")

    @JvmStatic
    val COOKED_MEAT: HTMaterialKey = create("cooked_meat")
}
