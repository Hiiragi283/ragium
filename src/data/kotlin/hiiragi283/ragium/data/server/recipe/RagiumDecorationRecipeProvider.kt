package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.HTBuildingBlockSets
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

        RagiumBlocks.DECORATIONS.forEach(::registerBuildings)
        glass()
    }

    private fun glass() {
        // Quartz Glass
        createAlloying()
            .itemOutput(RagiumBlocks.QUARTZ_GLASS)
            .itemInput(Items.QUARTZ_BLOCK)
            .itemInput(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC)
            .save(output)
        // Soul Glass
        createAlloying()
            .itemOutput(RagiumBlocks.SOUL_GLASS)
            .itemInput(Items.SOUL_SAND)
            .itemInput(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC)
            .save(output)
        // Obsidian Glass
        createAlloying()
            .itemOutput(RagiumBlocks.OBSIDIAN_GLASS)
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

        output.accept(
            sets.slab
                .asItemHolder()
                .idOrThrow
                .withPrefix("stonecutting/"),
            StonecutterRecipe(
                "",
                Ingredient.of(sets.base),
                sets.slab.toStack(2),
            ),
            null,
        )
        // Base -> Stairs
        HTShapedRecipeBuilder(sets.stairs, 4, CraftingBookCategory.BUILDING)
            .pattern("A  ")
            .pattern("AA ")
            .pattern("AAA")
            .define('A', sets.base)
            .save(output)

        output.accept(
            sets.stairs.id.withPrefix("stonecutting/"),
            StonecutterRecipe(
                "",
                Ingredient.of(sets.base),
                ItemStack(sets.stairs),
            ),
            null,
        )
        // Base -> Wall
        HTShapedRecipeBuilder(sets.wall, 4, CraftingBookCategory.BUILDING)
            .pattern("AAA")
            .pattern("AAA")
            .define('A', sets.base)
            .save(output)

        output.accept(
            sets.wall.id.withPrefix("stonecutting/"),
            StonecutterRecipe(
                "",
                Ingredient.of(sets.base),
                ItemStack(sets.wall),
            ),
            null,
        )
    }
}
