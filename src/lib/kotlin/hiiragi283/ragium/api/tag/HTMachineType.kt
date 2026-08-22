package hiiragi283.ragium.api.tag

import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix

enum class HTMachineType : HTMaterialLike {
    MECHANICAL,
    HEAT,
    CHEMICAL,
    BIO,
    ELECTRONICS,
    ARCANE,
    ;

    companion object {
        @JvmField
        val PREFIX = HTTagPrefix("machines", "machines/%s")
    }

    override val materialName: String get() = name.lowercase()
}
