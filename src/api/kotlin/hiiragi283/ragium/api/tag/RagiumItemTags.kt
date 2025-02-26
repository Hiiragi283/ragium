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

    @JvmStatic
    private fun commonTag(path: String): TagKey<Item> = itemTagKey(commonId(path))

    @JvmField
    val COAL_COKE: TagKey<Item> = commonTag("coal_coke")

    @JvmField
    val CROPS_WARPED_WART: TagKey<Item> = commonTag("crops/warped_wart")

    @JvmField
    val DOUGH: TagKey<Item> = commonTag("foods/dough")

    @JvmField
    val PAPER: TagKey<Item> = commonTag("paper")

    @JvmField
    val PLASTICS: TagKey<Item> = commonTag("plastics")

    @JvmField
    val SILICON: TagKey<Item> = commonTag("silicon")

    @JvmField
    val SLAG: TagKey<Item> = commonTag("slag")

    @JvmField
    val TOOLS_KNIFE: TagKey<Item> = commonTag("tools/knife")

    // Circuits
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
    val IGNORED_IN_INGREDIENT: TagKey<Item> = itemTagKey(RagiumAPI.id("ignored_in_ingredient"))

    @JvmField
    val LED_BLOCKS: TagKey<Item> = itemTagKey(RagiumAPI.id("led_blocks"))

    // Molds
    @JvmField
    val BALL_MOLDS: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/ball"))

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

    // Soils
    @JvmField
    val DIRT_SOILS: TagKey<Item> = itemTagKey(RagiumAPI.id("soils/dirt"))

    @JvmField
    val MUSHROOM_SOILS: TagKey<Item> = itemTagKey(RagiumAPI.id("soils/mushroom"))

    @JvmField
    val NETHER_SOILS: TagKey<Item> = itemTagKey(RagiumAPI.id("soils/nether"))

    @JvmField
    val END_SOILS: TagKey<Item> = itemTagKey(RagiumAPI.id("soils/end"))
}
