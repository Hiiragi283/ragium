package hiiragi283.ragium.api.tags

import hiiragi283.ragium.api.RagiumAPI
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object RagiumItemTags {
    //    Custom    //

    @JvmField
    val ALKALI: TagKey<Item> = create(RagiumAPI.MOD_ID, "alkali")

    @JvmField
    val FLUID_CUBES: TagKey<Item> = create(RagiumAPI.MOD_ID, "fluid_cubes")

    @JvmField
    val ORGANIC_OILS: TagKey<Item> = create(RagiumAPI.MOD_ID, "organic_oils")

    @JvmField
    val PROTEIN_FOODS: TagKey<Item> = create(RagiumAPI.MOD_ID, "foods/protein")

    //    Conventional    //

    @JvmField
    val BASALTS: TagKey<Item> = create("basalts")

    @JvmField
    val PLATES: TagKey<Item> = create("plates")

    @JvmField
    val COPPER_PLATES: TagKey<Item> = create("plates/copper")

    @JvmField
    val GOLD_PLATES: TagKey<Item> = create("plates/gold")

    @JvmField
    val IRON_PLATES: TagKey<Item> = create("plates/iron")

    @JvmField
    val RAGINITE_ORES: TagKey<Item> = create("ores/raginite")

    @JvmField
    val SILICON_PLATES: TagKey<Item> = create("plates/silicon")

    @JvmField
    val STEEL_BLOCKS: TagKey<Item> = create("storage_blocks/steel")

    @JvmField
    val STEEL_INGOTS: TagKey<Item> = create("ingots/steel")

    @JvmField
    val STEEL_PLATES: TagKey<Item> = create("plates/steel")

    @JvmField
    val SULFUR_DUSTS: TagKey<Item> = create("dusts/sulfur")

    @JvmStatic
    fun create(namespace: String, path: String): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier.of(namespace, path))

    @JvmStatic
    fun create(path: String): TagKey<Item> = create(TagUtil.C_TAG_NAMESPACE, path)
}
