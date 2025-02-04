package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.*
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType

object HTChemicalRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerNitrogen(output)
        registerFluorine(output)

        registerAlkali(output)
        registerAluminum(output)
        registerSludge(output)

        registerUranium(output)
        registerPlutonium(output)
    }

    private fun registerNitrogen(output: RecipeOutput) {
        // Nitration
        HTInfuserRecipeBuilder()
            .itemInput(RagiumItems.CREEPER_REAGENT)
            .fluidInput(RagiumFluidTags.NON_NITRO_FUEL, FluidType.BUCKET_VOLUME * 8)
            .fluidOutput(RagiumFluids.NITRO_FUEL, FluidType.BUCKET_VOLUME * 8)
            .save(output)

        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(Tags.Items.STRINGS, 4)
            .itemInput(Items.PAPER, 4)
            .itemInput(RagiumItems.CREEPER_REAGENT)
            .itemOutput(RagiumItems.DYNAMITE, 8)
            .save(output)

        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(Tags.Items.SANDS, 4)
            .itemInput(RagiumItems.CREEPER_REAGENT)
            .itemOutput(Items.TNT, 4)
            .save(output)
    }

    private fun registerFluorine(output: RecipeOutput) {
        // Glowstone -> 4x CaF2
        HTExtractorRecipeBuilder()
            .itemInput(Items.GLOWSTONE)
            .itemOutput(HTTagPrefix.GEM, CommonMaterials.FLUORITE, 4)
            .saveSuffixed(output, "_from_glowstone")
        // Sea Lantern -> 4x CaF2
        HTExtractorRecipeBuilder()
            .itemInput(Items.SEA_LANTERN)
            .itemOutput(HTTagPrefix.GEM, CommonMaterials.FLUORITE, 6)
            .saveSuffixed(output, "_from_sea_lantern")

        // CaF2 + H2SO4 -> CaSO4 + 2x HF
        HTInfuserRecipeBuilder()
            .itemInput(HTTagPrefix.GEM, CommonMaterials.FLUORITE)
            .fluidInput(RagiumFluids.SULFURIC_ACID)
            .fluidOutput(RagiumFluids.HYDROGEN_FLUORIDE)
            .save(output)
        // HF + H2O <-> HF(aq)
        HTMixerRecipeBuilder()
            .fluidInput(RagiumFluids.HYDROGEN_FLUORIDE)
            .waterInput()
            .fluidOutput(RagiumFluids.HYDROFLUORIC_ACID)
            .save(output)
    }

    private fun registerAlkali(output: RecipeOutput) {
        // Ash -> Alkali
        HTExtractorRecipeBuilder()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ASH)
            .itemOutput(RagiumItems.ALKALI_REAGENT)
            .saveSuffixed(output, "_from_ash")
        // Calcite -> Alkali
        HTGrinderRecipeBuilder()
            .itemInput(Items.CALCITE, 4)
            .itemOutput(RagiumItems.ALKALI_REAGENT)
            .saveSuffixed(output, "_from_calcite")

        // Alkali + Seed Oil -> Soap
        HTInfuserRecipeBuilder()
            .itemInput(RagiumItems.ALKALI_REAGENT)
            .fluidInput(RagiumFluids.PLANT_OIL)
            .itemOutput(RagiumItems.SOAP, 4)
            .save(output)
    }

    private fun registerAluminum(output: RecipeOutput) {
        // Lapis + Water -> Lapis Solution
        HTInfuserRecipeBuilder()
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.LAPIS)
            .waterInput()
            .fluidOutput(RagiumFluids.LAPIS_SOLUTION)
            .save(output)

        // 8x Netherrack -> 6x Bauxite + 2x Sulfur
        HTGrinderRecipeBuilder()
            .itemInput(Items.NETHERRACK, 8)
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.BAUXITE, 4)
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.BAUXITE, 2)
            .setChance(0.5f)
            .save(output)
        // Bauxite + Lapis solution -> Alumina + Water
        HTInfuserRecipeBuilder()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.BAUXITE)
            .fluidInput(RagiumFluids.LAPIS_SOLUTION)
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.ALUMINA)
            .waterOutput()
            .saveSuffixed(output, "_from_bauxite")
        // Alumina + 4x Coal -> Aluminum Ingot
        HTMultiItemRecipeBuilder
            .blastFurnace()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ALUMINA)
            .itemInput(ItemTags.COALS, 4)
            .itemOutput(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM)
            .saveSuffixed(output, "_with_coal")

        // Al + O2 -> Alumina
        HTInfuserRecipeBuilder()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ALUMINUM, 2)
            .fluidInput(RagiumFluids.OXYGEN, FluidType.BUCKET_VOLUME * 3)
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.ALUMINA, 5)
            .saveSuffixed(output, "_from_aluminum")
        // Alumina + HF -> Cryolite
        HTInfuserRecipeBuilder()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ALUMINA)
            .fluidInput(RagiumFluids.HYDROFLUORIC_ACID, FluidType.BUCKET_VOLUME * 6)
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

    private fun registerSludge(output: RecipeOutput) {
        // Slag -> Gravel
        HTGrinderRecipeBuilder()
            .itemInput(HTTagPrefix.GEM, RagiumMaterials.SLAG)
            .itemOutput(Items.GRAVEL)
            .saveSuffixed(output, "_from_slag")

        // Block of Slag -> Chemical Glass
        HTCookingRecipeBuilder
            .create(
                Ingredient.of(HTTagPrefix.STORAGE_BLOCK.createTag(RagiumMaterials.SLAG)),
                RagiumBlocks.CHEMICAL_GLASS,
            ).unlockedBy("has_slag", has(HTTagPrefix.STORAGE_BLOCK, RagiumMaterials.SLAG))
            .save(output)
    }

    private fun registerUranium(output: RecipeOutput) {
        // Poisonous Potato + H2SO4 -> Yellow Cake
        HTInfuserRecipeBuilder()
            .itemInput(Items.POISONOUS_POTATO, 8)
            .fluidInput(RagiumFluids.SULFURIC_ACID, FluidType.BUCKET_VOLUME * 8)
            .itemOutput(RagiumItems.YELLOW_CAKE)
            .save(output)
        // Cutting Yellow Cake
        ShapelessRecipeBuilder
            .shapeless(RecipeCategory.MISC, RagiumItems.YELLOW_CAKE_PIECE, 8)
            .requires(RagiumItems.YELLOW_CAKE)
            .unlockedBy("has_cake", has(RagiumItems.YELLOW_CAKE))
            .savePrefixed(output)
        // Uranium Fuel
        HTInfuserRecipeBuilder()
            .itemInput(RagiumItems.YELLOW_CAKE_PIECE, 16)
            .fluidInput(RagiumFluids.HYDROFLUORIC_ACID, FluidType.BUCKET_VOLUME * 8)
            .itemOutput(RagiumItems.URANIUM_FUEL)
            .save(output)
    }

    private fun registerPlutonium(output: RecipeOutput) {
    }
}
