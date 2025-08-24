package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.util.material.HTItemMaterialVariant
import hiiragi283.ragium.api.util.material.HTVanillaMaterialType
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.RagiumMaterialType
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumCrushingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Vanilla
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(ItemTags.WOOL),
                HTResultHelper.item(Items.STRING, 4),
            ).saveSuffixed(output, "_from_wool")
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(ItemTags.WOOL_CARPETS))
            .addResult(HTResultHelper.item(Items.STRING, 2))
            .addResult(HTResultHelper.item(Items.STRING), 1 / 3f)
            .saveSuffixed(output, "_from_carpet")
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.COBWEB),
                HTResultHelper.item(Items.STRING, 4),
            ).saveSuffixed(output, "_from_web")

        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.GLOWSTONE),
                HTResultHelper.item(Items.GLOWSTONE_DUST, 4),
            ).saveSuffixed(output, "_from_glowstone")

        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.MAGMA_BLOCK),
                HTResultHelper.item(Items.MAGMA_CREAM, 4),
            ).saveSuffixed(output, "_from_block")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.MUDDY_MANGROVE_ROOTS))
            .addResult(HTResultHelper.item(Items.MUD))
            .addResult(HTResultHelper.item(Items.MANGROVE_ROOTS), 1 / 4f)
            .saveSuffixed(output, "_from_roots")

        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Tags.Items.CROPS_SUGAR_CANE),
                HTResultHelper.item(Items.SUGAR, 3),
            ).saveSuffixed(output, "_from_cane")

        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Tags.Items.RODS_BLAZE),
                HTResultHelper.item(Items.BLAZE_POWDER, 4),
            ).saveSuffixed(output, "_from_rod")
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Tags.Items.RODS_BREEZE),
                HTResultHelper.item(Items.WIND_CHARGE, 6),
            ).saveSuffixed(output, "_from_rod")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.COARSE_DIRT))
            .addResult(HTResultHelper.item(Items.DIRT))
            .addResult(HTResultHelper.item(Items.FLINT), 1 / 4f)
            .saveSuffixed(output, "_from_coarse")
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.ROOTED_DIRT))
            .addResult(HTResultHelper.item(Items.DIRT))
            .addResult(HTResultHelper.item(Items.HANGING_ROOTS), 1 / 4f)
            .saveSuffixed(output, "_from_rooted")

        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.NETHER_WART_BLOCK),
                HTResultHelper.item(Items.NETHER_WART, 3),
            ).saveSuffixed(output, "_from_block")
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.WARPED_WART_BLOCK),
                HTResultHelper.item(RagiumBlocks.WARPED_WART, 3),
            ).saveSuffixed(output, "_from_block")
        // Ragium
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.GILDED_BLACKSTONE))
            .addResult(HTResultHelper.item(Items.BLACKSTONE))
            .addResult(HTResultHelper.item(Items.GOLD_NUGGET, 3))
            .addResult(HTResultHelper.item(Items.GOLD_NUGGET, 3), 1 / 2f)
            .addResult(HTResultHelper.item(Items.GOLD_NUGGET, 3), 1 / 4f)
            .saveSuffixed(output, "_from_gilded")

        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Tags.Items.OBSIDIANS_NORMAL),
                HTResultHelper.item(HTItemMaterialVariant.DUST, HTVanillaMaterialType.OBSIDIAN, 4),
            ).saveSuffixed(output, "_from_block")

        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(RagiumBlocks.ASH_LOG),
                HTResultHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.ASH, 3),
            ).saveSuffixed(output, "_from_log")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.GLOW_INK_SAC))
            .addResult(HTResultHelper.item(RagiumItems.LUMINOUS_PASTE))
            .addResult(HTResultHelper.item(Items.INK_SAC))
            .save(output)

        woodDust()
        sand()
        prismarine()
        snow()
    }

    private fun woodDust() {
        fun wood(
            tagKey: TagKey<Item>,
            input: Int,
            output: Int,
            suffix: String,
        ) {
            HTItemToObjRecipeBuilder
                .pulverizing(
                    HTIngredientHelper.item(tagKey, input),
                    HTResultHelper.item(HTItemMaterialVariant.DUST, HTVanillaMaterialType.WOOD, output),
                ).saveSuffixed(RagiumCrushingRecipeProvider.output, suffix)
        }

        wood(ItemTags.BOATS, 1, 5, "_from_boat")
        wood(ItemTags.LOGS_THAT_BURN, 1, 6, "_from_log")
        wood(ItemTags.PLANKS, 1, 1, "_from_planks")
        wood(ItemTags.WOODEN_BUTTONS, 1, 1, "_from_button")
        wood(ItemTags.WOODEN_DOORS, 1, 2, "_from_door")
        wood(ItemTags.WOODEN_PRESSURE_PLATES, 1, 2, "_from_pressure_plate")
        wood(ItemTags.WOODEN_SLABS, 2, 1, "_from_slabs")
        wood(ItemTags.WOODEN_STAIRS, 4, 3, "_from_stairs")
        wood(ItemTags.WOODEN_TRAPDOORS, 1, 3, "_from_trapdoor")
        wood(Tags.Items.BARRELS_WOODEN, 1, 7, "_from_wooden")
        wood(Tags.Items.CHESTS_WOODEN, 1, 8, "_from_chest")
        wood(Tags.Items.FENCE_GATES_WOODEN, 1, 4, "_from_fence_gate")
        wood(Tags.Items.FENCES_WOODEN, 1, 5, "_from_fence")
        wood(Tags.Items.RODS_WOODEN, 2, 1, "_from_stick")
    }

    private fun sand() {
        // Colorless
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.COBBLESTONES))
            .addResult(HTResultHelper.item(Items.GRAVEL))
            .saveSuffixed(output, "_from_cobble")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.GRAVELS))
            .addResult(HTResultHelper.item(Items.SAND))
            .addResult(HTResultHelper.item(Items.FLINT), 1 / 3f)
            .saveSuffixed(output, "_from_gravel")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS))
            .addResult(HTResultHelper.item(Items.SAND, 4))
            .addResult(HTResultHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.SALTPETER), 1 / 4f)
            .saveSuffixed(output, "_from_sandstone")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.SANDS))
            .addResult(HTResultHelper.item(RagiumBlocks.SILT))
            .saveSuffixed(output, "_from_sand")
        // Red
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.SANDSTONE_RED_BLOCKS))
            .addResult(HTResultHelper.item(Items.RED_SAND, 4))
            .addResult(HTResultHelper.item(Items.REDSTONE), 1 / 8f)
            .saveSuffixed(output, "_from_sandstone")
    }

    private fun prismarine() {
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.SEA_LANTERN),
                HTResultHelper.item(Items.PRISMARINE_CRYSTALS, 9),
            ).saveSuffixed(output, "_from_sea_lantern")
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.PRISMARINE_SHARD),
                HTResultHelper.item(Items.PRISMARINE_CRYSTALS),
            ).saveSuffixed(output, "_from_shard")

        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.PRISMARINE),
                HTResultHelper.item(Items.PRISMARINE_SHARD, 4),
            ).saveSuffixed(output, "_from_block")
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.PRISMARINE_BRICKS),
                HTResultHelper.item(Items.PRISMARINE_SHARD, 9),
            ).saveSuffixed(output, "_from_bricks")
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.DARK_PRISMARINE),
                HTResultHelper.item(Items.PRISMARINE_SHARD, 8),
            ).saveSuffixed(output, "_from_dark")
    }

    private fun snow() {
        // Snow
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.SNOW_BLOCK),
                HTResultHelper.item(Items.SNOWBALL, 4),
            ).saveSuffixed(output, "_from_block")
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.ICE),
                HTResultHelper.item(Items.SNOWBALL, 4),
            ).saveSuffixed(output, "_from_ice")
        // Ice
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.PACKED_ICE),
                HTResultHelper.item(Items.ICE, 9),
            ).saveSuffixed(output, "_from_packed")
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.BLUE_ICE),
                HTResultHelper.item(Items.PACKED_ICE, 9),
            ).saveSuffixed(output, "_from_blue")
    }
}
