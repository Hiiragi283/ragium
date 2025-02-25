package hiiragi283.ragium.data.server

import hiiragi283.ragium.data.server.integration.*
import hiiragi283.ragium.data.server.recipe.*
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import java.util.concurrent.CompletableFuture

class RagiumRecipeProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
    RecipeProvider(output, registries) {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        HTAlternativeRecipeProvider.buildRecipes(output, holderLookup)
        HTBlockRecipeProvider.buildRecipes(output, holderLookup)
        HTChemicalRecipeProvider.buildRecipes(output, holderLookup)
        HTChemicalRecipeProviderNew.buildRecipes(output, holderLookup)
        HTFoodRecipeProvider.buildRecipes(output, holderLookup)
        HTCommonRecipeProvider.buildRecipes(output, holderLookup)
        HTMachineRecipeProvider.buildRecipes(output, holderLookup)
        HTMaterialRecipeProvider.buildRecipes(output, holderLookup)

        HTAARecipeProvider.buildRecipes(output, holderLookup)
        HTDelightRecipeProvider.buildRecipes(output, holderLookup)
        HTEnderIORecipeProvider.buildRecipes(output, holderLookup)
        HTIERecipeProvider.buildRecipes(output, holderLookup)
        HTMekanismRecipeProvider.buildRecipes(output, holderLookup)
    }
}
