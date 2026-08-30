package hiiragi283.lib.material.property

import hiiragi283.lib.property.HTPropertyKey
import hiiragi283.ragium.api.RagiumAPI

data object HTMaterialPropertyKeys {
    @JvmField
    val ORIGIN_MOD_ID: HTPropertyKey.Simple<String> = HTPropertyKey.Simple(RagiumAPI.id("origin_mod_id"))

    //    Resource    //

    @JvmField
    val COLOR: HTPropertyKey.Simple<Int> = HTPropertyKey.Simple(RagiumAPI.id("color"))
}
