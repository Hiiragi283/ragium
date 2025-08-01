package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.buildTable
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.idOrNull
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.HTTable
import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumExtractingRecipeProvider : HTRecipeProvider() {
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
                HTIngredientHelper.item(Tags.Items.STORAGE_BLOCKS_REDSTONE),
                HTResultHelper.item(RagiumCommonTags.Items.DUSTS_CINNABAR, 3),
            ).saveSuffixed(output, "_from_redstone")

        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.item(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS),
                HTResultHelper.item(RagiumCommonTags.Items.DUSTS_SALTPETER),
            ).saveSuffixed(output, "_from_sandstone")

        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.item(Tags.Items.GUNPOWDERS),
                HTResultHelper.item(RagiumCommonTags.Items.DUSTS_SULFUR),
            ).saveSuffixed(output, "_from_gunpowder")

        flower()
    }

    private fun flower() {
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
