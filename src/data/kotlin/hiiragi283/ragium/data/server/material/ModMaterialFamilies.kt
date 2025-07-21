package hiiragi283.ragium.data.server.material

import hiiragi283.ragium.api.util.HTMaterialFamily

object ModMaterialFamilies {
    //    Common Metal    //

    @JvmField
    val METALS: Map<String, HTMaterialFamily> = listOf(
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
        // Draconic Evolution
        "draconium",
        "draconium_awakened",
        // Immersive Engineering,
        "hop_graphite",
        // Just Dire Things
        "ferricore",
        "blazegold",
        "eclipsealloy",
        // Mekanism
        "refined_obsidian",
        "refined_glowstone",
        // Occultism
        "iesnium",
        // Oritech
        "adamant",
        "duratium",
        "energite",
        "prometheum",
        // Replication
        "replica",
        // Twilight Forest
        "ironwood",
        "steeleaf",
        "wrought_iron",
        "knightmetal",
        "fiery",
    ).associateWith(::commonMetal)

    @JvmStatic
    private fun commonMetal(key: String): HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(null)
        .setEntry(HTMaterialFamily.Variant.DUSTS, null)
        .setEntry(HTMaterialFamily.Variant.NUGGETS, null)
        .setEntry(HTMaterialFamily.Variant.ORES, null)
        .setEntry(HTMaterialFamily.Variant.RAW_MATERIALS, null)
        .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, null)
        .setMod()
        .build(key)

    @JvmStatic
    fun getMetal(key: String): HTMaterialFamily = METALS[key] ?: error("Unregistered material: $key")

    //    Common Gem    //

    @JvmField
    val GEMS: Map<String, HTMaterialFamily> = listOf(
        "cinnabar",
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
        // Oritech
        "fluxite",
        // Twilight Forest
        "carminite",
    ).associateWith(::commonGem)

    @JvmStatic
    private fun commonGem(key: String): HTMaterialFamily = HTMaterialFamily.Builder
        .gem(null)
        .setEntry(HTMaterialFamily.Variant.DUSTS, null)
        .setEntry(HTMaterialFamily.Variant.ORES, null)
        .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, null)
        .setMod()
        .build(key)

    @JvmStatic
    fun getGem(key: String): HTMaterialFamily = GEMS[key] ?: error("Unregistered material: $key")
}
