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
    val CIRCUITS: TagKey<Item> = create(RagiumAPI.MOD_ID, "circuits")
    
    @JvmField
    val COILS: TagKey<Item> = create(RagiumAPI.MOD_ID, "coils")
    
    @JvmField
    val FLUID_CUBES: TagKey<Item> = create(RagiumAPI.MOD_ID, "fluid_cubes")

    @JvmField
    val HULLS: TagKey<Item> = create(RagiumAPI.MOD_ID, "hulls")

    @JvmField
    val MOTORS: TagKey<Item> = create(RagiumAPI.MOD_ID, "motors")

    @JvmField
    val ORGANIC_OILS: TagKey<Item> = create(RagiumAPI.MOD_ID, "organic_oils")

    @JvmField
    val PROTEIN_FOODS: TagKey<Item> = create(RagiumAPI.MOD_ID, "foods/protein")

    @JvmField
    val TOOL_MODULES: TagKey<Item> = create(RagiumAPI.MOD_ID, "tool_modules")

    //    Conventional    //

    @JvmField
    val BASALTS: TagKey<Item> = create("basalts")

    @JvmField
    val COPPER_DUSTS: TagKey<Item> = create("dusts/copper")

    @JvmField
    val COPPER_PLATES: TagKey<Item> = create("plates/copper")

    @JvmField
    val GOLD_DUSTS: TagKey<Item> = create("dusts/gold")

    @JvmField
    val GOLD_PLATES: TagKey<Item> = create("plates/gold")

    @JvmField
    val INVAR_BLOCKS: TagKey<Item> = create("storage_blocks/invar")

    @JvmField
    val INVAR_INGOTS: TagKey<Item> = create("ingots/invar")

    @JvmField
    val INVAR_PLATES: TagKey<Item> = create("plates/invar")

    @JvmField
    val IRON_DUSTS: TagKey<Item> = create("dusts/iron")

    @JvmField
    val IRON_PLATES: TagKey<Item> = create("plates/iron")

    @JvmField
    val NICKEL_BLOCKS: TagKey<Item> = create("storage_blocks/nickel")

    @JvmField
    val NICKEL_DUSTS: TagKey<Item> = create("dusts/nickel")

    @JvmField
    val NICKEL_INGOTS: TagKey<Item> = create("ingots/nickel")

    @JvmField
    val PLATES: TagKey<Item> = create("plates")

    @JvmField
    val RAGINITE_ORES: TagKey<Item> = create("ores/raginite")

    @JvmField
    val SILICON_PLATES: TagKey<Item> = create("plates/silicon")

    @JvmField
    val SILVER_BLOCKS: TagKey<Item> = create("storage_blocks/silver")

    @JvmField
    val SILVER_DUSTS: TagKey<Item> = create("dusts/silver")

    @JvmField
    val SILVER_INGOTS: TagKey<Item> = create("ingots/silver")

    @JvmField
    val SILVER_PLATES: TagKey<Item> = create("plates/silver")

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
