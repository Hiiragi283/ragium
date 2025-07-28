package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.data.server.material.ModMaterialFamilies
import hiiragi283.ragium.data.server.material.RagiumMaterialFamilies
import hiiragi283.ragium.data.server.material.VanillaMaterialFamilies
import hiiragi283.ragium.data.server.recipe.RagiumBlockInteractingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumCrushingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumDecorationRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumExtractingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumFluidRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumFoodRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumInfusingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumMaterialRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumMiscRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumPressingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumToolRecipeProvider
import hiiragi283.ragium.data.server.recipe.compat.RagiumArsRecipeProvider
import hiiragi283.ragium.data.server.recipe.compat.RagiumDelightRecipeProvider
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
    override fun buildRecipes(recipeOutput: RecipeOutput, holderLookup: HolderLookup.Provider) {
        VanillaMaterialFamilies
        RagiumMaterialFamilies
        ModMaterialFamilies

        RagiumBlockInteractingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumCrushingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumDecorationRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumExtractingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumFluidRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumFoodRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumInfusingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumMaterialRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumMiscRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumPressingRecipeProvider.buildRecipes(recipeOutput, holderLookup)
        RagiumToolRecipeProvider.buildRecipes(recipeOutput, holderLookup)

        RagiumArsRecipeProvider.buildRecipes(recipeOutput, holderLookup, RagiumConst.ARS_NOUVEAU)
        RagiumDelightRecipeProvider.buildRecipes(recipeOutput, holderLookup, RagiumConst.FARMERS_DELIGHT)
        RagiumMekanismRecipeProvider.buildRecipes(recipeOutput, holderLookup, RagiumConst.MEKANISM)
        RagiumOritechRecipeProvider.buildRecipes(recipeOutput, holderLookup, RagiumConst.ORITECH)
        RagiumReplicationRecipeProvider.buildRecipes(recipeOutput, holderLookup, RagiumConst.REPLICATION)
    }
}
