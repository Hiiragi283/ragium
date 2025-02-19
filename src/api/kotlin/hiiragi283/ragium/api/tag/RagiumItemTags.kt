package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * Ragiumが使用するアイテムの[TagKey]の一覧
 */
object RagiumItemTags {
    //    Common    //

    @JvmField
    val COAL_COKE: TagKey<Item> = itemTagKey(commonId("coal_coke"))

    @JvmField
    val DOUGH: TagKey<Item> = itemTagKey(commonId("foods/dough"))

    @JvmField
    val PLASTICS: TagKey<Item> = itemTagKey(commonId("plastics"))

    @JvmField
    val SLAG: TagKey<Item> = itemTagKey(commonId("slag"))

    @JvmField
    val BASIC_CIRCUIT: TagKey<Item> = itemTagKey(commonId("circuits/basic"))

    @JvmField
    val ADVANCED_CIRCUIT: TagKey<Item> = itemTagKey(commonId("circuits/advanced"))

    @JvmField
    val ELITE_CIRCUIT: TagKey<Item> = itemTagKey(commonId("circuits/elite"))

    @JvmField
    val ULTIMATE_CIRCUIT: TagKey<Item> = itemTagKey(commonId("circuits/ultimate"))

    //    Ragium    //

    @JvmField
    val CAPACITY_ENCHANTABLE: TagKey<Item> = itemTagKey(RagiumAPI.id("enchantable/capacity"))

    @JvmField
    val LED_BLOCKS: TagKey<Item> = itemTagKey(RagiumAPI.id("led_blocks"))

    @JvmField
    val GEAR_MOLDS: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/gear"))

    @JvmField
    val PLATE_MOLDS: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/plate"))

    @JvmField
    val ROD_MOLDS: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/rod"))

    @JvmField
    val WIRE_MOLDS: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/wire"))

    @JvmField
    val SOLAR_PANELS: TagKey<Item> = itemTagKey(RagiumAPI.id("solar_panels"))

    @JvmField
    val DIRT_SOILS: TagKey<Item> = itemTagKey(RagiumAPI.id("soils/dirt"))

    @JvmField
    val MUSHROOM_SOILS: TagKey<Item> = itemTagKey(RagiumAPI.id("soils/mushroom"))

    @JvmField
    val NETHER_SOILS: TagKey<Item> = itemTagKey(RagiumAPI.id("soils/nether"))

    @JvmField
    val END_SOILS: TagKey<Item> = itemTagKey(RagiumAPI.id("soils/end"))

    @JvmField
    val IGNORED_IN_INGREDIENT: TagKey<Item> = itemTagKey(RagiumAPI.id("ignored_in_ingredient"))
}
