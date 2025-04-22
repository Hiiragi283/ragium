package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.IntegrationMods
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder

object RagiumDelightRecipeProvider : HTRecipeProvider.Modded(IntegrationMods.FD) {
    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        crafting(output)
        cookingPot(output)
        cutting(output)
    }

    private fun crafting(output: RecipeOutput) {
        // Ragi-Cherry Popsicle
        /*HTShapedRecipeBuilder(RagiumDelightAddon.RAGI_CHERRY_POPSICLE)
            .pattern(
                " AA",
                "BAA",
                "CB ",
            ).define('A', RagiumItemTags.FOODS_RAGI_CHERRY)
            .define('B', Items.ICE)
            .define('C', Tags.Items.RODS_WOODEN)
            .save(output)*/
    }

    private fun cookingPot(output: RecipeOutput) {
        // Ragi-Cherry Jam
        CookingPotRecipeBuilder
            .cookingPotRecipe(
                RagiumItems.RAGI_CHERRY_JAM,
                1,
                200,
                0.35f,
                Items.GLASS_BOTTLE,
            ).addIngredient(RagiumItemTags.FOODS_RAGI_CHERRY)
            .addIngredient(RagiumItemTags.FOODS_RAGI_CHERRY)
            .addIngredient(Items.SUGAR)
            .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
            .save(output)
    }

    private fun cutting(output: RecipeOutput) {
        // Ragi-Cherry Pulp
        CuttingBoardRecipeBuilder
            .cuttingRecipe(
                Ingredient.of(RagiumItems.RAGI_CHERRY),
                Ingredient.of(RagiumItemTags.TOOLS_KNIFE),
                RagiumDelightAddon.RAGI_CHERRY_PULP,
                2,
            ).save(output)
    }
}
