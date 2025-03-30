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
    val PAPER: TagKey<Item> = commonTag("paper")

    @JvmField
    val PLASTICS: TagKey<Item> = commonTag("plastics")

    @JvmField
    val SILICON: TagKey<Item> = commonTag("silicon")

    @JvmField
    val SLAG: TagKey<Item> = commonTag("slag")

    @JvmField
    val TOOLS_KNIFE: TagKey<Item> = commonTag("tools/knife")

    @JvmField
    val TOOLS_FORGE_HAMMER: TagKey<Item> = commonTag("tools/forge_hammer")

    // Foods
    @JvmField
    val CROPS_WARPED_WART: TagKey<Item> = commonTag("crops/warped_wart")

    @JvmField
    val FLOURS: TagKey<Item> = commonTag("flours")

    @JvmField
    val FOOD_BUTTER: TagKey<Item> = commonTag("foods/butter")

    @JvmField
    val FOOD_CHEESE: TagKey<Item> = commonTag("foods/cheese")

    @JvmField
    val FOOD_CHOCOLATE: TagKey<Item> = commonTag("foods/chocolate")

    @JvmField
    val FOOD_DOUGH: TagKey<Item> = commonTag("foods/dough")

    // Circuits
    @JvmField
    val CIRCUITS: TagKey<Item> = itemTagKey(commonId("circuits"))

    @JvmField
    val CIRCUITS_BASIC: TagKey<Item> = itemTagKey(commonId("circuits/basic"))

    @JvmField
    val CIRCUITS_ADVANCED: TagKey<Item> = itemTagKey(commonId("circuits/advanced"))

    @JvmField
    val CIRCUITS_ELITE: TagKey<Item> = itemTagKey(commonId("circuits/elite"))

    // Glasses
    @JvmField
    val GLASS_BLOCKS_OBSIDIAN: TagKey<Item> = commonTag("glass_blocks/obsidian")

    @JvmField
    val GLASS_BLOCKS_QUARTZ: TagKey<Item> = commonTag("glass_blocks/quartz")

    @JvmField
    val GLASS_BLOCKS_SOUL: TagKey<Item> = commonTag("glass_blocks/soul")

    // Stones
    @JvmField
    val OBSIDIANS_MYSTERIOUS: TagKey<Item> = commonTag("obsidians/mysterious")

    //    Ragium    //

    @JvmField
    val CAPACITY_ENCHANTABLE: TagKey<Item> = itemTagKey(RagiumAPI.id("enchantable/capacity"))

    @JvmField
    val DYNAMITES: TagKey<Item> = itemTagKey(RagiumAPI.id("dynamites"))

    @JvmField
    val LED_BLOCKS: TagKey<Item> = itemTagKey(RagiumAPI.id("led_blocks"))

    // Molds
    @JvmField
    val MOLDS: TagKey<Item> = itemTagKey(RagiumAPI.id("molds"))

    @JvmField
    val MOLDS_BALL: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/ball"))

    @JvmField
    val MOLDS_BLANK: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/blank"))

    @JvmField
    val MOLDS_BLOCK: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/block"))

    @JvmField
    val MOLDS_GEAR: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/gear"))

    @JvmField
    val MOLDS_INGOT: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/ingot"))

    @JvmField
    val MOLDS_PLATE: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/plate"))

    @JvmField
    val MOLDS_ROD: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/rod"))

    @JvmField
    val MOLDS_WIRE: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/wire"))

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
