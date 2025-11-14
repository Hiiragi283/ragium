package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.variant.HTDecorationVariant
import hiiragi283.ragium.impl.data.HTVanillaWoodType
import hiiragi283.ragium.impl.data.recipe.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock
import net.neoforged.neoforge.common.Tags

object RagiumDecorationRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Ragi-Bricks
        HTShapedRecipeBuilder
            .building(RagiumBlocks.RAGI_BRICKS, 8)
            .hollow8()
            .define('A', ItemTags.STONE_BRICKS)
            .define('B', CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .save(output)
        // Azure Tiles
        HTShapedRecipeBuilder
            .building(RagiumBlocks.AZURE_TILES, 8)
            .hollow8()
            .define('A', Items.DEEPSLATE_TILES)
            .define('B', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.AZURE)
            .save(output)
        // Eldritch Stone
        HTShapedRecipeBuilder
            .building(RagiumBlocks.ELDRITCH_STONE, 8)
            .hollow8()
            .define('A', Tags.Items.END_STONES)
            .define('B', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL)
            .save(output)

        HTShapedRecipeBuilder
            .building(RagiumBlocks.ELDRITCH_STONE_BRICKS, 4)
            .storage4()
            .define('A', RagiumBlocks.ELDRITCH_STONE)
            .save(output)

        HTSingleItemRecipeBuilder
            .stonecutter(RagiumBlocks.ELDRITCH_STONE_BRICKS)
            .addIngredient(RagiumBlocks.ELDRITCH_STONE)
            .save(output)
        // Plastics
        HTShapedRecipeBuilder
            .building(RagiumBlocks.PLASTIC_BRICKS, 4)
            .storage4()
            .define('A', CommonMaterialPrefixes.STORAGE_BLOCK, CommonMaterialKeys.PLASTIC)
            .save(output)

        HTSingleItemRecipeBuilder
            .stonecutter(RagiumBlocks.PLASTIC_BRICKS)
            .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, CommonMaterialKeys.PLASTIC)
            .save(output)

        HTShapedRecipeBuilder
            .building(RagiumBlocks.PLASTIC_TILES, 4)
            .storage4()
            .define('A', RagiumBlocks.PLASTIC_BRICKS)
            .save(output)

        HTSingleItemRecipeBuilder
            .stonecutter(RagiumBlocks.PLASTIC_TILES)
            .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, CommonMaterialKeys.PLASTIC)
            .save(output)
        // Blue Nether Bricks
        HTShapedRecipeBuilder
            .building(RagiumBlocks.BLUE_NETHER_BRICKS, 1)
            .pattern(
                "AB",
                "BA",
            ).define('A', CommonMaterialPrefixes.CROP, FoodMaterialKeys.WARPED_WART)
            .define('B', Tags.Items.BRICKS_NETHER)
            .save(output)
        // Sponge Cake
        HTShapedRecipeBuilder
            .building(RagiumBlocks.SPONGE_CAKE, 4)
            .cross8()
            .define('A', Tags.Items.CROPS_WHEAT)
            .define('B', Items.SUGAR)
            .define('C', Tags.Items.EGGS)
            .save(output)

        HTDecorationVariant.entries.forEach(::registerBuildings)
        HTVanillaWoodType.entries.forEach(::addWoodSawing)

        glass()
    }

    @JvmStatic
    private fun glass() {
        // Quartz Glass
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumBlocks.getGlass(VanillaMaterialKeys.QUARTZ)),
                itemCreator.fromTagKey(CommonMaterialPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.QUARTZ),
                itemCreator.fromTagKey(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC),
            ).save(output)
        // Soul Glass
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumBlocks.getGlass(VanillaMaterialKeys.SOUL)),
                itemCreator.fromItem(Items.SOUL_SAND),
                itemCreator.fromTagKey(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC),
            ).save(output)
        // Obsidian Glass
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumBlocks.getGlass(VanillaMaterialKeys.OBSIDIAN)),
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.OBSIDIAN, 4),
                itemCreator.fromTagKey(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED),
            ).save(output)

        // Normal -> Tinted
        listOf(
            VanillaMaterialKeys.QUARTZ,
            VanillaMaterialKeys.SOUL,
            VanillaMaterialKeys.OBSIDIAN,
        ).forEach { key: HTMaterialKey ->
            HTShapedRecipeBuilder
                .building(RagiumBlocks.getTintedGlass(key))
                .hollow4()
                .define('A', CommonMaterialPrefixes.GEM, VanillaMaterialKeys.AMETHYST)
                .define('B', CommonMaterialPrefixes.GLASS_BLOCK, key)
                .save(output)
        }
    }

    //    Decorations    //

    @JvmStatic
    private fun registerBuildings(variant: HTDecorationVariant) {
        val base: HTDeferredBlock<*, *> = variant.base
        val slab: HTDeferredBlock<SlabBlock, *> = variant.slab
        val stairs: HTDeferredBlock<StairBlock, *> = variant.stairs
        val wall: HTDeferredBlock<WallBlock, *> = variant.wall
        // Base -> Slab
        HTShapedRecipeBuilder
            .building(slab, 6)
            .pattern("AAA")
            .define('A', base)
            .save(output)
        // Base -> Stairs
        HTShapedRecipeBuilder
            .building(stairs, 4)
            .pattern(
                "A  ",
                "AA ",
                "AAA",
            ).define('A', base)
            .save(output)
        // Base -> Wall
        HTShapedRecipeBuilder
            .building(wall, 4)
            .pattern(
                "AAA",
                "AAA",
            ).define('A', base)
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
