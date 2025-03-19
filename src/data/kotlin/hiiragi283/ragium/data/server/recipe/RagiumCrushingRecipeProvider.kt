package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumCrushingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Vanilla
        HTSingleItemRecipeBuilder
            .crush(Items.STRING, 4)
            .addIngredient(ItemTags.WOOL)
            .group("string")
            .saveSuffixed(output, "_from_wool")

        HTSingleItemRecipeBuilder
            .crush(Items.STRING, 3)
            .addIngredient(ItemTags.WOOL_CARPETS)
            .group("string")
            .saveSuffixed(output, "_from_carpet")

        HTSingleItemRecipeBuilder
            .crush(Items.STRING, 5)
            .addIngredient(Items.COBWEB)
            .group("string")
            .saveSuffixed(output, "_from_web")

        HTSingleItemRecipeBuilder
            .crush(Items.GLOWSTONE_DUST, 4)
            .addIngredient(HTTagPrefix.STORAGE_BLOCK, VanillaMaterials.GLOWSTONE)
            .group("glowstone_dust")
            .saveSuffixed(output, "_from_glowstone")

        HTSingleItemRecipeBuilder
            .crush(Items.MAGMA_CREAM, 4)
            .addIngredient(Items.MAGMA_BLOCK)
            .group("magma_cream")
            .saveSuffixed(output, "_from_block")

        HTSingleItemRecipeBuilder
            .crush(Items.MUD)
            .addIngredient(Items.MUDDY_MANGROVE_ROOTS)
            .group("mud")
            .saveSuffixed(output, "_from_roots")

        HTSingleItemRecipeBuilder
            .crush(Items.SUGAR, 3)
            .addIngredient(Items.SUGAR_CANE)
            .group("sugar")
            .saveSuffixed(output, "_from_cane")

        HTSingleItemRecipeBuilder
            .crush(Items.BLAZE_POWDER, 4)
            .addIngredient(Tags.Items.RODS_BLAZE)
            .group("blaze_powder")
            .saveSuffixed(output, "_from_rod")

        HTSingleItemRecipeBuilder
            .crush(Items.WIND_CHARGE, 4)
            .addIngredient(Tags.Items.RODS_BREEZE)
            .group("wind_charge")
            .saveSuffixed(output, "_from_rod")
        // Ragium
        HTSingleItemRecipeBuilder
            .crush(RagiumItems.Dusts.GOLD)
            .addIngredient(Items.GILDED_BLACKSTONE)
            .group("gold_dust")
            .saveSuffixed(output, "_from_blackstone")

        HTSingleItemRecipeBuilder
            .crush(RagiumItems.Dusts.OBSIDIAN, 4)
            .addIngredient(Tags.Items.OBSIDIANS_NORMAL)
            .group("obsidian_dust")
            .saveSuffixed(output, "_from_block")

        HTSingleItemRecipeBuilder
            .crush(RagiumItems.FLOUR, 9)
            .addIngredient(Tags.Items.STORAGE_BLOCKS_WHEAT)
            .group("flour")
            .saveSuffixed(output, "_from_block")

        HTSingleItemRecipeBuilder
            .crush(RagiumItems.FLOUR)
            .addIngredient(Tags.Items.CROPS_WHEAT)
            .group("flour")
            .saveSuffixed(output, "_from_crop")

        HTSingleItemRecipeBuilder
            .crush(RagiumItems.Dusts.IRON, 31)
            .addIngredient(ItemTags.ANVIL)
            .group("iron_dust")
            .saveSuffixed(output, "_from_anvil")

        HTSingleItemRecipeBuilder
            .crush(RagiumItems.Dusts.IRON, 7)
            .addIngredient(Items.CAULDRON)
            .group("iron_dust")
            .saveSuffixed(output, "_from_cauldron")

        woodDust(output)
        sand(output)
        prismarine(output)
        snow(output)
    }

    private fun woodDust(output: RecipeOutput) {
        HTSingleItemRecipeBuilder
            .crush(RagiumItems.Dusts.WOOD, 6)
            .addIngredient(ItemTags.LOGS_THAT_BURN)
            .group("wood_dust")
            .saveSuffixed(output, "_from_log")

        HTSingleItemRecipeBuilder
            .crush(RagiumItems.Dusts.WOOD)
            .addIngredient(ItemTags.PLANKS)
            .group("wood_dust")
            .saveSuffixed(output, "_from_planks")

        HTSingleItemRecipeBuilder
            .crush(RagiumItems.Dusts.WOOD)
            .addIngredient(ItemTags.WOODEN_STAIRS)
            .group("wood_dust")
            .saveSuffixed(output, "_from_stair")

        HTSingleItemRecipeBuilder
            .crush(RagiumItems.Dusts.WOOD)
            .addIngredient(ItemTags.WOODEN_FENCES)
            .group("wood_dust")
            .saveSuffixed(output, "_from_fence")

        HTSingleItemRecipeBuilder
            .crush(RagiumItems.Dusts.WOOD, 2)
            .addIngredient(ItemTags.FENCE_GATES)
            .group("wood_dust")
            .saveSuffixed(output, "_from_fence_gate")

        HTSingleItemRecipeBuilder
            .crush(RagiumItems.Dusts.WOOD, 2)
            .addIngredient(ItemTags.WOODEN_DOORS)
            .group("wood_dust")
            .saveSuffixed(output, "_from_door")

        HTSingleItemRecipeBuilder
            .crush(RagiumItems.Dusts.WOOD, 3)
            .addIngredient(ItemTags.WOODEN_TRAPDOORS)
            .group("wood_dust")
            .saveSuffixed(output, "_from_trapdoor")

        HTSingleItemRecipeBuilder
            .crush(RagiumItems.Dusts.WOOD, 8)
            .addIngredient(Tags.Items.CHESTS_WOODEN)
            .group("wood_dust")
            .saveSuffixed(output, "_from_chest")

        HTSingleItemRecipeBuilder
            .crush(RagiumItems.Dusts.WOOD, 7)
            .addIngredient(Tags.Items.BARRELS_WOODEN)
            .group("wood_dust")
            .saveSuffixed(output, "_from_wooden")

        HTSingleItemRecipeBuilder
            .crush(RagiumItems.Dusts.WOOD, 5)
            .addIngredient(ItemTags.BOATS)
            .group("wood_dust")
            .saveSuffixed(output, "_from_boat")
    }

    private fun sand(output: RecipeOutput) {
        // Colorless
        HTSingleItemRecipeBuilder
            .crush(Items.GRAVEL)
            .addIngredient(Tags.Items.COBBLESTONES)
            .group("gravel")
            .saveSuffixed(output, "_from_cobble")

        HTSingleItemRecipeBuilder
            .crush(Items.SAND)
            .addIngredient(Tags.Items.GRAVELS)
            .group("sand")
            .saveSuffixed(output, "_from_gravel")

        HTSingleItemRecipeBuilder
            .crush(RagiumBlocks.SILT)
            .addIngredient(Tags.Items.SANDS)
            .group("silt")
            .saveSuffixed(output, "_from_sand")

        HTSingleItemRecipeBuilder
            .crush(Items.SAND, 4)
            .addIngredient(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            .group("sand")
            .saveSuffixed(output, "_from_sandstone")
        // Red
        HTSingleItemRecipeBuilder
            .crush(Items.RED_SAND, 4)
            .addIngredient(Tags.Items.SANDSTONE_RED_BLOCKS)
            .group("red_sand")
            .saveSuffixed(output, "_from_sandstone")
    }

    private fun prismarine(output: RecipeOutput) {
        HTSingleItemRecipeBuilder
            .crush(Items.PRISMARINE_CRYSTALS, 9)
            .addIngredient(Items.SEA_LANTERN)
            .group("prismarine_crystal")
            .saveSuffixed(output, "_from_sea_lantern")

        HTSingleItemRecipeBuilder
            .crush(Items.PRISMARINE_CRYSTALS)
            .addIngredient(Items.PRISMARINE_SHARD)
            .group("prismarine_crystal")
            .saveSuffixed(output, "_from_shard")

        HTSingleItemRecipeBuilder
            .crush(Items.PRISMARINE_SHARD, 4)
            .addIngredient(Items.PRISMARINE)
            .group("prismarine_shard")
            .saveSuffixed(output, "_from_block")

        HTSingleItemRecipeBuilder
            .crush(Items.PRISMARINE_SHARD, 9)
            .addIngredient(Items.PRISMARINE_BRICKS)
            .group("prismarine_shard")
            .saveSuffixed(output, "_from_bricks")

        HTSingleItemRecipeBuilder
            .crush(Items.PRISMARINE_SHARD, 8)
            .addIngredient(Items.DARK_PRISMARINE)
            .group("prismarine_shard")
            .saveSuffixed(output, "_from_dark")
    }

    private fun snow(output: RecipeOutput) {
        // Snow
        HTSingleItemRecipeBuilder
            .crush(Items.SNOWBALL, 4)
            .addIngredient(Items.SNOW_BLOCK)
            .group("snowball")
            .saveSuffixed(output, "_from_block")

        HTSingleItemRecipeBuilder
            .crush(Items.SNOWBALL, 4)
            .addIngredient(Items.ICE)
            .group("snowball")
            .saveSuffixed(output, "_from_ice")
        // Ice
        HTSingleItemRecipeBuilder
            .crush(Items.ICE, 9)
            .addIngredient(Items.PACKED_ICE)
            .group("ice")
            .saveSuffixed(output, "_from_packed")

        HTSingleItemRecipeBuilder
            .crush(Items.PACKED_ICE, 9)
            .addIngredient(Items.BLACKSTONE)
            .group("packed_ice")
            .saveSuffixed(output, "_from_blue")
    }
}
