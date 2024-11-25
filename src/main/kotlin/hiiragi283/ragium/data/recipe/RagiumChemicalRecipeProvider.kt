package hiiragi283.ragium.data.recipe

import hiiragi283.ragium.api.data.HTMachineRecipeJsonBuilder
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.tags.RagiumFluidTags
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.fluid.Fluids
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture

class RagiumChemicalRecipeProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricRecipeProvider(output, registriesFuture) {
    override fun getName(): String = "Recipes/Chemical"

    override fun generate(exporter: RecipeExporter) {
        chemicalReactor(exporter)
        distillation(exporter)
        electrolyzer(exporter)
        mixer(exporter)
    }

    //    Chemical Reactor    //

    private fun chemicalReactor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(Items.DEEPSLATE, 16)
            .fluidInput(RagiumFluids.AQUA_REGIA)
            .itemOutput(RagiumItems.DEEPANT)
            .offerTo(exporter, RagiumItems.DEEPANT)
        // uranium enrichment
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .itemInput(Items.POISONOUS_POTATO, 8)
            .fluidInput(RagiumFluids.SULFURIC_ACID, FluidConstants.BUCKET * 4)
            .itemOutput(RagiumItems.YELLOW_CAKE)
            .offerTo(exporter, RagiumItems.YELLOW_CAKE)
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .itemInput(RagiumItems.YELLOW_CAKE)
            .fluidInput(RagiumFluids.HYDROGEN_FLUORIDE, FluidConstants.BUCKET * 6)
            .fluidOutput(RagiumFluids.URANIUM_HEXAFLUORIDE)
            .offerTo(exporter, RagiumFluids.URANIUM_HEXAFLUORIDE)
        // silicon refining
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(RagiumItemTags.SILICON_PLATES)
            .fluidInput(RagiumFluids.HYDROGEN_CHLORIDE, FluidConstants.BUCKET * 4)
            .fluidOutput(RagiumFluids.CHLOROSILANE)
            .fluidOutput(RagiumFluids.HYDROGEN, FluidConstants.BUCKET * 2)
            .offerTo(exporter, RagiumFluids.CHLOROSILANE)
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.CHLOROSILANE)
            .fluidInput(RagiumFluids.HYDROGEN, FluidConstants.BUCKET * 2)
            .itemOutput(RagiumItems.REFINED_SILICON)
            .fluidOutput(RagiumFluids.HYDROGEN_CHLORIDE, FluidConstants.BUCKET * 4)
            .offerTo(exporter, RagiumItems.REFINED_SILICON)
        // redstone
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(ItemTags.REDSTONE_ORES)
            .fluidInput(RagiumFluids.HYDROCHLORIC_ACID, FluidConstants.INGOT)
            .itemOutput(Items.REDSTONE, 12)
            .offerTo(exporter, Items.REDSTONE, "_3x")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(ItemTags.REDSTONE_ORES)
            .fluidInput(RagiumFluids.SULFURIC_ACID, FluidConstants.INGOT)
            .itemOutput(Items.REDSTONE, 16)
            .offerTo(exporter, Items.REDSTONE, "_4x")
        // netherite scrap
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(ConventionalItemTags.NETHERITE_SCRAP_ORES)
            .fluidInput(RagiumFluids.HYDROCHLORIC_ACID, FluidConstants.INGOT)
            .itemOutput(Items.NETHERITE_SCRAP, 3)
            .offerTo(exporter, Items.NETHERITE_SCRAP, "_3x")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(ConventionalItemTags.NETHERITE_SCRAP_ORES)
            .fluidInput(RagiumFluids.SULFURIC_ACID, FluidConstants.INGOT)
            .itemOutput(Items.NETHERITE_SCRAP, 4)
            .offerTo(exporter, Items.NETHERITE_SCRAP, "_4x")
        // steam reforming
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.REFINED_GAS)
            .fluidInput(Fluids.WATER)
            .catalyst(RagiumItems.BLAZING_CARBON_ELECTRODE)
            .fluidOutput(RagiumFluids.HYDROGEN)
            .fluidOutput(RagiumFluids.CARBON_MONOXIDE)
            .offerTo(exporter, RagiumFluids.CARBON_MONOXIDE)
        // water gas shift
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.CARBON_MONOXIDE)
            .fluidInput(Fluids.WATER)
            .catalyst(RagiumItems.BLAZING_CARBON_ELECTRODE)
            .fluidOutput(RagiumFluids.HYDROGEN)
            .fluidOutput(RagiumFluids.CARBON_DIOXIDE)
            .offerTo(exporter, RagiumFluids.CARBON_DIOXIDE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.REFINED_GAS, FluidConstants.BUCKET * 2)
            .fluidInput(Fluids.WATER, FluidConstants.BUCKET * 2)
            .catalyst(Items.HEART_OF_THE_SEA)
            .fluidOutput(RagiumFluids.HYDROGEN)
            .fluidOutput(RagiumFluids.ALCOHOL, FluidConstants.BUCKET * 2)
            .offerTo(exporter, RagiumFluids.ALCOHOL)
        // nitrogen
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.BASIC)
            .fluidInput(RagiumFluids.NITROGEN)
            .fluidInput(Fluids.WATER)
            .fluidOutput(RagiumFluids.NITRIC_ACID)
            .offerTo(exporter, RagiumFluids.NITRIC_ACID, "_from_nitrogen")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.BASIC)
            .itemInput(RagiumContents.Dusts.NITER)
            .fluidInput(Fluids.WATER)
            .fluidOutput(RagiumFluids.NITRIC_ACID)
            .offerTo(exporter, RagiumFluids.NITRIC_ACID, "_from_niter")
        // fluorine
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(RagiumContents.Gems.FLUORITE)
            .fluidInput(RagiumFluids.SULFURIC_ACID)
            .itemOutput(RagiumBlocks.GYPSUM)
            .fluidOutput(RagiumFluids.HYDROGEN_FLUORIDE, FluidConstants.BUCKET * 2)
            .offerTo(exporter, RagiumFluids.HYDROGEN_FLUORIDE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(RagiumContents.Dusts.ALUMINUM)
            .fluidInput(RagiumFluids.HYDROGEN_FLUORIDE)
            .fluidInput(RagiumFluids.ALKALI_SOLUTION)
            .itemOutput(RagiumContents.Gems.CRYOLITE, 2)
            .offerTo(exporter, RagiumContents.Gems.CRYOLITE)
        // sulfur
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.BASIC)
            .itemInput(RagiumContents.Dusts.SULFUR)
            .fluidInput(Fluids.WATER)
            .fluidOutput(RagiumFluids.SULFURIC_ACID)
            .offerTo(exporter, RagiumFluids.SULFURIC_ACID)
        // chlorine
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.HYDROGEN)
            .fluidInput(RagiumFluids.CHLORINE)
            .fluidOutput(RagiumFluids.HYDROGEN_CHLORIDE)
            .offerTo(exporter, RagiumFluids.HYDROGEN_CHLORIDE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.HYDROGEN_CHLORIDE)
            .fluidInput(Fluids.WATER)
            .fluidOutput(RagiumFluids.HYDROCHLORIC_ACID)
            .offerTo(exporter, RagiumFluids.HYDROCHLORIC_ACID)
        // aluminum
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(RagiumContents.Dusts.BAUXITE)
            .fluidInput(RagiumFluids.ALKALI_SOLUTION)
            .fluidOutput(RagiumFluids.ALUMINA_SOLUTION)
            .offerTo(exporter, RagiumFluids.ALUMINA_SOLUTION)
        // Fuels
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.ALCOHOL, FluidConstants.BUCKET * 3)
            .fluidInput(RagiumFluidTags.ORGANIC_OILS)
            .fluidOutput(RagiumFluids.BIO_FUEL, FluidConstants.BUCKET * 3)
            .fluidOutput(RagiumFluids.GLYCEROL)
            .offerTo(exporter, RagiumFluids.BIO_FUEL)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluidTags.FUEL)
            .fluidInput(RagiumFluids.MIXTURE_ACID, FluidConstants.BUCKET / 4)
            .fluidOutput(RagiumFluids.NITRO_FUEL, FluidConstants.BUCKET / 4)
            .offerTo(exporter, RagiumFluids.NITRO_FUEL)
        // Dynamite
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.BASIC)
            .fluidInput(RagiumFluids.GLYCEROL)
            .fluidInput(RagiumFluids.MIXTURE_ACID, FluidConstants.BUCKET * 3)
            .fluidOutput(RagiumFluids.NITRO_GLYCERIN)
            .offerTo(exporter, RagiumFluids.NITRO_GLYCERIN)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(Items.PAPER)
            .itemInput(ConventionalItemTags.STRINGS)
            .fluidInput(RagiumFluids.NITRO_GLYCERIN)
            .itemOutput(RagiumItems.DYNAMITE, 2)
            .offerTo(exporter, RagiumItems.DYNAMITE)
        // TNT
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .fluidInput(RagiumFluids.AROMATIC_COMPOUNDS)
            .fluidInput(RagiumFluids.MIXTURE_ACID, FluidConstants.BUCKET * 3)
            .fluidOutput(RagiumFluids.TRINITROTOLUENE)
            .offerTo(exporter, RagiumFluids.TRINITROTOLUENE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .fluidInput(RagiumFluids.TRINITROTOLUENE)
            .itemInput(ItemTags.SAND)
            .itemOutput(Items.TNT, 12)
            .offerTo(exporter, Items.TNT)
        // Plastics
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(RagiumItems.POLYMER_RESIN)
            .itemOutput(RagiumItems.PLASTIC_PLATE)
            .offerTo(exporter, RagiumItems.PLASTIC_PLATE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(RagiumItems.POLYMER_RESIN)
            .fluidInput(RagiumFluids.OXYGEN)
            .itemOutput(RagiumItems.PLASTIC_PLATE, 2)
            .offerTo(exporter, RagiumItems.PLASTIC_PLATE, "_with_oxygen")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(RagiumItems.BASALT_MESH)
            .itemInput(RagiumItems.POLYMER_RESIN, 4)
            .catalyst(RagiumItems.BASALT_MESH)
            .itemOutput(RagiumItems.ENGINEERING_PLASTIC_PLATE)
            .offerTo(exporter, RagiumItems.ENGINEERING_PLASTIC_PLATE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.AROMATIC_COMPOUNDS)
            .fluidInput(RagiumFluids.OXYGEN, FluidConstants.BOTTLE)
            .itemOutput(RagiumItems.ENGINEERING_PLASTIC_PLATE)
            .offerTo(exporter, RagiumItems.ENGINEERING_PLASTIC_PLATE, "_from_aromatic")
        // oxidization
        registerOxidize(exporter, Items.CHISELED_COPPER, Items.OXIDIZED_CHISELED_COPPER)
        registerOxidize(exporter, Items.COPPER_BLOCK, Items.OXIDIZED_COPPER)
        registerOxidize(exporter, Items.COPPER_BULB, Items.OXIDIZED_COPPER_BULB)
        registerOxidize(exporter, Items.COPPER_DOOR, Items.OXIDIZED_COPPER_DOOR)
        registerOxidize(exporter, Items.COPPER_GRATE, Items.OXIDIZED_COPPER_GRATE)
        registerOxidize(exporter, Items.COPPER_TRAPDOOR, Items.OXIDIZED_COPPER_TRAPDOOR)
        registerOxidize(exporter, Items.CUT_COPPER, Items.OXIDIZED_CUT_COPPER)
        registerOxidize(exporter, Items.CUT_COPPER_SLAB, Items.OXIDIZED_CUT_COPPER_SLAB)
        registerOxidize(exporter, Items.CUT_COPPER_STAIRS, Items.OXIDIZED_CUT_COPPER_STAIRS)
    }

    private fun registerOxidize(exporter: RecipeExporter, before: ItemConvertible, after: ItemConvertible) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(before)
            .fluidInput(RagiumFluids.OXYGEN, FluidConstants.INGOT)
            .itemOutput(after)
            .offerTo(exporter, after)
    }

    //    Distillation Tower    //

    private fun distillation(exporter: RecipeExporter) {
        // biomass -> alcohol
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.BIOMASS)
            .fluidOutput(RagiumFluids.ALCOHOL)
            .offerTo(exporter, RagiumFluids.ALCOHOL, "_from_bio")
        // biomass -> bio fuel
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.BIOMASS)
            .catalyst(RagiumContents.Circuits.BASIC)
            .fluidOutput(RagiumFluids.BIO_FUEL)
            .offerTo(exporter, RagiumFluids.BIO_FUEL)

        // crude oil -> refined gas + naphtha + residual oil
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.CRUDE_OIL, FluidConstants.BUCKET * 6)
            .fluidOutput(RagiumFluids.REFINED_GAS, FluidConstants.BUCKET * 2)
            .fluidOutput(RagiumFluids.NAPHTHA, FluidConstants.BUCKET * 3)
            .fluidOutput(RagiumFluids.RESIDUAL_OIL, FluidConstants.BUCKET * 1)
            .offerTo(exporter, RagiumFluids.CRUDE_OIL)

        // refined gas -> alcohol + noble gas
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.REFINED_GAS, FluidConstants.BUCKET * 8)
            .fluidOutput(RagiumFluids.ALCOHOL, FluidConstants.BUCKET * 6)
            .fluidOutput(RagiumFluids.NOBLE_GAS, FluidConstants.BUCKET * 2)
            .offerTo(exporter, RagiumFluids.REFINED_GAS)

        // naphtha -> polymer resin + fuel
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.NAPHTHA, FluidConstants.BUCKET * 8)
            .itemOutput(RagiumItems.POLYMER_RESIN, 4)
            .fluidOutput(RagiumFluids.FUEL, FluidConstants.BUCKET * 4)
            .offerTo(exporter, RagiumFluids.NAPHTHA)

        // residual oil -> fuel + asphalt
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.RESIDUAL_OIL, FluidConstants.BUCKET * 8)
            .catalyst(RagiumContents.Circuits.PRIMITIVE)
            .fluidOutput(RagiumFluids.FUEL, FluidConstants.BUCKET * 3)
            .fluidOutput(RagiumFluids.ASPHALT, FluidConstants.BUCKET * 5)
            .offerTo(exporter, RagiumFluids.ASPHALT)
        // residual oil -> residual coke + fuel
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.RESIDUAL_OIL, FluidConstants.BUCKET * 8)
            .catalyst(RagiumContents.Circuits.BASIC)
            .itemOutput(RagiumItems.RESIDUAL_COKE, 4)
            .fluidOutput(RagiumFluids.FUEL, FluidConstants.BUCKET * 4)
            .offerTo(exporter, RagiumItems.RESIDUAL_COKE)
        // residual oil -> fuel + aromatic compound
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.RESIDUAL_OIL, FluidConstants.BUCKET * 8)
            .catalyst(RagiumContents.Circuits.ADVANCED)
            .fluidOutput(RagiumFluids.FUEL, FluidConstants.BUCKET * 3)
            .fluidOutput(RagiumFluids.AROMATIC_COMPOUNDS, FluidConstants.BUCKET * 5)
            .offerTo(exporter, RagiumFluids.AROMATIC_COMPOUNDS)

        // sap -> refined gas + alcohol
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.SAP, FluidConstants.BUCKET * 2)
            .fluidOutput(RagiumFluids.REFINED_GAS)
            .fluidOutput(RagiumFluids.ALCOHOL)
            .offerTo(exporter, RagiumFluids.SAP)
        // crimson sap -> crimson crystal
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER, HTMachineTier.ADVANCED)
            .fluidInput(RagiumFluids.CRIMSON_SAP, FluidConstants.BUCKET * 4)
            .itemOutput(RagiumItems.CRIMSON_CRYSTAL)
            .fluidOutput(RagiumFluids.SAP, FluidConstants.BUCKET * 3)
            .offerTo(exporter, RagiumItems.CRIMSON_CRYSTAL)
        // warped sap -> warped crystal
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER, HTMachineTier.ADVANCED)
            .fluidInput(RagiumFluids.WARPED_SAP, FluidConstants.BUCKET * 4)
            .itemOutput(RagiumItems.WARPED_CRYSTAL)
            .fluidOutput(RagiumFluids.SAP, FluidConstants.BUCKET * 3)
            .offerTo(exporter, RagiumItems.WARPED_CRYSTAL)
    }

    //    Electrolyzer    //

    private fun electrolyzer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ELECTROLYZER)
            .fluidInput(Fluids.WATER)
            .fluidOutput(RagiumFluids.HYDROGEN, FluidConstants.BUCKET * 2)
            .fluidOutput(RagiumFluids.OXYGEN)
            .offerTo(exporter, Fluids.WATER)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ELECTROLYZER)
            .fluidInput(RagiumFluids.SALT_WATER)
            .itemOutput(RagiumContents.Dusts.ALKALI)
            .fluidOutput(RagiumFluids.HYDROGEN)
            .fluidOutput(RagiumFluids.CHLORINE)
            .offerTo(exporter, RagiumFluids.SALT_WATER)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ELECTROLYZER, HTMachineTier.BASIC)
            .itemInput(RagiumContents.Dusts.SALT)
            .itemOutput(RagiumContents.Dusts.ALKALI)
            .fluidOutput(RagiumFluids.CHLORINE)
            .offerTo(exporter, RagiumContents.Dusts.SALT)
    }

    //    Mixer    //

    private fun mixer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumContents.Dusts.CRUDE_RAGINITE)
            .fluidInput(Fluids.WATER, FluidConstants.BOTTLE)
            .itemOutput(RagiumContents.Dusts.RAGINITE)
            .offerTo(exporter, RagiumContents.Dusts.RAGINITE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumItemTags.ALKALI)
            .fluidInput(RagiumFluidTags.ORGANIC_OILS)
            .itemOutput(RagiumItems.SOAP_INGOT)
            .fluidOutput(RagiumFluids.GLYCEROL)
            .offerTo(exporter, RagiumItems.SOAP_INGOT)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumItems.FLOUR)
            .fluidInput(Fluids.WATER, FluidConstants.BOTTLE)
            .itemOutput(RagiumItems.DOUGH)
            .offerTo(exporter, RagiumItems.DOUGH)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumItems.BUTTER)
            .itemInput(Items.SUGAR)
            .fluidInput(ConventionalFluidTags.MILK)
            .itemOutput(RagiumItems.CARAMEL, 4)
            .offerTo(exporter, RagiumItems.CARAMEL)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(Items.COCOA_BEANS)
            .itemInput(Items.SUGAR)
            .fluidInput(ConventionalFluidTags.MILK)
            .itemOutput(RagiumItems.CHOCOLATE)
            .offerTo(exporter, RagiumItems.CHOCOLATE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumItems.FLOUR)
            .fluidInput(ConventionalFluidTags.MILK)
            .fluidOutput(RagiumFluids.BATTER)
            .offerTo(exporter, RagiumFluids.BATTER)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(Items.GRAVEL)
            .fluidInput(RagiumFluids.ASPHALT)
            .itemOutput(RagiumBlocks.ASPHALT, 4)
            .offerTo(exporter, RagiumBlocks.ASPHALT)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumItemTags.ALKALI)
            .fluidInput(Fluids.WATER)
            .fluidOutput(RagiumFluids.ALKALI_SOLUTION)
            .offerTo(exporter, RagiumFluids.ALKALI_SOLUTION)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumContents.Dusts.SALT)
            .fluidInput(Fluids.WATER)
            .fluidOutput(RagiumFluids.SALT_WATER)
            .offerTo(exporter, RagiumFluids.SALT_WATER)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(ItemTags.DIRT)
            .itemInput(RagiumItems.NUCLEAR_WASTE)
            .itemOutput(RagiumBlocks.MUTATED_SOIL)
            .offerTo(exporter, RagiumBlocks.MUTATED_SOIL)
        // carbon electrodes
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumItems.CARBON_ELECTRODE)
            .itemInput(Items.BLAZE_POWDER, 8)
            .itemOutput(RagiumItems.BLAZING_CARBON_ELECTRODE)
            .offerTo(exporter, RagiumItems.BLAZING_CARBON_ELECTRODE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumItems.CARBON_ELECTRODE)
            .itemInput(Items.WIND_CHARGE, 8)
            .itemOutput(RagiumItems.CHARGED_CARBON_ELECTRODE)
            .offerTo(exporter, RagiumItems.CHARGED_CARBON_ELECTRODE)
        // acids
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MIXER)
            .fluidInput(RagiumFluids.NITRIC_ACID)
            .fluidInput(RagiumFluids.SULFURIC_ACID)
            .fluidOutput(RagiumFluids.MIXTURE_ACID, FluidConstants.BUCKET * 2)
            .offerTo(exporter, RagiumFluids.MIXTURE_ACID)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MIXER)
            .fluidInput(RagiumFluids.NITRIC_ACID, FluidConstants.BUCKET * 3)
            .fluidInput(RagiumFluids.HYDROCHLORIC_ACID)
            .fluidOutput(RagiumFluids.AQUA_REGIA, FluidConstants.BUCKET * 4)
            .offerTo(exporter, RagiumFluids.AQUA_REGIA)
        // paper
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumItems.PULP)
            .fluidInput(Fluids.WATER)
            .itemOutput(Items.PAPER)
            .offerTo(exporter, Items.PAPER)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumItems.PULP)
            .itemInput(RagiumItemTags.ALKALI)
            .fluidInput(Fluids.WATER)
            .itemOutput(Items.PAPER, 2)
            .offerTo(exporter, Items.PAPER, "_from_alkali")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumItems.PULP)
            .fluidInput(RagiumFluids.SULFURIC_ACID)
            .itemOutput(Items.PAPER, 4)
            .offerTo(exporter, Items.PAPER, "_from_acid")
        // breaching
        registerBreaching(exporter, ConventionalItemTags.CONCRETE_POWDERS, Items.WHITE_CONCRETE_POWDER)
        registerBreaching(exporter, ConventionalItemTags.CONCRETES, Items.WHITE_CONCRETE)
        registerBreaching(exporter, ConventionalItemTags.GLASS_BLOCKS, Items.WHITE_STAINED_GLASS)
        registerBreaching(exporter, ConventionalItemTags.GLASS_PANES, Items.WHITE_STAINED_GLASS_PANE)
        registerBreaching(exporter, ConventionalItemTags.GLAZED_TERRACOTTAS, Items.WHITE_GLAZED_TERRACOTTA)
        registerBreaching(exporter, ItemTags.BANNERS, Items.WHITE_BANNER)
        registerBreaching(exporter, ItemTags.BEDS, Items.WHITE_BED)
        registerBreaching(exporter, ItemTags.CANDLES, Items.WHITE_CANDLE)
        registerBreaching(exporter, ItemTags.TERRACOTTA, Items.WHITE_TERRACOTTA)
        registerBreaching(exporter, ItemTags.WOOL, Items.WHITE_WOOL)
        registerBreaching(exporter, ItemTags.WOOL_CARPETS, Items.WHITE_CARPET)
        // concrete forming
        DyeColor.entries.forEach { color: DyeColor ->
            val powder: Item = Registries.ITEM.get(Identifier.of("${color.asString()}_concrete_powder"))
            val concrete: Item = Registries.ITEM.get(Identifier.of("${color.asString()}_concrete"))
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.MIXER)
                .itemInput(powder)
                .fluidInput(Fluids.WATER, FluidConstants.INGOT)
                .itemOutput(concrete)
                .offerTo(exporter, concrete)
        }
    }

    private fun registerBreaching(exporter: RecipeExporter, input: TagKey<Item>, output: Item) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(input)
            .itemInput(RagiumItems.SOAP_INGOT)
            .fluidInput(Fluids.WATER)
            .itemOutput(output)
            .offerTo(exporter, output, "_breaching")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(input)
            .fluidInput(RagiumFluids.CHLORINE)
            .itemOutput(output)
            .offerTo(exporter, output, "_breaching")
    }
}
