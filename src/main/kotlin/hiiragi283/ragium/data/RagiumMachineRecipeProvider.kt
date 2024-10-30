package hiiragi283.ragium.data

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTMachineRecipeJsonBuilder
import hiiragi283.ragium.api.data.recipe.HTMaterialItemRecipeRegistry
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTIngredient
import hiiragi283.ragium.api.tags.RagiumFluidTags
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumMachineTypes
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.entity.EntityType
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeKeys
import java.util.concurrent.CompletableFuture

class RagiumMachineRecipeProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricRecipeProvider(output, registriesFuture) {
    override fun getName(): String = "Recipes/Machine"

    override fun generate(exporter: RecipeExporter) {
        alloyFurnaceRecipes(exporter)
        assembler(exporter)
        blastFurnace(exporter)
        chemicalReactor(exporter)
        // compressor(exporter)
        // decompressor(exporter)
        distillation(exporter)
        electrolyzer(exporter)
        extractor(exporter)
        fluidDrill(exporter)
        grinder(exporter)
        metalFormer(exporter)
        mixer(exporter)
        mobExtractor(exporter)
        rockGenerator(exporter)
        sawMill(exporter)
        // patterned
        HTMaterialItemRecipeRegistry.generateRecipes(exporter, ::tagValidator)
    }

    // private fun exporterWrapper1(exporter: RecipeExporter, bool: Boolean): RecipeExporter = exporter

    private fun tagValidator(exporter: RecipeExporter, tagKey: TagKey<Item>): RecipeExporter =
        withConditions(exporter, ResourceConditions.tagsPopulated(tagKey))

    //    Alloy Furnace    //

    private fun alloyFurnaceRecipes(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ALLOY_FURNACE)
            .itemInput(ConventionalItemTags.COPPER_INGOTS)
            .itemInput(RagiumContents.Dusts.CRUDE_RAGINITE, 4)
            .itemOutput(RagiumContents.Ingots.RAGI_ALLOY)
            .offerTo(exporter, RagiumContents.Ingots.RAGI_ALLOY)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ALLOY_FURNACE)
            .itemInput(ConventionalItemTags.COPPER_INGOTS)
            .itemInput(RagiumContents.Dusts.RAGINITE)
            .itemOutput(RagiumContents.Ingots.RAGI_ALLOY)
            .offerTo(exporter, RagiumContents.Ingots.RAGI_ALLOY, "_alt")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ALLOY_FURNACE)
            .itemInput(ConventionalItemTags.IRON_INGOTS, 2)
            .itemInput(RagiumItemTags.NICKEL_INGOTS)
            .itemOutput(RagiumContents.Ingots.INVAR, 3)
            .offerTo(exporter, RagiumContents.Ingots.INVAR)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ALLOY_FURNACE, HTMachineTier.ADVANCED)
            .itemInput(ConventionalItemTags.GOLD_INGOTS, 5)
            .itemInput(Items.NETHERITE_SCRAP, 3)
            .itemOutput(Items.NETHERITE_INGOT, 2)
            .offerTo(exporter, Items.NETHERITE_INGOT)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ALLOY_FURNACE)
            .fluidInput(RagiumContents.Fluids.BATTER)
            .itemInput(RagiumContents.Foods.BUTTER)
            .itemOutput(RagiumBlocks.SPONGE_CAKE)
            .offerTo(exporter, RagiumBlocks.SPONGE_CAKE)
    }

    //    Assembler    //

    private fun assembler(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER)
            .itemInput(RagiumItemTags.STEEL_PLATES, 8)
            .itemInput(RagiumContents.Plates.RAGI_STEEL, 8)
            .itemOutput(RagiumContents.Misc.ENGINE)
            .offerTo(exporter, RagiumContents.Misc.ENGINE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER, HTMachineTier.ADVANCED)
            .itemInput(RagiumContents.Gems.RAGI_CRYSTAL, 8)
            .itemInput(RagiumContents.Misc.PROCESSOR_SOCKET)
            .itemOutput(RagiumContents.Misc.RAGI_CRYSTAL_PROCESSOR)
            .offerTo(exporter, RagiumContents.Misc.RAGI_CRYSTAL_PROCESSOR)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER, HTMachineTier.BASIC)
            .itemInput(RagiumItemTags.ALUMINUM_PLATES, 4)
            .itemInput(RagiumContents.Misc.BASALT_MESH, 4)
            .itemOutput(RagiumContents.Plates.STELLA)
            .offerTo(exporter, RagiumContents.Plates.STELLA)
        // circuits
        val boardMap: Map<HTMachineTier, Pair<ItemConvertible, Either<TagKey<Item>, ItemConvertible>>> = mapOf(
            HTMachineTier.PRIMITIVE to (RagiumContents.Plates.SILICON to Either.left(RagiumItemTags.COPPER_PLATES)),
            HTMachineTier.BASIC to (RagiumContents.Plates.PLASTIC to Either.left(RagiumItemTags.GOLD_PLATES)),
            HTMachineTier.ADVANCED to (RagiumContents.Plates.ENGINEERING_PLASTIC to Either.right(RagiumContents.Plates.RAGI_ALLOY)),
        )
        val circuitMap: Map<HTMachineTier, Either<TagKey<Item>, ItemConvertible>> = mapOf(
            HTMachineTier.PRIMITIVE to Either.left(ConventionalItemTags.REDSTONE_DUSTS),
            HTMachineTier.BASIC to Either.left(ConventionalItemTags.GLOWSTONE_DUSTS),
            HTMachineTier.ADVANCED to Either.right(RagiumContents.Dusts.RAGI_CRYSTAL),
        )

        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            val (first: ItemConvertible, second: Either<TagKey<Item>, ItemConvertible>) = boardMap[tier]
                ?: return@forEach
            val board: RagiumContents.CircuitBoards = tier.getCircuitBoard()
            val dope: Either<TagKey<Item>, ItemConvertible> = circuitMap[tier] ?: return@forEach
            val circuit: RagiumContents.Circuits = tier.getCircuit()
            // board
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineTypes.Processor.ASSEMBLER, tier)
                .itemInput(first)
                .itemInput(HTIngredient.ofItem(second))
                .itemOutput(board)
                .offerTo(exporter, board)
            // circuit
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineTypes.Processor.ASSEMBLER, tier)
                .itemInput(board)
                .itemInput(HTIngredient.ofItem(dope))
                .itemOutput(circuit)
                .offerTo(exporter, circuit)
        }

        // coils
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER)
            .itemInput(ConventionalItemTags.COPPER_INGOTS, 8)
            .itemInput(RagiumBlocks.SHAFT)
            .itemOutput(RagiumContents.Coils.COPPER)
            .offerTo(exporter, RagiumContents.Coils.COPPER)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER)
            .itemInput(ConventionalItemTags.GOLD_INGOTS, 8)
            .itemInput(RagiumBlocks.SHAFT)
            .itemOutput(RagiumContents.Coils.GOLD)
            .offerTo(exporter, RagiumContents.Coils.GOLD)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER)
            .itemInput(RagiumContents.Ingots.RAGI_ALLOY, 8)
            .itemInput(RagiumBlocks.SHAFT)
            .itemOutput(RagiumContents.Coils.RAGI_ALLOY)
            .offerTo(exporter, RagiumContents.Coils.RAGI_ALLOY)
    }

    //    Blast Furnace    //

    private fun blastFurnace(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.BLAST_FURNACE, HTMachineTier.BASIC)
            .itemInput(ConventionalItemTags.IRON_INGOTS)
            .itemInput(RagiumContents.Dusts.RAGINITE, 4)
            .itemOutput(RagiumContents.Ingots.RAGI_STEEL)
            .offerTo(exporter, RagiumContents.Ingots.RAGI_STEEL)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.BLAST_FURNACE, HTMachineTier.BASIC)
            .itemInput(ConventionalItemTags.IRON_INGOTS)
            .itemInput(ItemTags.COALS, 2)
            .itemOutput(RagiumContents.Ingots.STEEL)
            .offerTo(exporter, RagiumContents.Ingots.STEEL)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.BLAST_FURNACE, HTMachineTier.BASIC)
            .itemInput(ConventionalItemTags.REDSTONE_DUSTS, 4)
            .itemInput(RagiumContents.Dusts.RAGINITE, 5)
            .itemOutput(RagiumContents.Gems.RAGI_CRYSTAL)
            .offerTo(exporter, RagiumContents.Gems.RAGI_CRYSTAL)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.BLAST_FURNACE, HTMachineTier.BASIC)
            .itemInput(RagiumItemTags.STEEL_INGOTS)
            .itemInput(RagiumContents.Dusts.RAGI_CRYSTAL, 4)
            .itemInput(ConventionalItemTags.QUARTZ_GEMS)
            .itemOutput(RagiumContents.Ingots.REFINED_RAGI_STEEL)
            .offerTo(exporter, RagiumContents.Ingots.REFINED_RAGI_STEEL)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.BLAST_FURNACE, HTMachineTier.BASIC)
            .itemInput(ConventionalItemTags.QUARTZ_GEMS, 2)
            .itemInput(ItemTags.COALS, 4)
            .itemOutput(RagiumContents.Plates.SILICON)
            .offerTo(exporter, RagiumContents.Plates.SILICON)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.BLAST_FURNACE, HTMachineTier.ADVANCED)
            .itemInput(ItemTags.COALS, 8)
            .fluidInput(RagiumContents.Fluids.ALUMINA_SOLUTION)
            .itemOutput(RagiumContents.Ingots.ALUMINUM)
            .offerTo(exporter, RagiumContents.Ingots.ALUMINUM)
    }

    //    Chemical Reactor    //

    private fun chemicalReactor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .fluidInput(RagiumContents.Fluids.REFINED_GAS)
            .fluidInput(Fluids.WATER)
            .catalyst(Items.HEART_OF_THE_SEA)
            .fluidOutput(RagiumContents.Fluids.HYDROGEN)
            .fluidOutput(RagiumContents.Fluids.ALCOHOL)
            .offerTo(exporter, RagiumContents.Fluids.ALCOHOL)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, HTMachineTier.BASIC)
            .itemInput(Items.BLAZE_ROD)
            .fluidInput(Fluids.WATER, FluidConstants.BUCKET * 2)
            .fluidOutput(RagiumContents.Fluids.SULFURIC_ACID, FluidConstants.BUCKET * 2)
            .offerTo(exporter, RagiumContents.Fluids.SULFURIC_ACID)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, HTMachineTier.BASIC)
            .itemInput(Items.BREEZE_ROD)
            .fluidInput(Fluids.WATER, FluidConstants.BUCKET * 2)
            .fluidOutput(RagiumContents.Fluids.NITRIC_ACID, FluidConstants.BUCKET * 2)
            .offerTo(exporter, RagiumContents.Fluids.NITRIC_ACID)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .itemInput(RagiumContents.RawMaterials.BAUXITE)
            .fluidInput(RagiumContents.Fluids.SODIUM_HYDROXIDE)
            .fluidOutput(RagiumContents.Fluids.ALUMINA_SOLUTION)
            .offerTo(exporter, RagiumContents.Fluids.ALUMINA_SOLUTION)

        // Fuels
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .fluidInput(RagiumContents.Fluids.ALCOHOL, FluidConstants.BUCKET * 3)
            .fluidInput(RagiumFluidTags.ORGANIC_OILS)
            .fluidOutput(RagiumContents.Fluids.BIO_FUEL, FluidConstants.BUCKET * 3)
            .fluidOutput(RagiumContents.Fluids.GLYCEROL)
            .offerTo(exporter, RagiumContents.Fluids.BIO_FUEL)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluidTags.FUEL)
            .fluidInput(RagiumContents.Fluids.MIXTURE_ACID, FluidConstants.BUCKET / 4)
            .fluidOutput(RagiumContents.Fluids.NITRO_FUEL, FluidConstants.BUCKET / 4)
            .offerTo(exporter, RagiumContents.Fluids.NITRO_FUEL)
        // Dynamite
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, HTMachineTier.BASIC)
            .fluidInput(RagiumContents.Fluids.GLYCEROL)
            .fluidInput(RagiumContents.Fluids.MIXTURE_ACID, FluidConstants.BUCKET * 3)
            .fluidOutput(RagiumContents.Fluids.NITRO_GLYCERIN)
            .offerTo(exporter, RagiumContents.Fluids.NITRO_GLYCERIN)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .itemInput(Items.PAPER)
            .itemInput(ConventionalItemTags.STRINGS)
            .fluidInput(RagiumContents.Fluids.NITRO_GLYCERIN)
            .itemOutput(RagiumContents.Misc.DYNAMITE, 2)
            .offerTo(exporter, RagiumContents.Misc.DYNAMITE)
        // TNT
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .fluidInput(RagiumContents.Fluids.AROMATIC_COMPOUNDS)
            .fluidInput(RagiumContents.Fluids.MIXTURE_ACID, FluidConstants.BUCKET * 3)
            .fluidOutput(RagiumContents.Fluids.TRINITROTOLUENE)
            .offerTo(exporter, RagiumContents.Fluids.TRINITROTOLUENE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .fluidInput(RagiumContents.Fluids.TRINITROTOLUENE)
            .itemInput(ItemTags.SAND)
            .itemOutput(Items.TNT, 12)
            .offerTo(exporter, Items.TNT)

        // Plastics
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .itemInput(RagiumContents.Misc.POLYMER_RESIN)
            .itemOutput(RagiumContents.Plates.PLASTIC)
            .offerTo(exporter, RagiumContents.Plates.PLASTIC)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .itemInput(RagiumContents.Misc.POLYMER_RESIN, 4)
            .itemInput(RagiumContents.Misc.BASALT_MESH)
            .itemOutput(RagiumContents.Plates.ENGINEERING_PLASTIC)
            .offerTo(exporter, RagiumContents.Plates.ENGINEERING_PLASTIC)

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
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .itemInput(before)
            .fluidInput(RagiumContents.Fluids.OXYGEN, FluidConstants.INGOT)
            .itemOutput(after)
            .offerTo(exporter, after)
    }

    //    Distillation Tower    //

    private fun distillation(exporter: RecipeExporter) {
        // crude oil -> polymer resin + fuel
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.DISTILLATION_TOWER)
            .fluidInput(RagiumContents.Fluids.CRUDE_OIL, FluidConstants.BUCKET * 8)
            .itemOutput(RagiumContents.Misc.POLYMER_RESIN, 4)
            .fluidOutput(RagiumContents.Fluids.FUEL, FluidConstants.BUCKET * 4)
            .offerTo(exporter, RagiumContents.Fluids.CRUDE_OIL)

        // crude oil -> refined gas
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.DISTILLATION_TOWER)
            .fluidInput(RagiumContents.Fluids.CRUDE_OIL, FluidConstants.BUCKET * 8)
            .catalyst(RagiumContents.Circuits.BASIC)
            .fluidOutput(RagiumContents.Fluids.REFINED_GAS, FluidConstants.BUCKET * 5)
            .fluidOutput(RagiumContents.Fluids.RESIDUAL_OIL, FluidConstants.BUCKET * 3)
            .offerTo(exporter, RagiumContents.Misc.POLYMER_RESIN)

        // crude oil -> naphtha
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.DISTILLATION_TOWER)
            .fluidInput(RagiumContents.Fluids.CRUDE_OIL, FluidConstants.BUCKET * 8)
            .catalyst(RagiumContents.Circuits.ADVANCED)
            .fluidOutput(RagiumContents.Fluids.NAPHTHA, FluidConstants.BUCKET * 5)
            .fluidOutput(RagiumContents.Fluids.RESIDUAL_OIL, FluidConstants.BUCKET * 3)
            .offerTo(exporter, RagiumContents.Fluids.FUEL)

        // residual oil -> fuel + aromatic compound
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.DISTILLATION_TOWER)
            .fluidInput(RagiumContents.Fluids.RESIDUAL_OIL, FluidConstants.BUCKET * 8)
            .catalyst(RagiumContents.Circuits.PRIMITIVE)
            .fluidOutput(RagiumContents.Fluids.FUEL, FluidConstants.BUCKET * 3)
            .fluidOutput(RagiumContents.Fluids.AROMATIC_COMPOUNDS, FluidConstants.BUCKET * 5)
            .offerTo(exporter, RagiumContents.Fluids.AROMATIC_COMPOUNDS)

        // residual oil -> fuel + asphalt
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.DISTILLATION_TOWER)
            .fluidInput(RagiumContents.Fluids.RESIDUAL_OIL, FluidConstants.BUCKET * 8)
            .fluidOutput(RagiumContents.Fluids.FUEL, FluidConstants.BUCKET * 3)
            .fluidOutput(RagiumContents.Fluids.ASPHALT, FluidConstants.BUCKET * 5)
            .offerTo(exporter, RagiumContents.Fluids.ASPHALT)
    }

    //    Electrolyzer    //

    private fun electrolyzer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ELECTROLYZER)
            .fluidInput(Fluids.WATER)
            .fluidOutput(RagiumContents.Fluids.HYDROGEN, FluidConstants.BUCKET * 2)
            .fluidOutput(RagiumContents.Fluids.OXYGEN)
            .offerTo(exporter, Fluids.WATER)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ELECTROLYZER)
            .fluidInput(RagiumContents.Fluids.SALT_WATER)
            .itemOutput(RagiumAPI.getInstance().createFilledCube(RagiumContents.Fluids.SODIUM_HYDROXIDE.asFluid()))
            .fluidOutput(RagiumContents.Fluids.HYDROGEN)
            .fluidOutput(RagiumContents.Fluids.CHLORINE)
            .offerTo(exporter, RagiumContents.Fluids.SALT_WATER)
    }

    //    Extractor    //

    private fun extractor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .itemInput(ItemTags.VILLAGER_PLANTABLE_SEEDS, 4)
            .fluidOutput(RagiumContents.Fluids.SEED_OIL)
            .offerTo(exporter, RagiumContents.Fluids.SEED_OIL)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .itemInput(RagiumItemTags.PROTEIN_FOODS, 4)
            .fluidOutput(RagiumContents.Fluids.TALLOW)
            .offerTo(exporter, RagiumContents.Fluids.TALLOW)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .itemInput(RagiumItemTags.BASALTS)
            .itemOutput(RagiumContents.Misc.BASALT_MESH)
            .offerTo(exporter, RagiumContents.Misc.BASALT_MESH)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .itemInput(RagiumContents.Foods.CHOCOLATE)
            .fluidOutput(RagiumContents.Fluids.CHOCOLATE)
            .offerTo(exporter, RagiumContents.Fluids.CHOCOLATE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .itemInput(Items.SUGAR, 4)
            .fluidOutput(RagiumContents.Fluids.STARCH_SYRUP)
            .offerTo(exporter, RagiumContents.Fluids.STARCH_SYRUP)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .itemInput(Items.SWEET_BERRIES, 4)
            .fluidOutput(RagiumContents.Fluids.SWEET_BERRIES)
            .offerTo(exporter, RagiumContents.Fluids.SWEET_BERRIES)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .fluidInput(ConventionalFluidTags.MILK)
            .itemOutput(RagiumContents.Foods.BUTTER)
            .offerTo(exporter, RagiumContents.Foods.BUTTER)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .itemInput(Items.SOUL_SAND)
            .itemOutput(Items.SAND)
            .fluidOutput(RagiumContents.Fluids.CRUDE_OIL)
            .offerTo(exporter, RagiumContents.Fluids.CRUDE_OIL, "_from_soul_sand")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .itemInput(Items.SOUL_SOIL)
            .itemOutput(Items.SAND)
            .fluidOutput(RagiumContents.Fluids.CRUDE_OIL)
            .offerTo(exporter, RagiumContents.Fluids.CRUDE_OIL, "_from_soul_soil")
    }

    //    Fluid Drill    //

    private fun fluidDrill(exporter: RecipeExporter) {
        registerDrilling(exporter, BiomeKeys.WARM_OCEAN, RagiumContents.Fluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.LUKEWARM_OCEAN, RagiumContents.Fluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.DEEP_LUKEWARM_OCEAN, RagiumContents.Fluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.OCEAN, RagiumContents.Fluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.DEEP_OCEAN, RagiumContents.Fluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.COLD_OCEAN, RagiumContents.Fluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.DEEP_COLD_OCEAN, RagiumContents.Fluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.FROZEN_OCEAN, RagiumContents.Fluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.DEEP_FROZEN_OCEAN, RagiumContents.Fluids.SALT_WATER)

        registerDrilling(exporter, BiomeKeys.NETHER_WASTES, Fluids.LAVA)
        registerDrilling(exporter, BiomeKeys.WARPED_FOREST, Fluids.LAVA)
        registerDrilling(exporter, BiomeKeys.CRIMSON_FOREST, Fluids.LAVA)
        registerDrilling(exporter, BiomeKeys.SOUL_SAND_VALLEY, RagiumContents.Fluids.CRUDE_OIL)
        registerDrilling(exporter, BiomeKeys.BASALT_DELTAS, Fluids.LAVA)
    }

    private fun registerDrilling(exporter: RecipeExporter, biomeKey: RegistryKey<Biome>, fluid: Fluid) {
        /*HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.FLUID_DRILL)
            .addOutput(fluid)
            .setCustomData(HTRecipeComponentTypes.BIOME, biomeKey)
            .offerTo(exporter, biomeKey.value)*/
    }

    private fun registerDrilling(exporter: RecipeExporter, biomeKey: RegistryKey<Biome>, fluid: RagiumContents.Fluids) {
        /*HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.FLUID_DRILL)
            .addOutput(fluid)
            .setCustomData(HTRecipeComponentTypes.BIOME, biomeKey)
            .offerTo(exporter, biomeKey.value)*/
    }

    //    Grinder    //

    private fun grinder(exporter: RecipeExporter) {
        registerGrinder(exporter, ConventionalItemTags.COBBLESTONES to 1, Items.GRAVEL to 1)
        registerGrinder(exporter, ConventionalItemTags.QUARTZ_ORES to 1, Items.QUARTZ to 2)
        registerGrinder(exporter, ConventionalItemTags.RED_SANDSTONE_BLOCKS to 1, Items.RED_SAND to 4)
        registerGrinder(exporter, ConventionalItemTags.UNCOLORED_SANDSTONE_BLOCKS to 1, Items.SAND to 1)
        registerGrinder(exporter, ConventionalItemTags.WHEAT_CROPS to 1, RagiumContents.Foods.FLOUR to 1)
        registerGrinder(exporter, Items.ANCIENT_DEBRIS to 1, Items.NETHERITE_SCRAP to 2)
        registerGrinder(exporter, Items.COARSE_DIRT to 1, Items.DIRT to 1)
        registerGrinder(exporter, Items.DEEPSLATE to 1, Items.COBBLED_DEEPSLATE to 1)
        registerGrinder(exporter, Items.GRAVEL to 1, Items.SAND to 1, suffix = "_from_gravel")
        // registerGrinder(exporter, Items.NETHERRACK to 4, RagiumContents.Dusts.SULFUR to 1)
        registerGrinder(exporter, Items.STONE to 1, Items.COBBLESTONE to 1)
        registerGrinder(exporter, ItemTags.BOATS to 1, RagiumContents.Foods.PULP to 5, suffix = "_from_boat")
        registerGrinder(exporter, ItemTags.COAL_ORES to 1, Items.COAL to 2)
        registerGrinder(exporter, ItemTags.DIAMOND_ORES to 1, Items.DIAMOND to 2)
        registerGrinder(exporter, ItemTags.EMERALD_ORES to 1, Items.EMERALD to 2)
        registerGrinder(exporter, ItemTags.FENCE_GATES to 1, RagiumContents.Foods.PULP to 4, suffix = "_from_fence_gate")
        registerGrinder(exporter, ItemTags.LAPIS_ORES to 1, Items.LAPIS_LAZULI to 8)
        registerGrinder(exporter, ItemTags.LOGS to 1, RagiumContents.Foods.PULP to 4, suffix = "_from_log")
        registerGrinder(exporter, ItemTags.PLANKS to 1, RagiumContents.Foods.PULP to 1, suffix = "_from_plank")
        registerGrinder(exporter, ItemTags.REDSTONE_ORES to 1, Items.REDSTONE to 8)
        registerGrinder(exporter, ItemTags.SAPLINGS to 2, RagiumContents.Foods.PULP to 1, suffix = "_from_sapling")
        registerGrinder(exporter, ItemTags.WOODEN_BUTTONS to 1, RagiumContents.Foods.PULP to 1, suffix = "_from_button")
        registerGrinder(exporter, ItemTags.WOODEN_DOORS to 1, RagiumContents.Foods.PULP to 2, suffix = "_from_door")
        registerGrinder(exporter, ItemTags.WOODEN_FENCES to 3, RagiumContents.Foods.PULP to 5, suffix = "_from_fence")
        registerGrinder(exporter, ItemTags.WOODEN_PRESSURE_PLATES to 1, RagiumContents.Foods.PULP to 2, suffix = "_from_plate")
        registerGrinder(exporter, ItemTags.WOODEN_SLABS to 2, RagiumContents.Foods.PULP to 1, suffix = "_from_slab")
        registerGrinder(exporter, ItemTags.WOODEN_STAIRS to 4, RagiumContents.Foods.PULP to 6, suffix = "_from_stair")
        registerGrinder(exporter, ItemTags.WOODEN_TRAPDOORS to 1, RagiumContents.Foods.PULP to 3, suffix = "_from_trap_door")
        registerGrinder(exporter, ItemTags.WOOL to 1, Items.STRING to 4)
        registerGrinder(exporter, RagiumItemTags.PROTEIN_FOODS to 1, RagiumContents.Foods.MINCED_MEAT to 1)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.GRINDER)
            .itemInput(ConventionalItemTags.SUGAR_CANE_CROPS)
            .itemOutput(Items.SUGAR, 2)
            .itemOutput(RagiumContents.Foods.PULP)
            .offerTo(exporter, Items.SUGAR)
    }

    @JvmName("registerGrinderItem")
    private fun registerGrinder(
        exporter: RecipeExporter,
        input: Pair<ItemConvertible, Int>,
        output: Pair<ItemConvertible, Int>,
        suffix: String = "",
    ) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.GRINDER)
            .itemInput(input.first, input.second)
            .itemOutput(output.first, output.second)
            .offerTo(exporter, output.first, suffix)
    }

    @JvmName("registerGrinderTag")
    private fun registerGrinder(
        exporter: RecipeExporter,
        input: Pair<TagKey<Item>, Int>,
        output: Pair<ItemConvertible, Int>,
        suffix: String = "",
    ) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.GRINDER)
            .itemInput(input.first, input.second)
            .itemOutput(output.first, output.second)
            .offerTo(exporter, output.first, suffix)
    }

    //    Metal Former    //

    private fun metalFormer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.METAL_FORMER)
            .itemInput(RagiumItemTags.STEEL_INGOTS, 2)
            .itemOutput(RagiumBlocks.SHAFT)
            .offerTo(exporter, RagiumContents.Dusts.RAGINITE)

        // pipes
        registerPipe(exporter, RagiumItemTags.IRON_PLATES, RagiumContents.Pipes.IRON)
        registerPipe(exporter, RagiumItemTags.STEEL_PLATES, RagiumContents.Pipes.STEEL)
        registerPipe(exporter, RagiumItemTags.COPPER_PLATES, RagiumContents.Pipes.COPPER)
        registerPipe(exporter, RagiumItemTags.REFINED_RAGI_STEEL_PLATES, RagiumContents.Pipes.UNIVERSAL)
    }

    private fun registerPipe(exporter: RecipeExporter, input: TagKey<Item>, output: RagiumContents.Pipes) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.METAL_FORMER, output.tier)
            .itemInput(input, 2)
            .itemOutput(output)
            .offerTo(exporter, output)
    }

    //    Mixer    //

    private fun mixer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .itemInput(RagiumContents.Dusts.CRUDE_RAGINITE)
            .fluidInput(Fluids.WATER, FluidConstants.BOTTLE)
            .itemOutput(RagiumContents.Dusts.RAGINITE)
            .offerTo(exporter, RagiumContents.Dusts.RAGINITE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .itemInput(RagiumItemTags.ALKALI)
            .fluidInput(RagiumFluidTags.ORGANIC_OILS)
            .itemOutput(RagiumContents.Misc.SOAP_INGOT)
            .fluidOutput(RagiumContents.Fluids.GLYCEROL)
            .offerTo(exporter, RagiumContents.Misc.SOAP_INGOT)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .itemInput(RagiumContents.Foods.FLOUR)
            .fluidInput(Fluids.WATER, FluidConstants.BOTTLE)
            .itemOutput(RagiumContents.Foods.DOUGH)
            .offerTo(exporter, RagiumContents.Foods.DOUGH)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .itemInput(RagiumContents.Foods.PULP)
            .fluidInput(Fluids.WATER)
            .itemOutput(Items.PAPER)
            .offerTo(exporter, Items.PAPER)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .itemInput(RagiumContents.Foods.PULP)
            .itemInput(RagiumItemTags.ALKALI)
            .fluidInput(Fluids.WATER)
            .itemOutput(Items.PAPER, 2)
            .offerTo(exporter, Items.PAPER, "_from_alkali")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .itemInput(RagiumContents.Foods.PULP)
            .fluidInput(RagiumContents.Fluids.SULFURIC_ACID)
            .itemOutput(Items.PAPER, 4)
            .offerTo(exporter, Items.PAPER, "_from_acid")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .itemInput(RagiumContents.Foods.BUTTER)
            .itemInput(Items.SUGAR)
            .fluidInput(ConventionalFluidTags.MILK)
            .itemOutput(RagiumContents.Foods.CARAMEL, 4)
            .offerTo(exporter, RagiumContents.Foods.CARAMEL)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .itemInput(Items.COCOA_BEANS)
            .itemInput(Items.SUGAR)
            .fluidInput(ConventionalFluidTags.MILK)
            .itemOutput(RagiumContents.Foods.CHOCOLATE)
            .offerTo(exporter, RagiumContents.Foods.CHOCOLATE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .itemInput(RagiumContents.Foods.FLOUR)
            .fluidInput(ConventionalFluidTags.MILK)
            .fluidOutput(RagiumContents.Fluids.BATTER)
            .offerTo(exporter, RagiumContents.Fluids.BATTER)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .fluidInput(RagiumContents.Fluids.NITRIC_ACID)
            .fluidInput(RagiumContents.Fluids.SULFURIC_ACID)
            .fluidOutput(RagiumContents.Fluids.MIXTURE_ACID, FluidConstants.BUCKET * 2)
            .offerTo(exporter, RagiumContents.Fluids.MIXTURE_ACID)

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
    }

    private fun registerBreaching(exporter: RecipeExporter, input: TagKey<Item>, output: Item) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .itemInput(input)
            .itemInput(RagiumContents.Misc.SOAP_INGOT)
            .fluidInput(Fluids.WATER)
            .itemOutput(output)
            .offerTo(exporter, output)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .itemInput(input)
            .fluidInput(RagiumContents.Fluids.CHLORINE)
            .itemOutput(output)
            .offerTo(exporter, output)
    }

    //    Rock Generator    //

    private fun rockGenerator(exporter: RecipeExporter) {
        registerRock(exporter, Items.STONE)
        registerRock(exporter, Items.COBBLESTONE)
        registerRock(exporter, Items.GRANITE)
        registerRock(exporter, Items.DIORITE)
        registerRock(exporter, Items.ANDESITE)
        registerRock(exporter, Items.DEEPSLATE)
        registerRock(exporter, Items.COBBLED_DEEPSLATE)
        registerRock(exporter, Items.CALCITE)
        registerRock(exporter, Items.TUFF)
        registerRock(exporter, Items.DRIPSTONE_BLOCK)
        registerRock(exporter, Items.NETHERRACK)
        registerRock(exporter, Items.BASALT)
        registerRock(exporter, Items.BLACKSTONE)
        registerRock(exporter, Items.END_STONE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ROCK_GENERATOR)
            .fluidInput(Fluids.LAVA)
            .itemOutput(Items.OBSIDIAN)
            .offerTo(exporter, Items.OBSIDIAN)
    }

    private fun registerRock(exporter: RecipeExporter, rock: ItemConvertible) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ROCK_GENERATOR)
            .itemOutput(rock, 8)
            .offerTo(exporter, rock)
    }

    //    Saw Mill    //

    private fun sawMill(exporter: RecipeExporter) {
        registerPlank(exporter, ItemTags.OAK_LOGS, Items.OAK_PLANKS)
        registerPlank(exporter, ItemTags.SPRUCE_LOGS, Items.SPRUCE_PLANKS)
        registerPlank(exporter, ItemTags.BIRCH_LOGS, Items.BIRCH_PLANKS)
        registerPlank(exporter, ItemTags.JUNGLE_LOGS, Items.JUNGLE_PLANKS)
        registerPlank(exporter, ItemTags.ACACIA_LOGS, Items.ACACIA_PLANKS)
        registerPlank(exporter, ItemTags.CHERRY_LOGS, Items.CHERRY_PLANKS)
        registerPlank(exporter, ItemTags.DARK_OAK_LOGS, Items.DARK_OAK_PLANKS)
        registerPlank(exporter, ItemTags.MANGROVE_LOGS, Items.MANGROVE_PLANKS)
        registerPlank(exporter, ItemTags.CRIMSON_STEMS, Items.CRIMSON_PLANKS)
        registerPlank(exporter, ItemTags.WARPED_STEMS, Items.WARPED_PLANKS)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.SAW_MILL)
            .itemInput(ItemTags.PLANKS)
            .itemOutput(Items.STICK, 4)
            .offerTo(exporter, Items.STICK)
    }

    private fun registerPlank(exporter: RecipeExporter, log: TagKey<Item>, plank: ItemConvertible) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.SAW_MILL)
            .itemInput(log)
            .itemOutput(plank, 6)
            .itemOutput(RagiumContents.Foods.PULP)
            .offerTo(exporter, plank)
    }

    //    Mob Extractor    //

    private fun mobExtractor(exporter: RecipeExporter) {
        registerMobDrop(exporter, Items.ARMADILLO_SCUTE, EntityType.ARMADILLO)
        registerMobDrop(exporter, Items.HONEYCOMB, EntityType.BEE)
        // registerMobDrop(exporter, RagiumContents.Fluids.SULFURIC_ACID, EntityType.BLAZE)
        // registerMobDrop(exporter, RagiumContents.Fluids.NITRIC_ACID, EntityType.BREEZE)
        registerMobDrop(exporter, Items.EGG, EntityType.CHICKEN)
        // registerMobDrop(exporter, RagiumContents.Fluids.MILK, EntityType.COW)
        registerMobDrop(exporter, Items.GLOW_INK_SAC, EntityType.GLOW_SQUID)
        registerMobDrop(exporter, Items.IRON_NUGGET, EntityType.IRON_GOLEM)
        // registerMobDrop(exporter, RagiumContents.Fluids.MILK, EntityType.MOOSHROOM) // TODO
        registerMobDrop(exporter, Items.WHITE_WOOL, EntityType.SHEEP)
        registerMobDrop(exporter, Items.SNOWBALL, EntityType.SNOW_GOLEM)
        registerMobDrop(exporter, Items.INK_SAC, EntityType.SQUID)
        registerMobDrop(exporter, Items.TURTLE_SCUTE, EntityType.TURTLE)
        registerMobDrop(exporter, Items.EMERALD, EntityType.WANDERING_TRADER)
        registerMobDrop(exporter, Items.ECHO_SHARD, EntityType.WARDEN) // TODO
        registerMobDrop(exporter, Items.WITHER_ROSE, EntityType.WITHER)
        registerMobDrop(exporter, Items.PLAYER_HEAD, EntityType.PLAYER)

        // registerMobDrop(exporter, RagiumContents.Gems.OBLIVION_CRYSTAL, RagiumEntityTypes.OBLIVION_CUBE)

        /*HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.MOB_EXTRACTOR)
            .addOutput(Items.RED_MUSHROOM)
            .addOutput(Items.BROWN_MUSHROOM)
            .setCustomData(HTRecipeComponentTypes.ENTITY_TYPE, EntityType.BOGGED)
            .offerTo(exporter)*/
    }

    private fun registerMobDrop(exporter: RecipeExporter, output: ItemConvertible, entityType: EntityType<*>) {
        /*HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.MOB_EXTRACTOR)
            .addOutput(output)
            .setCustomData(HTRecipeComponentTypes.ENTITY_TYPE, entityType)
            .offerTo(exporter, Registries.ENTITY_TYPE.getId(entityType))*/
    }
}
