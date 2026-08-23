package hiiragi283.lib.material

import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.resource.vanillaId
import net.minecraft.resources.Identifier

enum class VanillaMaterials(override val category: HTMaterialCategory, langName: HTLangName) :
    HTMaterial,
    HTLangName by langName {
    // Fuel
    COAL(HTMaterialCategory.FUEL, "Coal", "石炭"),
    CHARCOAL(HTMaterialCategory.FUEL, "Charcoal", "木炭"),

    // Mineral
    REDSTONE(HTMaterialCategory.MINERAL, "Redstone", "レッドストーン"),
    GLOWSTONE(HTMaterialCategory.MINERAL, "Glowstone", "グロウストーン"),

    // Gem
    LAPIS(HTMaterialCategory.GEM, "Lapis", "ラピス"),
    QUARTZ(HTMaterialCategory.GEM, "Quartz", "水晶"),
    AMETHYST(HTMaterialCategory.GEM, "Amethyst", "アメジスト"),
    DIAMOND(HTMaterialCategory.GEM, "Diamond", "ダイヤモンド"),
    EMERALD(HTMaterialCategory.GEM, "Emerald", "エメラルド"),
    ECHO(HTMaterialCategory.GEM, "Echo", "残響"),
    PRISMARINE(HTMaterialCategory.GEM, "Prismarine", "プリズマリン"),

    // Metal
    COPPER(HTMaterialCategory.METAL, "Copper", "銅"),
    IRON(HTMaterialCategory.METAL, "Iron", "鉄"),
    GOLD(HTMaterialCategory.METAL, "Gold", "金"),
    NETHERITE(HTMaterialCategory.METAL, "Netherite", "ネザライト"),

    // Other
    WOOD(HTMaterialCategory.OTHER, "Wood", "木"),
    GLASS(HTMaterialCategory.OTHER, "Glass", "ガラス"),
    OBSIDIAN(HTMaterialCategory.OTHER, "Obsidian", "黒曜石"),
    PAPER(HTMaterialCategory.OTHER, "Paper", "紙"),
    ;

    constructor(category: HTMaterialCategory, enName: String, jaName: String) : this(category, HTLangName(enName, jaName))

    override fun getId(): Identifier = vanillaId(name.lowercase())
}
