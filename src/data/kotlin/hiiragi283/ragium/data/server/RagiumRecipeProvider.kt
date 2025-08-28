package hiiragi283.ragium.data.server

import hiiragi283.ragium.data.server.material.ModMaterialFamilies
import hiiragi283.ragium.data.server.material.VanillaMaterialFamilies
import hiiragi283.ragium.data.server.recipe.RagiumCompressingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumCrushingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumDecorationRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumEnchantingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumEngineeringRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumExtractingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumFluidRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumFoodRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumInfusingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumMachineRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumMaterialRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumSimulatingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumToolRecipeProvider
import hiiragi283.ragium.data.server.recipe.compat.RagiumAE2RecipeProvider
import hiiragi283.ragium.data.server.recipe.compat.RagiumDelightRecipeProvider
import hiiragi283.ragium.data.server.recipe.compat.RagiumImmersiveRecipeProvider
import hiiragi283.ragium.data.server.recipe.compat.RagiumMekanismRecipeProvider
import hiiragi283.ragium.data.server.recipe.compat.RagiumOritechRecipeProvider
import hiiragi283.ragium.data.server.recipe.compat.RagiumReplicationRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import java.util.concurrent.CompletableFuture

class RagiumRecipeProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
    RecipeProvider(output, registries) {
    @Suppress("UnusedExpression")
    override fun buildRecipes(recipeOutput: RecipeOutput, holderLookup: HolderLookup.Provider) {
        VanillaMaterialFamilies
        // RagiumMaterialFamilies
        ModMaterialFamilies

        RagiumCompressingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumCrushingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumDecorationRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumEnchantingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumExtractingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumFluidRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumFoodRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumInfusingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumMaterialRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumMachineRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumEngineeringRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumSimulatingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumToolRecipeProvider.buildRecipes(recipeOutput, holderLookup)

        RagiumAE2RecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumDelightRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumImmersiveRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumMekanismRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumOritechRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumReplicationRecipeProvider.buildRecipes(recipeOutput, holderLookup)
    }
}
