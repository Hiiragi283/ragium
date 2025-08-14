package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumCrushingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Vanilla
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(ItemTags.WOOL))
            .addResult(HTResultHelper.item(Items.STRING, 4))
            .saveSuffixed(output, "_from_wool")
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(ItemTags.WOOL_CARPETS))
            .addResult(HTResultHelper.item(Items.STRING, 2))
            .addResult(HTResultHelper.item(Items.STRING), 1 / 3f)
            .saveSuffixed(output, "_from_carpet")
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.COBWEB))
            .addResult(HTResultHelper.item(Items.STRING, 5))
            .saveSuffixed(output, "_from_web")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.GLOWSTONE))
            .addResult(HTResultHelper.item(Items.GLOWSTONE_DUST, 4))
            .saveSuffixed(output, "_from_glowstone")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.MAGMA_BLOCK))
            .addResult(HTResultHelper.item(Items.MAGMA_CREAM, 4))
            .saveSuffixed(output, "_from_block")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.MUDDY_MANGROVE_ROOTS))
            .addResult(HTResultHelper.item(Items.MUD))
            .addResult(HTResultHelper.item(Items.MANGROVE_ROOTS), 1 / 4f)
            .saveSuffixed(output, "_from_roots")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.CROPS_SUGAR_CANE))
            .addResult(HTResultHelper.item(Items.SUGAR, 3))
            .saveSuffixed(output, "_from_cane")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.RODS_BLAZE))
            .addResult(HTResultHelper.item(Items.BLAZE_POWDER, 4))
            .saveSuffixed(output, "_from_rod")
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.RODS_BREEZE))
            .addResult(HTResultHelper.item(Items.WIND_CHARGE, 6))
            .saveSuffixed(output, "_from_rod")

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

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.NETHER_WART_BLOCK))
            .addResult(HTResultHelper.item(Items.NETHER_WART, 9))
            .saveSuffixed(output, "_from_block")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.WARPED_WART_BLOCK))
            .addResult(HTResultHelper.item(RagiumItems.WARPED_WART, 9))
            .saveSuffixed(output, "_from_block")
        // Ragium
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.GILDED_BLACKSTONE))
            .addResult(HTResultHelper.item(Items.BLACKSTONE))
            .addResult(HTResultHelper.item(Items.GOLD_NUGGET, 3))
            .addResult(HTResultHelper.item(Items.GOLD_NUGGET, 3), 1 / 2f)
            .addResult(HTResultHelper.item(Items.GOLD_NUGGET, 3), 1 / 4f)
            .saveSuffixed(output, "_from_gilded")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.OBSIDIANS_NORMAL))
            .addResult(HTResultHelper.item(RagiumCommonTags.Items.DUSTS_OBSIDIAN, 4))
            .saveSuffixed(output, "_from_block")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(RagiumBlocks.ASH_LOG))
            .addResult(HTResultHelper.item(RagiumCommonTags.Items.DUSTS_ASH, 3))
            .saveSuffixed(output, "_from_log")

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
            HTItemToChancedItemRecipeBuilder
                .crushing(HTIngredientHelper.item(tagKey, input))
                .addResult(HTResultHelper.item(RagiumCommonTags.Items.DUSTS_WOOD, output))
                .saveSuffixed(RagiumCrushingRecipeProvider.output, suffix)
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
            .addResult(HTResultHelper.item(RagiumCommonTags.Items.DUSTS_SALTPETER), 1 / 4f)
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
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.SEA_LANTERN))
            .addResult(HTResultHelper.item(Items.PRISMARINE_CRYSTALS, 9))
            .saveSuffixed(output, "_from_sea_lantern")
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.PRISMARINE_SHARD))
            .addResult(HTResultHelper.item(Items.PRISMARINE_CRYSTALS))
            .saveSuffixed(output, "_from_shard")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.PRISMARINE))
            .addResult(HTResultHelper.item(Items.PRISMARINE_SHARD, 4))
            .saveSuffixed(output, "_from_block")
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.PRISMARINE_BRICKS))
            .addResult(HTResultHelper.item(Items.PRISMARINE_SHARD, 9))
            .saveSuffixed(output, "_from_bricks")
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.DARK_PRISMARINE))
            .addResult(HTResultHelper.item(Items.PRISMARINE_SHARD, 8))
            .saveSuffixed(output, "_from_dark")
    }

    private fun snow() {
        // Snow
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.SNOW_BLOCK))
            .addResult(HTResultHelper.item(Items.SNOWBALL, 4))
            .saveSuffixed(output, "_from_block")
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.ICE))
            .addResult(HTResultHelper.item(Items.SNOWBALL, 4))
            .saveSuffixed(output, "_from_ice")
        // Ice
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.PACKED_ICE))
            .addResult(HTResultHelper.item(Items.ICE, 9))
            .saveSuffixed(output, "_from_packed")
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.BLUE_ICE))
            .addResult(HTResultHelper.item(Items.PACKED_ICE, 9))
            .saveSuffixed(output, "_from_blue")
    }
}
