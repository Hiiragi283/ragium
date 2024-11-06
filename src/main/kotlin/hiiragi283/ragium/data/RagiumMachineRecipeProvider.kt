package hiiragi283.ragium.data

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTMachineRecipeJsonBuilder
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTIngredient
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
import net.minecraft.block.Block
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.entity.EntityType
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
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
        distillation(exporter)
        electrolyzer(exporter)
        extractor(exporter)
        fluidDrill(exporter)
        grinder(exporter)
        laserTransformer(exporter)
        metalFormer(exporter)
        mixer(exporter)
        mobExtractor(exporter)
        rockGenerator(exporter)
        sawMill(exporter)
    }

    //    Alloy Furnace    //

    private fun alloyFurnaceRecipes(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ALLOY_FURNACE)
            .itemInput(ConventionalItemTags.COPPER_INGOTS)
            .itemInput(RagiumContents.Dusts.CRUDE_RAGINITE, 4)
            .itemOutput(RagiumContents.Ingots.RAGI_ALLOY)
            .offerTo(exporter, RagiumContents.Ingots.RAGI_ALLOY)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ALLOY_FURNACE)
            .itemInput(ConventionalItemTags.COPPER_INGOTS)
            .itemInput(RagiumContents.Dusts.RAGINITE)
            .itemOutput(RagiumContents.Ingots.RAGI_ALLOY)
            .offerTo(exporter, RagiumContents.Ingots.RAGI_ALLOY, "_alt")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ALLOY_FURNACE, HTMachineTier.ADVANCED)
            .itemInput(ConventionalItemTags.GOLD_INGOTS, 5)
            .itemInput(Items.NETHERITE_SCRAP, 3)
            .itemOutput(Items.NETHERITE_INGOT, 2)
            .offerTo(exporter, Items.NETHERITE_INGOT)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ALLOY_FURNACE)
            .fluidInput(RagiumFluids.BATTER)
            .itemInput(RagiumItems.BUTTER)
            .itemOutput(RagiumBlocks.SPONGE_CAKE)
            .offerTo(exporter, RagiumBlocks.SPONGE_CAKE)
    }

    //    Assembler    //

    private fun assembler(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ASSEMBLER)
            .itemInput(RagiumContents.Plates.STEEL, 8)
            .itemInput(RagiumContents.Plates.RAGI_STEEL, 8)
            .itemOutput(RagiumItems.ENGINE)
            .offerTo(exporter, RagiumItems.ENGINE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ASSEMBLER, HTMachineTier.ADVANCED)
            .itemInput(RagiumContents.Gems.RAGI_CRYSTAL, 8)
            .itemInput(RagiumItems.PROCESSOR_SOCKET)
            .itemOutput(RagiumItems.RAGI_CRYSTAL_PROCESSOR)
            .offerTo(exporter, RagiumItems.RAGI_CRYSTAL_PROCESSOR)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ASSEMBLER, HTMachineTier.BASIC)
            .itemInput(RagiumContents.Plates.ALUMINUM, 4)
            .itemInput(RagiumItems.BASALT_MESH, 4)
            .itemOutput(RagiumContents.Plates.STELLA)
            .offerTo(exporter, RagiumContents.Plates.STELLA)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ASSEMBLER, HTMachineTier.ADVANCED)
            .itemInput(RagiumContents.Plates.STEEL, 4)
            .itemInput(RagiumContents.Plates.ENGINEERING_PLASTIC, 4)
            .fluidInput(RagiumFluids.NOBLE_GAS)
            .itemOutput(RagiumItems.LASER_EMITTER)
            .offerTo(exporter, RagiumItems.LASER_EMITTER)
        // circuits
        val boardMap: Map<HTMachineTier, Pair<RagiumContents.Plates, RagiumContents.Plates>> = mapOf(
            HTMachineTier.PRIMITIVE to (RagiumContents.Plates.SILICON to RagiumContents.Plates.COPPER),
            HTMachineTier.BASIC to (RagiumContents.Plates.PLASTIC to RagiumContents.Plates.GOLD),
            HTMachineTier.ADVANCED to (RagiumContents.Plates.ENGINEERING_PLASTIC to RagiumContents.Plates.RAGI_ALLOY),
        )
        val circuitMap: Map<HTMachineTier, Either<TagKey<Item>, ItemConvertible>> = mapOf(
            HTMachineTier.PRIMITIVE to Either.left(ConventionalItemTags.REDSTONE_DUSTS),
            HTMachineTier.BASIC to Either.left(ConventionalItemTags.GLOWSTONE_DUSTS),
            HTMachineTier.ADVANCED to Either.right(RagiumContents.Dusts.RAGI_CRYSTAL),
        )

        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            val (first: RagiumContents.Plates, second: RagiumContents.Plates) = boardMap[tier] ?: return@forEach
            val board: RagiumContents.CircuitBoards = tier.getCircuitBoard()
            val dope: Either<TagKey<Item>, ItemConvertible> = circuitMap[tier] ?: return@forEach
            val circuit: RagiumContents.Circuits = tier.getCircuit()
            // board
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.ASSEMBLER, tier)
                .itemInput(first)
                .itemInput(second)
                .itemOutput(board)
                .offerTo(exporter, board)
            // circuit
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.ASSEMBLER, tier)
                .itemInput(board)
                .itemInput(HTIngredient.ofItem(dope))
                .itemOutput(circuit)
                .offerTo(exporter, circuit)
        }

        // coils
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ASSEMBLER)
            .itemInput(ConventionalItemTags.COPPER_INGOTS, 8)
            .itemInput(RagiumBlocks.SHAFT)
            .itemOutput(RagiumContents.Coils.PRIMITIVE, 2)
            .offerTo(exporter, RagiumContents.Coils.PRIMITIVE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ASSEMBLER)
            .itemInput(ConventionalItemTags.GOLD_INGOTS, 8)
            .itemInput(RagiumBlocks.SHAFT)
            .itemOutput(RagiumContents.Coils.BASIC, 2)
            .offerTo(exporter, RagiumContents.Coils.BASIC)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ASSEMBLER)
            .itemInput(RagiumContents.Ingots.RAGI_ALLOY, 8)
            .itemInput(RagiumBlocks.SHAFT)
            .itemOutput(RagiumContents.Coils.ADVANCED, 2)
            .offerTo(exporter, RagiumContents.Coils.ADVANCED)
    }

    //    Blast Furnace    //

    private fun blastFurnace(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE)
            .itemInput(ConventionalItemTags.IRON_INGOTS)
            .itemInput(RagiumContents.Dusts.RAGINITE, 4)
            .itemOutput(RagiumContents.Ingots.RAGI_STEEL)
            .offerTo(exporter, RagiumContents.Ingots.RAGI_STEEL)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE)
            .itemInput(ConventionalItemTags.IRON_INGOTS)
            .itemInput(ItemTags.COALS, 2)
            .itemOutput(RagiumContents.Ingots.STEEL)
            .offerTo(exporter, RagiumContents.Ingots.STEEL)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.BASIC)
            .itemInput(ConventionalItemTags.REDSTONE_DUSTS, 4)
            .itemInput(RagiumContents.Dusts.RAGINITE, 5)
            .itemOutput(RagiumContents.Gems.RAGI_CRYSTAL)
            .offerTo(exporter, RagiumContents.Gems.RAGI_CRYSTAL)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.BASIC)
            .itemInput(RagiumContents.Ingots.STEEL)
            .itemInput(RagiumContents.Dusts.RAGI_CRYSTAL, 4)
            .itemInput(ConventionalItemTags.QUARTZ_GEMS)
            .itemOutput(RagiumContents.Ingots.REFINED_RAGI_STEEL)
            .offerTo(exporter, RagiumContents.Ingots.REFINED_RAGI_STEEL)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.BASIC)
            .itemInput(ConventionalItemTags.QUARTZ_GEMS, 2)
            .itemInput(ItemTags.COALS, 4)
            .itemOutput(RagiumContents.Plates.SILICON)
            .offerTo(exporter, RagiumContents.Plates.SILICON)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.ADVANCED)
            .itemInput(ItemTags.COALS, 8)
            .fluidInput(RagiumFluids.ALUMINA_SOLUTION)
            .itemOutput(RagiumContents.Ingots.ALUMINUM)
            .offerTo(exporter, RagiumContents.Ingots.ALUMINUM)
    }

    //    Chemical Reactor    //

    private fun chemicalReactor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.REFINED_GAS)
            .fluidInput(Fluids.WATER)
            .catalyst(Items.HEART_OF_THE_SEA)
            .fluidOutput(RagiumFluids.HYDROGEN)
            .fluidOutput(RagiumFluids.ALCOHOL)
            .offerTo(exporter, RagiumFluids.ALCOHOL)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.BASIC)
            .itemInput(RagiumContents.Dusts.SULFUR)
            .fluidInput(Fluids.WATER)
            .fluidOutput(RagiumFluids.SULFURIC_ACID)
            .offerTo(exporter, RagiumFluids.SULFURIC_ACID)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.BASIC)
            .itemInput(RagiumContents.Dusts.NITER)
            .fluidInput(Fluids.WATER)
            .fluidOutput(RagiumFluids.NITRIC_ACID)
            .offerTo(exporter, RagiumFluids.NITRIC_ACID)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(RagiumContents.Dusts.BAUXITE)
            .fluidInput(RagiumFluids.SODIUM_HYDROXIDE)
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
            .itemOutput(RagiumContents.Plates.PLASTIC)
            .offerTo(exporter, RagiumContents.Plates.PLASTIC)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(RagiumItems.POLYMER_RESIN, 4)
            .itemInput(RagiumItems.BASALT_MESH)
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
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(before)
            .fluidInput(RagiumFluids.OXYGEN, FluidConstants.INGOT)
            .itemOutput(after)
            .offerTo(exporter, after)
    }

    //    Distillation Tower    //

    private fun distillation(exporter: RecipeExporter) {
        // crude oil -> polymer resin + fuel
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.CRUDE_OIL, FluidConstants.BUCKET * 8)
            .itemOutput(RagiumItems.POLYMER_RESIN, 4)
            .fluidOutput(RagiumFluids.FUEL, FluidConstants.BUCKET * 4)
            .offerTo(exporter, RagiumFluids.CRUDE_OIL)

        // crude oil -> refined gas
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.CRUDE_OIL, FluidConstants.BUCKET * 8)
            .catalyst(RagiumContents.Circuits.BASIC)
            .fluidOutput(RagiumFluids.REFINED_GAS, FluidConstants.BUCKET * 5)
            .fluidOutput(RagiumFluids.RESIDUAL_OIL, FluidConstants.BUCKET * 3)
            .offerTo(exporter, RagiumItems.POLYMER_RESIN)

        // crude oil -> naphtha
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.CRUDE_OIL, FluidConstants.BUCKET * 8)
            .catalyst(RagiumContents.Circuits.ADVANCED)
            .fluidOutput(RagiumFluids.NAPHTHA, FluidConstants.BUCKET * 5)
            .fluidOutput(RagiumFluids.RESIDUAL_OIL, FluidConstants.BUCKET * 3)
            .offerTo(exporter, RagiumFluids.FUEL)

        // refined gas -> alcohol + noble gas
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.REFINED_GAS, FluidConstants.BUCKET * 8)
            .catalyst(RagiumContents.Circuits.BASIC)
            .fluidOutput(RagiumFluids.ALCOHOL, FluidConstants.BUCKET * 6)
            .fluidOutput(RagiumFluids.NOBLE_GAS, FluidConstants.BUCKET * 2)
            .offerTo(exporter, RagiumFluids.ALCOHOL)

        // residual oil -> fuel + aromatic compound
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.RESIDUAL_OIL, FluidConstants.BUCKET * 8)
            .catalyst(RagiumContents.Circuits.PRIMITIVE)
            .fluidOutput(RagiumFluids.FUEL, FluidConstants.BUCKET * 3)
            .fluidOutput(RagiumFluids.AROMATIC_COMPOUNDS, FluidConstants.BUCKET * 5)
            .offerTo(exporter, RagiumFluids.AROMATIC_COMPOUNDS)

        // residual oil -> fuel + asphalt
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.RESIDUAL_OIL, FluidConstants.BUCKET * 8)
            .fluidOutput(RagiumFluids.FUEL, FluidConstants.BUCKET * 3)
            .fluidOutput(RagiumFluids.ASPHALT, FluidConstants.BUCKET * 5)
            .offerTo(exporter, RagiumFluids.ASPHALT)
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
            .itemOutput(RagiumAPI.getInstance().createFilledCube(RagiumFluids.SODIUM_HYDROXIDE.value))
            .fluidOutput(RagiumFluids.HYDROGEN)
            .fluidOutput(RagiumFluids.CHLORINE)
            .offerTo(exporter, RagiumFluids.SALT_WATER)
    }

    //    Extractor    //

    private fun extractor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(ItemTags.VILLAGER_PLANTABLE_SEEDS, 4)
            .fluidOutput(RagiumFluids.SEED_OIL)
            .offerTo(exporter, RagiumFluids.SEED_OIL)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(RagiumItemTags.PROTEIN_FOODS, 4)
            .fluidOutput(RagiumFluids.TALLOW)
            .offerTo(exporter, RagiumFluids.TALLOW)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Items.SMOOTH_BASALT)
            .itemOutput(RagiumItems.BASALT_MESH)
            .offerTo(exporter, RagiumItems.BASALT_MESH)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(RagiumItems.CHOCOLATE)
            .fluidOutput(RagiumFluids.CHOCOLATE)
            .offerTo(exporter, RagiumFluids.CHOCOLATE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Items.SUGAR, 4)
            .fluidOutput(RagiumFluids.STARCH_SYRUP)
            .offerTo(exporter, RagiumFluids.STARCH_SYRUP)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Items.SWEET_BERRIES, 4)
            .fluidOutput(RagiumFluids.SWEET_BERRIES)
            .offerTo(exporter, RagiumFluids.SWEET_BERRIES)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .fluidInput(ConventionalFluidTags.MILK)
            .itemOutput(RagiumItems.BUTTER)
            .offerTo(exporter, RagiumItems.BUTTER)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Items.SOUL_SAND)
            .itemOutput(Items.SAND)
            .fluidOutput(RagiumFluids.CRUDE_OIL)
            .offerTo(exporter, RagiumFluids.CRUDE_OIL, "_from_soul_sand")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Items.SOUL_SOIL)
            .itemOutput(Items.SAND)
            .fluidOutput(RagiumFluids.CRUDE_OIL)
            .offerTo(exporter, RagiumFluids.CRUDE_OIL, "_from_soul_soil")
    }

    //    Fluid Drill    //

    private fun fluidDrill(exporter: RecipeExporter) {
        registerDrilling(exporter, BiomeKeys.WARM_OCEAN, RagiumFluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.LUKEWARM_OCEAN, RagiumFluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.DEEP_LUKEWARM_OCEAN, RagiumFluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.OCEAN, RagiumFluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.DEEP_OCEAN, RagiumFluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.COLD_OCEAN, RagiumFluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.DEEP_COLD_OCEAN, RagiumFluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.FROZEN_OCEAN, RagiumFluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.DEEP_FROZEN_OCEAN, RagiumFluids.SALT_WATER)

        registerDrilling(exporter, BiomeKeys.NETHER_WASTES, Fluids.LAVA)
        registerDrilling(exporter, BiomeKeys.WARPED_FOREST, Fluids.LAVA)
        registerDrilling(exporter, BiomeKeys.CRIMSON_FOREST, Fluids.LAVA)
        registerDrilling(exporter, BiomeKeys.SOUL_SAND_VALLEY, RagiumFluids.CRUDE_OIL)
        registerDrilling(exporter, BiomeKeys.BASALT_DELTAS, Fluids.LAVA)
    }

    private fun registerDrilling(exporter: RecipeExporter, biomeKey: RegistryKey<Biome>, fluid: Fluid) {
        /*HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.FLUID_DRILL)
            .addOutput(fluid)
            .setCustomData(HTRecipeComponentTypes.BIOME, biomeKey)
            .offerTo(exporter, biomeKey.value)*/
    }

    private fun registerDrilling(exporter: RecipeExporter, biomeKey: RegistryKey<Biome>, fluid: RagiumFluids) {
        /*HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.FLUID_DRILL)
            .addOutput(fluid)
            .setCustomData(HTRecipeComponentTypes.BIOME, biomeKey)
            .offerTo(exporter, biomeKey.value)*/
    }

    //    Grinder    //

    private fun grinder(exporter: RecipeExporter) {
        registerGrinder(exporter, ConventionalItemTags.COBBLESTONES to 1, Items.GRAVEL to 1)
        registerGrinder(exporter, ConventionalItemTags.WHEAT_CROPS to 1, RagiumItems.FLOUR to 1)
        registerGrinder(exporter, Items.ANCIENT_DEBRIS to 1, Items.NETHERITE_SCRAP to 2)
        registerGrinder(exporter, Items.COARSE_DIRT to 1, Items.DIRT to 1)
        registerGrinder(exporter, Items.DEEPSLATE to 1, Items.COBBLED_DEEPSLATE to 1)
        registerGrinder(exporter, Items.GRAVEL to 1, Items.SAND to 1, suffix = "_from_gravel")
        registerGrinder(exporter, Items.STONE to 1, Items.COBBLESTONE to 1)
        registerGrinder(exporter, ItemTags.BOATS to 1, RagiumItems.PULP to 5, suffix = "_from_boat")
        registerGrinder(exporter, ItemTags.COAL_ORES to 1, Items.COAL to 2)
        registerGrinder(exporter, ItemTags.DIAMOND_ORES to 1, Items.DIAMOND to 2)
        registerGrinder(exporter, ItemTags.EMERALD_ORES to 1, Items.EMERALD to 2)
        registerGrinder(exporter, ItemTags.FENCE_GATES to 1, RagiumItems.PULP to 4, suffix = "_from_fence_gate")
        registerGrinder(exporter, ItemTags.LAPIS_ORES to 1, Items.LAPIS_LAZULI to 8)
        registerGrinder(exporter, ItemTags.LOGS to 1, RagiumItems.PULP to 4, suffix = "_from_log")
        registerGrinder(exporter, ItemTags.PLANKS to 1, RagiumItems.PULP to 1, suffix = "_from_plank")
        registerGrinder(exporter, ItemTags.REDSTONE_ORES to 1, Items.REDSTONE to 8)
        registerGrinder(exporter, ItemTags.SAPLINGS to 2, RagiumItems.PULP to 1, suffix = "_from_sapling")
        registerGrinder(exporter, ItemTags.WOODEN_BUTTONS to 1, RagiumItems.PULP to 1, suffix = "_from_button")
        registerGrinder(exporter, ItemTags.WOODEN_DOORS to 1, RagiumItems.PULP to 2, suffix = "_from_door")
        registerGrinder(exporter, ItemTags.WOODEN_FENCES to 3, RagiumItems.PULP to 5, suffix = "_from_fence")
        registerGrinder(exporter, ItemTags.WOODEN_PRESSURE_PLATES to 1, RagiumItems.PULP to 2, suffix = "_from_plate")
        registerGrinder(exporter, ItemTags.WOODEN_SLABS to 2, RagiumItems.PULP to 1, suffix = "_from_slab")
        registerGrinder(exporter, ItemTags.WOODEN_STAIRS to 4, RagiumItems.PULP to 6, suffix = "_from_stair")
        registerGrinder(exporter, ItemTags.WOODEN_TRAPDOORS to 1, RagiumItems.PULP to 3, suffix = "_from_trap_door")
        registerGrinder(exporter, ItemTags.WOOL to 1, Items.STRING to 4)
        registerGrinder(exporter, RagiumItemTags.PROTEIN_FOODS to 1, RagiumItems.MINCED_MEAT to 1)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(Items.NETHERRACK, 8)
            .itemOutput(Items.GRAVEL, 4)
            .itemOutput(RagiumContents.Dusts.SULFUR)
            .offerTo(exporter, Items.GRAVEL, "_from_nether")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(ConventionalItemTags.UNCOLORED_SANDSTONE_BLOCKS)
            .itemOutput(Items.SAND, 4)
            .itemOutput(RagiumContents.Dusts.NITER)
            .offerTo(exporter, Items.SAND)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(ConventionalItemTags.RED_SANDSTONE_BLOCKS)
            .itemOutput(Items.RED_SAND, 4)
            .itemOutput(RagiumContents.Dusts.BAUXITE)
            .offerTo(exporter, Items.RED_SAND)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(ConventionalItemTags.SUGAR_CANE_CROPS)
            .itemOutput(Items.SUGAR, 2)
            .itemOutput(RagiumItems.PULP)
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
            .create(RagiumMachineKeys.GRINDER)
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
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(input.first, input.second)
            .itemOutput(output.first, output.second)
            .offerTo(exporter, output.first, suffix)
    }

    //    Laser Transformer    //

    private fun laserTransformer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.LASER_TRANSFORMER, HTMachineTier.ADVANCED)
            .itemInput(RagiumContents.Gems.RAGI_CRYSTAL, 8)
            .itemOutput(RagiumContents.Gems.RAGIUM)
            .offerTo(exporter, RagiumContents.Gems.RAGIUM)
    }

    //    Metal Former    //

    private fun metalFormer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.METAL_FORMER)
            .itemInput(RagiumContents.Ingots.STEEL, 2)
            .itemOutput(RagiumBlocks.SHAFT)
            .catalyst(RagiumBlocks.SHAFT)
            .offerTo(exporter, RagiumContents.Dusts.RAGINITE)

        // pipes
        registerPipe(exporter, RagiumContents.Plates.IRON, RagiumContents.Pipes.IRON)
        registerPipe(exporter, RagiumContents.Plates.STEEL, RagiumContents.Pipes.STEEL)
        registerPipe(exporter, RagiumContents.Plates.COPPER, RagiumContents.Pipes.COPPER)
        registerPipe(exporter, RagiumContents.Plates.REFINED_RAGI_STEEL, RagiumContents.Pipes.UNIVERSAL)
    }

    private fun registerPipe(exporter: RecipeExporter, input: RagiumContents.Plates, output: RagiumContents.Pipes) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.METAL_FORMER, output.tier)
            .itemInput(input, 2)
            .itemOutput(output)
            .offerTo(exporter, output)
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
            .fluidInput(RagiumFluids.NITRIC_ACID)
            .fluidInput(RagiumFluids.SULFURIC_ACID)
            .fluidOutput(RagiumFluids.MIXTURE_ACID, FluidConstants.BUCKET * 2)
            .offerTo(exporter, RagiumFluids.MIXTURE_ACID)

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

        DyeColor.entries.forEach { color: DyeColor ->
            val powder: Block = Registries.BLOCK.get(Identifier.of("${color.asString()}_concrete_powder"))
            val concrete: Block = Registries.BLOCK.get(Identifier.of("${color.asString()}_concrete"))
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
            .create(RagiumMachineKeys.ROCK_GENERATOR)
            .fluidInput(Fluids.LAVA)
            .itemOutput(Items.OBSIDIAN)
            .offerTo(exporter, Items.OBSIDIAN)
    }

    private fun registerRock(exporter: RecipeExporter, rock: ItemConvertible) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ROCK_GENERATOR)
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
            .create(RagiumMachineKeys.SAW_MILL)
            .itemInput(ItemTags.PLANKS)
            .itemOutput(Items.STICK, 4)
            .offerTo(exporter, Items.STICK)
    }

    private fun registerPlank(exporter: RecipeExporter, log: TagKey<Item>, plank: ItemConvertible) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.SAW_MILL)
            .itemInput(log)
            .itemOutput(plank, 6)
            .itemOutput(RagiumItems.PULP)
            .offerTo(exporter, plank)
    }

    //    Mob Extractor    //

    private fun mobExtractor(exporter: RecipeExporter) {
        registerMobDrop(exporter, Items.ARMADILLO_SCUTE, EntityType.ARMADILLO)
        registerMobDrop(exporter, Items.HONEYCOMB, EntityType.BEE)
        // registerMobDrop(exporter, RagiumFluids.SULFURIC_ACID, EntityType.BLAZE)
        // registerMobDrop(exporter, RagiumFluids.NITRIC_ACID, EntityType.BREEZE)
        registerMobDrop(exporter, Items.EGG, EntityType.CHICKEN)
        // registerMobDrop(exporter, RagiumFluids.MILK, EntityType.COW)
        registerMobDrop(exporter, Items.GLOW_INK_SAC, EntityType.GLOW_SQUID)
        registerMobDrop(exporter, Items.IRON_NUGGET, EntityType.IRON_GOLEM)
        // registerMobDrop(exporter, RagiumFluids.MILK, EntityType.MOOSHROOM) // TODO
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
            .create(RagiumMachineKeys.MOB_EXTRACTOR)
            .addOutput(Items.RED_MUSHROOM)
            .addOutput(Items.BROWN_MUSHROOM)
            .setCustomData(HTRecipeComponentTypes.ENTITY_TYPE, EntityType.BOGGED)
            .offerTo(exporter)*/
    }

    private fun registerMobDrop(exporter: RecipeExporter, output: ItemConvertible, entityType: EntityType<*>) {
        /*HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.MOB_EXTRACTOR)
            .addOutput(output)
            .setCustomData(HTRecipeComponentTypes.ENTITY_TYPE, entityType)
            .offerTo(exporter, Registries.ENTITY_TYPE.getId(entityType))*/
    }
}
