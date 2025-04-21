package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTMachineRecipeBuilder
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.idOrNull
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipes
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumExtractingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Vanilla
        HTMachineRecipeBuilder(RagiumRecipes.EXTRACTING)
            .itemOutput(Items.FLINT)
            .itemInput(Tags.Items.GRAVELS)
            .saveSuffixed(output, "_from_gravel")

        HTMachineRecipeBuilder(RagiumRecipes.EXTRACTING)
            .itemOutput(Items.REDSTONE)
            .itemInput(Tags.Items.SANDSTONE_RED_BLOCKS)
            .saveSuffixed(output, "_from_red_sandstone")

        HTMachineRecipeBuilder(RagiumRecipes.EXTRACTING)
            .itemOutput(Items.BROWN_MUSHROOM, 3)
            .itemInput(Items.BROWN_MUSHROOM_BLOCK)
            .saveSuffixed(output, "_from_block")

        HTMachineRecipeBuilder(RagiumRecipes.EXTRACTING)
            .itemOutput(Items.RED_MUSHROOM, 3)
            .itemInput(Items.RED_MUSHROOM_BLOCK)
            .saveSuffixed(output, "_from_block")
        // Ragium
        HTMachineRecipeBuilder(RagiumRecipes.EXTRACTING)
            .itemOutput(RagiumItems.Dusts.SALTPETER)
            .itemInput(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            .saveSuffixed(output, "_from_sandstone")

        HTMachineRecipeBuilder(RagiumRecipes.EXTRACTING)
            .itemOutput(RagiumItems.Dusts.SULFUR)
            .itemInput(Tags.Items.GUNPOWDERS)
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
            HTMachineRecipeBuilder(RagiumRecipes.EXTRACTING)
                .itemOutput(dye, 2)
                .itemInput(flower)
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
            HTMachineRecipeBuilder(RagiumRecipes.EXTRACTING)
                .itemOutput(dye, 4)
                .itemInput(flower)
                .saveSuffixed(output, "_from_$flowerName")
        }
    }
}
