package hiiragi283.ragium.data.server.material

import hiiragi283.ragium.api.util.HTMaterialFamily
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items

object VanillaMaterialFamilies {
    @JvmField
    val COPPER: HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(Items::COPPER_INGOT)
        .setDefaultedEntry(HTMaterialFamily.Variant.ORES, Items::COPPER_ORE)
        .setDefaultedEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Items::RAW_COPPER)
        .setDefaultedEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, Items::COPPER_BLOCK)
        .setVanilla()
        .build("copper")

    @JvmField
    val IRON: HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(Items::IRON_INGOT)
        .setDefaultedEntry(HTMaterialFamily.Variant.NUGGETS, Items::IRON_NUGGET)
        .setDefaultedEntry(HTMaterialFamily.Variant.ORES, Items::IRON_ORE)
        .setDefaultedEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Items::RAW_IRON)
        .setDefaultedEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, Items::IRON_BLOCK)
        .setVanilla()
        .build("iron")

    @JvmField
    val GOLD: HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(Items::GOLD_INGOT)
        .setDefaultedEntry(HTMaterialFamily.Variant.NUGGETS, Items::GOLD_NUGGET)
        .setDefaultedEntry(HTMaterialFamily.Variant.ORES, Items::GOLD_ORE)
        .setDefaultedEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Items::RAW_GOLD)
        .setDefaultedEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, Items::GOLD_BLOCK)
        .setVanilla()
        .build("gold")

    @JvmField
    val COAL: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Items::COAL)
        .setDefaultedEntry(HTMaterialFamily.Variant.ORES, Items::COAL_ORE)
        .setDefaultedEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Items::COAL)
        .setDefaultedEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, Items::COAL_BLOCK)
        .setCustomTag(HTMaterialFamily.Variant.GEMS, ItemTags.COALS)
        .setVanilla()
        .build("coal")

    @JvmField
    val LAPIS: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Items::LAPIS_LAZULI)
        .setDefaultedEntry(HTMaterialFamily.Variant.ORES, Items::LAPIS_ORE)
        .setDefaultedEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Items::LAPIS_LAZULI)
        .setDefaultedEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, Items::LAPIS_BLOCK)
        .setVanilla()
        .build("lapis")

    @JvmField
    val QUARTZ: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Items::QUARTZ)
        .setDefaultedEntry(HTMaterialFamily.Variant.ORES, Items::NETHER_QUARTZ_ORE)
        .setDefaultedEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Items::QUARTZ)
        .setVanilla()
        .build("quartz")

    @JvmField
    val DIAMOND: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Items::DIAMOND)
        .setDefaultedEntry(HTMaterialFamily.Variant.ORES, Items::DIAMOND_ORE)
        .setDefaultedEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Items::DIAMOND)
        .setDefaultedEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, Items::DIAMOND_BLOCK)
        .setVanilla()
        .build("diamond")

    @JvmField
    val EMERALD: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Items::EMERALD)
        .setDefaultedEntry(HTMaterialFamily.Variant.ORES, Items::EMERALD_ORE)
        .setDefaultedEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Items::EMERALD)
        .setDefaultedEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, Items::EMERALD_BLOCK)
        .setVanilla()
        .build("emerald")
}
