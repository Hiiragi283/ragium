package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.extension.catalyst
import hiiragi283.ragium.api.extension.cooling
import hiiragi283.ragium.api.extension.heating
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredItem

object HTChemicalRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerCarbon(output)
        registerNitrogen(output)
        registerFluorine(output)

        registerAlkali(output)
        registerAluminum(output)
        registerSludge(output)
        registerSulfur(output)
        registerChlorine(output)

        registerUranium(output)
        registerPlutonium(output)
    }

    private fun solidSolution(
        output: RecipeOutput,
        material: HTMaterialKey,
        solution: RagiumFluids,
        tier: HTMachineTier = HTMachineTier.BASIC,
    ) {
        val item: DeferredItem<out Item> = RagiumItems.getMaterialItem(HTTagPrefix.DUST, material)
        // Gas -> Solution
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER, tier)
            .itemInput(HTTagPrefix.DUST, material)
            .waterInput()
            .fluidOutput(solution)
            .save(output)
        // Solution -> Gas
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR, tier)
            .fluidInput(solution)
            .itemOutput(item)
            .waterOutput()
            .save(output, RecipeBuilder.getDefaultRecipeId(item).withSuffix("_from_solution"))
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
            .waterInput()
            .fluidOutput(solution)
            .save(output)
        // Solution -> Gas
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR, tier)
            .fluidInput(solution)
            .fluidOutput(gas)
            .waterOutput()
            .save(output, gas.fluidHolder.id.withSuffix("_from_solution"))
    }

    private fun solidRedox(output: RecipeOutput, material: HTMaterialKey, oxide: RagiumFluids) {
        val item: DeferredItem<out Item> = RagiumItems.getMaterialItem(HTTagPrefix.DUST, material)
        // Oxidize
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(HTTagPrefix.DUST, material)
            .heating(HTMachineTier.ADVANCED)
            .fluidOutput(oxide)
            .saveSuffixed(output, "_by_oxidization")
        // Reduction
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(oxide)
            .catalyst(Items.HEART_OF_THE_SEA)
            .itemOutput(item)
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
        solidRedox(output, RagiumMaterialKeys.CARBON, RagiumFluids.CARBON_DIOXIDE)

        // Steam Reforming
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.METHANE)
            .waterInput()
            .heating(HTMachineTier.ELITE)
            .fluidOutput(RagiumFluids.HYDROGEN)
            .fluidOutput(RagiumFluids.CARBON_MONOXIDE)
            .save(output, RagiumAPI.id("steam_reforming"))
        // Water Gas Shift
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.CARBON_MONOXIDE)
            .waterInput()
            .heating(HTMachineTier.ELITE)
            .fluidOutput(RagiumFluids.HYDROGEN)
            .fluidOutput(RagiumFluids.CARBON_DIOXIDE)
            .save(output, RagiumAPI.id("water_gas_shift"))

        // C2H5OH + H2SO4 -> C2H4 + H2O
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.ALCOHOL)
            .catalyst(HTTagPrefix.DUST, RagiumMaterialKeys.SULFUR)
            .fluidOutput(RagiumFluids.ETHENE)
            .waterOutput()
            .save(output)
        // C2H4 -> C2H2
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.ETHENE)
            .catalyst(Items.BLAZE_POWDER)
            .fluidOutput(RagiumFluids.ACETYLENE)
            .saveSuffixed(output, "_from_ethylene")

        // CaO + 3C -> CaC2 + CO
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(HTTagPrefix.DUST, RagiumMaterialKeys.ALKALI)
            .itemInput(HTTagPrefix.DUST, RagiumMaterialKeys.CARBON, 3)
            .heating(HTMachineTier.ADVANCED)
            .itemOutput(RagiumItems.CALCIUM_CARBIDE)
            .fluidOutput(RagiumFluids.CARBON_MONOXIDE)
            .save(output)
        // CaC2 + H2O -> C2H2 + CaO
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(RagiumItems.CALCIUM_CARBIDE)
            .waterInput()
            .itemOutput(RagiumItems.Dusts.ALKALI)
            .fluidOutput(RagiumFluids.ACETYLENE)
            .save(output, RagiumAPI.id("acetylene_from_carbide"))
    }

    private fun registerNitrogen(output: RecipeOutput) {
        // Air
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .catalyst(Items.IRON_BARS)
            .fluidOutput(RagiumFluids.AIR)
            .save(output)
        // Air -> Nitrogen
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .fluidInput(RagiumFluids.AIR)
            .fluidOutput(RagiumFluids.NITROGEN, 800)
            .save(output)
        // N2 -> N2 liq.
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .fluidInput(RagiumFluids.NITROGEN)
            .cooling(HTMachineTier.ELITE)
            .fluidOutput(RagiumFluids.LIQUID_NITROGEN, 10)
            .save(output)

        // Sandstone -> KNO3
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            .itemOutput(Items.SAND, 4)
            .itemOutput(RagiumItems.Dusts.NITER)
            .save(output)
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
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.NITROGEN)
            .fluidInput(RagiumFluids.HYDROGEN, FluidType.BUCKET_VOLUME * 3)
            .catalyst(HTTagPrefix.DUST, RagiumMaterialKeys.IRON)
            .fluidOutput(RagiumFluids.AMMONIA, FluidType.BUCKET_VOLUME * 2)
            .save(output)
        // NH3 + 2x O2 -> HNO3 + H2O
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .fluidInput(RagiumFluids.AMMONIA)
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
            .itemInput(Tags.Items.STRINGS, 4)
            .itemInput(Items.PAPER, 4)
            .fluidInput(RagiumFluids.GLYCEROL)
            .fluidInput(RagiumFluids.MIXTURE_ACID)
            .itemOutput(RagiumItems.DYNAMITE, 8)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER, HTMachineTier.ADVANCED)
            .itemInput(Tags.Items.SANDS)
            .fluidInput(RagiumFluids.AROMATIC_COMPOUNDS)
            .fluidInput(RagiumFluids.MIXTURE_ACID)
            .itemOutput(Items.TNT, 16)
            .save(output)
    }

    private fun registerFluorine(output: RecipeOutput) {
        val fluorite: DeferredItem<out Item> = RagiumItems.getMaterialItem(HTTagPrefix.GEM, RagiumMaterialKeys.FLUORITE)
        // Glowstone -> 4x CaF2
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Items.GLOWSTONE)
            .itemOutput(fluorite, 4)
            .itemOutput(Items.GOLD_NUGGET)
            .saveSuffixed(output, "_from_glowstone")
        // Sea Lantern -> 4x CaF2
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Items.SEA_LANTERN)
            .itemOutput(fluorite, 6)
            .saveSuffixed(output, "_from_sea_lantern")

        // CaF2 + H2SO4 -> CaSO4 + 2x HF
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ELITE)
            .itemInput(HTTagPrefix.GEM, RagiumMaterialKeys.FLUORITE)
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
            .itemInput(HTTagPrefix.DUST, RagiumMaterialKeys.ASH)
            .itemOutput(RagiumItems.Dusts.CARBON)
            .itemOutput(RagiumItems.Dusts.ALKALI)
            .saveSuffixed(output, "_from_ash")
        // Alkali + Seed Oil -> Soap
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(HTTagPrefix.DUST, RagiumMaterialKeys.ALKALI)
            .fluidInput(RagiumFluids.PLANT_OIL)
            .itemOutput(RagiumItems.SOAP, 4)
            .fluidOutput(RagiumFluids.GLYCEROL)
            .saveSuffixed(output, "_from_seed_oil")
        // Alkali + Tallow -> Soap
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(HTTagPrefix.DUST, RagiumMaterialKeys.ALKALI)
            .itemInput(RagiumItems.TALLOW)
            .itemOutput(RagiumItems.SOAP, 8)
            .fluidOutput(RagiumFluids.GLYCEROL)
            .saveSuffixed(output, "_from_tallow")

        // Alkali + H2O <-> Alkali Solution
        solidSolution(output, RagiumMaterialKeys.ALKALI, RagiumFluids.ALKALI_SOLUTION)
        // Alkali Solution + SiO2 -> Sodium Silicate
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(HTTagPrefix.DUST, RagiumMaterialKeys.QUARTZ)
            .fluidInput(RagiumFluids.ALKALI_SOLUTION, FluidType.BUCKET_VOLUME * 2)
            .heating(HTMachineTier.BASIC)
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
            .itemInput(HTTagPrefix.DUST, RagiumMaterialKeys.BAUXITE)
            .fluidInput(RagiumFluids.ALKALI_SOLUTION)
            .fluidOutput(RagiumFluids.ALUMINA_SOLUTION)
            .save(output)
        // Alumina Solution + 4x Coal -> Aluminum Ingot
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.ADVANCED)
            .itemInput(RagiumItemTags.COAL_COKE, 2)
            .fluidInput(RagiumFluids.ALUMINA_SOLUTION)
            .itemOutput(RagiumItems.Ingots.ALUMINUM)
            .itemOutput(RagiumItems.SLAG, 2)
            .saveSuffixed(output, "_with_coal")

        // 3x Na + Al + 6x HF -> Cryolite + 3x H2
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ELITE)
            .itemInput(HTTagPrefix.DUST, RagiumMaterialKeys.ALKALI, 3)
            .itemInput(HTTagPrefix.DUST, RagiumMaterialKeys.ALUMINUM)
            .fluidInput(RagiumFluids.HYDROFLUORIC_ACID, FluidType.BUCKET_VOLUME * 6)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.GEM, RagiumMaterialKeys.CRYOLITE))
            .fluidOutput(RagiumFluids.HYDROGEN, FluidType.BUCKET_VOLUME * 3)
            .save(output)
        // Alumina Solution + Cryolite -> 3x Aluminum Ingot
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.ELITE)
            .itemInput(HTTagPrefix.GEM, RagiumMaterialKeys.CRYOLITE)
            .fluidInput(RagiumFluids.ALUMINA_SOLUTION)
            .itemOutput(RagiumItems.Ingots.ALUMINUM, 3)
            .itemOutput(RagiumItems.SLAG)
            .saveSuffixed(output, "_with_cryolite")
    }

    private fun registerSludge(output: RecipeOutput) {
        // Slag -> Gravel
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(RagiumItems.SLAG)
            .itemOutput(Items.GRAVEL)
            .saveSuffixed(output, "_from_slag")

        // Chemical Sludge -> Sand + Clay + Gold
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER)
            .fluidInput(RagiumFluids.CHEMICAL_SLUDGE)
            .waterInput(FluidType.BUCKET_VOLUME * 4)
            .itemOutput(Items.SAND)
            .itemOutput(Items.CLAY)
            .itemOutput(Items.GOLD_NUGGET)
            .saveSuffixed(output, "_from_sludge")
    }

    private fun registerSulfur(output: RecipeOutput) {
        // Gunpowder -> Sulfur
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Tags.Items.GUNPOWDERS)
            .itemOutput(RagiumItems.Dusts.SULFUR)
            .save(output)

        // S <-> SO2
        solidRedox(output, RagiumMaterialKeys.SULFUR, RagiumFluids.SULFUR_DIOXIDE)
        // SO2 -> H2SO4
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.SULFUR_DIOXIDE)
            .waterInput()
            .catalyst(Items.BLAZE_POWDER)
            .fluidOutput(RagiumFluids.SULFURIC_ACID)
            .save(output)
    }

    private fun registerChlorine(output: RecipeOutput) {
        // NaCl + H2O <-> NaCl(aq)
        solidSolution(output, RagiumMaterialKeys.SALT, RagiumFluids.BRINE)
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
            .itemInput(HTTagPrefix.DUST, RagiumMaterialKeys.SALT, 2)
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
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ELITE)
            .itemInput(Items.POISONOUS_POTATO, 8)
            .fluidInput(RagiumFluids.SULFURIC_ACID, FluidType.BUCKET_VOLUME * 8)
            .itemOutput(RagiumItems.YELLOW_CAKE)
            .save(output)
        // Cutting Yellow Cake
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CUTTING_MACHINE, HTMachineTier.ELITE)
            .itemInput(RagiumItems.YELLOW_CAKE)
            .itemOutput(RagiumItems.YELLOW_CAKE_PIECE, 8)
            .save(output)
        // Uranium Fuel
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ELITE)
            .itemInput(RagiumItems.YELLOW_CAKE_PIECE, 16)
            .fluidInput(RagiumFluids.HYDROFLUORIC_ACID, FluidType.BUCKET_VOLUME * 8)
            .itemOutput(RagiumItems.URANIUM_FUEL)
            .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, FluidType.BUCKET_VOLUME * 12)
            .save(output)
    }

    private fun registerPlutonium(output: RecipeOutput) {
    }
}
