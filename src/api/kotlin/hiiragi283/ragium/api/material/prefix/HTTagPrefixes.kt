package hiiragi283.ragium.api.material.prefix

import hiiragi283.ragium.api.material.HTMaterial

object HTTagPrefixes {
    //    Common    //

    @JvmField
    val DUST: HTTagPrefix = HTSimpleTagPrefix("dust")

    @JvmField
    val GEAR: HTTagPrefix = HTSimpleTagPrefix("gear")

    @JvmField
    val GEM: HTTagPrefix = object : HTSimpleTagPrefix("gem") {
        override fun createPath(material: HTMaterial): String = material.materialName
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
        override fun createPath(material: HTMaterial): String = "raw_${material.materialName}"
    }

    @JvmField
    val ROD: HTTagPrefix = HTSimpleTagPrefix("rod")

    @JvmField
    val STORAGE_BLOCK: HTTagPrefix = object : HTSimpleTagPrefix("storage_block") {
        override fun createPath(material: HTMaterial): String = "${material.materialName}_block"
    }

    //    Mekanism    //

    @JvmField
    val DIRTY_DUST: HTTagPrefix = object : HTSimpleTagPrefix("dirty_dust") {
        override fun createPath(material: HTMaterial): String = "dirty_${material.materialName}_dust"
    }

    @JvmField
    val CLUMP: HTTagPrefix = HTSimpleTagPrefix("clump")

    @JvmField
    val SHARD: HTTagPrefix = HTSimpleTagPrefix("shard")

    @JvmField
    val CRYSTAL: HTTagPrefix = HTSimpleTagPrefix("crystal")
}
