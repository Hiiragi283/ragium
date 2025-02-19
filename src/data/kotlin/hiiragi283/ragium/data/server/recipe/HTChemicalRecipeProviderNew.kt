package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTMultiItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object HTChemicalRecipeProviderNew : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        nitrogen(output)
        fluorine(output)

        aluminum(output)
        sulfur(output)

        slag(output)
        uranium(output)
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
        // Nitration
        HTFluidOutputRecipeBuilder
            .mixer()
            .fluidInput(RagiumFluidTags.NON_NITRO_FUEL, 800)
            .fluidInput(RagiumVirtualFluids.MIXTURE_ACID.commonTag, 100)
            .fluidOutput(RagiumVirtualFluids.NITRO_FUEL, 800)
            .save(output)

        HTFluidOutputRecipeBuilder
            .infuser()
            .itemInput(Items.PAPER)
            .fluidInput(RagiumVirtualFluids.NITROGLYCERIN.commonTag, 125)
            .itemOutput(RagiumItems.DYNAMITE)
            .save(output)

        HTFluidOutputRecipeBuilder
            .mixer()
            .itemInput(Tags.Items.SANDS)
            .fluidInput(RagiumVirtualFluids.AROMATIC_COMPOUND.commonTag, 250)
            .fluidInput(RagiumVirtualFluids.MIXTURE_ACID.commonTag, 250)
            .itemOutput(Items.TNT)
            .save(output)
    }

    //    Fluorine    //

    private fun fluorine(output: RecipeOutput) {
    }

    //    Aluminum    //

    private fun aluminum(output: RecipeOutput) {
        // Alkali + Water -> Alkali Solution
        HTFluidOutputRecipeBuilder
            .infuser()
            .itemInput(RagiumItems.ALKALI_REAGENT)
            .waterInput()
            .fluidOutput(RagiumVirtualFluids.ALKALI_SOLUTION)
            .save(output)

        // 8x Netherrack -> 6x Bauxite + 2x Sulfur
        HTSingleItemRecipeBuilder
            .grinder()
            .itemInput(Items.NETHERRACK, 8)
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.BAUXITE, 4)
            .save(output)
        // Bauxite + Lapis solution -> Alumina + Water
        HTFluidOutputRecipeBuilder
            .infuser()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.BAUXITE)
            .fluidInput(RagiumVirtualFluids.ALKALI_SOLUTION.commonTag)
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.ALUMINA)
            .waterOutput()
            .save(output)
        // Alumina + 4x Coal -> Aluminum Ingot
        HTMultiItemRecipeBuilder
            .blastFurnace()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ALUMINA)
            .itemInput(ItemTags.COALS, 4)
            .itemOutput(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM)
            .saveSuffixed(output, "_with_coal")

        // Al + HF + Alkali -> Na3AlF6
        HTFluidOutputRecipeBuilder
            .mixer()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ALUMINUM)
            .fluidInput(RagiumVirtualFluids.HYDROFLUORIC_ACID.commonTag, 6000)
            .fluidInput(RagiumVirtualFluids.ALKALI_SOLUTION.commonTag, 3000)
            .itemOutput(HTTagPrefix.GEM, CommonMaterials.CRYOLITE)
            .save(output)
        // Alumina + Cryolite -> 3x Aluminum Ingot
        HTMultiItemRecipeBuilder
            .blastFurnace()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ALUMINA)
            .itemInput(HTTagPrefix.GEM, CommonMaterials.CRYOLITE)
            .itemOutput(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM, 3)
            .saveSuffixed(output, "_with_cryolite")
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

    private fun slag(output: RecipeOutput) {
        // Slag -> Gravel
        HTSingleItemRecipeBuilder
            .grinder()
            .itemInput(RagiumItemTags.SLAG)
            .itemOutput(Items.GRAVEL)
            .saveSuffixed(output, "_from_slag")
    }

    //    Uranium    //

    private fun uranium(output: RecipeOutput) {
        // Poisonous Potato + H2SO4 -> Yellow Cake
        HTFluidOutputRecipeBuilder
            .infuser()
            .itemInput(Items.POISONOUS_POTATO, 8)
            .fluidInput(RagiumVirtualFluids.SULFURIC_ACID.commonTag, 8000)
            .itemOutput(RagiumItems.YELLOW_CAKE)
            .save(output)
        // Cutting Yellow Cake
        ShapelessRecipeBuilder
            .shapeless(RecipeCategory.MISC, RagiumItems.YELLOW_CAKE_PIECE, 8)
            .requires(RagiumItems.YELLOW_CAKE)
            .unlockedBy("has_cake", has(RagiumItems.YELLOW_CAKE))
            .savePrefixed(output)
        // Catastrophic Fuel
    }
}
