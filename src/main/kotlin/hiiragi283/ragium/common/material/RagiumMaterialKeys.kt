package hiiragi283.ragium.common.material

import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.ragium.api.RagiumAPI

data object RagiumMaterialKeys {
    @JvmStatic
    private fun create(path: String): HTMaterialKey = HTMaterialKey(RagiumAPI.id(path))

    //    Fuels    //

    @JvmField
    val COAL_COKE: HTMaterialKey = create("coal_coke")

    //    Minerals    //

    @JvmField
    val BORAX: HTMaterialKey = create("borax")

    @JvmField
    val NITER: HTMaterialKey = create("niter")

    @JvmField
    val SALT: HTMaterialKey = create("salt")

    @JvmField
    val SULFUR: HTMaterialKey = create("sulfur")

    @JvmField
    val RAGINITE: HTMaterialKey = create("raginite")

    //    Alloys    //

    @JvmField
    val STEEL: HTMaterialKey = create("steel")
}
