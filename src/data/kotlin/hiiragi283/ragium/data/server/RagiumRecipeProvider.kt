package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.data.server.recipe.RagiumCompressingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumCrushingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumDecorationRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumEnchantingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumEngineeringRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumExtractingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumFluidRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumFoodRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumMachineRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumMaterialRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumPlantingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumSimulatingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumToolRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumWashingRecipeProvider
import hiiragi283.ragium.data.server.recipe.compat.RagiumAARecipeProvider
import hiiragi283.ragium.data.server.recipe.compat.RagiumAE2RecipeProvider
import hiiragi283.ragium.data.server.recipe.compat.RagiumDelightRecipeProvider
import hiiragi283.ragium.data.server.recipe.compat.RagiumImmersiveRecipeProvider
import hiiragi283.ragium.data.server.recipe.compat.RagiumMagitechRecipeProvider
import hiiragi283.ragium.data.server.recipe.compat.RagiumMekanismRecipeProvider
import hiiragi283.ragium.data.server.recipe.compat.RagiumOritechRecipeProvider
import hiiragi283.ragium.data.server.recipe.compat.RagiumReplicationRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider

class RagiumRecipeProvider(context: HTDataGenContext) : RecipeProvider(context.output, context.registries) {
    override fun buildRecipes(recipeOutput: RecipeOutput, holderLookup: HolderLookup.Provider) {
        RagiumCompressingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumCrushingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumDecorationRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumEnchantingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumEngineeringRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumExtractingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumFluidRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumFoodRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumMachineRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumMaterialRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumPlantingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumSimulatingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumToolRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumWashingRecipeProvider.buildRecipes(recipeOutput, holderLookup)

        RagiumAARecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumAE2RecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumDelightRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumImmersiveRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumMagitechRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumMekanismRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumOritechRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumReplicationRecipeProvider.buildRecipes(recipeOutput, holderLookup)
    }
}
