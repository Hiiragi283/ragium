package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.neoforged.neoforge.fluids.FluidType

object HTDistillationRecipeProvider : RecipeProviderChild {
    override fun buildRecipes(output: RecipeOutput) {
        // Biomass -> Alcohol
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.BIOMASS)
            .catalyst(RagiumItems.Circuits.BASIC)
            .fluidOutput(RagiumFluids.ALCOHOL)
            .saveSuffixed(output, "_from_biomass")
        // Biomass -> Bio Fuel
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.BIOMASS)
            .catalyst(RagiumItems.Circuits.ADVANCED)
            .fluidOutput(RagiumFluids.BIO_FUEL)
            .saveSuffixed(output, "_from_biomass")

        // Crude Oil -> Refined Gas + Naphtha + Residual Oil
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.CRUDE_OIL, FluidType.BUCKET_VOLUME * 6)
            .catalyst(RagiumItems.Circuits.BASIC)
            .fluidOutput(RagiumFluids.REFINED_GAS, FluidType.BUCKET_VOLUME * 2)
            .fluidOutput(RagiumFluids.NAPHTHA, FluidType.BUCKET_VOLUME * 3)
            .fluidOutput(RagiumFluids.RESIDUAL_OIL, FluidType.BUCKET_VOLUME * 1)
            .save(output, RagiumAPI.id("crude_oil"))

        // Refined Gas -> Alcohol
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.REFINED_GAS, FluidType.BUCKET_VOLUME * 8)
            .catalyst(RagiumItems.Circuits.BASIC)
            .fluidOutput(RagiumFluids.ALCOHOL, FluidType.BUCKET_VOLUME * 6)
            .save(output, RagiumAPI.id("refined_gas"))

        // Naphtha -> polymer resin + fuel
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.NAPHTHA, FluidType.BUCKET_VOLUME * 8)
            .catalyst(RagiumItems.Circuits.BASIC)
            .itemOutput(RagiumItems.POLYMER_RESIN, 4)
            .fluidOutput(RagiumFluids.FUEL, FluidType.BUCKET_VOLUME * 4)
            .save(output, RagiumAPI.id("naphtha"))

        // Residual Oil -> Fuel + Asphalt
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.RESIDUAL_OIL, FluidType.BUCKET_VOLUME * 8)
            .catalyst(RagiumItems.Circuits.BASIC)
            .fluidOutput(RagiumFluids.FUEL, FluidType.BUCKET_VOLUME * 3)
            .save(output, RagiumAPI.id("asphalt"))
        // Residual Oil -> Residual Coke + Fuel
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.RESIDUAL_OIL, FluidType.BUCKET_VOLUME * 8)
            .catalyst(RagiumItems.Circuits.ADVANCED)
            .itemOutput(RagiumItems.RESIDUAL_COKE, 4)
            .fluidOutput(RagiumFluids.FUEL, FluidType.BUCKET_VOLUME * 4)
            .save(output)
        // Residual Oil -> Fuel + Aromatic compound
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.RESIDUAL_OIL, FluidType.BUCKET_VOLUME * 8)
            .catalyst(RagiumItems.Circuits.ELITE)
            .fluidOutput(RagiumFluids.FUEL, FluidType.BUCKET_VOLUME * 3)
            .fluidOutput(RagiumFluids.AROMATIC_COMPOUNDS, FluidType.BUCKET_VOLUME * 5)
            .save(output, RagiumAPI.id("aromatic_compounds"))

        registerSaps(output)
    }

    private fun registerSaps(output: RecipeOutput) {
        // XX Log -> Sap + Pulp
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(ItemTags.LOGS)
            .itemOutput(RagiumItems.Dusts.WOOD, 4)
            .fluidOutput(RagiumFluids.SAP)
            .save(output)
        // Sap -> Refined Gas + Alcohol
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.SAP, FluidType.BUCKET_VOLUME * 2)
            .catalyst(RagiumItems.Circuits.BASIC)
            .fluidOutput(RagiumFluids.REFINED_GAS)
            .fluidOutput(RagiumFluids.ALCOHOL)
            .save(output, RagiumAPI.id("sap"))

        // Crimson Stem -> Crimson Sap
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR, HTMachineTier.ADVANCED)
            .itemInput(ItemTags.CRIMSON_STEMS)
            .catalyst(ItemTags.CRIMSON_STEMS)
            .itemOutput(RagiumItems.Dusts.WOOD, 4)
            .fluidOutput(RagiumFluids.CRIMSON_SAP)
            .savePrefixed(output, "crimson_")
        // Crimson Sap -> Crimson Crystal
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER, HTMachineTier.ADVANCED)
            .fluidInput(RagiumFluids.CRIMSON_SAP, FluidType.BUCKET_VOLUME * 4)
            .catalyst(RagiumItems.Circuits.BASIC)
            .itemOutput(RagiumItems.CRIMSON_CRYSTAL)
            .fluidOutput(RagiumFluids.SAP, FluidType.BUCKET_VOLUME * 3)
            .save(output, RagiumAPI.id("crimson_sap"))

        // Warped Stem -> Warped Sap
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR, HTMachineTier.ADVANCED)
            .itemInput(ItemTags.WARPED_STEMS)
            .catalyst(ItemTags.WARPED_STEMS)
            .itemOutput(RagiumItems.Dusts.WOOD, 4)
            .fluidOutput(RagiumFluids.WARPED_SAP)
            .savePrefixed(output, "warped_")
        // Warped Sap -> Warped Crystal
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER, HTMachineTier.ADVANCED)
            .fluidInput(RagiumFluids.WARPED_SAP, FluidType.BUCKET_VOLUME * 4)
            .catalyst(RagiumItems.Circuits.BASIC)
            .itemOutput(RagiumItems.WARPED_CRYSTAL)
            .fluidOutput(RagiumFluids.SAP, FluidType.BUCKET_VOLUME * 3)
            .save(output, RagiumAPI.id("warped_sap"))
    }
}
