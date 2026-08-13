package hiiragi283.lib.tag

/**
 * 共通の[HTTagPrefix]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object CommonTagPrefixes {
    //    Block    //

    @JvmField
    val ORE = HTTagPrefix("ores", "ores/%s")

    @JvmField
    val STORAGE_BLOCK = HTTagPrefix("storage_blocks", "storage_blocks/%s")

    @JvmField
    val RAW_STORAGE_BLOCK = HTTagPrefix("storage_blocks", "storage_blocks/raw_%s")

    //    Item    //

    @JvmField
    val DUST = HTTagPrefix("dusts", "dusts/%s")

    @JvmField
    val GEAR = HTTagPrefix("gears", "gears/%s")

    @JvmField
    val GEM = HTTagPrefix("gems", "gems/%s")

    @JvmField
    val INGOT = HTTagPrefix("ingots", "ingots/%s")

    @JvmField
    val NUGGET = HTTagPrefix("nuggets", "nuggets/%s")

    @JvmField
    val PLATE = HTTagPrefix("plates", "plates/%s")

    @JvmField
    val RAW_MATERIALS = HTTagPrefix("raw_materials", "raw_materials/%s")

    @JvmField
    val ROD = HTTagPrefix("rods", "rods/%s")

    @JvmField
    val TINY = HTTagPrefix("tiny", "tiny/%s")
}
