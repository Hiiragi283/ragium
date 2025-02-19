package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput

object HTChemicalRecipeProviderNew : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        nitrogen(output)

        sulfur(output)
    }

    //    Nitrogen    //

    private fun nitrogen(output: RecipeOutput) {
        // 2x KNO3 + H2SO4 -> 2x HNO3 + K2SO4
        HTFluidOutputRecipeBuilder
            .infuser()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.SALTPETER)
            .fluidInput(RagiumVirtualFluids.SULFURIC_ACID.commonTag, 500)
            .itemOutput(RagiumItems.ALKALI_REAGENT)
            .fluidOutput(RagiumVirtualFluids.NITRIC_ACID, 500)
            .save(output, RagiumAPI.id("nitric_acid"))

        // 3x H2SO4 + HNO3 -> Mixture Acid
        HTFluidOutputRecipeBuilder
            .mixer()
            .fluidInput(RagiumVirtualFluids.SULFURIC_ACID.commonTag, 300)
            .fluidInput(RagiumVirtualFluids.NITRIC_ACID.commonTag, 100)
            .fluidOutput(RagiumVirtualFluids.MIXTURE_ACID, 400)
            .save(output)
    }

    //    Sulfur    //

    private fun sulfur(output: RecipeOutput) {
        // S + O2 -> SO2
        HTFluidOutputRecipeBuilder
            .infuser()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.SULFUR)
            .fluidInput(RagiumVirtualFluids.OXYGEN.commonTag)
            .fluidOutput(RagiumVirtualFluids.SULFUR_DIOXIDE)
            .save(output)
        // SO2 + O -> SO3
        HTFluidOutputRecipeBuilder
            .mixer()
            .fluidInput(RagiumVirtualFluids.SULFUR_DIOXIDE.commonTag)
            .fluidInput(RagiumVirtualFluids.OXYGEN.commonTag, 500)
            .fluidOutput(RagiumVirtualFluids.SULFUR_TRIOXIDE)
            .save(output)
        // SO3 + H2O -> H2SO4
        HTFluidOutputRecipeBuilder
            .mixer()
            .fluidInput(RagiumVirtualFluids.SULFUR_TRIOXIDE.commonTag)
            .waterInput()
            .fluidOutput(RagiumVirtualFluids.SULFURIC_ACID)
            .save(output)
    }
}
