package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCombineItemToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTStonecuttingRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.util.material.HTVanillaMaterialType
import hiiragi283.ragium.util.variant.HTDecorationVariant
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumDecorationRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Ragi-Stone
        HTShapedRecipeBuilder(RagiumBlocks.RAGI_STONE, 8, CraftingBookCategory.BUILDING)
            .hollow8()
            .define('A', Tags.Items.STONES)
            .define('B', RagiumCommonTags.Items.DUSTS_RAGINITE)
            .save(output)
        // Ragi-Stone Bricks
        HTStonecuttingRecipeBuilder(RagiumBlocks.RAGI_STONE_BRICKS)
            .addIngredient(RagiumBlocks.RAGI_STONE)
            .save(output)
        // Ragi-Stone Square
        HTStonecuttingRecipeBuilder(RagiumBlocks.RAGI_STONE_SQUARE)
            .addIngredient(RagiumBlocks.RAGI_STONE)
            .save(output)
        // Azure Tiles
        HTShapedRecipeBuilder(RagiumBlocks.AZURE_TILES, 8, CraftingBookCategory.BUILDING)
            .hollow4()
            .define('A', RagiumCommonTags.Items.GEMS_AZURE)
            .define('B', Items.DEEPSLATE_TILES)
            .save(output)
        // Ember Stone
        HTShapedRecipeBuilder(RagiumBlocks.EMBER_STONE, 8, CraftingBookCategory.BUILDING)
            .hollow4()
            .define('A', Items.BLAZE_POWDER)
            .define('B', Items.STONE_BRICKS)
            .save(output)
        // Plastic Block
        HTShapedRecipeBuilder(RagiumBlocks.PLASTIC_BLOCK, 4, CraftingBookCategory.BUILDING)
            .hollow4()
            .define('A', RagiumModTags.Items.PLASTICS)
            .define('B', RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
            .save(output)
        // Blue Nether Bricks
        HTShapedRecipeBuilder(RagiumBlocks.BLUE_NETHER_BRICKS, 1, CraftingBookCategory.BUILDING)
            .pattern(
                "AB",
                "BA",
            ).define('A', RagiumCommonTags.Items.CROPS_WARPED_WART)
            .define('B', Tags.Items.BRICKS_NETHER)
            .save(output)
        // Sponge Cake
        HTShapedRecipeBuilder(RagiumBlocks.SPONGE_CAKE, 4)
            .cross8()
            .define('A', Tags.Items.CROPS_WHEAT)
            .define('B', Items.SUGAR)
            .define('C', Tags.Items.EGGS)
            .save(output)

        HTDecorationVariant.entries.forEach(::registerBuildings)
        glass()
    }

    private fun glass() {
        // Quartz Glass
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumBlocks.getGlass(HTVanillaMaterialType.QUARTZ)),
                HTIngredientHelper.item(Items.QUARTZ_BLOCK),
                HTIngredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC),
            ).save(output)
        // Soul Glass
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumBlocks.getGlass(HTVanillaMaterialType.SOUL)),
                HTIngredientHelper.item(Items.SOUL_SAND),
                HTIngredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC),
            ).save(output)
        // Obsidian Glass
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumBlocks.getGlass(HTVanillaMaterialType.OBSIDIAN)),
                HTIngredientHelper.item(RagiumCommonTags.Items.DUSTS_OBSIDIAN, 4),
                HTIngredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED),
            ).save(output)

        // Normal -> Tinted
        listOf(
            HTVanillaMaterialType.QUARTZ,
            HTVanillaMaterialType.SOUL,
            HTVanillaMaterialType.OBSIDIAN,
        ).forEach { material: HTVanillaMaterialType ->
            HTShapedRecipeBuilder(RagiumBlocks.getTintedGlass(material))
                .hollow4()
                .define('A', gemOrDust("amethyst"))
                .define('B', HTMaterialVariant.GLASS_BLOCK.itemTagKey(material))
                .save(output)
        }
    }

    private fun registerBuildings(variant: HTDecorationVariant) {
        val base: ItemLike = RagiumBlocks.DECORATION_MAP[variant] ?: return
        val slab: ItemLike = RagiumBlocks.Slabs.entries.first { it.variant == variant }
        val stairs: ItemLike = RagiumBlocks.Stairs.entries.first { it.variant == variant }
        val wall: ItemLike = RagiumBlocks.Walls.entries.first { it.variant == variant }
        // Base -> Slab
        HTShapedRecipeBuilder(slab, 6)
            .pattern("AAA")
            .define('A', base)
            .save(output)

        HTStonecuttingRecipeBuilder(slab, 2)
            .addIngredient(base)
            .save(output)
        // Base -> Stairs
        HTShapedRecipeBuilder(stairs, 4, CraftingBookCategory.BUILDING)
            .pattern("A  ")
            .pattern("AA ")
            .pattern("AAA")
            .define('A', base)
            .save(output)

        HTStonecuttingRecipeBuilder(stairs)
            .addIngredient(base)
            .save(output)
        // Base -> Wall
        HTShapedRecipeBuilder(wall, 4, CraftingBookCategory.BUILDING)
            .pattern("AAA")
            .pattern("AAA")
            .define('A', base)
            .save(output)

        HTStonecuttingRecipeBuilder(wall)
            .addIngredient(base)
            .save(output)
    }
}
