package hiiragi283.ragium.common.material

import hiiragi283.ragium.api.material.HTMaterialKey

object CommonMaterialKeys {
    //    Common Metal    //

    @JvmField
    val METALS: Map<String, HTMaterialKey> = listOf(
        // 3rd
        "aluminum",
        // 4th
        "titanium",
        "chrome",
        "chromium",
        "manganese",
        "cobalt",
        "nickel",
        "zinc",
        // 5th
        "palladium",
        "silver",
        "tin",
        "antimony",
        // 6th
        "tungsten",
        "osmium",
        "iridium",
        "platinum",
        "lead",
        // 7th
        "uranium",
        // Draconic Evolution
        "draconium",
        "draconium_awakened",
        // Just Dire Things
        "ferricore",
        "blazegold",
        "eclipsealloy",
        // Occultism
        "iesnium",
        // Replication
        "replica",
        // Twilight Forest
        "ironwood",
        "wrought_iron",
        "knightmetal",
    ).associateWith(HTMaterialKey::of)

    @JvmStatic
    fun getMetal(key: String): HTMaterialKey = METALS[key] ?: error("Unknown material: $key")

    @JvmField
    val ALLOYS: Map<String, HTMaterialKey> = listOf(
        // Alloys
        "steel",
        "invar",
        "electrum",
        "bronze",
        "brass",
        "enderium",
        "lumium",
        "signalum",
        "constantan",
        // Immersive Engineering,
        "hop_graphite",
        // Mekanism
        "refined_obsidian",
        "refined_glowstone",
        // Oritech
        "adamant",
        "duratium",
        "energite",
        "prometheum",
        // Twilight Forest
        "steeleaf",
        "fiery",
    ).associateWith(HTMaterialKey::of)

    @JvmStatic
    fun getAlloy(key: String): HTMaterialKey = ALLOYS[key] ?: error("Unknown material: $key")

    //    Common Gem    //

    @JvmField
    val GEMS: Map<String, HTMaterialKey> = listOf(
        "fluorite",
        "peridot",
        "ruby",
        "sapphire",
        // AA,
        "black_quartz",
        // AE2
        "certus_quartz",
        "fluix",
        // Ars Nouveau
        "source",
        // EvilCraft
        "dark_gem",
        "dark_power",
        // Forbidden
        "arcane_crystal",
        "corrupted_arcane_crystal",
        // Magitech,
        "tourmaline",
        // Oritech
        "fluxite",
        // Twilight Forest
        "carminite",
    ).associateWith(HTMaterialKey::of)

    @JvmStatic
    fun getGem(key: String): HTMaterialKey = GEMS[key] ?: error("Unknown material: $key")
}
