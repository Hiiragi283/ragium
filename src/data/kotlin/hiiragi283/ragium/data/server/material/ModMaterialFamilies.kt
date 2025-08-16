package hiiragi283.ragium.data.server.material

import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.material.HTMaterialFamily

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
    ).associateWith(::commonMetal)

    @JvmStatic
    private fun commonMetal(key: String): HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(null)
        .setMod()
        .build(key)

    @JvmStatic
    fun getMetal(key: String): HTMaterialFamily = METALS[key] ?: error("Unregistered material: $key")

    @JvmField
    val ALLOYS: Map<String, HTMaterialFamily> = listOf(
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
    ).associateWith(::commonAlloy)

    @JvmStatic
    private fun commonAlloy(key: String): HTMaterialFamily = HTMaterialFamily.Builder
        .ingotAlloy(null)
        .setMod()
        .build(key)

    @JvmStatic
    fun getAlloy(key: String): HTMaterialFamily = ALLOYS[key] ?: error("Unregistered material: $key")

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
        .setMod()
        .build(key)

    @JvmStatic
    fun getGem(key: String): HTMaterialFamily = GEMS[key] ?: error("Unregistered material: $key")

    //    Other    //

    @JvmField
    val COAL_COKE: HTMaterialFamily = HTMaterialFamily.Builder
        .fuel(null)
        .setMod()
        .build(RagiumConst.COAL_COKE)
}
