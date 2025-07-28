package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTStonecuttingRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.util.HTBuildingBlockSets
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.neoforged.neoforge.common.Tags

object RagiumDecorationRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        // Ragi-Stone
        HTShapedRecipeBuilder(RagiumBlocks.RAGI_STONE_SETS.base, 8, CraftingBookCategory.BUILDING)
            .hollow8()
            .define('A', Tags.Items.STONES)
            .define('B', RagiumCommonTags.Items.DUSTS_RAGINITE)
            .save(output)
        // Ragi-Stone Bricks
        HTStonecuttingRecipeBuilder(RagiumBlocks.RAGI_STONE_BRICKS_SETS.base)
            .addIngredient(RagiumBlocks.RAGI_STONE_SETS.base)
            .save(output)
        // Ragi-Stone Square
        HTStonecuttingRecipeBuilder(RagiumBlocks.RAGI_STONE_SQUARE_SETS.base)
            .addIngredient(RagiumBlocks.RAGI_STONE_SETS.base)
            .save(output)
        // Azure Tiles
        HTShapedRecipeBuilder(RagiumBlocks.AZURE_TILE_SETS.base, 8, CraftingBookCategory.BUILDING)
            .hollow4()
            .define('A', RagiumCommonTags.Items.GEMS_AZURE)
            .define('B', Items.DEEPSLATE_TILES)
            .save(output)
        // Ember Stone
        HTShapedRecipeBuilder(RagiumBlocks.EMBER_STONE_SETS.base, 8, CraftingBookCategory.BUILDING)
            .hollow4()
            .define('A', Items.BLAZE_POWDER)
            .define('B', Items.STONE_BRICKS)
            .save(output)
        // Plastic Block
        HTShapedRecipeBuilder(RagiumBlocks.PLASTIC_SETS.base, 4, CraftingBookCategory.BUILDING)
            .hollow4()
            .define('A', RagiumCommonTags.Items.PLASTICS)
            .define('B', RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
            .save(output)
        // Blue Nether Bricks
        HTShapedRecipeBuilder(RagiumBlocks.BLUE_NETHER_BRICK_SETS.base, 1, CraftingBookCategory.BUILDING)
            .pattern(
                "AB",
                "BA",
            ).define('A', RagiumCommonTags.Items.CROPS_WARPED_WART)
            .define('B', Tags.Items.BRICKS_NETHER)
            .save(output)
        // Sponge Cake
        HTShapedRecipeBuilder(RagiumBlocks.SPONGE_CAKE_SETS.base, 4)
            .cross8()
            .define('A', Tags.Items.CROPS_WHEAT)
            .define('B', Items.SUGAR)
            .define('C', Tags.Items.EGGS)
            .save(output)

        RagiumBlocks.DECORATIONS.forEach(::registerBuildings)
        glass()
    }

    private fun glass() {
        // Quartz Glass
        createAlloying()
            .itemOutput(RagiumBlocks.Glasses.QUARTZ)
            .itemInput(Items.QUARTZ_BLOCK)
            .itemInput(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC)
            .save(output)
        // Soul Glass
        createAlloying()
            .itemOutput(RagiumBlocks.Glasses.SOUL)
            .itemInput(Items.SOUL_SAND)
            .itemInput(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC)
            .save(output)
        // Obsidian Glass
        createAlloying()
            .itemOutput(RagiumBlocks.Glasses.OBSIDIAN)
            .itemInput(RagiumCommonTags.Items.DUSTS_OBSIDIAN, 4)
            .itemInput(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED)
            .save(output)
    }

    private fun registerBuildings(sets: HTBuildingBlockSets) {
        // Base -> Slab
        HTShapedRecipeBuilder(sets.slab, 6)
            .pattern("AAA")
            .define('A', sets.base)
            .save(output)

        HTStonecuttingRecipeBuilder(sets.slab, 2)
            .addIngredient(sets.base)
            .save(output)
        // Base -> Stairs
        HTShapedRecipeBuilder(sets.stairs, 4, CraftingBookCategory.BUILDING)
            .pattern("A  ")
            .pattern("AA ")
            .pattern("AAA")
            .define('A', sets.base)
            .save(output)

        HTStonecuttingRecipeBuilder(sets.stairs)
            .addIngredient(sets.base)
            .save(output)
        // Base -> Wall
        HTShapedRecipeBuilder(sets.wall, 4, CraftingBookCategory.BUILDING)
            .pattern("AAA")
            .pattern("AAA")
            .define('A', sets.base)
            .save(output)

        HTStonecuttingRecipeBuilder(sets.wall)
            .addIngredient(sets.base)
            .save(output)
    }
}
