package hiiragi283.ragium.setup

import hiiragi283.ragium.api.material.HTMaterialPrefix
import hiiragi283.ragium.api.registry.impl.HTDeferredMaterialPrefixRegister

object CommonMaterialPrefixes {
    @JvmField
    val REGISTER = HTDeferredMaterialPrefixRegister()

    //    Block    //

    @JvmField
    val ORE: HTMaterialPrefix = REGISTER.register("ore")

    @JvmField
    val GLASS_BLOCK: HTMaterialPrefix = REGISTER.register("glass_block")

    @JvmField
    val GLASS_BLOCK_TINTED: HTMaterialPrefix = REGISTER.register("tinted_glass_block", "c:glass_blocks/tinted")

    @JvmField
    val STORAGE_BLOCK: HTMaterialPrefix = REGISTER.register("storage_block")

    @JvmField
    val RAW_STORAGE_BLOCK: HTMaterialPrefix = REGISTER.register("raw_storage_block", "c:storage_blocks", "c:storage_blocks/raw_%s")

    //    Item    //

    @JvmField
    val DUST: HTMaterialPrefix = REGISTER.register("dust")

    @JvmField
    val GEM: HTMaterialPrefix = REGISTER.register("gem")

    @JvmField
    val GEAR: HTMaterialPrefix = REGISTER.register("gear")

    @JvmField
    val INGOT: HTMaterialPrefix = REGISTER.register("ingot")

    @JvmField
    val NUGGET: HTMaterialPrefix = REGISTER.register("nugget")

    @JvmField
    val PLATE: HTMaterialPrefix = REGISTER.register("plate")

    @JvmField
    val RAW_MATERIAL: HTMaterialPrefix = REGISTER.register("raw_material")

    @JvmField
    val ROD: HTMaterialPrefix = REGISTER.register("rod")

    @JvmField
    val CIRCUIT: HTMaterialPrefix = REGISTER.register("circuit")

    @JvmField
    val FUEL: HTMaterialPrefix = REGISTER.register("fuel")

    @JvmField
    val SCRAP: HTMaterialPrefix = REGISTER.register("scrap")
}
