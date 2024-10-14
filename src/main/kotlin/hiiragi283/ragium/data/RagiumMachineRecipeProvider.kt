package hiiragi283.ragium.data

import hiiragi283.ragium.api.data.HTInfusionRecipeJsonBuilder
import hiiragi283.ragium.api.data.HTMachineRecipeJsonBuilder
import hiiragi283.ragium.api.data.HTMetalItemRecipeRegistry
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.machine.HTRecipeComponentTypes
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.data.HTHardModeResourceCondition
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineTypes
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.registry.Registries
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
        compressor(exporter)
        fluidDrill(exporter)
        decompressor(exporter)
        distillation(exporter)
        electrolyzer(exporter)
        extractor(exporter)
        grinder(exporter)
        metalFormer(exporter)
        mixer(exporter)
        mobExtractor(exporter)
        rockGenerator(exporter)
        sawMill(exporter)
        // alchemy
        infusion(exporter)
        transform(exporter)
        // patterned
        RagiumMetalItemRecipes.init()
        HTMetalItemRecipeRegistry.generateRecipes(exporter, ::exporterWrapper)
    }

    private fun exporterWrapper(exporter: RecipeExporter, bool: Boolean): RecipeExporter =
        withConditions(exporter, HTHardModeResourceCondition.fromBool((bool)))

    //    Alloy Furnace    //

    private fun alloyFurnaceRecipes(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ALLOY_FURNACE)
            .addInput(ConventionalItemTags.COPPER_INGOTS)
            .addInput(RagiumContents.Dusts.CRUDE_RAGINITE, 4)
            .addOutput(RagiumContents.Ingots.RAGI_ALLOY)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ALLOY_FURNACE)
            .addInput(ConventionalItemTags.COPPER_INGOTS)
            .addInput(RagiumContents.Dusts.RAGINITE)
            .addOutput(RagiumContents.Ingots.RAGI_ALLOY)
            .offerSuffix(exporter, "_alt")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ALLOY_FURNACE)
            .addInput(ConventionalItemTags.IRON_INGOTS, 2)
            .addInput(RagiumItemTags.NICKEL_INGOTS)
            .addOutput(RagiumContents.Ingots.INVAR, 3)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ALLOY_FURNACE, HTMachineTier.ADVANCED, true)
            .addInput(ConventionalItemTags.GOLD_INGOTS, 5)
            .addInput(Items.NETHERITE_SCRAP, 3)
            .addOutput(Items.NETHERITE_INGOT)
            .offerTo(exporter)
    }

    //    Assembler    //

    private fun assembler(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER)
            .addInput(RagiumContents.Plates.PE, 8)
            .addInput(Items.CHEST)
            .addOutput(RagiumContents.Accessories.BACKPACK)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER, HTMachineTier.BASIC)
            .addInput(RagiumContents.Plates.PVC, 8)
            .addInput(Items.CHEST)
            .addOutput(RagiumContents.Accessories.LARGE_BACKPACK)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER, HTMachineTier.ADVANCED)
            .addInput(RagiumContents.Plates.PTFE, 8)
            .addInput(Items.ENDER_EYE)
            .addOutput(RagiumContents.Accessories.ENDER_BACKPACK)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER)
            .addInput(RagiumContents.Plates.PE)
            .addInput(RagiumItemTags.COPPER_PLATES)
            .addInput(ConventionalItemTags.REDSTONE_DUSTS)
            .addOutput(RagiumContents.Circuits.PRIMITIVE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER, HTMachineTier.BASIC)
            .addInput(RagiumContents.Plates.PVC)
            .addInput(RagiumItemTags.GOLD_PLATES)
            .addInput(ConventionalItemTags.GLOWSTONE_DUSTS)
            .addOutput(RagiumContents.Circuits.BASIC)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER, HTMachineTier.ADVANCED)
            .addInput(RagiumContents.Plates.PTFE)
            .addInput(RagiumContents.Plates.RAGI_ALLOY)
            .addInput(RagiumContents.Dusts.RAGI_CRYSTAL)
            .addOutput(RagiumContents.Circuits.ADVANCED)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER, HTMachineTier.BASIC)
            .addInput(RagiumContents.Misc.BASALT_FIBER, 4)
            .addOutput(RagiumContents.Plates.BASALT_FIBER)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER, HTMachineTier.BASIC)
            .addInput(RagiumContents.Fluids.MOLTEN_BASALT)
            .addOutput(RagiumContents.Misc.BASALT_FIBER)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER, HTMachineTier.BASIC)
            .addInput(RagiumItemTags.STEEL_PLATES, 8)
            .addInput(RagiumContents.Plates.RAGI_STEEL, 8)
            .addOutput(RagiumContents.Misc.ENGINE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER)
            .addInput(RagiumItemTags.IRON_PLATES, 8)
            .addInput(RagiumContents.Coils.COPPER)
            .addOutput(RagiumContents.Motors.PRIMITIVE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER)
            .addInput(RagiumItemTags.IRON_PLATES, 8)
            .addInput(RagiumContents.Coils.GOLD)
            .addOutput(RagiumContents.Motors.BASIC)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER)
            .addInput(RagiumItemTags.IRON_PLATES, 8)
            .addInput(RagiumContents.Coils.RAGI_ALLOY)
            .addOutput(RagiumContents.Motors.ADVANCED)
            .offerTo(exporter)
    }

    //    Blast Furnace    //

    private fun blastFurnace(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.BLAST_FURNACE)
            .addInput(ConventionalItemTags.IRON_INGOTS)
            .addInput(RagiumContents.Dusts.RAGINITE, 4)
            .addOutput(RagiumContents.Ingots.RAGI_STEEL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.BLAST_FURNACE)
            .addInput(ConventionalItemTags.IRON_INGOTS)
            .addInput(ItemTags.COALS, 4)
            .addOutput(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.BLAST_FURNACE, HTMachineTier.BASIC)
            .addInput(ConventionalItemTags.REDSTONE_DUSTS, 4)
            .addInput(RagiumContents.Dusts.RAGINITE, 5)
            .addOutput(RagiumContents.Misc.RAGI_CRYSTAL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.BLAST_FURNACE, HTMachineTier.BASIC)
            .addInput(RagiumItemTags.STEEL_INGOTS)
            .addInput(RagiumContents.Dusts.RAGI_CRYSTAL, 4)
            .addInput(ConventionalItemTags.QUARTZ_GEMS)
            .addOutput(RagiumContents.Ingots.REFINED_RAGI_STEEL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.BLAST_FURNACE, HTMachineTier.BASIC)
            .addInput(ConventionalItemTags.QUARTZ_GEMS, 2)
            .addInput(ItemTags.COALS, 4)
            .addOutput(RagiumContents.Plates.SILICON)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.BLAST_FURNACE)
            .addInput(RagiumContents.Fluids.BATTER)
            .addInput(RagiumContents.Foods.BUTTER)
            .addOutput(RagiumBlocks.SPONGE_CAKE)
            .offerTo(exporter)
    }

    //    Chemical Reactor    //

    private fun chemicalReactor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(RagiumContents.Fluids.WATER)
            .addInput(RagiumContents.Fluids.METHANE)
            .setCatalyst(Items.HEART_OF_THE_SEA)
            .addOutput(RagiumContents.Fluids.HYDROGEN)
            .addOutput(RagiumContents.Fluids.METHANOL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, HTMachineTier.BASIC)
            .addInput(RagiumItemTags.SULFUR_DUSTS)
            .addInput(RagiumContents.Fluids.WATER)
            .addInput(RagiumContents.Fluids.OXYGEN)
            .addOutput(RagiumContents.Fluids.SULFURIC_ACID)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, HTMachineTier.BASIC)
            .addInput(RagiumContents.Dusts.NITER)
            .addInput(RagiumContents.Fluids.WATER)
            .addOutput(RagiumContents.Fluids.NITRIC_ACID)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(RagiumContents.Fluids.PETROLEUM)
            .addInput(RagiumContents.Fluids.HYDROGEN)
            .addOutput(RagiumContents.Fluids.CRUDE_OIL)
            .addOutput(RagiumContents.Dusts.SULFUR)
            .offerTo(exporter)

        // Dynamite
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, HTMachineTier.BASIC)
            .addInput(RagiumContents.Fluids.GLYCEROL)
            .addInput(RagiumContents.Fluids.MIXTURE_ACID, 3)
            .addOutput(RagiumContents.Fluids.NITRO_GLYCERIN)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE, 3)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(Items.PAPER)
            .addInput(ConventionalItemTags.STRINGS)
            .addInput(RagiumContents.Fluids.NITRO_GLYCERIN)
            .addOutput(RagiumContents.Misc.DYNAMITE, 2)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)
        // TNT
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .addInput(RagiumContents.Fluids.TOLUENE)
            .addInput(RagiumContents.Fluids.MIXTURE_ACID, 3)
            .addOutput(RagiumContents.Fluids.TRINITROTOLUENE)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE, 3)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .addInput(RagiumContents.Fluids.TRINITROTOLUENE)
            .addInput(ItemTags.SAND)
            .addOutput(Items.TNT, 12)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        // PE
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(RagiumContents.Fluids.ETHYLENE)
            .addInput(RagiumContents.Fluids.OXYGEN)
            .addOutput(RagiumContents.Plates.PE)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        // PVC
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(RagiumContents.Fluids.ETHYLENE)
            .addInput(RagiumContents.Fluids.CHLORINE)
            .addOutput(RagiumContents.Fluids.VINYL_CHLORIDE)
            .addOutput(RagiumContents.Fluids.HYDROGEN)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(RagiumContents.Fluids.VINYL_CHLORIDE)
            .addInput(RagiumContents.Fluids.OXYGEN)
            .addOutput(RagiumContents.Plates.PVC)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        // PTFE
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(RagiumContents.Fluids.ETHYLENE)
            .addInput(RagiumContents.Fluids.FLUORINE, 2)
            .addOutput(RagiumContents.Fluids.TETRA_FLUORO_ETHYLENE)
            .addOutput(RagiumContents.Fluids.HYDROGEN, 2)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(RagiumContents.Fluids.TETRA_FLUORO_ETHYLENE)
            .addInput(RagiumContents.Fluids.OXYGEN)
            .addOutput(RagiumContents.Plates.PTFE)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

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
            .addInput(before)
            .addInput(RagiumContents.Fluids.OXYGEN)
            .addOutput(after)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)
    }

    //    Compressor    //

    private fun compressor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.COMPRESSOR)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addInput(Items.WATER_BUCKET)
            .addOutput(RagiumContents.Fluids.WATER)
            .addOutput(Items.BUCKET)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.COMPRESSOR)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addInput(Items.LAVA_BUCKET)
            .addOutput(RagiumContents.Fluids.LAVA)
            .addOutput(Items.BUCKET)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.COMPRESSOR)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addInput(Items.MILK_BUCKET)
            .addOutput(RagiumContents.Fluids.MILK)
            .addOutput(Items.BUCKET)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.COMPRESSOR)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addInput(Items.HONEY_BOTTLE, 4)
            .addOutput(RagiumContents.Fluids.HONEY)
            .addOutput(Items.GLASS_BOTTLE, 4)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.COMPRESSOR)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addInput(Items.HONEY_BLOCK)
            .addOutput(RagiumContents.Fluids.HONEY)
            .offerSuffix(exporter, suffix = "_alt")
    }

    //    Decompressor    //

    private fun decompressor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.DECOMPRESSOR)
            .addInput(Items.KELP, 64)
            .addOutput(Items.DRIED_KELP, 64)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.DECOMPRESSOR)
            .addInput(ConventionalItemTags.STORAGE_BLOCKS_DRIED_KELP, 64)
            .addOutput(Items.DRIED_KELP_BLOCK, 64)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.DECOMPRESSOR)
            .addInput(RagiumContents.Fluids.LAVA)
            .addOutput(RagiumBlocks.POROUS_NETHERRACK)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.DECOMPRESSOR)
            .addInput(Items.MUD)
            .addOutput(Items.CLAY)
            .offerTo(exporter)
    }

    //    Distillation Tower    //

    private fun distillation(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.DISTILLATION_TOWER)
            .addInput(RagiumContents.Fluids.CRUDE_OIL, 4)
            .addOutput(RagiumContents.Fluids.REFINED_GAS)
            .addOutput(RagiumContents.Fluids.NAPHTHA, 2)
            .addOutput(RagiumContents.Fluids.TAR)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.DISTILLATION_TOWER)
            .addInput(RagiumContents.Fluids.REFINED_GAS, 4)
            .addOutput(RagiumContents.Fluids.METHANE, 2)
            .addOutput(RagiumContents.Fluids.LPG)
            .addOutput(RagiumContents.Fluids.HELIUM)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.DISTILLATION_TOWER)
            .addInput(RagiumContents.Fluids.NAPHTHA, 2)
            .addOutput(RagiumContents.Fluids.ETHYLENE)
            .addOutput(RagiumContents.Fluids.DIESEL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.DISTILLATION_TOWER)
            .addInput(RagiumContents.Fluids.TAR, 3)
            .addOutput(RagiumContents.Fluids.BENZENE)
            .addOutput(RagiumContents.Fluids.TOLUENE)
            .addOutput(RagiumContents.Fluids.PHENOL)
            .offerTo(exporter)
    }

    //    Electrolyzer    //

    private fun electrolyzer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ELECTROLYZER)
            .addInput(RagiumContents.Fluids.WATER)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE, 2)
            .addOutput(RagiumContents.Fluids.HYDROGEN, 2)
            .addOutput(RagiumContents.Fluids.OXYGEN)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ELECTROLYZER)
            .addInput(RagiumContents.Fluids.SALT_WATER)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE, 2)
            .addOutput(RagiumContents.Fluids.HYDROGEN)
            .addOutput(RagiumContents.Fluids.CHLORINE)
            .addOutput(RagiumContents.Fluids.SODIUM_HYDROXIDE)
            .offerSuffix(exporter, suffix = "_salty")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ELECTROLYZER)
            .addInput(Items.GLOWSTONE_DUST)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addOutput(RagiumContents.Fluids.FLUORINE)
            .addOutput(Items.GOLD_NUGGET)
            .offerTo(exporter)
    }

    //    Extractor    //

    private fun extractor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .addInput(ItemTags.VILLAGER_PLANTABLE_SEEDS, 4)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addOutput(RagiumContents.Fluids.SEED_OIL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .addInput(RagiumItemTags.PROTEIN_FOODS)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addOutput(RagiumContents.Fluids.TALLOW)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .addInput(RagiumItemTags.BASALTS)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addOutput(RagiumContents.Fluids.MOLTEN_BASALT)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .addInput(RagiumContents.Foods.CHOCOLATE)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addOutput(RagiumContents.Fluids.CHOCOLATE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .addInput(Items.SUGAR, 4)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addOutput(RagiumContents.Fluids.STARCH_SYRUP)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .addInput(Items.SWEET_BERRIES)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addOutput(RagiumContents.Fluids.SWEET_BERRIES)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .addInput(RagiumContents.Fluids.MILK)
            .addOutput(RagiumContents.Foods.BUTTER)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR, HTMachineTier.BASIC)
            .addInput(Items.SOUL_SAND)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addOutput(Items.SAND)
            .addOutput(RagiumContents.Fluids.CRUDE_OIL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR, HTMachineTier.BASIC)
            .addInput(Items.SOUL_SOIL)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addOutput(Items.CLAY)
            .addOutput(RagiumContents.Fluids.CRUDE_OIL)
            .offerTo(exporter)
    }

    //    Grinder    //

    private fun grinder(exporter: RecipeExporter) {
        registerGrinder(exporter, ConventionalItemTags.COBBLESTONES to 1, Items.GRAVEL to 1)
        registerGrinder(exporter, ConventionalItemTags.QUARTZ_ORES to 1, Items.QUARTZ to 2)
        registerGrinder(exporter, ConventionalItemTags.RED_SANDSTONE_BLOCKS to 1, Items.RED_SAND to 4)
        registerGrinder(exporter, ConventionalItemTags.WHEAT_CROPS to 1, RagiumContents.Foods.FLOUR to 1)
        registerGrinder(exporter, Items.ANCIENT_DEBRIS to 1, Items.NETHERITE_SCRAP to 2)
        registerGrinder(exporter, Items.COARSE_DIRT to 1, Items.DIRT to 1)
        registerGrinder(exporter, Items.DEEPSLATE to 1, Items.COBBLED_DEEPSLATE to 1)
        registerGrinder(exporter, Items.GRAVEL to 1, Items.SAND to 1, suffix = "_from_gravel")
        registerGrinder(exporter, Items.NETHERRACK to 4, RagiumContents.Dusts.SULFUR to 1)
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
            .addInput(ConventionalItemTags.SUGAR_CANE_CROPS)
            .addOutput(Items.SUGAR, 2)
            .addOutput(RagiumContents.Foods.PULP)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.GRINDER)
            .addInput(ConventionalItemTags.UNCOLORED_SANDSTONE_BLOCKS)
            .addOutput(Items.SAND, 4)
            .addOutput(RagiumContents.Dusts.NITER)
            .offerTo(exporter)
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
            .addInput(input.first, input.second)
            .addOutput(output.first, output.second)
            .offerSuffix(exporter, suffix)
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
            .addInput(input.first, input.second)
            .addOutput(output.first, output.second)
            .offerSuffix(exporter, suffix)
    }

    //    Metal Former    //

    private fun metalFormer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.METAL_FORMER)
            .addInput(RagiumItemTags.STEEL_INGOTS, 2)
            .addOutput(RagiumBlocks.SHAFT)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.METAL_FORMER)
            .addInput(ConventionalItemTags.COPPER_INGOTS, 8)
            .addInput(RagiumBlocks.SHAFT)
            .addOutput(RagiumContents.Coils.COPPER)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.METAL_FORMER)
            .addInput(ConventionalItemTags.GOLD_INGOTS, 8)
            .addInput(RagiumBlocks.SHAFT)
            .addOutput(RagiumContents.Coils.GOLD)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.METAL_FORMER)
            .addInput(RagiumContents.Ingots.RAGI_ALLOY, 8)
            .addInput(RagiumBlocks.SHAFT)
            .addOutput(RagiumContents.Coils.RAGI_ALLOY)
            .offerTo(exporter)
    }

    //    Mixer    //

    private fun mixer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumContents.Dusts.CRUDE_RAGINITE, 4)
            .addInput(RagiumContents.Fluids.WATER)
            .addOutput(RagiumContents.Dusts.RAGINITE, 4)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumItemTags.ORGANIC_OILS)
            .addInput(RagiumItemTags.ALKALI)
            .addOutput(RagiumContents.Misc.SOAP_INGOT)
            .addOutput(RagiumContents.Fluids.GLYCEROL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumContents.Foods.FLOUR, 3)
            .addInput(RagiumContents.Fluids.WATER)
            .addOutput(RagiumContents.Foods.DOUGH, 3)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumContents.Foods.PULP)
            .addInput(RagiumContents.Fluids.WATER)
            .addOutput(Items.PAPER)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumContents.Foods.PULP)
            .addInput(RagiumContents.Fluids.SODIUM_HYDROXIDE)
            .addOutput(Items.PAPER, 2)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerSuffix(exporter, "_alkali")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumContents.Foods.PULP)
            .addInput(RagiumContents.Fluids.WATER)
            .addInput(RagiumContents.Fluids.SULFURIC_ACID)
            .addOutput(Items.PAPER, 4)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE, 2)
            .offerSuffix(exporter, "_sulfuric_acid")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER, HTMachineTier.BASIC)
            .addInput(RagiumContents.Fluids.NITRIC_ACID)
            .addInput(RagiumContents.Fluids.SULFURIC_ACID)
            .addOutput(RagiumContents.Fluids.MIXTURE_ACID, 2)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumContents.Foods.BUTTER)
            .addInput(Items.SUGAR)
            .addInput(RagiumContents.Fluids.MILK)
            .addOutput(RagiumContents.Foods.CARAMEL, 4)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(Items.COCOA_BEANS)
            .addInput(Items.SUGAR)
            .addInput(RagiumContents.Fluids.MILK)
            .addOutput(RagiumContents.Foods.CHOCOLATE)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumContents.Fluids.MILK)
            .addInput(RagiumContents.Foods.FLOUR)
            .addInput(Items.SUGAR)
            .addOutput(RagiumContents.Fluids.BATTER)
            .offerTo(exporter)

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
            .addInput(input)
            .addInput(RagiumContents.Misc.SOAP_INGOT)
            .addInput(RagiumContents.Fluids.WATER)
            .addOutput(output)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(input)
            .addInput(RagiumContents.Fluids.CHLORINE)
            .addOutput(output)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)
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
            .addInput(RagiumContents.Fluids.LAVA)
            .addOutput(Items.OBSIDIAN)
            .offerTo(exporter)
    }

    private fun registerRock(exporter: RecipeExporter, rock: ItemConvertible) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ROCK_GENERATOR)
            .setCatalyst(rock)
            .addOutput(rock, 8)
            .offerTo(exporter)
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
            .addInput(ItemTags.PLANKS)
            .addOutput(Items.STICK, 4)
            .offerTo(exporter)
    }

    private fun registerPlank(exporter: RecipeExporter, log: TagKey<Item>, plank: ItemConvertible) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.SAW_MILL)
            .addInput(log)
            .addOutput(plank, 6)
            .addOutput(RagiumContents.Foods.PULP)
            .offerTo(exporter)
    }

    //    Alchemical Infusion    //

    private fun infusion(exporter: RecipeExporter) {
        HTInfusionRecipeJsonBuilder(Items.ENCHANTED_GOLDEN_APPLE)
            .addInput(Items.APPLE)
            .addInput(ConventionalItemTags.STORAGE_BLOCKS_GOLD, 8)
            .addInput(Items.ENCHANTED_BOOK)
            .hasInput(Items.ENCHANTED_GOLDEN_APPLE)
            .offerTo(exporter)

        HTInfusionRecipeJsonBuilder(Items.HEART_OF_THE_SEA)
            .addInput(Items.PRISMARINE_SHARD, 64)
            .addInput(Items.PRISMARINE_CRYSTALS, 64)
            .addInput(ConventionalItemTags.LAPIS_GEMS, 64)
            .hasInput(Items.AXOLOTL_BUCKET)
            .offerTo(exporter)

        // buddings
        registerBudding(
            exporter,
            RagiumContents.Element.RAGIUM,
            Items.RED_STAINED_GLASS,
            Items.MANGROVE_LOG,
            Items.BLAZE_POWDER,
        )
        registerBudding(
            exporter,
            RagiumContents.Element.RIGIUM,
            Items.YELLOW_STAINED_GLASS,
            Items.GLOWSTONE,
            Items.GOLDEN_APPLE,
        )
        registerBudding(
            exporter,
            RagiumContents.Element.RUGIUM,
            Items.LIME_STAINED_GLASS,
            Items.MELON,
            Items.ENDER_PEARL,
        )
        registerBudding(
            exporter,
            RagiumContents.Element.REGIUM,
            Items.LIGHT_BLUE_STAINED_GLASS,
            Items.BLUE_ICE,
            Items.PRISMARINE_SHARD,
        )
        registerBudding(
            exporter,
            RagiumContents.Element.ROGIUM,
            Items.PURPLE_STAINED_GLASS,
            Items.AMETHYST_BLOCK,
            Items.SHULKER_SHELL,
        )
        // pendant
        registerPendant(exporter, RagiumContents.Element.RAGIUM, Items.MAGMA_BLOCK, 64)
        registerPendant(exporter, RagiumContents.Element.RIGIUM, RagiumContents.Fluids.MILK, 64)
        registerPendant(exporter, RagiumContents.Element.RUGIUM, Items.EMERALD_BLOCK, 32)
        registerPendant(exporter, RagiumContents.Element.REGIUM, Items.HEART_OF_THE_SEA, 8)
        registerPendant(exporter, RagiumContents.Element.ROGIUM, Items.AMETHYST_BLOCK, 64)
    }

    private fun registerBudding(
        exporter: RecipeExporter,
        element: RagiumContents.Element,
        glass: ItemConvertible,
        ing1: ItemConvertible,
        ing2: ItemConvertible,
    ) {
        HTInfusionRecipeJsonBuilder(element.buddingBlock)
            .addInput(Items.CHISELED_QUARTZ_BLOCK)
            .addInput(glass, 64)
            .addInput(ing1, 16)
            .addInput(ing2, 8)
            .hasInput(RagiumBlocks.ALCHEMICAL_INFUSER)
            .offerTo(exporter)
    }

    private fun registerPendant(
        exporter: RecipeExporter,
        element: RagiumContents.Element,
        ing1: ItemConvertible,
        count1: Int,
    ) {
        HTInfusionRecipeJsonBuilder(element.pendantItem)
            .addInput(RagiumItemTags.SILVER_PLATES, 32)
            .addInput(element.dustItem, 64)
            .addInput(ing1, count1)
            .hasInput(element.dustItem)
            .offerTo(exporter)
    }

    //    Alchemical Transform    //

    private fun transform(exporter: RecipeExporter) {
        /*HTTransformRecipeJsonBuilder(Items.DIAMOND, Items.COAL_BLOCK)
            .addUpgrade(Items.COAL_BLOCK, 7)
            .hasInput(Items.COAL_BLOCK)
            .modifyComponents {
                add(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
            }.offerTo(exporter)*/
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

        registerDrilling(exporter, BiomeKeys.NETHER_WASTES, RagiumContents.Fluids.LAVA)
        registerDrilling(exporter, BiomeKeys.WARPED_FOREST, RagiumContents.Fluids.LAVA)
        registerDrilling(exporter, BiomeKeys.CRIMSON_FOREST, RagiumContents.Fluids.LAVA)
        registerDrilling(exporter, BiomeKeys.SOUL_SAND_VALLEY, RagiumContents.Fluids.CRUDE_OIL)
        registerDrilling(exporter, BiomeKeys.BASALT_DELTAS, RagiumContents.Fluids.LAVA)
    }

    private fun registerDrilling(exporter: RecipeExporter, biomeKey: RegistryKey<Biome>, fluid: RagiumContents.Fluids) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.FLUID_DRILL)
            .addOutput(fluid)
            .setCustomData(HTRecipeComponentTypes.BIOME, biomeKey)
            .offerTo(exporter, biomeKey.value)
        /*exporter.accept(
            biomeKey.value.withPrefixedPath("fluid_drill/"),
            HTFluidDrillRecipe(biomeKey, HTRecipeResult.item(fluid)),
            null,
        )*/
    }

    //    Mob Extractor    //

    private fun mobExtractor(exporter: RecipeExporter) {
        registerMobDrop(exporter, Items.ARMADILLO_SCUTE, EntityType.ARMADILLO)
        registerMobDrop(exporter, Items.HONEYCOMB, EntityType.BEE)
        registerMobDrop(exporter, Items.BLAZE_POWDER, EntityType.BLAZE)
        registerMobDrop(exporter, Items.WIND_CHARGE, EntityType.BREEZE)
        registerMobDrop(exporter, Items.EGG, EntityType.CHICKEN)
        registerMobDrop(exporter, RagiumContents.Fluids.MILK, EntityType.COW)
        registerMobDrop(exporter, Items.GLOW_INK_SAC, EntityType.GLOW_SQUID)
        registerMobDrop(exporter, Items.IRON_NUGGET, EntityType.IRON_GOLEM)
        registerMobDrop(exporter, RagiumContents.Fluids.MILK, EntityType.MOOSHROOM) // TODO
        registerMobDrop(exporter, Items.WHITE_WOOL, EntityType.SHEEP)
        registerMobDrop(exporter, Items.SNOWBALL, EntityType.SNOW_GOLEM)
        registerMobDrop(exporter, Items.INK_SAC, EntityType.SQUID)
        registerMobDrop(exporter, Items.TURTLE_SCUTE, EntityType.TURTLE)
        registerMobDrop(exporter, Items.EMERALD, EntityType.WANDERING_TRADER)
        registerMobDrop(exporter, Items.ECHO_SHARD, EntityType.WARDEN) // TODO
        registerMobDrop(exporter, Items.WITHER_ROSE, EntityType.WITHER)
        registerMobDrop(exporter, Items.PLAYER_HEAD, EntityType.PLAYER)

        registerMobDrop(exporter, RagiumContents.Misc.OBLIVION_CRYSTAL, RagiumEntityTypes.OBLIVION_CUBE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.MOB_EXTRACTOR)
            .addOutput(Items.RED_MUSHROOM)
            .addOutput(Items.BROWN_MUSHROOM)
            .setCustomData(HTRecipeComponentTypes.ENTITY_TYPE, EntityType.BOGGED)
            .offerTo(exporter)
    }

    private fun registerMobDrop(exporter: RecipeExporter, output: ItemConvertible, entityType: EntityType<*>) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.MOB_EXTRACTOR)
            .addOutput(output)
            .setCustomData(HTRecipeComponentTypes.ENTITY_TYPE, entityType)
            .offerTo(exporter, Registries.ENTITY_TYPE.getId(entityType))
    }
}
