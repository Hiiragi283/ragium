package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.variant.HTDecorationVariant
import hiiragi283.ragium.common.variant.HTGlassVariant
import hiiragi283.ragium.common.variant.HTItemMaterialVariant
import hiiragi283.ragium.common.variant.HTStorageMaterialVariant
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
            .define('B', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .save(output)
        // Azure Tiles
        HTShapedRecipeBuilder
            .building(RagiumBlocks.AZURE_TILES, 8)
            .hollow8()
            .define('A', Items.DEEPSLATE_TILES)
            .define('B', gemOrDust(RagiumMaterialType.AZURE))
            .save(output)
        // Eldritch Stone
        HTShapedRecipeBuilder
            .building(RagiumBlocks.ELDRITCH_STONE, 8)
            .hollow8()
            .define('A', Tags.Items.END_STONES)
            .define('B', gemOrDust(RagiumMaterialType.ELDRITCH_PEARL))
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
            .define('A', HTStorageMaterialVariant, RagiumMaterialType.PLASTIC)
            .save(output)

        HTSingleItemRecipeBuilder
            .stonecutter(RagiumBlocks.PLASTIC_BRICKS)
            .addIngredient(HTStorageMaterialVariant, RagiumMaterialType.PLASTIC)
            .save(output)

        HTShapedRecipeBuilder
            .building(RagiumBlocks.PLASTIC_TILES, 4)
            .storage4()
            .define('A', RagiumBlocks.PLASTIC_BRICKS)
            .save(output)

        HTSingleItemRecipeBuilder
            .stonecutter(RagiumBlocks.PLASTIC_TILES)
            .addIngredient(HTStorageMaterialVariant, RagiumMaterialType.PLASTIC)
            .save(output)
        // Blue Nether Bricks
        HTShapedRecipeBuilder
            .building(RagiumBlocks.BLUE_NETHER_BRICKS, 1)
            .pattern(
                "AB",
                "BA",
            ).define('A', RagiumCommonTags.Items.CROPS_WARPED_WART)
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
                resultHelper.item(RagiumBlocks.getGlass(HTVanillaMaterialType.QUARTZ)),
                itemCreator.fromTagKey(HTStorageMaterialVariant, HTVanillaMaterialType.QUARTZ),
                itemCreator.fromTagKey(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC),
            ).save(output)
        // Soul Glass
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumBlocks.getGlass(HTVanillaMaterialType.SOUL)),
                itemCreator.fromItem(Items.SOUL_SAND),
                itemCreator.fromTagKey(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC),
            ).save(output)
        // Obsidian Glass
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumBlocks.getGlass(HTVanillaMaterialType.OBSIDIAN)),
                itemCreator.fromTagKey(HTItemMaterialVariant.DUST, HTVanillaMaterialType.OBSIDIAN, 4),
                itemCreator.fromTagKey(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED),
            ).save(output)

        // Normal -> Tinted
        listOf(
            HTVanillaMaterialType.QUARTZ,
            HTVanillaMaterialType.SOUL,
            HTVanillaMaterialType.OBSIDIAN,
        ).forEach { material: HTVanillaMaterialType ->
            HTShapedRecipeBuilder
                .building(RagiumBlocks.getTintedGlass(material))
                .hollow4()
                .define('A', gemOrDust(HTVanillaMaterialType.AMETHYST))
                .define('B', HTGlassVariant.COLORLESS, material)
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
            return HTStorageMaterialVariant.toIngredient(RagiumMaterialType.PLASTIC)
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
