package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.neoforged.neoforge.common.Tags

object RagiumDecorationRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Ragi-Stone
        HTShapedRecipeBuilder(RagiumBlocks.RAGI_STONE_SETS.base, 8, CraftingBookCategory.BUILDING)
            .hollow8()
            .define('A', Tags.Items.STONES)
            .define('B', HTTagPrefixes.DUST, RagiumMaterials.RAGINITE)
            .save(output)
        // Ragi-Stone Square
        output.accept(
            RagiumAPI.id("stonecutting/ragi_stone_square"),
            StonecutterRecipe(
                "",
                Ingredient.of(RagiumBlocks.RAGI_STONE_SETS.base),
                ItemStack(RagiumBlocks.RAGI_STONE_SQUARE_SETS.base),
            ),
            null,
        )
        // Azure Tiles
        HTShapedRecipeBuilder(RagiumBlocks.AZURE_TILE_SETS.base, 4, CraftingBookCategory.BUILDING)
            .hollow4()
            .define('A', HTTagPrefixes.DUST, RagiumMaterials.AZURE_STEEL)
            .define('B', Items.DEEPSLATE_TILES)
            .save(output)
        // Ember Stone
        HTShapedRecipeBuilder(RagiumBlocks.EMBER_STONE_SETS.base, 8, CraftingBookCategory.BUILDING)
            .hollow4()
            .define('A', HTTagPrefixes.DUST, RagiumMaterials.ADVANCED_RAGI_ALLOY)
            .define('B', Items.STONE_BRICKS)
            .save(output)
        // Plastic Block
        HTShapedRecipeBuilder(RagiumBlocks.PLASTIC_SETS.base, 4, CraftingBookCategory.BUILDING)
            .hollow4()
            .define('A', RagiumItemTags.PLASTICS)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)
        // Blue Nether Bricks
        HTShapedRecipeBuilder(RagiumBlocks.BLUE_NETHER_BRICK_SETS.base, 1, CraftingBookCategory.BUILDING)
            .pattern(
                "AB",
                "BA",
            ).define('A', RagiumItemTags.CROPS_WARPED_WART)
            .define('B', Tags.Items.BRICKS_NETHER)
            .save(output)

        RagiumBlocks.RAGI_STONE_SETS.addRecipes(output, holderLookup)
        RagiumBlocks.RAGI_STONE_SQUARE_SETS.addRecipes(output, holderLookup)
        RagiumBlocks.AZURE_TILE_SETS.addRecipes(output, holderLookup)
        RagiumBlocks.EMBER_STONE_SETS.addRecipes(output, holderLookup)
        RagiumBlocks.PLASTIC_SETS.addRecipes(output, holderLookup)
        RagiumBlocks.BLUE_NETHER_BRICK_SETS.addRecipes(output, holderLookup)
    }
}
