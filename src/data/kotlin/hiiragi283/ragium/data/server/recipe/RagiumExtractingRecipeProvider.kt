package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.buildTable
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.idOrNull
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.util.material.HTVanillaMaterialType
import hiiragi283.ragium.util.material.RagiumMaterialType
import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumExtractingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Vanilla
        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.item(Tags.Items.GRAVELS),
                HTResultHelper.item(Items.FLINT),
            ).saveSuffixed(output, "_from_gravel")

        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.item(Tags.Items.SANDSTONE_RED_BLOCKS),
                HTResultHelper.item(Items.REDSTONE),
            ).saveSuffixed(output, "_from_red_sandstone")

        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.item(Items.BROWN_MUSHROOM_BLOCK),
                HTResultHelper.item(Items.BROWN_MUSHROOM, 3),
            ).saveSuffixed(output, "_from_block")

        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.item(Items.RED_MUSHROOM_BLOCK),
                HTResultHelper.item(Items.RED_MUSHROOM, 3),
            ).saveSuffixed(output, "_from_block")
        // Ragium
        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.item(HTMaterialVariant.STORAGE_BLOCK, HTVanillaMaterialType.REDSTONE),
                HTResultHelper.item(HTMaterialVariant.DUST, RagiumMaterialType.CINNABAR, 3),
            ).saveSuffixed(output, "_from_redstone")

        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.item(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS),
                HTResultHelper.item(HTMaterialVariant.DUST, RagiumMaterialType.SALTPETER),
            ).saveSuffixed(output, "_from_sandstone")

        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.item(Tags.Items.GUNPOWDERS),
                HTResultHelper.item(HTMaterialVariant.DUST, RagiumMaterialType.SULFUR),
            ).saveSuffixed(output, "_from_gunpowder")

        dyes()
    }

    private fun dyes() {
        // Charcoal -> Brown
        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.fuelOrDust(HTVanillaMaterialType.CHARCOAL),
                HTResultHelper.item(Items.BROWN_DYE),
            ).saveSuffixed(output, "_from_charcoal")
        // Grass -> Green
        HTShapelessRecipeBuilder(Items.GREEN_DYE)
            .addIngredient(Items.SHORT_GRASS)
            .addIngredient(Items.SHORT_GRASS)
            .addIngredient(Items.SHORT_GRASS)
            .addIngredient(Items.SHORT_GRASS)
            .addIngredient(RagiumModTags.Items.TOOLS_HAMMER)
            .saveSuffixed(output, "_from_grasses")

        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.item(Items.SHORT_GRASS, 4),
                HTResultHelper.item(Items.GREEN_DYE),
            ).saveSuffixed(output, "_from_grass")
        // Coal -> Black
        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.fuelOrDust(HTVanillaMaterialType.COAL),
                HTResultHelper.item(Items.BLACK_DYE),
            ).saveSuffixed(output, "_from_coal")

        // Flowers
        val table: HTTable<Item, Item, Int> = buildTable {
            // Small
            put(Items.DANDELION, Items.YELLOW_DYE, 2)
            put(Items.POPPY, Items.RED_DYE, 2)
            put(Items.BLUE_ORCHID, Items.LIGHT_BLUE_DYE, 2)
            put(Items.ALLIUM, Items.MAGENTA_DYE, 2)
            put(Items.AZURE_BLUET, Items.LIGHT_GRAY_DYE, 2)
            put(Items.RED_TULIP, Items.RED_DYE, 2)
            put(Items.ORANGE_TULIP, Items.ORANGE_DYE, 2)
            put(Items.WHITE_TULIP, Items.LIGHT_GRAY_DYE, 2)
            put(Items.PINK_TULIP, Items.PINK_DYE, 2)
            put(Items.OXEYE_DAISY, Items.LIGHT_GRAY_DYE, 2)
            put(Items.CORNFLOWER, Items.BLUE_DYE, 2)
            put(Items.LILY_OF_THE_VALLEY, Items.WHITE_DYE, 2)
            put(Items.WITHER_ROSE, Items.BLACK_DYE, 2)
            put(Items.PINK_PETALS, Items.PINK_DYE, 2)
            put(Items.CACTUS, Items.GREEN_DYE, 2)
            // Large
            put(Items.TORCHFLOWER, Items.ORANGE_DYE, 4)
            put(Items.SUNFLOWER, Items.YELLOW_DYE, 4)
            put(Items.LILAC, Items.MAGENTA_DYE, 4)
            put(Items.ROSE_BUSH, Items.RED_DYE, 4)
            put(Items.PEONY, Items.PINK_DYE, 4)
            put(Items.PITCHER_PLANT, Items.CYAN_DYE, 4)
        }

        table.forEach { (flower: Item, dye: Item, count: Int) ->
            val flowerHolder: Holder.Reference<Item> = flower.asItemHolder()
            val flowerName: String = flowerHolder.idOrNull?.path ?: return@forEach

            HTItemToObjRecipeBuilder
                .extracting(
                    HTIngredientHelper.item(flower),
                    HTResultHelper.item(dye, count),
                ).saveSuffixed(output, "_from_$flowerName")
        }
    }
}
