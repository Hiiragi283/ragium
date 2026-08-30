package hiiragi283.lib.material.part

/**
 * 一般に使用される[HTPartKey]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
data object CommonParts {
    //    Block    //

    @JvmField
    val ORE = HTPartKey("ore")

    @JvmField
    val ORE_DEEPSLATE = HTPartKey("ore/deepslate")

    @JvmField
    val ORE_NETHER = HTPartKey("ore/nether")

    @JvmField
    val ORE_END = HTPartKey("ore/end")

    @JvmField
    val BLOCK = HTPartKey("block")

    @JvmField
    val RAW_BLOCK = HTPartKey("raw_block")

    //    Item    //

    @JvmField
    val DUST = HTPartKey("dust")

    @JvmField
    val FUEL = HTPartKey("fuel")

    @JvmField
    val GEAR = HTPartKey("gear")

    @JvmField
    val GEM = HTPartKey("gem")

    @JvmField
    val INGOT = HTPartKey("ingot")

    @JvmField
    val NUGGET = HTPartKey("nugget")

    @JvmField
    val PLATE = HTPartKey("plate")

    @JvmField
    val RAW = HTPartKey("raw")

    @JvmField
    val ROD = HTPartKey("rod")

    @JvmField
    val TINY = HTPartKey("tiny")
}
