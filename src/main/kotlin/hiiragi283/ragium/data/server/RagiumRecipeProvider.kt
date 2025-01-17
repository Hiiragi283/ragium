package hiiragi283.ragium.data.server

import hiiragi283.ragium.data.server.recipe.*
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import java.util.concurrent.CompletableFuture

class RagiumRecipeProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
    RecipeProvider(output, registries) {
    override fun buildRecipes(recipeOutput: RecipeOutput) {
        HTBuildingRecipeProvider.buildRecipes(recipeOutput)
        HTChemicalRecipeProvider.buildRecipes(recipeOutput)
        HTFoodRecipeProvider.buildRecipes(recipeOutput)
        HTIngredientRecipeProvider.buildRecipes(recipeOutput)
        HTMachineRecipeProvider.buildRecipes(recipeOutput)
        HTMaterialRecipeProvider.buildRecipes(recipeOutput)
    }
}
