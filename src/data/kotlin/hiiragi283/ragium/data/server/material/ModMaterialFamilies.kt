package hiiragi283.ragium.data.server.material

import com.buuz135.replication.ReplicationRegistry
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
    ).associateWith(::commonGem)

    @JvmStatic
    private fun commonGem(key: String): HTMaterialFamily = HTMaterialFamily.Builder
        .gem(null)
        .setEntry(HTMaterialFamily.Variant.DUSTS, null)
        .setEntry(HTMaterialFamily.Variant.ORES, null)
        .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, null)
        .setMod()
        .build(key)

    //    Replication    //

    @JvmField
    val REPLICA: HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(ReplicationRegistry.Items.REPLICA_INGOT.get())
        .setEntry(HTMaterialFamily.Variant.RAW_MATERIALS, ReplicationRegistry.Items.RAW_REPLICA.get())
        .setMod()
        .build("replica")
}
