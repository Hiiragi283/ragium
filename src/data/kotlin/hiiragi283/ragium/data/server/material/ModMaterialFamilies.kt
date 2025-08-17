package hiiragi283.ragium.data.server.material

import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.util.material.HTMaterialFamily
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.util.material.RagiumMaterialType

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
        .build(MaterialImpl(key))

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
        .build(MaterialImpl(key))

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
        .build(MaterialImpl(key))

    @JvmStatic
    fun getGem(key: String): HTMaterialFamily = GEMS[key] ?: error("Unregistered material: $key")

    //    Other    //

    @JvmField
    val COAL_COKE: HTMaterialFamily = HTMaterialFamily.Builder
        .fuel(null)
        .setMod()
        .build(RagiumMaterialType.COAL_COKE)

    //    MaterialImpl    //

    private data class MaterialImpl(private val name: String) : HTMaterialType {
        override fun getTranslatedName(type: HTLanguageType): String = throw UnsupportedOperationException()

        override fun getSerializedName(): String = name
    }
}
