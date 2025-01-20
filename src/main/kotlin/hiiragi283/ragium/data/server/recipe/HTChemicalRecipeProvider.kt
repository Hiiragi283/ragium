package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialProvider
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType

object HTChemicalRecipeProvider : RecipeProviderChild {
    override fun buildRecipes(output: RecipeOutput) {
        registerCarbon(output)
        registerNitrogen(output)
        registerFluorine(output)

        registerAlkali(output)
        registerAluminum(output)
        registerSulfur(output)
        registerChlorine(output)
    }

    private fun solidSolution(
        output: RecipeOutput,
        solid: HTMaterialProvider,
        solution: RagiumFluids,
        tier: HTMachineTier = HTMachineTier.BASIC,
    ) {
        // Gas -> Solution
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER, tier)
            .itemInput(solid)
            .fluidInput(Tags.Fluids.WATER)
            .fluidOutput(solution)
            .save(output)
        // Solution -> Gas
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR, tier)
            .fluidInput(solution)
            .itemOutput(solid)
            .fluidOutput(Fluids.WATER)
            .save(output, RecipeBuilder.getDefaultRecipeId(solid).withSuffix("_from_solution"))
    }

    private fun gasSolution(
        output: RecipeOutput,
        gas: RagiumFluids,
        solution: RagiumFluids,
        tier: HTMachineTier = HTMachineTier.BASIC,
    ) {
        // Gas -> Solution
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER, tier)
            .fluidInput(gas)
            .fluidInput(Tags.Fluids.WATER)
            .fluidOutput(solution)
            .save(output)
        // Solution -> Gas
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR, tier)
            .fluidInput(solution)
            .fluidOutput(gas)
            .fluidOutput(Fluids.WATER)
            .save(output, gas.id.withSuffix("_from_solution"))
    }

    private fun solidRedox(output: RecipeOutput, solid: HTMaterialProvider, oxide: RagiumFluids) {
        // Oxidize
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(solid)
            .catalyst(RagiumItems.OXIDIZATION_CATALYST)
            .fluidOutput(oxide)
            .saveSuffixed(output, "_by_oxidization")
        // Reduction
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ELITE)
            .fluidInput(oxide)
            .catalyst(RagiumItems.REDUCTION_CATALYST)
            .itemOutput(solid)
            .saveSuffixed(output, "_by_reduction")
    }

    private fun registerCarbon(output: RecipeOutput) {
        // Coal -> Carbon Dust
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(ItemTags.COALS)
            .itemOutput(RagiumItems.Dusts.CARBON)
            .saveSuffixed(output, "_from_coals")
        // C <-> CO2
        solidRedox(output, RagiumItems.Dusts.CARBON, RagiumFluids.CARBON_DIOXIDE)
    }

    private fun registerNitrogen(output: RecipeOutput) {
        // 2x KNO3 + H2SO4 -> 2x HNO3 + K2SO4
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(HTTagPrefix.DUST, RagiumMaterialKeys.NITER)
            .fluidInput(RagiumFluids.SULFURIC_ACID)
            .itemOutput(RagiumItems.Dusts.ALKALI, 2)
            .fluidOutput(RagiumFluids.NITRIC_ACID)
            .save(output, RagiumAPI.id("nitric_acid_with_sulfuric"))

        // N2 + 3x H2 -> 2x NH3
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .fluidInput(RagiumFluids.NITROGEN)
            .fluidInput(RagiumFluids.HYDROGEN, FluidType.BUCKET_VOLUME * 3)
            .catalyst(HTTagPrefix.DUST, RagiumMaterialKeys.IRON)
            .fluidOutput(RagiumFluids.AMMONIA, FluidType.BUCKET_VOLUME * 2)
            .save(output)
        // NH3 + 2x O2 -> HNO3 + H2O
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .fluidInput(RagiumFluids.AMMONIA)
            .catalyst(RagiumItems.OXIDIZATION_CATALYST)
            .fluidOutput(RagiumFluids.NITRIC_ACID)
            .fluidOutput(RagiumFluids.HYDROGEN, FluidType.BUCKET_VOLUME * 4)
            .save(output)

        // HNO3 + H2SO4 -> Mixture Acid
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER, HTMachineTier.ADVANCED)
            .fluidInput(RagiumFluids.NITRIC_ACID)
            .fluidInput(RagiumFluids.SULFURIC_ACID)
            .fluidOutput(RagiumFluids.MIXTURE_ACID, FluidType.BUCKET_VOLUME * 2)
            .save(output)
        // Nitration
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER, HTMachineTier.ADVANCED)
            .fluidInput(RagiumFluids.FUEL)
            .fluidInput(RagiumFluids.MIXTURE_ACID, FluidType.BUCKET_VOLUME / 10)
            .fluidOutput(RagiumFluids.NITRO_FUEL)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER, HTMachineTier.ADVANCED)
            .itemInput(Tags.Items.STRINGS)
            .itemInput(Items.PAPER)
            .fluidInput(RagiumFluids.GLYCEROL)
            .fluidInput(RagiumFluids.MIXTURE_ACID)
        // .itemOutput()

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER, HTMachineTier.ADVANCED)
            .itemInput(Tags.Items.SANDS)
            .fluidInput(RagiumFluids.AROMATIC_COMPOUNDS)
            .fluidInput(RagiumFluids.MIXTURE_ACID)
            .itemOutput(Items.TNT, 16)
            .save(output)
    }

    private fun registerFluorine(output: RecipeOutput) {
        // Glowstone -> 4x CaF2
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Items.GLOWSTONE)
            .itemOutput(RagiumItems.RawResources.FLUORITE, 4)
            .itemOutput(Items.GOLD_NUGGET)
            .saveSuffixed(output, "_from_glowstone")
        // Sea Lantern -> 4x CaF2
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Items.SEA_LANTERN)
            .itemOutput(RagiumItems.RawResources.FLUORITE, 6)
            .saveSuffixed(output, "_from_sea_lantern")

        // CaF2 + H2SO4 -> CaSO4 + 2x HF
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ELITE)
            .itemInput(RagiumItems.RawResources.FLUORITE)
            .fluidInput(RagiumFluids.SULFURIC_ACID)
            .fluidOutput(RagiumFluids.HYDROGEN_FLUORIDE)
            .save(output)
        // HF + H2O <-> HF(aq)
        gasSolution(output, RagiumFluids.HYDROGEN_FLUORIDE, RagiumFluids.HYDROFLUORIC_ACID, HTMachineTier.ELITE)
    }

    private fun registerAlkali(output: RecipeOutput) {
        // Ash -> Alkali
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(RagiumItems.Dusts.ASH)
            .itemOutput(RagiumItems.Dusts.CARBON)
            .itemOutput(RagiumItems.Dusts.ALKALI)
            .saveSuffixed(output, "_from_ash")
        // Alkali + Seed Oil -> Soap
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumItems.Dusts.ALKALI)
            .fluidInput(RagiumFluids.PLANT_OIL)
            .itemOutput(RagiumItems.SOAP, 4)
            .fluidOutput(RagiumFluids.GLYCEROL)
            .saveSuffixed(output, "_from_seed_oil")
        // Alkali + Tallow -> Soap
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumItems.Dusts.ALKALI)
            .itemInput(RagiumItems.TALLOW)
            .itemOutput(RagiumItems.SOAP, 8)
            .fluidOutput(RagiumFluids.GLYCEROL)
            .saveSuffixed(output, "_from_tallow")

        // Alkali + H2O <-> Alkali Solution
        solidSolution(output, RagiumItems.Dusts.ALKALI, RagiumFluids.ALKALI_SOLUTION)
        // Alkali Solution + SiO2 -> Sodium Silicate
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .itemInput(RagiumItems.Dusts.QUARTZ)
            .fluidInput(RagiumFluids.ALKALI_SOLUTION, FluidType.BUCKET_VOLUME * 2)
            .catalyst(RagiumItems.HEATING_CATALYST)
            .fluidOutput(RagiumFluids.SODIUM_SILICATE)
            .save(output)
        // Sodium Silicate -> Sponge
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE)
            .fluidInput(RagiumFluids.SODIUM_SILICATE)
            .itemOutput(Items.SPONGE)
            .save(output)
    }

    private fun registerAluminum(output: RecipeOutput) {
        // 8x Netherrack -> 6x Bauxite + 2x Sulfur
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER, HTMachineTier.ADVANCED)
            .itemInput(Items.NETHERRACK, 8)
            .itemOutput(RagiumItems.Dusts.BAUXITE, 6)
            .itemOutput(RagiumItems.Dusts.SULFUR, 2)
            .save(output)
        // Bauxite + Alkali solution -> Alumina Solution
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumItems.Dusts.BAUXITE)
            .fluidInput(RagiumFluids.ALKALI_SOLUTION)
            .fluidOutput(RagiumFluids.ALUMINA_SOLUTION)
            .save(output)
        // Alumina Solution + 4x Coal -> Aluminum Ingot
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.ADVANCED)
            .itemInput(ItemTags.COALS, 4)
            .fluidInput(RagiumFluids.ALUMINA_SOLUTION)
            .itemOutput(RagiumItems.Ingots.ALUMINUM)
            .itemOutput(RagiumItems.SLAG, 2)
            .saveSuffixed(output, "_with_coal")

        // 3x Na + Al + 6x HF -> Cryolite + 3x H2
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ELITE)
            .itemInput(RagiumItems.Dusts.ALKALI, 3)
            .itemInput(RagiumItems.Dusts.ALUMINUM)
            .fluidInput(RagiumFluids.HYDROFLUORIC_ACID, FluidType.BUCKET_VOLUME * 6)
            .itemOutput(RagiumItems.RawResources.CRYOLITE)
            .fluidOutput(RagiumFluids.HYDROGEN, FluidType.BUCKET_VOLUME * 3)
            .save(output)
        // Alumina Solution + Cryolite -> 3x Aluminum Ingot
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.ELITE)
            .itemInput(RagiumItems.RawResources.CRYOLITE)
            .fluidInput(RagiumFluids.ALUMINA_SOLUTION)
            .itemOutput(RagiumItems.Ingots.ALUMINUM, 3)
            .itemOutput(RagiumItems.SLAG)
            .saveSuffixed(output, "_with_cryolite")
    }

    private fun registerSulfur(output: RecipeOutput) {
        // Gunpowder -> Sulfur
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Tags.Items.GUNPOWDERS)
            .itemOutput(RagiumItems.Dusts.SULFUR)
            .save(output)

        // S <-> SO2
        solidRedox(output, RagiumItems.Dusts.SULFUR, RagiumFluids.SULFUR_DIOXIDE)
        // SO2 -> H2SO4
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.SULFUR_DIOXIDE)
            .fluidInput(Tags.Fluids.WATER)
            .catalyst(RagiumItems.OXIDIZATION_CATALYST)
            .fluidOutput(RagiumFluids.SULFURIC_ACID)
            .save(output)
    }

    private fun registerChlorine(output: RecipeOutput) {
        // NaCl + H2O <-> NaCl(aq)
        solidSolution(output, RagiumItems.Dusts.SALT, RagiumFluids.BRINE)
        // 2x NaCl(aq) -> H2 + Cl2 + 2x NaOH
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .fluidInput(RagiumFluids.BRINE, FluidType.BUCKET_VOLUME * 2)
            .itemOutput(RagiumItems.Dusts.ALKALI, 2)
            .fluidOutput(RagiumFluids.HYDROGEN)
            .fluidOutput(RagiumFluids.CHLORINE)
            .saveSuffixed(output, "_from_brine")
        // 2x NaCl -> Cl2 + 2x Na
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR, HTMachineTier.ELITE)
            .itemInput(RagiumItems.Dusts.SALT, 2)
            .itemOutput(RagiumItems.Dusts.ALKALI, 2)
            .fluidOutput(RagiumFluids.CHLORINE)
            .saveSuffixed(output, "_from_salt")

        // H2 + Cl2 -> 2x HCl
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.HYDROGEN)
            .fluidInput(RagiumFluids.CHLORINE)
            .fluidOutput(RagiumFluids.HYDROGEN_CHLORIDE, FluidType.BUCKET_VOLUME * 2)
            .save(output)
        // HCl + H2O <-> HCl(aq)
        gasSolution(output, RagiumFluids.HYDROGEN_CHLORIDE, RagiumFluids.HYDROCHLORIC_ACID, HTMachineTier.ELITE)

        // HNO3 + 3x HCl -> 4x Aqua Regia
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER, HTMachineTier.ADVANCED)
            .fluidInput(RagiumFluids.NITRIC_ACID)
            .fluidInput(RagiumFluids.HYDROCHLORIC_ACID, FluidType.BUCKET_VOLUME * 3)
            .fluidOutput(RagiumFluids.AQUA_REGIA, FluidType.BUCKET_VOLUME * 4)
            .save(output)
    }

    private fun registerUranium(output: RecipeOutput) {
        // Poisonous Potato + H2SO4 -> Yellow Cake
    }
}
