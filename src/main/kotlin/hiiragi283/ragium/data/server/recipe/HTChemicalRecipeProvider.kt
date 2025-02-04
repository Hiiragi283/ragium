package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.*
import hiiragi283.ragium.api.extension.catalyst
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.extension.sources
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumBlockTags
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumRecipes
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType

object HTChemicalRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerCarbon(output)
        registerNitrogen(output)
        registerFluorine(output)

        registerAlkali(output)
        registerAluminum(output)
        registerSludge(output)
        registerSulfur(output)

        registerUranium(output)
        registerPlutonium(output)
    }

    private fun solidSolution(
        output: RecipeOutput,
        item: ItemLike,
        tagKey: TagKey<Item>,
        solution: RagiumFluids,
    ) {
        // Gas -> Solution
        HTInfuserRecipeBuilder()
            .itemInput(tagKey)
            .waterInput()
            .fluidOutput(solution)
            .save(output)
    }

    private fun gasSolution(output: RecipeOutput, gas: RagiumFluids, solution: RagiumFluids) {
        // Gas -> Solution
        HTMixerRecipeBuilder()
            .fluidInput(gas)
            .waterInput()
            .fluidOutput(solution)
            .save(output)
    }

    private fun solidRedox(output: RecipeOutput, material: HTMaterialKey, oxide: RagiumFluids) {
        // Oxidize
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR)
            .itemInput(HTTagPrefix.DUST, material)
            .sources(RagiumBlockTags.HEATING_SOURCES)
            .fluidOutput(oxide)
            .saveSuffixed(output, "_by_oxidization")
        // Reduction
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR)
            .fluidInput(oxide)
            .catalyst(Items.HEART_OF_THE_SEA)
            .itemOutput(HTTagPrefix.DUST, material)
            .saveSuffixed(output, "_by_reduction")
    }

    private fun registerCarbon(output: RecipeOutput) {
        // Coal -> Carbon Dust
        HTExtractorRecipeBuilder()
            .itemInput(ItemTags.COALS)
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.CARBON)
            .saveSuffixed(output, "_from_coals")
        // C <-> CO2
        solidRedox(output, CommonMaterials.CARBON, RagiumFluids.CARBON_DIOXIDE)

        // Steam Reforming
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.METHANE)
            .waterInput()
            .sources(RagiumBlockTags.HEATING_SOURCES)
            .fluidOutput(RagiumFluids.HYDROGEN)
            .fluidOutput(RagiumFluids.CARBON_MONOXIDE)
            .save(output, RagiumAPI.id("steam_reforming"))
        // Water Gas Shift
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.CARBON_MONOXIDE)
            .waterInput()
            .sources(RagiumBlockTags.HEATING_SOURCES)
            .fluidOutput(RagiumFluids.HYDROGEN)
            .fluidOutput(RagiumFluids.CARBON_DIOXIDE)
            .save(output, RagiumAPI.id("water_gas_shift"))

        // C2H5OH + H2SO4 -> C2H4 + H2O
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.ETHANOL)
            .catalyst(RagiumItems.BLAZE_REAGENT)
            .fluidOutput(RagiumFluids.ETHENE)
            .waterOutput()
            .save(output)
        // C2H4 -> C2H2
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.ETHENE)
            .catalyst(Items.BLAZE_POWDER)
            .fluidOutput(RagiumFluids.ACETYLENE)
            .saveSuffixed(output, "_from_ethylene")

        // CaO + 3C -> CaC2 + CO
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR)
            .itemInput(RagiumItems.ALKALI_REAGENT)
            .itemInput(HTTagPrefix.DUST, CommonMaterials.CARBON, 3)
            .sources(RagiumBlockTags.HEATING_SOURCES)
            .itemOutput(RagiumItems.CALCIUM_CARBIDE)
            .fluidOutput(RagiumFluids.CARBON_MONOXIDE)
            .save(output)
        // CaC2 + H2O -> C2H2 + CaO
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR)
            .itemInput(RagiumItems.CALCIUM_CARBIDE)
            .waterInput()
            .itemOutput(RagiumItems.ALKALI_REAGENT)
            .fluidOutput(RagiumFluids.ACETYLENE)
            .save(output, RagiumAPI.id("acetylene_from_carbide"))
    }

    private fun registerNitrogen(output: RecipeOutput) {
        // Air
        /*HTExtractorRecipeBuilder()
            .fluidOutput(RagiumFluids.AIR)
            .save(output)
        // Air -> Nitrogen
        HTMachineRecipeBuilder
            .create(RagiumRecipes.EXTRACTOR)
            .fluidInput(RagiumFluids.AIR)
            .fluidOutput(RagiumFluids.NITROGEN, 800)
            .save(output)
        // N2 -> N2 liq.
        HTMachineRecipeBuilder
            .create(RagiumRecipes.EXTRACTOR)
            .fluidInput(RagiumFluids.NITROGEN)
            .sources(RagiumBlockTags.COOLING_SOURCES)
            .fluidOutput(RagiumFluids.LIQUID_NITROGEN, 10)
            .save(output)*/

        // Sandstone -> KNO3
        HTGrinderRecipeBuilder()
            .itemInput(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            .itemOutput(Items.SAND, 4)
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.SALTPETER)
            .setChance(0.25f)
            .save(output)
        // 2x KNO3 + H2SO4 -> 2x HNO3 + K2SO4
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR)
            .itemInput(HTTagPrefix.DUST, CommonMaterials.SALTPETER)
            .fluidInput(RagiumFluids.SULFURIC_ACID)
            .itemOutput(RagiumItems.ALKALI_REAGENT, 2)
            .fluidOutput(RagiumFluids.NITRIC_ACID)
            .save(output, RagiumAPI.id("nitric_acid_with_sulfuric"))

        // N2 + 3x H2 -> 2x NH3
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.NITROGEN)
            .fluidInput(RagiumFluids.HYDROGEN, FluidType.BUCKET_VOLUME * 3)
            .catalyst(HTTagPrefix.DUST, VanillaMaterials.IRON)
            .fluidOutput(RagiumFluids.AMMONIA, FluidType.BUCKET_VOLUME * 2)
            .save(output)
        // NH3 + 2x O2 -> HNO3 + H2O
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .fluidInput(RagiumFluids.AMMONIA)
            .fluidOutput(RagiumFluids.NITRIC_ACID)
            .fluidOutput(RagiumFluids.HYDROGEN, FluidType.BUCKET_VOLUME * 4)
            .save(output)

        // HNO3 + H2SO4 -> Mixture Acid
        HTMixerRecipeBuilder()
            .fluidInput(RagiumFluids.NITRIC_ACID)
            .fluidInput(RagiumFluids.SULFURIC_ACID)
            .fluidOutput(RagiumFluids.MIXTURE_ACID, FluidType.BUCKET_VOLUME * 2)
            .save(output)
        // Nitration
        HTMixerRecipeBuilder()
            .fluidInput(RagiumFluidTags.NON_NITRO_FUEL)
            .fluidInput(RagiumFluids.MIXTURE_ACID, FluidType.BUCKET_VOLUME / 10)
            .fluidOutput(RagiumFluids.NITRO_FUEL)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .itemInput(Tags.Items.STRINGS, 4)
            .itemInput(Items.PAPER, 4)
            .fluidInput(RagiumFluids.GLYCEROL)
            .fluidInput(RagiumFluids.MIXTURE_ACID)
            .itemOutput(RagiumItems.DYNAMITE, 8)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumRecipes.ASSEMBLER, HTMachineTier.ADVANCED)
            .itemInput(Tags.Items.SANDS)
            .itemInput(RagiumItems.CREEPER_REAGENT)
            .itemOutput(Items.TNT)
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
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR, HTMachineTier.ELITE)
            .itemInput(HTTagPrefix.GEM, CommonMaterials.FLUORITE)
            .fluidInput(RagiumFluids.SULFURIC_ACID)
            .fluidOutput(RagiumFluids.HYDROGEN_FLUORIDE)
            .save(output)
        // HF + H2O <-> HF(aq)
        gasSolution(output, RagiumFluids.HYDROGEN_FLUORIDE, RagiumFluids.HYDROFLUORIC_ACID)
    }

    private fun registerAlkali(output: RecipeOutput) {
        // Ash -> Alkali
        HTExtractorRecipeBuilder()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ASH)
            .itemOutput(RagiumItems.ALKALI_REAGENT)
            .saveSuffixed(output, "_from_ash")
        // Alkali + Seed Oil -> Soap
        HTInfuserRecipeBuilder()
            .itemInput(RagiumItems.ALKALI_REAGENT)
            .fluidInput(RagiumFluids.PLANT_OIL)
            .itemOutput(RagiumItems.SOAP, 4)
            .save(output)

        // Alkali + H2O <-> Alkali Solution
        solidSolution(output, RagiumItems.ALKALI_REAGENT, RagiumItemTags.ALKALI_REAGENTS, RagiumFluids.ALKALI_SOLUTION)
        // Alkali Solution + SiO2 -> Sodium Silicate
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.QUARTZ)
            .fluidInput(RagiumFluids.ALKALI_SOLUTION, FluidType.BUCKET_VOLUME * 2)
            .sources(RagiumBlockTags.HEATING_SOURCES)
            .fluidOutput(RagiumFluids.SODIUM_SILICATE)
            .save(output)
        // Sodium Silicate -> Sponge
        HTMachineRecipeBuilder
            .create(RagiumRecipes.BLAST_FURNACE)
            .fluidInput(RagiumFluids.SODIUM_SILICATE)
            .itemOutput(Items.SPONGE)
            .save(output)
    }

    private fun registerAluminum(output: RecipeOutput) {
        // 8x Netherrack -> 6x Bauxite + 2x Sulfur
        HTGrinderRecipeBuilder()
            .itemInput(Items.NETHERRACK, 8)
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.BAUXITE, 4)
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.BAUXITE, 2)
            .setChance(0.5f)
            .save(output)
        // Bauxite + Alkali solution -> Alumina Solution
        HTInfuserRecipeBuilder()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.BAUXITE)
            .fluidInput(RagiumFluids.ALKALI_SOLUTION)
            .fluidOutput(RagiumFluids.ALUMINA_SOLUTION)
            .save(output)
        // Alumina Solution + 4x Coal -> Aluminum Ingot
        HTMachineRecipeBuilder
            .create(RagiumRecipes.BLAST_FURNACE, HTMachineTier.ADVANCED)
            .itemInput(ItemTags.COALS, 4)
            .fluidInput(RagiumFluids.ALUMINA_SOLUTION)
            .itemOutput(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM)
            .itemOutput(HTTagPrefix.GEM, RagiumMaterials.SLAG, 2)
            .saveSuffixed(output, "_with_coal")

        // 3x Na + Al + 6x HF -> Cryolite + 3x H2
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR, HTMachineTier.ELITE)
            .itemInput(RagiumItems.ALKALI_REAGENT, 3)
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ALUMINUM)
            .fluidInput(RagiumFluids.HYDROFLUORIC_ACID, FluidType.BUCKET_VOLUME * 6)
            .itemOutput(HTTagPrefix.GEM, CommonMaterials.CRYOLITE)
            .fluidOutput(RagiumFluids.HYDROGEN, FluidType.BUCKET_VOLUME * 3)
            .save(output)
        // Alumina Solution + Cryolite -> 3x Aluminum Ingot
        HTMachineRecipeBuilder
            .create(RagiumRecipes.BLAST_FURNACE, HTMachineTier.ELITE)
            .itemInput(HTTagPrefix.GEM, CommonMaterials.CRYOLITE)
            .fluidInput(RagiumFluids.ALUMINA_SOLUTION)
            .itemOutput(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM, 3)
            .itemOutput(HTTagPrefix.GEM, RagiumMaterials.SLAG)
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

        // Chemical Sludge -> Sand + Clay + Gold
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.CHEMICAL_SLUDGE)
            .waterInput(FluidType.BUCKET_VOLUME * 4)
            .itemOutput(Items.SAND)
            .itemOutput(Items.CLAY)
            .itemOutput(Items.GOLD_NUGGET)
            .saveSuffixed(output, "_from_sludge")
        // Chemical Sludge -> Slag
        HTMachineRecipeBuilder
            .create(RagiumRecipes.BLAST_FURNACE)
            .fluidInput(RagiumFluids.CHEMICAL_SLUDGE)
            .itemOutput(HTTagPrefix.GEM, RagiumMaterials.SLAG, 4)
            .save(output)
    }

    private fun registerSulfur(output: RecipeOutput) {
        // Blaze Powder -> Blaze Reagent
        HTExtractorRecipeBuilder()
            .itemInput(Items.BLAZE_POWDER)
            .itemOutput(RagiumItems.BLAZE_REAGENT)
            .save(output)
        // Blaze Reagent -> Blaze Acid
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR)
            .itemInput(RagiumItems.BLAZE_REAGENT)
            .waterInput()
            .fluidOutput(RagiumFluids.SULFURIC_ACID)
            .save(output)
    }

    private fun registerUranium(output: RecipeOutput) {
        // Poisonous Potato + H2SO4 -> Yellow Cake
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR, HTMachineTier.ELITE)
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
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR, HTMachineTier.ELITE)
            .itemInput(RagiumItems.YELLOW_CAKE_PIECE, 16)
            .fluidInput(RagiumFluids.HYDROFLUORIC_ACID, FluidType.BUCKET_VOLUME * 8)
            .itemOutput(RagiumItems.URANIUM_FUEL)
            .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, FluidType.BUCKET_VOLUME * 12)
            .save(output)
    }

    private fun registerPlutonium(output: RecipeOutput) {
    }
}
