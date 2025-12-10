package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.common.HTDecorationType
import hiiragi283.ragium.common.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTShapelessInputsRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleExtraItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTStonecuttingRecipeBuilder
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.variant.HTGlassVariant
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumDecorationRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Smooth Blackstone
        HTCookingRecipeBuilder
            .smelting(RagiumBlocks.SMOOTH_BLACKSTONE)
            .addIngredient(Items.BLACKSTONE)
            .setExp(0.1f)
            .save(output)

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

        HTStonecuttingRecipeBuilder
            .create(RagiumBlocks.ELDRITCH_STONE_BRICKS)
            .addIngredient(RagiumBlocks.ELDRITCH_STONE)
            .save(output)
        // Plastics
        HTShapedRecipeBuilder
            .create(RagiumBlocks.PLASTIC_BRICKS, 4)
            .storage4()
            .define('A', CommonMaterialPrefixes.STORAGE_BLOCK, CommonMaterialKeys.PLASTIC)
            .setCategory(CraftingBookCategory.BUILDING)
            .save(output)

        HTStonecuttingRecipeBuilder
            .create(RagiumBlocks.PLASTIC_BRICKS)
            .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, CommonMaterialKeys.PLASTIC)
            .save(output)

        HTShapedRecipeBuilder
            .create(RagiumBlocks.PLASTIC_TILES, 4)
            .storage4()
            .define('A', RagiumBlocks.PLASTIC_BRICKS)
            .setCategory(CraftingBookCategory.BUILDING)
            .save(output)

        HTStonecuttingRecipeBuilder
            .create(RagiumBlocks.PLASTIC_TILES)
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

        metalBars()
        glass()
        planks()

        HTDecorationType.entries.forEach(::registerBuildings)
    }

    @JvmStatic
    private fun metalBars() {
        for ((key: HTMaterialKey, bars: ItemLike) in RagiumBlocks.METAL_BARS) {
            HTShapedRecipeBuilder
                .create(bars, 16)
                .pattern(
                    "AAA",
                    "AAA",
                ).define('A', CommonMaterialPrefixes.INGOT, key)
                .setCategory(CraftingBookCategory.BUILDING)
                .save(output)
        }
    }

    @JvmStatic
    private fun glass() {
        RagiumBlocks.GLASSES.forEach { (variant: HTGlassVariant, key: HTMaterialKey, block: ItemLike) ->
            when (variant) {
                HTGlassVariant.DEFAULT ->
                    HTShapelessInputsRecipeBuilder
                        .alloying(
                            resultHelper.item(block, 2),
                            when (key) {
                                VanillaMaterialKeys.QUARTZ -> listOf(
                                    itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.QUARTZ),
                                    itemCreator.fromTagKey(ItemTags.SMELTS_TO_GLASS),
                                )
                                VanillaMaterialKeys.OBSIDIAN -> listOf(
                                    itemCreator.fromTagKey(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL),
                                    itemCreator.fromTagKey(Tags.Items.OBSIDIANS_NORMAL),
                                )
                                else -> listOf(
                                    itemCreator.fromTagKey(CommonMaterialPrefixes.GEM, key),
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

    @JvmStatic
    private fun planks() {
        mapOf(
            ItemTags.OAK_LOGS to Items.OAK_PLANKS,
            ItemTags.SPRUCE_LOGS to Items.SPRUCE_PLANKS,
            ItemTags.BIRCH_LOGS to Items.BIRCH_PLANKS,
            ItemTags.JUNGLE_LOGS to Items.JUNGLE_PLANKS,
            ItemTags.ACACIA_LOGS to Items.ACACIA_PLANKS,
            ItemTags.CHERRY_LOGS to Items.CHERRY_PLANKS,
            ItemTags.DARK_OAK_LOGS to Items.DARK_OAK_PLANKS,
            ItemTags.MANGROVE_LOGS to Items.MANGROVE_PLANKS,
            ItemTags.BAMBOO_BLOCKS to Items.BAMBOO_PLANKS,
            ItemTags.CRIMSON_STEMS to Items.CRIMSON_PLANKS,
            ItemTags.WARPED_STEMS to Items.WARPED_PLANKS,
        ).forEach { (log: TagKey<Item>, planks: Item) ->
            // Log -> 6x Planks
            HTSingleExtraItemRecipeBuilder
                .cutting(
                    itemCreator.fromTagKey(log),
                    resultHelper.item(planks, 6),
                ).save(output)
        }
    }

    //    Decorations    //

    @JvmStatic
    private fun registerBuildings(type: HTDecorationType) {
        val base: ItemLike = type.base
        val slab: ItemLike = type.slab
        val stairs: ItemLike = type.stairs
        val wall: ItemLike = type.wall
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
        val cuttingIngredient: Ingredient = getCuttingIngredient(type)

        HTStonecuttingRecipeBuilder
            .create(slab, 2)
            .addIngredient(cuttingIngredient)
            .save(output)

        HTStonecuttingRecipeBuilder
            .create(stairs)
            .addIngredient(cuttingIngredient)
            .save(output)

        HTStonecuttingRecipeBuilder
            .create(wall)
            .addIngredient(cuttingIngredient)
            .save(output)
    }

    @JvmStatic
    private fun getCuttingIngredient(type: HTDecorationType): Ingredient {
        if (type == HTDecorationType.PLASTIC_BRICK || type == HTDecorationType.PLASTIC_TILE) {
            return Ingredient.of(CommonMaterialPrefixes.STORAGE_BLOCK.itemTagKey(CommonMaterialKeys.PLASTIC))
        }
        return buildList {
            add(type.base)
            // Eldritch
            if (type == HTDecorationType.ELDRITCH_STONE_BRICK) {
                add(RagiumBlocks.ELDRITCH_STONE)
            }
        }.toTypedArray().let { Ingredient.of(*it) }
    }
}
