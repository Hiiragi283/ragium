package hiiragi283.ragium.common.material

import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.HTMaterialCategory
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.resources.Identifier

enum class RagiumMaterials(override val category: HTMaterialCategory, langName: HTLangName) :
    HTMaterial,
    HTLangName by langName {
    RAGINITE(HTMaterialCategory.MINERAL, "Raginite", "ラギナイト"),
    ;

    constructor(category: HTMaterialCategory, enName: String, jaName: String) : this(category, HTLangName(enName, jaName))

    override fun getId(): Identifier = RagiumAPI.id(name.lowercase())
}
