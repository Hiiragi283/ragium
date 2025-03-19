package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.neoforged.neoforge.common.Tags

object RagiumDecorationRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Ragi-Bricks
        HTShapedRecipeBuilder(RagiumBlocks.RAGI_BRICK_SETS.base, 4, CraftingBookCategory.BUILDING)
            .cross8()
            .define('A', HTTagPrefix.DUST, RagiumMaterials.RAGINITE)
            .define('B', Tags.Items.BRICKS_NORMAL)
            .define('C', Items.CLAY)
            .save(output)
        // Azure Tiles
        HTShapedRecipeBuilder(RagiumBlocks.AZURE_TILE_SETS.base, 4, CraftingBookCategory.BUILDING)
            .cross8()
            .define('A', HTTagPrefix.DUST, VanillaMaterials.LAPIS)
            .define('B', HTTagPrefix.DUST, VanillaMaterials.AMETHYST)
            .define('C', Items.DEEPSLATE_TILES)
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

        RagiumBlocks.RAGI_BRICK_SETS.addRecipes(output, holderLookup)
        RagiumBlocks.AZURE_TILE_SETS.addRecipes(output, holderLookup)
        RagiumBlocks.PLASTIC_SETS.addRecipes(output, holderLookup)
        RagiumBlocks.BLUE_NETHER_BRICK_SETS.addRecipes(output, holderLookup)
    }
}
