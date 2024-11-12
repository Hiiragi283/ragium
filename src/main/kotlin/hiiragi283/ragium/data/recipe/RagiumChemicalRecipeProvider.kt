package hiiragi283.ragium.data.recipe

import hiiragi283.ragium.api.data.recipe.HTMachineRecipeJsonBuilder
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.tags.RagiumFluidTags
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import java.util.concurrent.CompletableFuture

class RagiumChemicalRecipeProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricRecipeProvider(output, registriesFuture) {
    override fun getName(): String = "Recipes/Chemical"

    override fun generate(exporter: RecipeExporter) {
        chemicalReactor(exporter)
        electrolyzer(exporter)
    }

    //    Chemical Reactor    //

    private fun chemicalReactor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(Items.DEEPSLATE, 16)
            .fluidInput(RagiumFluids.AQUA_REGIA)
            .itemOutput(RagiumItems.DEEPANT)
            .offerTo(exporter, RagiumItems.DEEPANT)
        // netherite scrap
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(Items.ANCIENT_DEBRIS)
            .fluidInput(RagiumFluids.HYDROCHLORIC_ACID, FluidConstants.INGOT)
            .itemOutput(Items.NETHERITE_SCRAP, 3)
            .offerTo(exporter, Items.NETHERITE_SCRAP, "_3x")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(Items.ANCIENT_DEBRIS)
            .fluidInput(RagiumFluids.SULFURIC_ACID, FluidConstants.INGOT)
            .itemOutput(Items.NETHERITE_SCRAP, 4)
            .offerTo(exporter, Items.NETHERITE_SCRAP, "_4x")
        // steam reforming
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.REFINED_GAS)
            .fluidInput(Fluids.WATER)
            .catalyst(RagiumItems.HEART_OF_THE_NETHER)
            .fluidOutput(RagiumFluids.HYDROGEN)
            .fluidOutput(RagiumFluids.CARBON_MONOXIDE)
            .offerTo(exporter, RagiumFluids.CARBON_MONOXIDE)
        // water gas shift
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.CARBON_MONOXIDE)
            .fluidInput(Fluids.WATER)
            .catalyst(RagiumItems.HEART_OF_THE_NETHER)
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
            .itemOutput(Items.BONE_MEAL)
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
}
