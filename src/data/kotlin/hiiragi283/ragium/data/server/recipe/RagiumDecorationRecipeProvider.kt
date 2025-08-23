package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTStonecuttingRecipeBuilder
import hiiragi283.ragium.api.registry.HTDeferredBlockHolder
import hiiragi283.ragium.api.registry.HTSimpleDeferredBlockHolder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.util.material.HTVanillaMaterialType
import hiiragi283.ragium.util.material.RagiumMaterialType
import hiiragi283.ragium.util.variant.HTDecorationVariant
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock
import net.neoforged.neoforge.common.Tags

object RagiumDecorationRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Ragi-Bricks
        HTShapedRecipeBuilder(RagiumBlocks.RAGI_BRICKS, 8, CraftingBookCategory.BUILDING)
            .hollow8()
            .define('A', ItemTags.STONE_BRICKS)
            .define('B', HTMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .save(output)
        // Azure Tiles
        HTShapedRecipeBuilder(RagiumBlocks.AZURE_TILES, 8, CraftingBookCategory.BUILDING)
            .hollow8()
            .define('A', Items.DEEPSLATE_TILES)
            .define('B', gemOrDust(RagiumMaterialType.AZURE))
            .save(output)
        // Eldritch Stone
        HTShapedRecipeBuilder(RagiumBlocks.ELDRITCH_STONE, 8, CraftingBookCategory.BUILDING)
            .hollow8()
            .define('A', Items.BLACKSTONE)
            .define('B', gemOrDust(RagiumMaterialType.ELDRITCH_PEARL))
            .save(output)

        HTShapedRecipeBuilder(RagiumBlocks.POLISHED_ELDRITCH_STONE, 4, CraftingBookCategory.BUILDING)
            .storage4()
            .define('A', RagiumBlocks.ELDRITCH_STONE)
            .save(output)

        HTStonecuttingRecipeBuilder(RagiumBlocks.POLISHED_ELDRITCH_STONE)
            .addIngredient(RagiumBlocks.ELDRITCH_STONE)
            .save(output)

        HTShapedRecipeBuilder(RagiumBlocks.POLISHED_ELDRITCH_STONE_BRICKS, 4, CraftingBookCategory.BUILDING)
            .storage4()
            .define('A', RagiumBlocks.POLISHED_ELDRITCH_STONE)
            .save(output)

        HTStonecuttingRecipeBuilder(RagiumBlocks.POLISHED_ELDRITCH_STONE_BRICKS)
            .addIngredient(RagiumBlocks.ELDRITCH_STONE, RagiumBlocks.POLISHED_ELDRITCH_STONE)
            .save(output)
        // Plastics
        HTShapedRecipeBuilder(RagiumBlocks.PLASTIC_BRICKS, 4, CraftingBookCategory.BUILDING)
            .storage4()
            .define('A', HTMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.PLASTIC)
            .save(output)

        HTStonecuttingRecipeBuilder(RagiumBlocks.PLASTIC_BRICKS)
            .addIngredient(HTMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.PLASTIC)
            .save(output)

        HTShapedRecipeBuilder(RagiumBlocks.PLASTIC_TILES, 4, CraftingBookCategory.BUILDING)
            .storage4()
            .define('A', RagiumBlocks.PLASTIC_BRICKS)
            .save(output)

        HTStonecuttingRecipeBuilder(RagiumBlocks.PLASTIC_TILES)
            .addIngredient(HTMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.PLASTIC)
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
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumBlocks.getGlass(HTVanillaMaterialType.QUARTZ)),
                HTIngredientHelper.item(Items.QUARTZ_BLOCK),
                HTIngredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC),
            ).save(output)
        // Soul Glass
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumBlocks.getGlass(HTVanillaMaterialType.SOUL)),
                HTIngredientHelper.item(Items.SOUL_SAND),
                HTIngredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC),
            ).save(output)
        // Obsidian Glass
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumBlocks.getGlass(HTVanillaMaterialType.OBSIDIAN)),
                HTIngredientHelper.item(HTMaterialVariant.DUST, HTVanillaMaterialType.OBSIDIAN, 4),
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
                .define('A', gemOrDust(HTVanillaMaterialType.AMETHYST))
                .define('B', HTMaterialVariant.GLASS_BLOCK, material)
                .save(output)
        }
    }

    private fun registerBuildings(variant: HTDecorationVariant) {
        val base: HTSimpleDeferredBlockHolder = variant.base
        val slab: HTDeferredBlockHolder<SlabBlock, *> = variant.slab
        val stairs: HTDeferredBlockHolder<StairBlock, *> = variant.stairs
        val wall: HTDeferredBlockHolder<WallBlock, *> = variant.wall
        // Base -> Slab
        HTShapedRecipeBuilder(slab, 6)
            .pattern("AAA")
            .define('A', base)
            .save(output)
        // Base -> Stairs
        HTShapedRecipeBuilder(stairs, 4, CraftingBookCategory.BUILDING)
            .pattern(
                "A  ",
                "AA ",
                "AAA",
            ).define('A', base)
            .save(output)
        // Base -> Wall
        HTShapedRecipeBuilder(wall, 4, CraftingBookCategory.BUILDING)
            .pattern(
                "AAA",
                "AAA",
            ).define('A', base)
            .save(output)
        // Stonecutting
        val cuttingIngredient: Ingredient = getCuttingIngredient(variant)

        HTStonecuttingRecipeBuilder(slab, 2)
            .addIngredient(cuttingIngredient)
            .save(output)

        HTStonecuttingRecipeBuilder(stairs)
            .addIngredient(cuttingIngredient)
            .save(output)

        HTStonecuttingRecipeBuilder(wall)
            .addIngredient(cuttingIngredient)
            .save(output)
    }

    private fun getCuttingIngredient(variant: HTDecorationVariant): Ingredient {
        if (variant == HTDecorationVariant.PLASTIC_BRICK || variant == HTDecorationVariant.PLASTIC_TILE) {
            return HTMaterialVariant.STORAGE_BLOCK.toIngredient(RagiumMaterialType.PLASTIC)
        }
        return buildList {
            add(variant.base)
            // Eldritch
            if (variant == HTDecorationVariant.POLISHED_ELDRITCH_STONE) {
                add(RagiumBlocks.ELDRITCH_STONE)
            }
            if (variant == HTDecorationVariant.POLISHED_ELDRITCH_STONE_BRICK) {
                add(RagiumBlocks.ELDRITCH_STONE)
                add(RagiumBlocks.POLISHED_ELDRITCH_STONE)
            }
        }.toTypedArray().let { Ingredient.of(*it) }
    }
}
