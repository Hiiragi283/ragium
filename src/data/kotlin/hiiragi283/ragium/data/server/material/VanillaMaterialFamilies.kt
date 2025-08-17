package hiiragi283.ragium.data.server.material

import hiiragi283.ragium.api.util.material.HTMaterialFamily
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.util.material.HTVanillaMaterialType
import net.minecraft.world.item.Items

object VanillaMaterialFamilies {
    @JvmField
    val COPPER: HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(Items::COPPER_INGOT)
        .setDefaultedEntry(HTMaterialVariant.ORE, Items::COPPER_ORE)
        .setDefaultedEntry(HTMaterialVariant.RAW_MATERIAL, Items::RAW_COPPER)
        .setDefaultedEntry(HTMaterialVariant.STORAGE_BLOCK, Items::COPPER_BLOCK)
        .setVanilla()
        .build(HTVanillaMaterialType.COPPER)

    @JvmField
    val IRON: HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(Items::IRON_INGOT)
        .setDefaultedEntry(HTMaterialVariant.NUGGET, Items::IRON_NUGGET)
        .setDefaultedEntry(HTMaterialVariant.ORE, Items::IRON_ORE)
        .setDefaultedEntry(HTMaterialVariant.RAW_MATERIAL, Items::RAW_IRON)
        .setDefaultedEntry(HTMaterialVariant.STORAGE_BLOCK, Items::IRON_BLOCK)
        .setVanilla()
        .build(HTVanillaMaterialType.IRON)

    @JvmField
    val GOLD: HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(Items::GOLD_INGOT)
        .setDefaultedEntry(HTMaterialVariant.NUGGET, Items::GOLD_NUGGET)
        .setDefaultedEntry(HTMaterialVariant.ORE, Items::GOLD_ORE)
        .setDefaultedEntry(HTMaterialVariant.RAW_MATERIAL, Items::RAW_GOLD)
        .setDefaultedEntry(HTMaterialVariant.STORAGE_BLOCK, Items::GOLD_BLOCK)
        .setVanilla()
        .build(HTVanillaMaterialType.GOLD)

    @JvmField
    val COAL: HTMaterialFamily = HTMaterialFamily.Builder
        .fuel(Items::COAL)
        .setDefaultedEntry(HTMaterialVariant.ORE, Items::COAL_ORE)
        .setDefaultedEntry(HTMaterialVariant.RAW_MATERIAL, Items::COAL)
        .setDefaultedEntry(HTMaterialVariant.STORAGE_BLOCK, Items::COAL_BLOCK)
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
        .setDefaultedEntry(HTMaterialVariant.ORE, Items::LAPIS_ORE)
        .setDefaultedEntry(HTMaterialVariant.RAW_MATERIAL, Items::LAPIS_LAZULI)
        .setDefaultedEntry(HTMaterialVariant.STORAGE_BLOCK, Items::LAPIS_BLOCK)
        .setVanilla()
        .build(HTVanillaMaterialType.LAPIS)

    @JvmField
    val QUARTZ: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Items::QUARTZ)
        .setDefaultedEntry(HTMaterialVariant.ORE, Items::NETHER_QUARTZ_ORE)
        .setDefaultedEntry(HTMaterialVariant.RAW_MATERIAL, Items::QUARTZ)
        .setVanilla()
        .build(HTVanillaMaterialType.QUARTZ)

    @JvmField
    val DIAMOND: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Items::DIAMOND)
        .setDefaultedEntry(HTMaterialVariant.ORE, Items::DIAMOND_ORE)
        .setDefaultedEntry(HTMaterialVariant.RAW_MATERIAL, Items::DIAMOND)
        .setDefaultedEntry(HTMaterialVariant.STORAGE_BLOCK, Items::DIAMOND_BLOCK)
        .setVanilla()
        .build(HTVanillaMaterialType.DIAMOND)

    @JvmField
    val EMERALD: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Items::EMERALD)
        .setDefaultedEntry(HTMaterialVariant.ORE, Items::EMERALD_ORE)
        .setDefaultedEntry(HTMaterialVariant.RAW_MATERIAL, Items::EMERALD)
        .setDefaultedEntry(HTMaterialVariant.STORAGE_BLOCK, Items::EMERALD_BLOCK)
        .setVanilla()
        .build(HTVanillaMaterialType.EMERALD)
}
