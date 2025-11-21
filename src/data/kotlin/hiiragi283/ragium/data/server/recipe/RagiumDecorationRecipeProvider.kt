package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.variant.HTDecorationVariant
import hiiragi283.ragium.common.variant.HTGlassVariant
import hiiragi283.ragium.impl.data.HTVanillaWoodType
import hiiragi283.ragium.impl.data.recipe.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumDecorationRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Ragi-Bricks
        HTShapedRecipeBuilder
            .create(RagiumBlocks.RAGI_BRICKS, 8)
            .hollow8()
            .define('A', ItemTags.STONE_BRICKS)
            .define('B', CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .setCategory(CraftingBookCategory.BUILDING)
            .save(output)
        // Azure Tiles
        HTShapedRecipeBuilder
            .create(RagiumBlocks.AZURE_TILES, 8)
            .hollow8()
            .define('A', Items.DEEPSLATE_TILES)
            .define('B', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.AZURE)
            .setCategory(CraftingBookCategory.BUILDING)
            .save(output)
        // Eldritch Stone
        HTShapedRecipeBuilder
            .create(RagiumBlocks.ELDRITCH_STONE, 8)
            .hollow8()
            .define('A', Tags.Items.END_STONES)
            .define('B', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL)
            .setCategory(CraftingBookCategory.BUILDING)
            .save(output)

        HTShapedRecipeBuilder
            .create(RagiumBlocks.ELDRITCH_STONE_BRICKS, 4)
            .storage4()
            .define('A', RagiumBlocks.ELDRITCH_STONE)
            .setCategory(CraftingBookCategory.BUILDING)
            .save(output)

        HTSingleItemRecipeBuilder
            .stonecutter(RagiumBlocks.ELDRITCH_STONE_BRICKS)
            .addIngredient(RagiumBlocks.ELDRITCH_STONE)
            .save(output)
        // Plastics
        HTShapedRecipeBuilder
            .create(RagiumBlocks.PLASTIC_BRICKS, 4)
            .storage4()
            .define('A', CommonMaterialPrefixes.STORAGE_BLOCK, CommonMaterialKeys.PLASTIC)
            .setCategory(CraftingBookCategory.BUILDING)
            .save(output)

        HTSingleItemRecipeBuilder
            .stonecutter(RagiumBlocks.PLASTIC_BRICKS)
            .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, CommonMaterialKeys.PLASTIC)
            .save(output)

        HTShapedRecipeBuilder
            .create(RagiumBlocks.PLASTIC_TILES, 4)
            .storage4()
            .define('A', RagiumBlocks.PLASTIC_BRICKS)
            .setCategory(CraftingBookCategory.BUILDING)
            .save(output)

        HTSingleItemRecipeBuilder
            .stonecutter(RagiumBlocks.PLASTIC_TILES)
            .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, CommonMaterialKeys.PLASTIC)
            .save(output)
        // Blue Nether Bricks
        HTShapedRecipeBuilder
            .create(RagiumBlocks.BLUE_NETHER_BRICKS, 1)
            .mosaic4()
            .define('A', CommonMaterialPrefixes.CROP, FoodMaterialKeys.WARPED_WART)
            .define('B', Tags.Items.BRICKS_NETHER)
            .setCategory(CraftingBookCategory.BUILDING)
            .save(output)
        // Sponge Cake
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.FLOUR, FoodMaterialKeys.WHEAT, 2))
            .addIngredient(itemCreator.fromTagKey(Tags.Items.EGGS))
            .addIngredient(itemCreator.fromItem(Items.SUGAR))
            .addIngredient(fluidCreator.milk(1000))
            .setResult(resultHelper.item(RagiumBlocks.SPONGE_CAKE, 4))
            .save(output)

        HTDecorationVariant.entries.forEach(::registerBuildings)
        HTVanillaWoodType.entries.forEach(::addWoodSawing)

        glass()
    }

    @JvmStatic
    private fun glass() {
        RagiumBlocks.GLASSES.forEach { (variant: HTGlassVariant, key: HTMaterialKey, block: ItemLike) ->
            when (variant) {
                HTGlassVariant.DEFAULT ->
                    HTCombineItemToObjRecipeBuilder
                        .alloying(
                            resultHelper.item(block, 2),
                            when (key) {
                                VanillaMaterialKeys.QUARTZ -> listOf(
                                    itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.QUARTZ),
                                    itemCreator.fromTagKey(ItemTags.SMELTS_TO_GLASS),
                                )
                                VanillaMaterialKeys.OBSIDIAN -> listOf(
                                    itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.DEEP_STEEL),
                                    itemCreator.fromTagKey(Tags.Items.OBSIDIANS_NORMAL),
                                )
                                else -> listOf(
                                    itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, key),
                                    itemCreator.fromTagKey(CommonMaterialPrefixes.GLASS_BLOCK, VanillaMaterialKeys.OBSIDIAN, 2),
                                )
                            },
                        ).save(output)

                HTGlassVariant.TINTED -> {
                    // Normal -> Tinted
                    HTShapedRecipeBuilder
                        .create(block)
                        .hollow4()
                        .define('A', CommonMaterialPrefixes.GEM, VanillaMaterialKeys.AMETHYST)
                        .define('B', CommonMaterialPrefixes.GLASS_BLOCK, key)
                        .setCategory(CraftingBookCategory.BUILDING)
                        .save(output)
                }
            }
        }
    }

    //    Decorations    //

    @JvmStatic
    private fun registerBuildings(variant: HTDecorationVariant) {
        val base: ItemLike = variant.base
        val slab: ItemLike = variant.slab
        val stairs: ItemLike = variant.stairs
        val wall: ItemLike = variant.wall
        // Base -> Slab
        HTShapedRecipeBuilder
            .create(slab, 6)
            .pattern("AAA")
            .define('A', base)
            .setCategory(CraftingBookCategory.BUILDING)
            .save(output)
        // Base -> Stairs
        HTShapedRecipeBuilder
            .create(stairs, 4)
            .pattern(
                "A  ",
                "AA ",
                "AAA",
            ).define('A', base)
            .setCategory(CraftingBookCategory.BUILDING)
            .save(output)
        // Base -> Wall
        HTShapedRecipeBuilder
            .create(wall, 4)
            .pattern(
                "AAA",
                "AAA",
            ).define('A', base)
            .setCategory(CraftingBookCategory.BUILDING)
            .save(output)
        // Stonecutting
        val cuttingIngredient: Ingredient = getCuttingIngredient(variant)

        HTSingleItemRecipeBuilder
            .stonecutter(slab, 2)
            .addIngredient(cuttingIngredient)
            .save(output)

        HTSingleItemRecipeBuilder
            .stonecutter(stairs)
            .addIngredient(cuttingIngredient)
            .save(output)

        HTSingleItemRecipeBuilder
            .stonecutter(wall)
            .addIngredient(cuttingIngredient)
            .save(output)
    }

    @JvmStatic
    private fun getCuttingIngredient(variant: HTDecorationVariant): Ingredient {
        if (variant == HTDecorationVariant.PLASTIC_BRICK || variant == HTDecorationVariant.PLASTIC_TILE) {
            return Ingredient.of(CommonMaterialPrefixes.STORAGE_BLOCK.itemTagKey(CommonMaterialKeys.PLASTIC))
        }
        return buildList {
            add(variant.base)
            // Eldritch
            if (variant == HTDecorationVariant.ELDRITCH_STONE_BRICK) {
                add(RagiumBlocks.ELDRITCH_STONE)
            }
        }.toTypedArray().let { Ingredient.of(*it) }
    }
}
