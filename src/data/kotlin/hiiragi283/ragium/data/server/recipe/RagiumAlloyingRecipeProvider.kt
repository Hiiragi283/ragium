package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTMachineRecipeBuilder
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumRecipes
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput

object RagiumAlloyingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Copper + Raginite -> Ragi-Alloy
        HTMachineRecipeBuilder(RagiumRecipes.ALLOYING)
            .itemInput(HTTagPrefixes.INGOT, VanillaMaterials.COPPER)
            .itemInput(HTTagPrefixes.DUST, RagiumMaterials.RAGINITE, 4)
            .itemOutput(RagiumItems.Ingots.RAGI_ALLOY)
            .save(output)
        // Gold + Raginite -> Advanced Ragi-Alloy
        HTMachineRecipeBuilder(RagiumRecipes.ALLOYING)
            .itemInput(HTTagPrefixes.INGOT, VanillaMaterials.GOLD)
            .itemInput(HTTagPrefixes.DUST, RagiumMaterials.RAGINITE, 4)
            .itemOutput(RagiumItems.Ingots.ADVANCED_RAGI_ALLOY)
            .save(output)

        // Iron + Azure Mixture -> Azure Steel Ingot
        HTMachineRecipeBuilder(RagiumRecipes.ALLOYING)
            .itemInput(HTTagPrefixes.INGOT, VanillaMaterials.IRON)
            .itemInput(RagiumItems.AZURE_MIXTURE, 2)
            .itemOutput(RagiumItems.Ingots.AZURE_STEEL)
            .save(output)
    }
}
