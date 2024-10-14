package hiiragi283.ragium.data

import hiiragi283.ragium.api.data.HTMetalItemRecipeRegistry
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.api.util.BothEither
import hiiragi283.ragium.common.RagiumContents
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.item.Items
import net.minecraft.registry.tag.ItemTags

object RagiumMetalItemRecipes {
    @JvmStatic
    fun init() {
        HTMetalItemRecipeRegistry.register("iron") {
            set(HTMetalItemRecipeRegistry.BLOCK, BothEither.right(ConventionalItemTags.STORAGE_BLOCKS_IRON))
            set(HTMetalItemRecipeRegistry.DISABLE_BLOCK_RECIPE, Unit)
            set(HTMetalItemRecipeRegistry.DUST, BothEither.both(RagiumContents.Dusts.IRON, RagiumItemTags.IRON_DUSTS))
            set(HTMetalItemRecipeRegistry.INGOT, BothEither.both(Items.IRON_INGOT, ConventionalItemTags.IRON_INGOTS))
            set(HTMetalItemRecipeRegistry.ORE, BothEither.right(ItemTags.IRON_ORES))
            set(HTMetalItemRecipeRegistry.ORE_SUB_PRODUCTS, RagiumContents.Dusts.NICKEL)
            set(HTMetalItemRecipeRegistry.PLATE, BothEither.left(RagiumContents.Plates.IRON))
            set(HTMetalItemRecipeRegistry.RAW, BothEither.both(Items.RAW_IRON, ConventionalItemTags.IRON_RAW_MATERIALS))
        }

        HTMetalItemRecipeRegistry.register("gold") {
            set(HTMetalItemRecipeRegistry.BLOCK, BothEither.right(ConventionalItemTags.STORAGE_BLOCKS_GOLD))
            set(HTMetalItemRecipeRegistry.DISABLE_BLOCK_RECIPE, Unit)
            set(HTMetalItemRecipeRegistry.DUST, BothEither.both(RagiumContents.Dusts.GOLD, RagiumItemTags.GOLD_DUSTS))
            set(HTMetalItemRecipeRegistry.INGOT, BothEither.both(Items.GOLD_INGOT, ConventionalItemTags.GOLD_INGOTS))
            set(HTMetalItemRecipeRegistry.ORE, BothEither.right(ItemTags.GOLD_ORES))
            set(HTMetalItemRecipeRegistry.ORE_SUB_PRODUCTS, RagiumContents.Dusts.SILVER)
            set(HTMetalItemRecipeRegistry.PLATE, BothEither.left(RagiumContents.Plates.GOLD))
            set(HTMetalItemRecipeRegistry.RAW, BothEither.both(Items.RAW_GOLD, ConventionalItemTags.GOLD_RAW_MATERIALS))
        }

        HTMetalItemRecipeRegistry.register("copper") {
            set(HTMetalItemRecipeRegistry.BLOCK, BothEither.right(ConventionalItemTags.STORAGE_BLOCKS_COPPER))
            set(HTMetalItemRecipeRegistry.DISABLE_BLOCK_RECIPE, Unit)
            set(HTMetalItemRecipeRegistry.DUST, BothEither.both(RagiumContents.Dusts.COPPER, RagiumItemTags.COPPER_DUSTS))
            set(HTMetalItemRecipeRegistry.INGOT, BothEither.both(Items.COPPER_INGOT, ConventionalItemTags.COPPER_INGOTS))
            set(HTMetalItemRecipeRegistry.ORE, BothEither.right(ItemTags.COPPER_ORES))
            set(HTMetalItemRecipeRegistry.ORE_SUB_PRODUCTS, RagiumContents.Dusts.SULFUR)
            set(
                HTMetalItemRecipeRegistry.PLATE,
                BothEither.both(RagiumContents.Plates.COPPER, RagiumItemTags.COPPER_PLATES),
            )
            set(
                HTMetalItemRecipeRegistry.RAW,
                BothEither.both(Items.RAW_COPPER, ConventionalItemTags.COPPER_RAW_MATERIALS),
            )
        }

        HTMetalItemRecipeRegistry.register("crude_raginite") {
            set(HTMetalItemRecipeRegistry.DUST, BothEither.left(RagiumContents.Dusts.CRUDE_RAGINITE))
            set(HTMetalItemRecipeRegistry.ORE, BothEither.left(RagiumContents.Ores.CRUDE_RAGINITE))
            set(HTMetalItemRecipeRegistry.ORE_SUB_PRODUCTS, RagiumContents.Dusts.RAGINITE)
            set(HTMetalItemRecipeRegistry.RAW, BothEither.left(RagiumContents.RawMaterials.CRUDE_RAGINITE))
        }

        HTMetalItemRecipeRegistry.register("raginite") {
            set(HTMetalItemRecipeRegistry.DUST, BothEither.left(RagiumContents.Dusts.RAGINITE))
            set(HTMetalItemRecipeRegistry.ORE, BothEither.right(RagiumItemTags.RAGINITE_ORES))
            set(HTMetalItemRecipeRegistry.ORE_SUB_PRODUCTS, RagiumContents.Dusts.RAGINITE)
            set(HTMetalItemRecipeRegistry.RAW, BothEither.left(RagiumContents.RawMaterials.RAGINITE))
        }

        HTMetalItemRecipeRegistry.register("ragi_crystal") {
            set(HTMetalItemRecipeRegistry.DUST, BothEither.left(RagiumContents.Dusts.RAGI_CRYSTAL))
            set(HTMetalItemRecipeRegistry.ORE, BothEither.left(RagiumContents.Ores.END_RAGI_CRYSTAL))
            set(HTMetalItemRecipeRegistry.RAW, BothEither.left(RagiumContents.Misc.RAGI_CRYSTAL))
        }

        HTMetalItemRecipeRegistry.register("ragi_alloy") {
            set(HTMetalItemRecipeRegistry.BLOCK, BothEither.left(RagiumContents.StorageBlocks.RAGI_ALLOY))
            set(HTMetalItemRecipeRegistry.INGOT, BothEither.left(RagiumContents.Ingots.RAGI_ALLOY))
            set(HTMetalItemRecipeRegistry.PLATE, BothEither.left(RagiumContents.Plates.RAGI_ALLOY))
        }

        HTMetalItemRecipeRegistry.register("ragi_steel") {
            set(HTMetalItemRecipeRegistry.BLOCK, BothEither.left(RagiumContents.StorageBlocks.RAGI_STEEL))
            set(HTMetalItemRecipeRegistry.INGOT, BothEither.left(RagiumContents.Ingots.RAGI_STEEL))
            set(HTMetalItemRecipeRegistry.PLATE, BothEither.left(RagiumContents.Plates.RAGI_STEEL))
        }

        HTMetalItemRecipeRegistry.register("refined_ragi_steel") {
            set(HTMetalItemRecipeRegistry.BLOCK, BothEither.left(RagiumContents.StorageBlocks.REFINED_RAGI_STEEL))
            set(HTMetalItemRecipeRegistry.INGOT, BothEither.left(RagiumContents.Ingots.REFINED_RAGI_STEEL))
            set(HTMetalItemRecipeRegistry.PLATE, BothEither.left(RagiumContents.Plates.REFINED_RAGI_STEEL))
        }
        HTMetalItemRecipeRegistry.register("invar") {
            set(
                HTMetalItemRecipeRegistry.BLOCK,
                BothEither.both(RagiumContents.StorageBlocks.INVAR, RagiumItemTags.INVAR_BLOCKS),
            )
            set(
                HTMetalItemRecipeRegistry.INGOT,
                BothEither.both(RagiumContents.Ingots.INVAR, RagiumItemTags.INVAR_INGOTS),
            )
            set(
                HTMetalItemRecipeRegistry.PLATE,
                BothEither.both(RagiumContents.Plates.INVAR, RagiumItemTags.INVAR_PLATES),
            )
        }
        HTMetalItemRecipeRegistry.register("nickel") {
            set(
                HTMetalItemRecipeRegistry.BLOCK,
                BothEither.both(RagiumContents.StorageBlocks.NICKEL, RagiumItemTags.NICKEL_BLOCKS),
            )
            set(
                HTMetalItemRecipeRegistry.DUST,
                BothEither.both(RagiumContents.Dusts.NICKEL, RagiumItemTags.NICKEL_DUSTS),
            )
            set(
                HTMetalItemRecipeRegistry.INGOT,
                BothEither.both(RagiumContents.Ingots.NICKEL, RagiumItemTags.NICKEL_INGOTS),
            )
        }
        HTMetalItemRecipeRegistry.register("silver") {
            set(
                HTMetalItemRecipeRegistry.BLOCK,
                BothEither.both(RagiumContents.StorageBlocks.SILVER, RagiumItemTags.SILVER_BLOCKS),
            )
            set(
                HTMetalItemRecipeRegistry.DUST,
                BothEither.both(RagiumContents.Dusts.SILVER, RagiumItemTags.SILVER_DUSTS),
            )
            set(
                HTMetalItemRecipeRegistry.INGOT,
                BothEither.both(RagiumContents.Ingots.SILVER, RagiumItemTags.SILVER_INGOTS),
            )
            set(
                HTMetalItemRecipeRegistry.PLATE,
                BothEither.both(RagiumContents.Plates.SILVER, RagiumItemTags.SILVER_PLATES),
            )
        }
        HTMetalItemRecipeRegistry.register("steel") {
            set(
                HTMetalItemRecipeRegistry.BLOCK,
                BothEither.both(RagiumContents.StorageBlocks.STEEL, RagiumItemTags.STEEL_BLOCKS),
            )
            set(
                HTMetalItemRecipeRegistry.INGOT,
                BothEither.both(RagiumContents.Ingots.STEEL, RagiumItemTags.STEEL_INGOTS),
            )
            set(
                HTMetalItemRecipeRegistry.PLATE,
                BothEither.both(RagiumContents.Plates.STEEL, RagiumItemTags.STEEL_PLATES),
            )
        }
    }
}
