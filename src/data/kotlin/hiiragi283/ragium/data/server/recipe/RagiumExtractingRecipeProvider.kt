package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.idOrNull
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumExtractingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Vanilla
        HTSingleItemRecipeBuilder
            .extract(Items.FLINT)
            .addIngredient(Tags.Items.GRAVELS)
            .group("flint")
            .saveSuffixed(output, "_from_gravel")

        HTSingleItemRecipeBuilder
            .extract(Items.REDSTONE)
            .addIngredient(Tags.Items.SANDSTONE_RED_BLOCKS)
            .group("redstone")
            .saveSuffixed(output, "_from_red_sandstone")

        HTSingleItemRecipeBuilder
            .extract(Items.BROWN_MUSHROOM, 3)
            .addIngredient(Items.BROWN_MUSHROOM_BLOCK)
            .group("brown_mushroom")
            .saveSuffixed(output, "_from_block")

        HTSingleItemRecipeBuilder
            .extract(Items.RED_MUSHROOM, 3)
            .addIngredient(Items.RED_MUSHROOM_BLOCK)
            .group("red_mushroom")
            .saveSuffixed(output, "_from_block")
        // Ragium
        HTSingleItemRecipeBuilder
            .extract(RagiumItems.Dusts.SALTPETER)
            .addIngredient(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            .group("saltpeter_dust")
            .saveSuffixed(output, "_from_sandstone")

        HTSingleItemRecipeBuilder
            .extract(RagiumItems.Dusts.SULFUR)
            .addIngredient(Tags.Items.GUNPOWDERS)
            .group("sulfur_dust")
            .saveSuffixed(output, "_from_gunpowder")

        flower(output)
    }

    private fun flower(output: RecipeOutput) {
        val small: Map<Item, Item> = mapOf(
            Items.DANDELION to Items.YELLOW_DYE,
            Items.POPPY to Items.RED_DYE,
            Items.BLUE_ORCHID to Items.LIGHT_BLUE_DYE,
            Items.ALLIUM to Items.MAGENTA_DYE,
            Items.AZURE_BLUET to Items.LIGHT_GRAY_DYE,
            Items.RED_TULIP to Items.RED_DYE,
            Items.ORANGE_TULIP to Items.ORANGE_DYE,
            Items.WHITE_TULIP to Items.LIGHT_GRAY_DYE,
            Items.PINK_TULIP to Items.PINK_DYE,
            Items.OXEYE_DAISY to Items.LIGHT_GRAY_DYE,
            Items.CORNFLOWER to Items.BLUE_DYE,
            Items.LILY_OF_THE_VALLEY to Items.WHITE_DYE,
            Items.WITHER_ROSE to Items.BLACK_DYE,
            Items.PINK_PETALS to Items.PINK_DYE,
            Items.CACTUS to Items.GREEN_DYE,
        )
        for ((flower: Item, dye: Item) in small) {
            val holder: Holder.Reference<Item> = flower.asItemHolder()
            val flowerName: String = holder.idOrNull?.path ?: continue
            HTSingleItemRecipeBuilder
                .extract(dye, 2)
                .addIngredient(flower)
                .saveSuffixed(output, "_from_$flowerName")
        }

        val large: Map<Item, Item> = mapOf(
            Items.TORCHFLOWER to Items.ORANGE_DYE,
            Items.SUNFLOWER to Items.YELLOW_DYE,
            Items.LILAC to Items.MAGENTA_DYE,
            Items.ROSE_BUSH to Items.RED_DYE,
            Items.PEONY to Items.PINK_DYE,
            Items.PITCHER_PLANT to Items.CYAN_DYE,
        )
        for ((flower: Item, dye: Item) in large) {
            val holder: Holder.Reference<Item> = flower.asItemHolder()
            val flowerName: String = holder.idOrNull?.path ?: continue
            HTSingleItemRecipeBuilder
                .extract(dye, 4)
                .addIngredient(flower)
                .saveSuffixed(output, "_from_$flowerName")
        }
    }
}
