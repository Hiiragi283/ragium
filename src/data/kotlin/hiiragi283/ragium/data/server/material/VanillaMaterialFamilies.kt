package hiiragi283.ragium.data.server.material

import hiiragi283.ragium.api.util.material.HTBlockMaterialVariant
import hiiragi283.ragium.api.util.material.HTItemMaterialVariant
import hiiragi283.ragium.api.util.material.HTMaterialFamily
import hiiragi283.ragium.api.util.material.HTVanillaMaterialType
import net.minecraft.world.item.Items

object VanillaMaterialFamilies {
    @JvmField
    val COPPER: HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(Items::COPPER_INGOT)
        .setDefaultedEntry(HTBlockMaterialVariant.ORE, Items::COPPER_ORE)
        .setDefaultedEntry(HTItemMaterialVariant.RAW_MATERIAL, Items::RAW_COPPER)
        .setDefaultedEntry(HTBlockMaterialVariant.STORAGE_BLOCK, Items::COPPER_BLOCK)
        .setVanilla()
        .build(HTVanillaMaterialType.COPPER)

    @JvmField
    val IRON: HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(Items::IRON_INGOT)
        .setDefaultedEntry(HTItemMaterialVariant.NUGGET, Items::IRON_NUGGET)
        .setDefaultedEntry(HTBlockMaterialVariant.ORE, Items::IRON_ORE)
        .setDefaultedEntry(HTItemMaterialVariant.RAW_MATERIAL, Items::RAW_IRON)
        .setDefaultedEntry(HTBlockMaterialVariant.STORAGE_BLOCK, Items::IRON_BLOCK)
        .setVanilla()
        .build(HTVanillaMaterialType.IRON)

    @JvmField
    val GOLD: HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(Items::GOLD_INGOT)
        .setDefaultedEntry(HTItemMaterialVariant.NUGGET, Items::GOLD_NUGGET)
        .setDefaultedEntry(HTBlockMaterialVariant.ORE, Items::GOLD_ORE)
        .setDefaultedEntry(HTItemMaterialVariant.RAW_MATERIAL, Items::RAW_GOLD)
        .setDefaultedEntry(HTBlockMaterialVariant.STORAGE_BLOCK, Items::GOLD_BLOCK)
        .setVanilla()
        .build(HTVanillaMaterialType.GOLD)

    @JvmField
    val COAL: HTMaterialFamily = HTMaterialFamily.Builder
        .fuel(Items::COAL)
        .setDefaultedEntry(HTBlockMaterialVariant.ORE, Items::COAL_ORE)
        .setDefaultedEntry(HTItemMaterialVariant.RAW_MATERIAL, Items::COAL)
        .setDefaultedEntry(HTBlockMaterialVariant.STORAGE_BLOCK, Items::COAL_BLOCK)
        .setVanilla()
        .build(HTVanillaMaterialType.COAL)

    @JvmField
    val CHARCOAL: HTMaterialFamily = HTMaterialFamily.Builder
        .fuel(Items::CHARCOAL)
        .setVanilla()
        .build(HTVanillaMaterialType.CHARCOAL)

    @JvmField
    val LAPIS: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Items::LAPIS_LAZULI)
        .setDefaultedEntry(HTBlockMaterialVariant.ORE, Items::LAPIS_ORE)
        .setDefaultedEntry(HTItemMaterialVariant.RAW_MATERIAL, Items::LAPIS_LAZULI)
        .setDefaultedEntry(HTBlockMaterialVariant.STORAGE_BLOCK, Items::LAPIS_BLOCK)
        .setVanilla()
        .build(HTVanillaMaterialType.LAPIS)

    @JvmField
    val QUARTZ: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Items::QUARTZ)
        .setDefaultedEntry(HTBlockMaterialVariant.ORE, Items::NETHER_QUARTZ_ORE)
        .setDefaultedEntry(HTItemMaterialVariant.RAW_MATERIAL, Items::QUARTZ)
        .setVanilla()
        .build(HTVanillaMaterialType.QUARTZ)

    @JvmField
    val DIAMOND: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Items::DIAMOND)
        .setDefaultedEntry(HTBlockMaterialVariant.ORE, Items::DIAMOND_ORE)
        .setDefaultedEntry(HTItemMaterialVariant.RAW_MATERIAL, Items::DIAMOND)
        .setDefaultedEntry(HTBlockMaterialVariant.STORAGE_BLOCK, Items::DIAMOND_BLOCK)
        .setVanilla()
        .build(HTVanillaMaterialType.DIAMOND)

    @JvmField
    val EMERALD: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Items::EMERALD)
        .setDefaultedEntry(HTBlockMaterialVariant.ORE, Items::EMERALD_ORE)
        .setDefaultedEntry(HTItemMaterialVariant.RAW_MATERIAL, Items::EMERALD)
        .setDefaultedEntry(HTBlockMaterialVariant.STORAGE_BLOCK, Items::EMERALD_BLOCK)
        .setVanilla()
        .build(HTVanillaMaterialType.EMERALD)
}
