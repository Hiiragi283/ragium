package hiiragi283.ragium.data.recipe

import hiiragi283.ragium.api.data.HTMachineRecipeJsonBuilder
import hiiragi283.ragium.api.machine.HTMachineTier
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
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import java.util.concurrent.CompletableFuture

class RagiumMachineRecipeProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricRecipeProvider(output, registriesFuture) {
    override fun getName(): String = "Recipes/Machine"

    override fun generate(exporter: RecipeExporter) {
        alloyFurnaceRecipes(exporter)
        assembler(exporter)
        blastFurnace(exporter)
        compressor(exporter)
        extractor(exporter)
        grinder(exporter)
        growthChamber(exporter)
        laserTransformer(exporter)
        metalFormer(exporter)
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
            .create(RagiumMachineKeys.ALLOY_FURNACE)
            .fluidInput(RagiumFluids.BATTER)
            .itemInput(RagiumItems.BUTTER)
            .itemOutput(RagiumBlocks.SPONGE_CAKE)
            .offerTo(exporter, RagiumBlocks.SPONGE_CAKE)
    }

    //    Assembler    //

    private fun assembler(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ASSEMBLER, HTMachineTier.ADVANCED)
            .itemInput(RagiumItems.EMPTY_FLUID_CUBE)
            .fluidInput(RagiumFluids.ENRICHED_URANIUM_HEXAFLUORIDE)
            .fluidInput(Fluids.WATER)
            .itemOutput(RagiumItems.URANIUM_FUEL)
            .fluidOutput(RagiumFluids.HYDROGEN_FLUORIDE)
            .offerTo(exporter, RagiumItems.URANIUM_FUEL)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ASSEMBLER, HTMachineTier.ADVANCED)
            .itemInput(RagiumItems.EMPTY_FLUID_CUBE)
            .itemInput(RagiumItems.NUCLEAR_WASTE, 64)
            .itemInput(RagiumItems.NUCLEAR_WASTE, 64)
            .fluidInput(RagiumFluids.AQUA_REGIA)
            .itemOutput(RagiumItems.PLUTONIUM_FUEL)
            .offerTo(exporter, RagiumItems.PLUTONIUM_FUEL)
        // LED
        // processor
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ASSEMBLER, HTMachineTier.ADVANCED)
            .itemInput(RagiumContents.Gems.RAGI_CRYSTAL, 8)
            .itemInput(RagiumItems.PROCESSOR_SOCKET)
            .itemOutput(RagiumItems.RAGI_CRYSTAL_PROCESSOR)
            .offerTo(exporter, RagiumItems.RAGI_CRYSTAL_PROCESSOR, "_from_crystal")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ASSEMBLER, HTMachineTier.ADVANCED)
            .itemInput(RagiumContents.Gems.RAGIUM)
            .itemInput(RagiumItems.PROCESSOR_SOCKET)
            .itemOutput(RagiumItems.RAGI_CRYSTAL_PROCESSOR)
            .offerTo(exporter, RagiumItems.RAGI_CRYSTAL_PROCESSOR, "_from_ragium")
    }

    //    Blast Furnace    //

    private fun blastFurnace(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE)
            .itemInput(ConventionalItemTags.IRON_INGOTS)
            .itemInput(RagiumContents.Dusts.RAGINITE, 4)
            .itemOutput(RagiumContents.Ingots.RAGI_STEEL)
            .itemOutput(RagiumItems.SLAG)
            .offerTo(exporter, RagiumContents.Ingots.RAGI_STEEL)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE)
            .itemInput(ConventionalItemTags.IRON_INGOTS)
            .itemInput(ItemTags.COALS, 2)
            .itemOutput(RagiumContents.Ingots.STEEL)
            .itemOutput(RagiumItems.SLAG)
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
            .itemOutput(RagiumItems.SLAG)
            .offerTo(exporter, RagiumContents.Ingots.REFINED_RAGI_STEEL)
        // silicon
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.BASIC)
            .itemInput(RagiumContents.Dusts.QUARTZ, 2)
            .itemInput(ItemTags.COALS, 4)
            .itemOutput(RagiumItems.SILICON)
            .itemOutput(RagiumItems.SLAG, 2)
            .offerTo(exporter, RagiumItems.SILICON, "_from_quartz")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.BASIC)
            .itemInput(RagiumItemTags.SILICON, 2)
            .itemInput(ItemTags.COALS, 2)
            .itemOutput(RagiumItems.SILICON)
            .itemOutput(RagiumItems.SLAG)
            .offerTo(exporter, RagiumItems.SILICON, "_from_crude")
        // aluminum
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.BASIC)
            .itemInput(ItemTags.COALS, 8)
            .fluidInput(RagiumFluids.ALUMINA_SOLUTION)
            .itemOutput(RagiumContents.Ingots.ALUMINUM)
            .offerTo(exporter, RagiumContents.Ingots.ALUMINUM, "_with_coal")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.BASIC)
            .itemInput(RagiumContents.Gems.CRYOLITE)
            .fluidInput(RagiumFluids.ALUMINA_SOLUTION)
            .itemOutput(RagiumContents.Ingots.ALUMINUM)
            .itemOutput(RagiumItems.SLAG, 2)
            .offerTo(exporter, RagiumContents.Ingots.ALUMINUM, "_with_cryolite")
        // deep steel
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.BASIC)
            .itemInput(RagiumContents.Ingots.STEEL)
            .itemInput(RagiumItems.DEEPANT, 4)
            .itemOutput(RagiumContents.Ingots.DEEP_STEEL)
            .offerTo(exporter, RagiumContents.Ingots.DEEP_STEEL)
    }

    //    Compressor    //

    private fun compressor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.COMPRESSOR)
            .itemInput(RagiumItems.PULP)
            .itemOutput(RagiumContents.Plates.WOOD)
            .offerTo(exporter, RagiumContents.Plates.WOOD)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.COMPRESSOR)
            .itemInput(RagiumItems.POLYMER_RESIN)
            .catalyst(Items.LEATHER)
            .itemOutput(Items.LEATHER)
            .offerTo(exporter, Items.LEATHER)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.COMPRESSOR)
            .itemInput(RagiumItems.POLYMER_RESIN)
            .catalyst(Items.STRING)
            .itemOutput(Items.STRING, 4)
            .offerTo(exporter, Items.STRING)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.COMPRESSOR)
            .itemInput(RagiumItems.POLYMER_RESIN)
            .catalyst(Items.GLASS)
            .itemOutput(Items.GLASS)
            .offerTo(exporter, Items.GLASS)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.COMPRESSOR)
            .itemInput(ItemTags.COALS, 8)
            .itemOutput(RagiumItems.CARBON_ELECTRODE)
            .offerTo(exporter, RagiumItems.CARBON_ELECTRODE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.COMPRESSOR)
            .itemInput(RagiumItems.MINCED_MEAT)
            .itemOutput(RagiumItems.MEAT_INGOT)
            .offerTo(exporter, RagiumItems.MEAT_INGOT)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.COMPRESSOR)
            .fluidInput(RagiumFluids.NITROGEN, FluidConstants.BUCKET * 8)
            .fluidOutput(RagiumFluids.LIQUID_NITROGEN)
            .offerTo(exporter, RagiumFluids.LIQUID_NITROGEN)
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
            .itemInput(Items.GLOWSTONE)
            .itemOutput(RagiumContents.Gems.FLUORITE, 4)
            .itemOutput(Items.GOLD_NUGGET)
            .offerTo(exporter, RagiumContents.Gems.FLUORITE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .fluidInput(RagiumFluids.ALKALI_SOLUTION)
            .itemOutput(RagiumContents.Dusts.ALKALI)
            .fluidOutput(Fluids.WATER)
            .offerTo(exporter, RagiumContents.Dusts.ALKALI)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .fluidInput(RagiumFluids.AIR, FluidConstants.BUCKET * 5)
            .fluidOutput(RagiumFluids.NITROGEN, FluidConstants.BUCKET * 4)
            .fluidOutput(RagiumFluids.OXYGEN)
            .offerTo(exporter, RagiumFluids.AIR)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Items.HONEY_BOTTLE, 4)
            .itemOutput(Items.GLASS_BOTTLE, 4)
            .fluidOutput(RagiumFluids.HONEY)
            .offerTo(exporter, RagiumFluids.HONEY)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .fluidInput(RagiumFluids.SALT_WATER)
            .itemOutput(RagiumContents.Dusts.SALT)
            .fluidOutput(Fluids.WATER)
            .offerTo(exporter, RagiumContents.Dusts.SALT)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Items.GLOW_INK_SAC)
            .itemOutput(RagiumItems.LUMINESCENCE_DUST)
            .itemOutput(Items.INK_SAC)
            .offerTo(exporter, RagiumItems.LUMINESCENCE_DUST)
        // uranium enrichment
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR, HTMachineTier.ADVANCED)
            .fluidInput(RagiumFluids.URANIUM_HEXAFLUORIDE, FluidConstants.BUCKET * 8)
            .fluidOutput(RagiumFluids.URANIUM_HEXAFLUORIDE, FluidConstants.BUCKET * 7)
            .fluidOutput(RagiumFluids.ENRICHED_URANIUM_HEXAFLUORIDE)
            .offerTo(exporter, RagiumFluids.ENRICHED_URANIUM_HEXAFLUORIDE)
        // sap
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(ItemTags.LOGS)
            .itemOutput(RagiumItems.PULP, 6)
            .fluidOutput(RagiumFluids.SAP, FluidConstants.BUCKET)
            .offerTo(exporter, RagiumFluids.SAP)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR, HTMachineTier.ADVANCED)
            .itemInput(ItemTags.CRIMSON_STEMS)
            .catalyst(ItemTags.CRIMSON_STEMS)
            .itemOutput(RagiumItems.PULP, 6)
            .fluidOutput(RagiumFluids.CRIMSON_SAP, FluidConstants.BUCKET)
            .offerTo(exporter, RagiumFluids.CRIMSON_SAP)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR, HTMachineTier.ADVANCED)
            .itemInput(ItemTags.WARPED_STEMS)
            .catalyst(ItemTags.WARPED_STEMS)
            .itemOutput(RagiumItems.PULP, 6)
            .fluidOutput(RagiumFluids.WARPED_SAP, FluidConstants.BUCKET)
            .offerTo(exporter, RagiumFluids.WARPED_SAP)
        // crude oil
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

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.EXTRACTOR, HTMachineTier.BASIC)
            .itemInput(Items.COAL, 3)
            .fluidOutput(RagiumFluids.CRUDE_OIL)
            .offerTo(exporter, RagiumFluids.CRUDE_OIL, "_from_coal")
    }

    //    Grinder    //

    private fun grinder(exporter: RecipeExporter) {
        registerGrinder(exporter, ConventionalItemTags.COBBLESTONES to 1, Items.GRAVEL to 1)
        registerGrinder(exporter, ConventionalItemTags.WHEAT_CROPS to 1, RagiumItems.FLOUR to 1)
        registerGrinder(exporter, Items.ANCIENT_DEBRIS to 1, Items.NETHERITE_SCRAP to 2)
        registerGrinder(exporter, Items.COARSE_DIRT to 1, Items.DIRT to 1)
        registerGrinder(exporter, Items.DEEPSLATE to 1, Items.COBBLED_DEEPSLATE to 1)
        registerGrinder(exporter, Items.STONE to 1, Items.COBBLESTONE to 1)
        registerGrinder(exporter, ItemTags.BOATS to 1, RagiumItems.PULP to 5, suffix = "_from_boat")
        registerGrinder(exporter, ItemTags.FENCE_GATES to 1, RagiumItems.PULP to 4, suffix = "_from_fence_gate")
        registerGrinder(exporter, ItemTags.LOGS to 1, RagiumItems.PULP to 4, suffix = "_from_log")
        registerGrinder(exporter, ItemTags.PLANKS to 1, RagiumItems.PULP to 1, suffix = "_from_plank")
        // registerGrinder(exporter, ItemTags.REDSTONE_ORES to 1, Items.REDSTONE to 8)
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
        registerGrinder(exporter, RagiumItems.SLAG to 1, Items.GRAVEL to 1, "_from_slag")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(Items.GRAVEL)
            .itemOutput(Items.FLINT)
            .catalyst(Items.FLINT)
            .offerTo(exporter, Items.FLINT, "_from_gravel")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(Items.GRAVEL)
            .itemOutput(Items.SAND)
            .catalyst(Items.SAND)
            .offerTo(exporter, Items.SAND, "_from_gravel")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(Items.NETHERRACK, 8)
            .itemOutput(RagiumContents.Dusts.BAUXITE, 2)
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

    //    Growth Chamber    //

    private fun growthChamber(exporter: RecipeExporter) {
        // crops
        registerCrop(exporter, Items.BAMBOO, Items.BAMBOO)
        registerCrop(exporter, Items.BEETROOT_SEEDS, Items.BEETROOT)
        registerCrop(exporter, Items.BROWN_MUSHROOM, Items.BROWN_MUSHROOM)
        registerCrop(exporter, Items.CACTUS, Items.CACTUS, Items.SAND)
        registerCrop(exporter, Items.CARROT, Items.CARROT)
        registerCrop(exporter, Items.CHORUS_FLOWER, Items.CHORUS_FRUIT, Items.END_STONE)
        registerCrop(exporter, Items.COCOA_BEANS, Items.COCOA_BEANS, Items.JUNGLE_LOG)
        registerCrop(exporter, Items.GLOW_BERRIES, Items.GLOW_BERRIES)
        registerCrop(exporter, Items.MELON_SEEDS, Items.MELON)
        registerCrop(exporter, Items.NETHER_WART, Items.NETHER_WART, Items.SOUL_SAND)
        registerCrop(exporter, Items.PITCHER_POD, Items.PITCHER_PLANT)
        registerCrop(exporter, Items.POTATO, Items.POTATO)
        registerCrop(exporter, Items.PUMPKIN_SEEDS, Items.PUMPKIN)
        registerCrop(exporter, Items.RED_MUSHROOM, Items.RED_MUSHROOM)
        registerCrop(exporter, Items.SUGAR_CANE, Items.SUGAR_CANE, Items.SAND)
        registerCrop(exporter, Items.SWEET_BERRIES, Items.SWEET_BERRIES)
        registerCrop(exporter, Items.TORCHFLOWER_SEEDS, Items.TORCHFLOWER)
        registerCrop(exporter, Items.WHEAT_SEEDS, Items.WHEAT)

        // registerCrop(exporter, Items.BEETROOT_SEEDS, Items.NETHER_WART, RagiumBlocks.MUTATED_SOIL)
        registerCrop(exporter, Items.BROWN_MUSHROOM, Items.WARPED_FUNGUS, RagiumBlocks.MUTATED_SOIL)
        registerCrop(exporter, Items.POTATO, Items.POISONOUS_POTATO, RagiumBlocks.MUTATED_SOIL)
        registerCrop(exporter, Items.PUMPKIN_SEEDS, Items.CARVED_PUMPKIN, RagiumBlocks.MUTATED_SOIL)
        registerCrop(exporter, Items.RED_MUSHROOM, Items.CRIMSON_FUNGUS, RagiumBlocks.MUTATED_SOIL)
        // registerCrop(exporter, Items.SWEET_BERRIES, Items.GLOW_BERRIES, RagiumBlocks.MUTATED_SOIL)
        registerCrop(exporter, Items.TORCHFLOWER_SEEDS, Items.TORCH, RagiumBlocks.MUTATED_SOIL)
        // trees
        registerTree(exporter, Items.ACACIA_SAPLING, Items.ACACIA_LOG)
        registerTree(exporter, Items.BIRCH_SAPLING, Items.BIRCH_LOG)
        registerTree(exporter, Items.CHERRY_SAPLING, Items.CHERRY_LOG)
        registerTree(exporter, Items.CRIMSON_FUNGUS, Items.CRIMSON_STEM, Items.NETHERRACK)
        registerTree(exporter, Items.DARK_OAK_SAPLING, Items.DARK_OAK_LOG)
        registerTree(exporter, Items.JUNGLE_SAPLING, Items.JUNGLE_LOG)
        registerTree(exporter, Items.MANGROVE_PROPAGULE, Items.MANGROVE_LOG)
        registerTree(exporter, Items.OAK_SAPLING, Items.OAK_LOG)
        registerTree(exporter, Items.SPRUCE_SAPLING, Items.SPRUCE_LOG)
        registerTree(exporter, Items.WARPED_FUNGUS, Items.WARPED_STEM, Items.NETHERRACK)
    }

    private fun registerCrop(
        exporter: RecipeExporter,
        seed: ItemConvertible,
        crop: ItemConvertible,
        soil: ItemConvertible = Items.DIRT,
    ) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.GROWTH_CHAMBER)
            .itemInput(seed)
            .catalyst(soil)
            .itemOutput(crop)
            .itemOutput(seed)
            .offerTo(exporter, crop)
    }

    private fun registerTree(
        exporter: RecipeExporter,
        sapling: ItemConvertible,
        log: ItemConvertible,
        soil: ItemConvertible = Items.DIRT,
    ) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.GROWTH_CHAMBER)
            .itemInput(sapling)
            .catalyst(soil)
            .itemOutput(log, 8)
            .itemOutput(sapling)
            .offerTo(exporter, log)
    }

    //    Laser Transformer    //

    private fun laserTransformer(exporter: RecipeExporter) {
        // primitive
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.LASER_TRANSFORMER)
            .itemInput(ConventionalItemTags.GLASS_BLOCKS_COLORLESS, 8)
            .itemOutput(Items.QUARTZ)
            .offerTo(exporter, Items.QUARTZ)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.LASER_TRANSFORMER)
            .itemInput(Items.ICE)
            .itemOutput(Items.PRISMARINE_CRYSTALS)
            .offerTo(exporter, Items.PRISMARINE_CRYSTALS)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.LASER_TRANSFORMER)
            .itemInput(Items.PACKED_ICE)
            .itemOutput(Items.PRISMARINE_SHARD)
            .offerTo(exporter, Items.PRISMARINE_SHARD)
        // basic
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.LASER_TRANSFORMER, HTMachineTier.BASIC)
            .itemInput(ItemTags.COALS, 64)
            .itemOutput(Items.DIAMOND)
            .offerTo(exporter, Items.DIAMOND)
        // advanced
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.LASER_TRANSFORMER, HTMachineTier.ADVANCED)
            .itemInput(RagiumContents.Gems.RAGI_CRYSTAL, 8)
            .itemOutput(RagiumContents.Gems.RAGIUM)
            .offerTo(exporter, RagiumContents.Gems.RAGIUM)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.LASER_TRANSFORMER, HTMachineTier.ADVANCED)
            .itemInput(Items.BLUE_ICE, 64)
            .itemOutput(Items.HEART_OF_THE_SEA)
            .offerTo(exporter, Items.HEART_OF_THE_SEA)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.LASER_TRANSFORMER, HTMachineTier.ADVANCED)
            .itemInput(ConventionalItemTags.STORAGE_BLOCKS_GOLD, 8)
            .itemInput(Items.APPLE)
            .itemOutput(Items.ENCHANTED_GOLDEN_APPLE)
            .offerTo(exporter, Items.ENCHANTED_GOLDEN_APPLE)
    }

    //    Metal Former    //

    private fun metalFormer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.METAL_FORMER)
            .itemInput(RagiumContents.Ingots.STEEL, 2)
            .itemOutput(RagiumBlocks.SHAFT)
            .catalyst(RagiumBlocks.SHAFT)
            .offerTo(exporter, RagiumBlocks.SHAFT)
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
            .catalyst(Items.OBSIDIAN)
            .itemOutput(Items.OBSIDIAN)
            .offerTo(exporter, Items.OBSIDIAN)
    }

    private fun registerRock(exporter: RecipeExporter, rock: ItemConvertible) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ROCK_GENERATOR)
            .catalyst(rock)
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
}
