package hiiragi283.ragium.api.material.prefix

import hiiragi283.ragium.api.material.HTMaterialKey

object HTTagPrefixes {
    //    Common    //

    @JvmField
    val DUST: HTTagPrefix = HTSimpleTagPrefix("dust")

    @JvmField
    val GEAR: HTTagPrefix = HTSimpleTagPrefix("gear")

    @JvmField
    val GEM: HTTagPrefix = object : HTSimpleTagPrefix("gem") {
        override fun createPath(key: HTMaterialKey): String = key.name
    }

    @JvmField
    val INGOT: HTTagPrefix = HTSimpleTagPrefix("ingot")

    @JvmField
    val NUGGET: HTTagPrefix = HTSimpleTagPrefix("nugget")

    @JvmField
    val ORE: HTTagPrefix = HTSimpleTagPrefix("ore")

    @JvmField
    val PLATE: HTTagPrefix = HTSimpleTagPrefix("plate")

    @JvmField
    val RAW_MATERIAL: HTTagPrefix = object : HTSimpleTagPrefix("raw_material") {
        override fun createPath(key: HTMaterialKey): String = "raw_${key.name}"
    }

    @JvmField
    val ROD: HTTagPrefix = HTSimpleTagPrefix("rod")

    @JvmField
    val STORAGE_BLOCK: HTTagPrefix = object : HTSimpleTagPrefix("storage_block") {
        override fun createPath(key: HTMaterialKey): String = "${key.name}_block"
    }

    //    Mekanism    //

    @JvmField
    val DIRTY_DUST: HTTagPrefix = object : HTSimpleTagPrefix("dirty_dust") {
        override fun createPath(key: HTMaterialKey): String = "dirty_${key.name}_dust"
    }

    @JvmField
    val CLUMP: HTTagPrefix = HTSimpleTagPrefix("clump")

    @JvmField
    val SHARD: HTTagPrefix = HTSimpleTagPrefix("shard")

    @JvmField
    val CRYSTAL: HTTagPrefix = HTSimpleTagPrefix("crystal")
}
