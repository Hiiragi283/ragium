package hiiragi283.ragium.data.server.material

import hiiragi283.ragium.api.util.HTMaterialFamily
import net.minecraft.world.item.Items

object VanillaMaterialFamilies {
    @JvmField
    val COPPER: HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(Items::COPPER_INGOT)
        .setEntry(HTMaterialFamily.Variant.DUSTS, null)
        .setEntry(HTMaterialFamily.Variant.ORES, Items::COPPER_ORE)
        .setEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Items::RAW_COPPER)
        .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, Items::COPPER_BLOCK)
        .setVanilla()
        .build("copper")

    @JvmField
    val IRON: HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(Items::IRON_INGOT)
        .setEntry(HTMaterialFamily.Variant.DUSTS, null)
        .setEntry(HTMaterialFamily.Variant.NUGGETS, Items::IRON_NUGGET)
        .setEntry(HTMaterialFamily.Variant.ORES, Items::IRON_ORE)
        .setEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Items::RAW_IRON)
        .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, Items::IRON_BLOCK)
        .setVanilla()
        .build("iron")

    @JvmField
    val GOLD: HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(Items::GOLD_INGOT)
        .setEntry(HTMaterialFamily.Variant.DUSTS, null)
        .setEntry(HTMaterialFamily.Variant.NUGGETS, Items::GOLD_NUGGET)
        .setEntry(HTMaterialFamily.Variant.ORES, Items::GOLD_ORE)
        .setEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Items::RAW_GOLD)
        .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, Items::GOLD_BLOCK)
        .setVanilla()
        .build("gold")

    @JvmField
    val COAL: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Items::COAL)
        .setEntry(HTMaterialFamily.Variant.DUSTS, null)
        .setEntry(HTMaterialFamily.Variant.ORES, Items::COAL_ORE)
        .setEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Items::COAL)
        .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, Items::COAL_BLOCK)
        .setVanilla()
        .build("coal")

    @JvmField
    val LAPIS: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Items::LAPIS_LAZULI)
        .setEntry(HTMaterialFamily.Variant.DUSTS, null)
        .setEntry(HTMaterialFamily.Variant.ORES, Items::LAPIS_ORE)
        .setEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Items::LAPIS_LAZULI)
        .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, Items::LAPIS_BLOCK)
        .setVanilla()
        .build("lapis")

    @JvmField
    val QUARTZ: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Items::QUARTZ)
        .setEntry(HTMaterialFamily.Variant.DUSTS, null)
        .setEntry(HTMaterialFamily.Variant.ORES, Items::NETHER_QUARTZ_ORE)
        .setEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Items::QUARTZ)
        .setVanilla()
        .build("quartz")

    @JvmField
    val DIAMOND: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Items::DIAMOND)
        .setEntry(HTMaterialFamily.Variant.DUSTS, null)
        .setEntry(HTMaterialFamily.Variant.ORES, Items::DIAMOND_ORE)
        .setEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Items::DIAMOND)
        .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, Items::DIAMOND_BLOCK)
        .setVanilla()
        .build("diamond")

    @JvmField
    val EMERALD: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Items::EMERALD)
        .setEntry(HTMaterialFamily.Variant.DUSTS, null)
        .setEntry(HTMaterialFamily.Variant.ORES, Items::EMERALD_ORE)
        .setEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Items::EMERALD)
        .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, Items::EMERALD_BLOCK)
        .setVanilla()
        .build("emerald")
}
