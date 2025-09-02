package hiiragi283.ragium.api.util.material

object HTCommonMaterialTypes {
    //    Common Metal    //

    @JvmField
    val METALS: Map<String, HTMaterialType> = listOf(
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
    ).associateWith(::MaterialImpl)

    @JvmStatic
    fun getMetal(key: String): HTMaterialType = METALS[key] ?: error("Unknown material: $key")

    @JvmField
    val ALLOYS: Map<String, HTMaterialType> = listOf(
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
    ).associateWith(::MaterialImpl)

    @JvmStatic
    fun getAlloy(key: String): HTMaterialType = ALLOYS[key] ?: error("Unknown material: $key")

    //    Common Gem    //

    @JvmField
    val GEMS: Map<String, HTMaterialType> = listOf(
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
    ).associateWith(::MaterialImpl)

    @JvmStatic
    fun getGem(key: String): HTMaterialType = GEMS[key] ?: error("Unknown material: $key")

    //    MaterialImpl    //

    private data class MaterialImpl(private val name: String) : HTMaterialType {
        override fun getSerializedName(): String = name
    }
}
