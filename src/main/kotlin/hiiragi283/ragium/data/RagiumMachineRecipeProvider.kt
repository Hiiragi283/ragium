package hiiragi283.ragium.data

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.data.HTHardModeResourceCondition
import hiiragi283.ragium.common.data.HTInfusionRecipeJsonBuilder
import hiiragi283.ragium.common.data.HTMachineRecipeJsonBuilder
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.tags.RagiumItemTags
import hiiragi283.ragium.data.util.HTMetalItemRecipeGroup
import hiiragi283.ragium.data.util.RagiumMetalItemRecipeGroups
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture

class RagiumMachineRecipeProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricRecipeProvider(output, registriesFuture) {
    override fun getName(): String = "Recipes/Machine"

    override fun generate(exporter: RecipeExporter) {
        alloyFurnaceRecipes(exporter)
        assembler(exporter)
        blastFurnace(exporter)
        centrifuge(exporter)
        chemicalReactor(exporter)
        compressor(exporter)
        distillation(exporter)
        electrolyzer(exporter)
        extractor(exporter)
        grinderRecipes(exporter)
        metalFormer(exporter)
        mixer(exporter)
        rockGenerator(exporter)
        // alchemy
        infusion(exporter)
        transform(exporter)
        // patterned
        RagiumMetalItemRecipeGroups
        HTMetalItemRecipeGroup.registry.forEach { (_: String, family: HTMetalItemRecipeGroup) ->
            family.generateRecipes(exporter, ::exporterWrapper)
        }
    }

    private fun exporterWrapper(exporter: RecipeExporter, bool: Boolean): RecipeExporter =
        withConditions(exporter, HTHardModeResourceCondition.fromBool((bool)))

    //    Grinder    //

    private fun grinderRecipes(exporter: RecipeExporter) {
        registerGrinderTag(exporter, ConventionalItemTags.QUARTZ_ORES to 1, Items.QUARTZ to 2)
        registerGrinderTag(exporter, ConventionalItemTags.RED_SANDSTONE_BLOCKS to 1, Items.RED_SAND to 4)
        registerGrinderTag(exporter, ConventionalItemTags.UNCOLORED_SANDSTONE_BLOCKS to 1, Items.SAND to 4)
        registerGrinderItem(exporter, Items.COARSE_DIRT to 1, Items.DIRT to 1)
        registerGrinderItem(exporter, Items.COBBLESTONE to 1, Items.GRAVEL to 1)
        registerGrinderItem(exporter, Items.DEEPSLATE to 1, Items.DEEPSLATE to 1)
        registerGrinderItem(exporter, Items.GRAVEL to 1, Items.SAND to 1, id = Identifier.of("gravel_to_sand"))
        registerGrinderItem(exporter, Items.STONE to 1, Items.COBBLESTONE to 1)
        registerGrinderTag(exporter, ItemTags.COAL_ORES to 1, Items.COAL to 2)
        registerGrinderTag(exporter, ItemTags.DIAMOND_ORES to 1, Items.DIAMOND to 2)
        registerGrinderTag(exporter, ItemTags.EMERALD_ORES to 1, Items.EMERALD to 2)
        registerGrinderTag(exporter, ItemTags.LAPIS_ORES to 1, Items.LAPIS_LAZULI to 8)
        registerGrinderTag(exporter, ItemTags.REDSTONE_ORES to 1, Items.REDSTONE to 8)
        registerGrinderTag(exporter, ItemTags.WOOL to 1, Items.STRING to 4)

        registerGrinderItem(exporter, RagiumContents.RAGI_CRYSTAL to 1, RagiumContents.Dusts.RAGI_CRYSTAL to 1)
        registerGrinderItem(exporter, Items.NETHERRACK to 4, RagiumContents.Dusts.SULFUR to 1)
    }

    private fun registerGrinderItem(
        exporter: RecipeExporter,
        input: Pair<ItemConvertible, Int>,
        output: Pair<ItemConvertible, Int>,
        id: Identifier = CraftingRecipeJsonBuilder.getItemId(output.first),
    ) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.GRINDER)
            .addInput(input.first, input.second)
            .addOutput(output.first, output.second)
            .offerTo(exporter, id)
    }

    private fun registerGrinderTag(
        exporter: RecipeExporter,
        input: Pair<TagKey<Item>, Int>,
        output: Pair<ItemConvertible, Int>,
        id: Identifier = CraftingRecipeJsonBuilder.getItemId(output.first),
    ) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.GRINDER)
            .addInput(input.first, input.second)
            .addOutput(output.first, output.second)
            .offerTo(exporter, id)
    }

    //    Alloy Furnace    //

    private fun alloyFurnaceRecipes(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ALLOY_FURNACE)
            .addInput(ConventionalItemTags.COPPER_INGOTS)
            .addInput(RagiumContents.Dusts.RAW_RAGINITE, 4)
            .addOutput(RagiumContents.Ingots.RAGI_ALLOY)
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
            .addOutput(RagiumContents.BACKPACK)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER)
            .addInput(RagiumContents.Plates.PVC, 8)
            .addInput(Items.CHEST)
            .addOutput(RagiumContents.LARGE_BACKPACK)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER)
            .addInput(RagiumContents.Plates.PTFE, 8)
            .addInput(Items.ENDER_EYE)
            .addOutput(RagiumContents.ENDER_BACKPACK)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER)
            .addInput(RagiumContents.Plates.PE)
            .addInput(RagiumItemTags.COPPER_PLATES)
            .addInput(ConventionalItemTags.REDSTONE_DUSTS)
            .addOutput(RagiumContents.Circuit.PRIMITIVE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER)
            .addInput(RagiumContents.Plates.PVC)
            .addInput(RagiumItemTags.GOLD_PLATES)
            .addInput(ConventionalItemTags.GLOWSTONE_DUSTS)
            .addOutput(RagiumContents.Circuit.BASIC)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ASSEMBLER)
            .addInput(RagiumContents.Plates.PTFE)
            .addInput(RagiumContents.Plates.RAGI_ALLOY)
            .addInput(RagiumContents.Dusts.RAGI_CRYSTAL)
            .addOutput(RagiumContents.Circuit.ADVANCED)
            .offerTo(exporter)
    }

    //    Compressor    //

    private fun compressor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.COMPRESSOR)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addInput(Items.WATER_BUCKET)
            .addOutput(RagiumContents.Fluids.WATER)
            .addOutput(Items.BUCKET)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.COMPRESSOR)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addInput(Items.LAVA_BUCKET)
            .addOutput(RagiumContents.Fluids.LAVA)
            .addOutput(Items.BUCKET)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.COMPRESSOR)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addInput(Items.MILK_BUCKET)
            .addOutput(RagiumContents.Fluids.MILK)
            .addOutput(Items.BUCKET)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.COMPRESSOR)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addInput(Items.HONEY_BOTTLE, 4)
            .addOutput(RagiumContents.Fluids.HONEY)
            .addOutput(Items.GLASS_BOTTLE, 4)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.COMPRESSOR)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addInput(Items.HONEY_BLOCK)
            .addOutput(RagiumContents.Fluids.HONEY)
            .offerSuffix(exporter, suffix = "_alt")
    }

    //    Extractor    //

    private fun extractor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .addInput(ItemTags.VILLAGER_PLANTABLE_SEEDS, 4)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addOutput(RagiumContents.Fluids.SEED_OIL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .addInput(ItemTags.MEAT, 2)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addOutput(RagiumContents.Fluids.TALLOW)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR)
            .addInput(RagiumContents.RUBBER_LOG)
            .addOutput(RagiumContents.RAW_RUBBER_BALL, 3)
            .offerTo(exporter)
    }

    //    Metal Former    //

    private fun metalFormer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.METAL_FORMER)
            .addInput(RagiumItemTags.STEEL_INGOTS, 2)
            .addOutput(RagiumContents.SHAFT)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.METAL_FORMER)
            .addInput(ConventionalItemTags.COPPER_INGOTS, 8)
            .addInput(RagiumContents.SHAFT)
            .addOutput(RagiumContents.Coils.COPPER)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.METAL_FORMER)
            .addInput(ConventionalItemTags.GOLD_INGOTS, 8)
            .addInput(RagiumContents.SHAFT)
            .addOutput(RagiumContents.Coils.GOLD)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.METAL_FORMER)
            .addInput(RagiumContents.Ingots.RAGI_ALLOY, 8)
            .addInput(RagiumContents.SHAFT)
            .addOutput(RagiumContents.Coils.RAGI_ALLOY)
            .offerTo(exporter)
    }

    //    Mixer    //

    private fun mixer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumContents.Dusts.RAW_RAGINITE, 4)
            .addInput(RagiumContents.Fluids.WATER)
            .addOutput(RagiumContents.Dusts.RAGINITE, 4)
            .addOutput(RagiumContents.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumItemTags.ORGANIC_OILS)
            .addInput(RagiumContents.Dusts.ASH)
            .addOutput(RagiumContents.SOAP_INGOT)
            .addOutput(RagiumContents.Fluids.GLYCEROL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumContents.Dusts.RAGINITE, 2)
            .addInput(RagiumContents.SOAP_INGOT)
            .addInput(RagiumContents.Fluids.WATER)
            .addOutput(RagiumContents.Dusts.RAGI_CRYSTAL, 2)
            .addOutput(RagiumContents.EMPTY_FLUID_CUBE)
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
            .addInput(RagiumContents.SOAP_INGOT)
            .addInput(RagiumContents.Fluids.WATER)
            .addOutput(output)
            .addOutput(RagiumContents.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(input)
            .addInput(RagiumContents.Fluids.CHLORINE)
            .addOutput(output)
            .addOutput(RagiumContents.EMPTY_FLUID_CUBE)
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

    //    Centrifuge    //

    private fun centrifuge(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CENTRIFUGE)
            .addInput(Items.SOUL_SAND)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addOutput(Items.SAND)
            .addOutput(RagiumContents.Fluids.OIL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CENTRIFUGE)
            .addInput(Items.SOUL_SOIL)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addOutput(Items.CLAY)
            .addOutput(RagiumContents.Fluids.OIL)
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

        // PE
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(RagiumContents.Fluids.ETHYLENE)
            .addInput(RagiumContents.Fluids.OXYGEN)
            .addOutput(RagiumContents.Plates.PE)
            .addOutput(RagiumContents.EMPTY_FLUID_CUBE)
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
            .addOutput(RagiumContents.EMPTY_FLUID_CUBE)
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
            .addOutput(RagiumContents.EMPTY_FLUID_CUBE)
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
            .addOutput(RagiumContents.EMPTY_FLUID_CUBE)
            .offerTo(exporter)
    }

    //    Electrolyzer    //

    private fun electrolyzer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ELECTROLYZER)
            .addInput(RagiumContents.Fluids.WATER)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE, 2)
            .addOutput(RagiumContents.Fluids.HYDROGEN, 2)
            .addOutput(RagiumContents.Fluids.OXYGEN)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ELECTROLYZER)
            .addInput(RagiumContents.Fluids.SALT_WATER)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addOutput(RagiumContents.Fluids.HYDROGEN)
            .addOutput(RagiumContents.Fluids.CHLORINE)
            .addOutput(RagiumContents.Dusts.ASH)
            .offerSuffix(exporter, suffix = "_salty")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ELECTROLYZER)
            .addInput(Items.GLOWSTONE_DUST)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addOutput(RagiumContents.Fluids.FLUORINE)
            .addOutput(Items.GOLD_NUGGET)
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
            .addOutput(RagiumContents.RAGI_CRYSTAL)
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
    }

    //    Distillation Tower    //

    private fun distillation(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.DISTILLATION_TOWER)
            .addInput(RagiumContents.Fluids.OIL, 4)
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

    //    Alchemical Infusion    //

    private fun infusion(exporter: RecipeExporter) {
        registerBudding(
            exporter,
            RagiElement.RAGIUM,
            Items.RED_STAINED_GLASS,
            Items.RED_NETHER_BRICKS,
            Items.BLAZE_POWDER,
        )
        registerBudding(
            exporter,
            RagiElement.RIGIUM,
            Items.YELLOW_STAINED_GLASS,
            Items.GLOWSTONE,
            Items.GOLDEN_APPLE,
        )
        registerBudding(
            exporter,
            RagiElement.RUGIUM,
            Items.LIME_STAINED_GLASS,
            Items.MELON,
            Items.ENDER_PEARL,
        )
        registerBudding(
            exporter,
            RagiElement.REGIUM,
            Items.LIGHT_BLUE_STAINED_GLASS,
            Items.BLUE_ICE,
            Items.PRISMARINE_SHARD,
        )
        registerBudding(
            exporter,
            RagiElement.ROGIUM,
            Items.PURPLE_STAINED_GLASS,
            Items.AMETHYST_BLOCK,
            Items.SHULKER_SHELL,
        )
    }

    private fun registerBudding(
        exporter: RecipeExporter,
        element: RagiElement,
        glass: ItemConvertible,
        ing1: ItemConvertible,
        ing2: ItemConvertible,
    ) {
        HTInfusionRecipeJsonBuilder(element.buddingBlock)
            .addInput(Items.CHISELED_QUARTZ_BLOCK)
            .addInput(glass, 64)
            .addInput(ing1, 16)
            .addInput(ing2, 8)
            .hasInput(RagiumContents.ALCHEMICAL_INFUSER)
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
}
