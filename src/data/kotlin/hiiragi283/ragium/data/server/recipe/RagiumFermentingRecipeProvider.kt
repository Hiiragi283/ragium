package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumFermentingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Vanilla
        HTSingleItemRecipeBuilder
            .ferment(Items.MOSSY_COBBLESTONE)
            .addIngredient(Tags.Items.COBBLESTONES_NORMAL)
            .save(output)

        HTSingleItemRecipeBuilder
            .ferment(Items.MOSSY_STONE_BRICKS)
            .addIngredient(Items.STONE_BRICKS)
            .save(output)

        HTSingleItemRecipeBuilder
            .ferment(Items.MYCELIUM)
            .addIngredient(Items.GRASS_BLOCK)
            .save(output)

        HTSingleItemRecipeBuilder
            .ferment(Items.MUD)
            .addIngredient(Items.DIRT)
            .save(output)

        HTSingleItemRecipeBuilder
            .ferment(Items.MOSS_BLOCK, 4)
            .addIngredient(Items.MOSS_BLOCK)
            .group("moss_block")
            .saveSuffixed(output, "_from_block")

        HTSingleItemRecipeBuilder
            .ferment(Items.MOSS_BLOCK)
            .addIngredient(Items.MOSS_CARPET)
            .group("moss_block")
            .saveSuffixed(output, "_from_carpet")

        HTSingleItemRecipeBuilder
            .ferment(Items.GLOWSTONE_DUST)
            .addIngredient(Items.GLOW_BERRIES)
            .group("glowstone_dust")
            .save(output)
        
        HTSingleItemRecipeBuilder
            .ferment(Items.FERMENTED_SPIDER_EYE)
            .addIngredient(Items.SPIDER_EYE)
            .save(output)
    }
}
