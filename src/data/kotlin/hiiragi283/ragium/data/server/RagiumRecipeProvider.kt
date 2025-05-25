package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.IntegrationMods
import hiiragi283.ragium.data.server.recipe.RagiumArsRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumBlockInteractingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumCrushingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumDecorationRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumDelightRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumExtractingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumFluidRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumFoodRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumMaterialRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumMekanismRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumMiscRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumToolRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import java.util.concurrent.CompletableFuture

class RagiumRecipeProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
    RecipeProvider(output, registries) {
    override fun buildRecipes(recipeOutput: RecipeOutput, holderLookup: HolderLookup.Provider) {
        RagiumBlockInteractingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumCrushingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumDecorationRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumExtractingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumFluidRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumFoodRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumMaterialRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumMiscRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumToolRecipeProvider.buildRecipes(recipeOutput, holderLookup)

        RagiumArsRecipeProvider.buildRecipes(recipeOutput, holderLookup, IntegrationMods.ARS)
        RagiumDelightRecipeProvider.buildRecipes(recipeOutput, holderLookup, IntegrationMods.FD)
        RagiumMekanismRecipeProvider.buildRecipes(recipeOutput, holderLookup, IntegrationMods.MEK)
    }
}
