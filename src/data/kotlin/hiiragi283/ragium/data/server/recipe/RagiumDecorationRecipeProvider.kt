package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.util.HTBuildingBlockSets
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.StonecutterRecipe
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
        save(
            RagiumAPI.id("stonecutting/ragi_stone_bricks"),
            StonecutterRecipe(
                "",
                Ingredient.of(RagiumBlocks.RAGI_STONE_SETS.base),
                ItemStack(RagiumBlocks.RAGI_STONE_BRICKS_SETS.base),
            ),
        )
        // Ragi-Stone Square
        save(
            RagiumAPI.id("stonecutting/ragi_stone_square"),
            StonecutterRecipe(
                "",
                Ingredient.of(RagiumBlocks.RAGI_STONE_SETS.base),
                ItemStack(RagiumBlocks.RAGI_STONE_SQUARE_SETS.base),
            ),
        )
        // Azure Tiles
        HTShapedRecipeBuilder(RagiumBlocks.AZURE_TILE_SETS.base, 8, CraftingBookCategory.BUILDING)
            .hollow4()
            .define('A', RagiumItems.AZURE_SHARD)
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

        for (sets: HTBuildingBlockSets in RagiumBlocks.DECORATIONS) {
            sets.addRecipes(output, provider)
        }

        glass()
    }

    private fun glass() {
        // Quartz Glass
        HTCookingRecipeBuilder
            .blasting(RagiumBlocks.QUARTZ_GLASS)
            .addIngredient(Items.QUARTZ_BLOCK)
            .save(output)
        // Soul Glass
        HTCookingRecipeBuilder
            .blasting(RagiumBlocks.SOUL_GLASS)
            .addIngredient(ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .save(output)
        // Obsidian Glass
        HTShapedRecipeBuilder(RagiumBlocks.OBSIDIAN_GLASS)
            .hollow4()
            .define('A', RagiumCommonTags.Items.DUSTS_OBSIDIAN)
            .define('B', Tags.Items.GLASS_BLOCKS_COLORLESS)
            .save(output)
    }
}
