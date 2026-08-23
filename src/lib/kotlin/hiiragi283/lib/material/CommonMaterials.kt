package hiiragi283.lib.material

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.resource.toId
import net.minecraft.resources.Identifier

enum class CommonMaterials(override val category: HTMaterialCategory, langName: HTLangName) :
    HTMaterial,
    HTLangName by langName {
    // Fuel
    COAL_COKE(HTMaterialCategory.FUEL, "Coal Coke", "石炭コークス"),

    // Mineral
    SALT(HTMaterialCategory.MINERAL, "Salt", "食塩"),
    NITER(HTMaterialCategory.MINERAL, "Niter", "硝石"),
    BORAX(HTMaterialCategory.MINERAL, "Borax", "ホウ砂"),

    // Metal
    STEEL(HTMaterialCategory.METAL, "Steel", "鋼鉄"),
    ;

    constructor(category: HTMaterialCategory, enName: String, jaName: String) : this(category, HTLangName(enName, jaName))

    override fun getId(): Identifier = HTConstants.COMMON.toId(name.lowercase())
}
