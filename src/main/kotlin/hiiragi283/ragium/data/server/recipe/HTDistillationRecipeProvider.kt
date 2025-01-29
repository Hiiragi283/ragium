package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.extension.catalyst
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.fluids.FluidType

object HTDistillationRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Soul XX -> Crude Oil
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .itemOutput(Items.SAND)
            .fluidOutput(RagiumFluids.CRUDE_OIL, FluidType.BUCKET_VOLUME / 2)
            .save(output, RagiumAPI.id("crude_oil"))
        // Crude Oil -> Refined Gas + Naphtha + Residual Oil
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.CRUDE_OIL, FluidType.BUCKET_VOLUME * 6)
            .catalyst(HTMachineTier.BASIC.getCircuitTag())
            .fluidOutput(RagiumFluids.REFINED_GAS, FluidType.BUCKET_VOLUME * 2)
            .fluidOutput(RagiumFluids.NAPHTHA, FluidType.BUCKET_VOLUME * 3)
            .fluidOutput(RagiumFluids.RESIDUAL_OIL, FluidType.BUCKET_VOLUME * 1)
            .save(output, RagiumAPI.id("crude_oil"))

        // Refined Gas -> Methane + Ethene + Propylene
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.REFINED_GAS, FluidType.BUCKET_VOLUME * 8)
            .catalyst(HTMachineTier.BASIC.getCircuitTag())
            .fluidOutput(RagiumFluids.METHANE, FluidType.BUCKET_VOLUME * 2)
            .fluidOutput(RagiumFluids.ETHENE, FluidType.BUCKET_VOLUME * 4)
            .fluidOutput(RagiumFluids.PROPENE, FluidType.BUCKET_VOLUME * 2)
            .save(output, RagiumAPI.id("refined_gas"))

        // Naphtha -> Fuel
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.NAPHTHA, FluidType.BUCKET_VOLUME * 8)
            .catalyst(HTMachineTier.BASIC.getCircuitTag())
            .fluidOutput(RagiumFluids.FUEL, FluidType.BUCKET_VOLUME * 6)
            .save(output, RagiumAPI.id("naphtha"))

        // Residual Oil -> Fuel + Asphalt
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.RESIDUAL_OIL, FluidType.BUCKET_VOLUME * 8)
            .catalyst(HTMachineTier.BASIC.getCircuitTag())
            .fluidOutput(RagiumFluids.FUEL, FluidType.BUCKET_VOLUME * 3)
            .save(output, RagiumAPI.id("asphalt"))
        // Residual Oil -> Residual Coke + Fuel
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.RESIDUAL_OIL, FluidType.BUCKET_VOLUME * 8)
            .catalyst(HTMachineTier.ADVANCED.getCircuitTag())
            .itemOutput(RagiumItems.RESIDUAL_COKE, 4)
            .fluidOutput(RagiumFluids.FUEL, FluidType.BUCKET_VOLUME * 4)
            .save(output)
        // Residual Oil -> Fuel + Aromatic compound
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.RESIDUAL_OIL, FluidType.BUCKET_VOLUME * 8)
            .catalyst(HTMachineTier.ELITE.getCircuitTag())
            .fluidOutput(RagiumFluids.FUEL, FluidType.BUCKET_VOLUME * 3)
            .fluidOutput(RagiumFluids.AROMATIC_COMPOUNDS, FluidType.BUCKET_VOLUME * 5)
            .save(output, RagiumAPI.id("aromatic_compounds"))

        registerBio(output)
        registerSaps(output)
    }

    private fun registerBio(output: RecipeOutput) {
        // Biomass -> Alcohol
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.BIOMASS)
            .catalyst(HTMachineTier.BASIC.getCircuitTag())
            .fluidOutput(RagiumFluids.ALCOHOL)
            .saveSuffixed(output, "_from_biomass")

        // Biomass -> Bio Fuel
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.BIOMASS)
            .catalyst(HTMachineTier.ADVANCED.getCircuitTag())
            .fluidOutput(RagiumFluids.BIO_FUEL)
            .saveSuffixed(output, "_from_biomass")
        // Alcohol + Plant Oil -> Bio Fuel + Glycerol
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER)
            .fluidInput(RagiumFluids.ALCOHOL, FluidType.BUCKET_VOLUME * 4)
            .fluidInput(RagiumFluids.PLANT_OIL)
            .fluidOutput(RagiumFluids.BIO_FUEL, FluidType.BUCKET_VOLUME * 4)
            .fluidOutput(RagiumFluids.GLYCEROL)
            .saveSuffixed(output, "_from_plant_oil")
        // Alcohol + Tallow -> Bio Fuel + Glycerol
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER)
            .fluidInput(RagiumFluids.ALCOHOL, FluidType.BUCKET_VOLUME * 4)
            .itemInput(RagiumItems.TALLOW)
            .fluidOutput(RagiumFluids.BIO_FUEL, FluidType.BUCKET_VOLUME * 4)
            .fluidOutput(RagiumFluids.GLYCEROL)
            .saveSuffixed(output, "_from_tallow")
    }

    private fun registerSaps(output: RecipeOutput) {
        // XX Log -> Sap + Pulp
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(ItemTags.LOGS_THAT_BURN)
            .itemOutput(RagiumItems.Dusts.WOOD, 4)
            .fluidOutput(RagiumFluids.SAP)
            .save(output)
        // Sap -> Refined Gas + Alcohol
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.SAP, FluidType.BUCKET_VOLUME * 2)
            .catalyst(HTMachineTier.BASIC.getCircuitTag())
            .fluidOutput(RagiumFluids.REFINED_GAS)
            .fluidOutput(RagiumFluids.ALCOHOL)
            .save(output, RagiumAPI.id("sap"))

        // Crimson Stem -> Crimson Sap
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(ItemTags.CRIMSON_STEMS)
            .catalyst(HTMachineTier.ADVANCED.getCircuitTag())
            .itemOutput(RagiumItems.Dusts.WOOD, 4)
            .fluidOutput(RagiumFluids.CRIMSON_SAP)
            .savePrefixed(output, "crimson_")
        // Crimson Sap -> Crimson Crystal
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.CRIMSON_SAP, FluidType.BUCKET_VOLUME * 4)
            .catalyst(HTMachineTier.ADVANCED.getCircuitTag())
            .itemOutput(RagiumItems.CRIMSON_CRYSTAL)
            .fluidOutput(RagiumFluids.SAP, FluidType.BUCKET_VOLUME * 3)
            .save(output, RagiumAPI.id("crimson_sap"))

        // Warped Stem -> Warped Sap
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(ItemTags.WARPED_STEMS)
            .catalyst(HTMachineTier.ADVANCED.getCircuitTag())
            .itemOutput(RagiumItems.Dusts.WOOD, 4)
            .fluidOutput(RagiumFluids.WARPED_SAP)
            .savePrefixed(output, "warped_")
        // Warped Sap -> Warped Crystal
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.WARPED_SAP, FluidType.BUCKET_VOLUME * 4)
            .catalyst(HTMachineTier.ADVANCED.getCircuitTag())
            .itemOutput(RagiumItems.WARPED_CRYSTAL)
            .fluidOutput(RagiumFluids.SAP, FluidType.BUCKET_VOLUME * 3)
            .save(output, RagiumAPI.id("warped_sap"))
    }
}
