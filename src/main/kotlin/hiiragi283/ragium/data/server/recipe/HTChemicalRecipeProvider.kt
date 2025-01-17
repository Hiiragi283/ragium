package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType

object HTChemicalRecipeProvider : RecipeProviderChild {
    override fun buildRecipes(output: RecipeOutput) {
        registerNitrogen(output)

        registerAlkali(output)
        registerAluminum(output)
        registerSulfur(output)
        registerChlorine(output)
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
    }

    private fun registerAlkali(output: RecipeOutput) {
        // Ash -> Alkali
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(RagiumItems.Dusts.ASH)
            .itemOutput(RagiumItems.Dusts.ALKALI)
            .saveSuffixed(output, "_from_ash")
        // Alkali + Seed Oil -> Soap
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumItems.Dusts.ALKALI)
            .fluidInput(RagiumFluids.PLANT_OIL)
            .itemOutput(RagiumItems.SOAP, 4)
            .saveSuffixed(output, "_from_seed_oil")
        // Alkali + Tallow -> Soap
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumItems.Dusts.ALKALI)
            .itemInput(RagiumItems.TALLOW)
            .itemOutput(RagiumItems.SOAP, 8)
            .saveSuffixed(output, "_from_tallow")

        // Alkali + H2O -> Alkali Solution
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumItems.Dusts.ALKALI)
            .fluidInput(Tags.Fluids.WATER)
            .fluidOutput(RagiumFluids.ALKALI_SOLUTION)
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
            .fluidInput(RagiumFluids.HYDROGEN_FLUORIDE, FluidType.BUCKET_VOLUME * 6)
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

        // S -> SO2
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(HTTagPrefix.DUST, RagiumMaterialKeys.SULFUR)
            .catalyst(RagiumItems.OXIDIZATION_CATALYST)
            .fluidOutput(RagiumFluids.SULFUR_DIOXIDE)
            .save(output)
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
        // NaCl + H2O -> NaCl(aq)
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumItems.Dusts.SALT)
            .fluidInput(Tags.Fluids.WATER)
            .fluidOutput(RagiumFluids.BRINE)
            .save(output)
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
        // HCl + H2O -> HCl(aq)
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER)
            .fluidInput(RagiumFluids.HYDROGEN_CHLORIDE)
            .fluidInput(Tags.Fluids.WATER)
            .fluidOutput(RagiumFluids.HYDROCHLORIC_ACID)
            .save(output)

        // HNO3 + 3x HCl -> 4x Aqua Regia
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER, HTMachineTier.ADVANCED)
            .fluidInput(RagiumFluids.NITRIC_ACID)
            .fluidInput(RagiumFluids.HYDROCHLORIC_ACID, FluidType.BUCKET_VOLUME * 3)
            .fluidOutput(RagiumFluids.AQUA_REGIA, FluidType.BUCKET_VOLUME * 4)
            .save(output)
    }
}
