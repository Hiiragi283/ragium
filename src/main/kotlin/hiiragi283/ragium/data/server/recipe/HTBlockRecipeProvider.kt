package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.extension.catalyst
import hiiragi283.ragium.api.extension.define
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType

object HTBlockRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerGrates(output)
        registerCasings(output)
        registerHulls(output)
        registerCoils(output)
        registerBurners(output)
        registerDrums(output)

        registerDecorations(output)
        registerLEDs(output)

        registerAddons(output)
        registerMachines(output)
    }

    //    Components    //

    private fun registerGrates(output: RecipeOutput) {
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, Items.COPPER_GRATE, 4)
            .pattern("AAA")
            .pattern("ABA")
            .pattern("AAA")
            .define('A', HTTagPrefix.ROD, VanillaMaterials.COPPER)
            .define('B', RagiumItems.FORGE_HAMMER)
            .unlockedBy("has_rod", has(HTTagPrefix.ROD, VanillaMaterials.COPPER))
            .save(output, RagiumAPI.id("shaped/copper_grate"))

        RagiumBlocks.Grates.entries.forEach { grate: RagiumBlocks.Grates ->
            // Shaped Crafting
            ShapedRecipeBuilder
                .shaped(RecipeCategory.BUILDING_BLOCKS, grate, 4)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', HTTagPrefix.ROD, grate.machineTier.getSteelMetal())
                .define('B', RagiumItems.FORGE_HAMMER)
                .unlockedBy("has_rod", has(HTTagPrefix.ROD, grate.machineTier.getSteelMetal()))
                .savePrefixed(output)
            // Assembler
            HTMachineRecipeBuilder
                .create(RagiumRecipes.ASSEMBLER)
                .itemInput(HTTagPrefix.ROD, grate.machineTier.getSteelMetal(), 4)
                .catalyst(grate)
                .itemOutput(grate, 4)
                .save(output)
        }
    }

    private fun registerCasings(output: RecipeOutput) {
        RagiumBlocks.Casings.entries.forEach { casings: RagiumBlocks.Casings ->
            val corner: Item = when (casings) {
                RagiumBlocks.Casings.BASIC -> Items.STONE
                RagiumBlocks.Casings.ADVANCED -> Items.QUARTZ_BLOCK
                RagiumBlocks.Casings.ELITE -> Items.POLISHED_DEEPSLATE
                RagiumBlocks.Casings.ULTIMATE -> Items.OBSIDIAN
            }
            // Shaped Crafting
            val grate: HTBlockContent.Tier = casings.machineTier.getGrate()
            ShapedRecipeBuilder
                .shaped(RecipeCategory.BUILDING_BLOCKS, casings, 3)
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', corner)
                .define('B', grate)
                .define('C', HTTagPrefix.GEAR, casings.machineTier.getSteelMetal())
                .unlockedBy("has_grate", has(grate))
                .savePrefixed(output)
            // Assembler
            HTMachineRecipeBuilder
                .create(RagiumRecipes.ASSEMBLER)
                .itemInput(grate, 4)
                .itemInput(HTTagPrefix.GEAR, casings.machineTier.getSteelMetal())
                .itemInput(corner, 4)
                .catalyst(casings)
                .itemOutput(casings, 6)
                .save(output)
        }
    }

    private fun registerHulls(output: RecipeOutput) {
        RagiumBlocks.Hulls.entries.forEach { hull: RagiumBlocks.Hulls ->
            // Shaped Crafting
            ShapedRecipeBuilder
                .shaped(RecipeCategory.BUILDING_BLOCKS, hull, 3)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("CCC")
                .define('A', HTTagPrefix.INGOT, hull.machineTier.getMainMetal())
                .define('B', hull.machineTier.getCircuitTag())
                .define('C', hull.machineTier.getCasing())
                .unlockedBy("has_casing", has(hull.machineTier.getCasing()))
                .savePrefixed(output)
            // Assembler
            HTMachineRecipeBuilder
                .create(RagiumRecipes.ASSEMBLER)
                .itemInput(HTTagPrefix.INGOT, hull.machineTier.getMainMetal(), 5)
                .itemInput(hull.machineTier.getCasing(), 3)
                .itemInput(hull.machineTier.getCircuitTag())
                .catalyst(hull)
                .itemOutput(hull, 6)
                .save(output)
        }
    }

    private fun registerCoils(output: RecipeOutput) {
        RagiumBlocks.Coils.entries.forEach { coil: RagiumBlocks.Coils ->
            val previousTier: HTMachineTier = coil.machineTier.getPreviousTier() ?: HTMachineTier.BASIC
            // Shaped Crafting
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, coil, 2)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', HTTagPrefix.INGOT, coil.machineTier.getSubMetal())
                .define('B', RagiumBlocks.SHAFT)
                .unlockedBy("has_shaft", has(RagiumBlocks.SHAFT))
                .savePrefixed(output)
            // Assembler
            HTMachineRecipeBuilder
                .create(RagiumRecipes.ASSEMBLER)
                .itemInput(HTTagPrefix.INGOT, coil.machineTier.getSubMetal(), 8)
                .itemInput(RagiumBlocks.SHAFT)
                .catalyst(previousTier)
                .itemOutput(coil, 4)
                .save(output)
        }
    }

    private fun registerBurners(output: RecipeOutput) {
        RagiumBlocks.Burners.entries.forEach { burner: RagiumBlocks.Burners ->
            val core: ItemLike = when (burner) {
                RagiumBlocks.Burners.ADVANCED -> Items.MAGMA_BLOCK
                RagiumBlocks.Burners.ELITE -> RagiumBlocks.SOUL_MAGMA_BLOCK
                RagiumBlocks.Burners.ULTIMATE -> RagiumBlocks.STORAGE_BLOCKS[RagiumMaterials.FIERIUM]!!
            }
            // Shaped Crafting
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, burner)
                .pattern("A A")
                .pattern("ABA")
                .pattern("CCC")
                .define('A', burner.machineTier.getGrate())
                .define('B', core)
                .define('C', burner.machineTier.getCoil())
                .unlockedBy("has_core", has(core))
                .savePrefixed(output)
        }
    }

    private fun registerDrums(output: RecipeOutput) {
        RagiumBlocks.Drums.entries.forEach { drum: RagiumBlocks.Drums ->
            // Shaped Crafting
            val mainIngot: TagKey<Item> = HTTagPrefix.INGOT.createTag(drum.machineTier.getMainMetal())
            ShapedRecipeBuilder
                .shaped(RecipeCategory.TRANSPORTATION, drum)
                .pattern("ABA")
                .pattern("ACA")
                .pattern("ABA")
                .define('A', HTTagPrefix.INGOT, drum.machineTier.getSubMetal())
                .define('B', mainIngot)
                .define('C', Items.BUCKET)
                .unlockedBy("has_ingot", has(mainIngot))
                .savePrefixed(output)
        }
    }

    //    Decorations    //

    private fun registerDecorations(output: RecipeOutput) {
        // Shaped Crafting
        ShapedRecipeBuilder
            .shaped(RecipeCategory.BUILDING_BLOCKS, RagiumBlocks.PLASTIC_BLOCK, 4)
            .pattern(" A ")
            .pattern("ABA")
            .pattern(" A ")
            .define('A', RagiumItemTags.PLASTICS)
            .define('B', RagiumItems.FORGE_HAMMER)
            .unlockedBy("has_plastic", has(RagiumItemTags.PLASTICS))
            .savePrefixed(output)

        RagiumBlocks.Decorations.entries.forEach { decoration: RagiumBlocks.Decorations ->
            // Stone Cutting
            SingleItemRecipeBuilder
                .stonecutting(
                    Ingredient.of(RagiumBlocks.PLASTIC_BLOCK),
                    RecipeCategory.BUILDING_BLOCKS,
                    decoration,
                ).unlockedBy("has_plastic", has(RagiumBlocks.PLASTIC_BLOCK))
                .savePrefixed(output)
        }
    }

    private fun registerLEDs(output: RecipeOutput) {
        // LED
        RagiumBlocks.LEDBlocks.entries.forEach { ledBlock: RagiumBlocks.LEDBlocks ->
            // Shaped Crafting
            ShapedRecipeBuilder
                .shaped(RecipeCategory.BUILDING_BLOCKS, ledBlock, 4)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', ledBlock.baseBlock)
                .define('B', RagiumItems.LED)
                .unlockedBy("has_led", has(RagiumItems.LED))
                .savePrefixed(output)
        }
    }

    //    Machines    //

    private fun registerMachines(output: RecipeOutput) {
        // Manual Machine
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumBlocks.MANUAL_GRINDER)
            .pattern("A  ")
            .pattern("BBB")
            .pattern("CCC")
            .define('A', Tags.Items.RODS_WOODEN)
            .define('B', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('C', Items.BRICKS)
            .unlockedBy("has_ragi_alloy", has(HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY))
            .savePrefixed(output)

        basicMachines(output)
        advancedMachines(output)
        eliteMachines(output)
        ultimateMachines(output)
    }

    private fun registerAddons(output: RecipeOutput) {
        // Catalyst Addon
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumBlocks.CATALYST_ADDON)
            .pattern(" A ")
            .pattern("ABA")
            .pattern(" A ")
            .define('A', HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .define('B', Items.ITEM_FRAME)
            .unlockedBy("has_iron", has(HTTagPrefix.INGOT, VanillaMaterials.IRON))
            .savePrefixed(output)
        // E.N.I.
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumBlocks.ENERGY_NETWORK_INTERFACE)
            .pattern("ABA")
            .pattern("BCB")
            .pattern("ABA")
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.DEEP_STEEL)
            .define('B', HTMachineTier.ELITE.getCircuitTag())
            .define('C', Tags.Items.OBSIDIANS_CRYING)
            .unlockedBy("has_circuit", has(HTMachineTier.ELITE.getCircuitTag()))
            .savePrefixed(output)
        // Superconductive Coolant
        HTMachineRecipeBuilder
            .create(RagiumRecipes.ASSEMBLER)
            .itemInput(RagiumBlocks.Casings.ELITE, 4)
            .itemInput(RagiumBlocks.CHEMICAL_GLASS, 4)
            .fluidInput(RagiumFluids.LIQUID_NITROGEN, FluidType.BUCKET_VOLUME * 8)
            .catalyst(HTMachineTier.ELITE)
            .itemOutput(RagiumBlocks.SUPERCONDUCTIVE_COOLANT)
            .save(output)
    }

    private fun basicMachines(output: RecipeOutput) {
        // Blast Furnace
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.BLAST_FURNACE.getBlock())
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', Items.BLAST_FURNACE)
            .define('C', HTMachineTier.BASIC.getCircuitTag())
            .define('D', Items.DEEPSLATE_BRICKS)
            .unlockedBy("has_circuit", has(HTMachineTier.BASIC.getCircuitTag()))
            .savePrefixed(output)
        // Coke Oven
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.COKE_OVEN.getBlock())
            .pattern("AAA")
            .pattern("ABA")
            .pattern("CCC")
            .define('A', Items.MUD_BRICKS)
            .define('B', Items.FURNACE)
            .define('C', Items.BRICKS)
            .unlockedBy("has_mud_bricks", has(Items.MUD_BRICKS))
            .savePrefixed(output)
        // Compressor
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.COMPRESSOR.getBlock())
            .pattern("AAA")
            .pattern("B B")
            .pattern("CCC")
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', Items.PISTON)
            .define('C', RagiumBlocks.Casings.BASIC)
            .unlockedBy("has_ragi_alloy", has(HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY))
            .savePrefixed(output)
        // Mixer
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.MIXER.getBlock())
            .pattern("A A")
            .pattern("A A")
            .pattern("ABA")
            .define('A', HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .define('B', HTTagPrefix.STORAGE_BLOCK, VanillaMaterials.COPPER)
            .unlockedBy("has_iron", has(HTTagPrefix.INGOT, VanillaMaterials.IRON))
            .savePrefixed(output)
        // Steam Boiler
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.STEAM_BOILER.getBlock())
            .pattern("AAA")
            .pattern("A A")
            .pattern("BBB")
            .define('A', HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .define('B', Items.BRICKS)
            .unlockedBy("has_copper", has(HTTagPrefix.INGOT, VanillaMaterials.COPPER))
            .savePrefixed(output)
    }

    private fun advancedMachines(output: RecipeOutput) {
        // Solar Generator
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.SOLAR_GENERATOR.getBlock(), 2)
            .pattern("AAA")
            .pattern("BCB")
            .define('A', RagiumItemTags.SOLAR_PANELS)
            .define('B', RagiumBlocks.Casings.ADVANCED)
            .define('C', HTMachineTier.ADVANCED.getCircuitTag())
            .unlockedBy("has_solar_panel", has(RagiumItemTags.SOLAR_PANELS))
            .savePrefixed(output)

        // Assembler
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.ASSEMBLER.getBlock())
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_STEEL)
            .define('B', HTMachineTier.ADVANCED.getCircuitTag())
            .define('C', Items.CRAFTER)
            .define('D', RagiumBlocks.Casings.ADVANCED)
            .unlockedBy("has_circuit", has(HTMachineTier.ADVANCED.getCircuitTag()))
            .savePrefixed(output)
        // Chemical Reactor
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.CHEMICAL_REACTOR.getBlock())
            .pattern("AAA")
            .pattern("B B")
            .pattern("CCC")
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_STEEL)
            .define('B', RagiumBlocks.CHEMICAL_GLASS)
            .define('C', RagiumBlocks.Casings.ADVANCED)
            .unlockedBy("has_ragi_steel", has(HTTagPrefix.INGOT, RagiumMaterials.RAGI_STEEL))
            .savePrefixed(output)
        // Cutting Machine
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.CUTTING_MACHINE.getBlock())
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .define('A', RagiumBlocks.CHEMICAL_GLASS)
            .define('B', HTMachineTier.ADVANCED.getCircuitTag())
            .define('C', Items.STONECUTTER)
            .define('D', RagiumBlocks.Casings.ADVANCED)
            .unlockedBy("has_circuit", has(HTMachineTier.ADVANCED.getCircuitTag()))
            .savePrefixed(output)
        // Distillation Tower
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.DISTILLATION_TOWER.getBlock())
            .pattern("A A")
            .pattern("BAB")
            .pattern("CCC")
            .define('A', RagiumBlocks.CHEMICAL_GLASS)
            .define('B', HTTagPrefix.GEAR, VanillaMaterials.DIAMOND)
            .define('C', RagiumBlocks.Casings.ADVANCED)
            .unlockedBy("has_casing", has(RagiumBlocks.Casings.ADVANCED))
            .savePrefixed(output)
        // Extractor
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.EXTRACTOR.getBlock())
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_STEEL)
            .define('B', Items.PISTON)
            .define('C', RagiumBlocks.CHEMICAL_GLASS)
            .define('D', RagiumBlocks.Casings.ADVANCED)
            .unlockedBy("has_ragi_steel", has(HTTagPrefix.INGOT, RagiumMaterials.RAGI_STEEL))
            .savePrefixed(output)
        // Grinder
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.GRINDER.getBlock())
            .pattern("AAA")
            .pattern(" B ")
            .pattern("CCC")
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_STEEL)
            .define('B', Items.GRINDSTONE)
            .define('C', RagiumBlocks.Casings.ADVANCED)
            .unlockedBy("has_ragi_steel", has(HTTagPrefix.INGOT, RagiumMaterials.RAGI_STEEL))
            .savePrefixed(output)
        // Growth Chamber
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.GROWTH_CHAMBER.getBlock())
            .pattern("AAA")
            .pattern("ABA")
            .pattern("CCC")
            .define('A', RagiumBlocks.CHEMICAL_GLASS)
            .define('B', Items.DIAMOND_HOE)
            .define('C', RagiumBlocks.Casings.ADVANCED)
            .unlockedBy("has_hoe", has(Items.DIAMOND_HOE))
            .savePrefixed(output)
    }

    private fun eliteMachines(output: RecipeOutput) {
        // Laser Transformer
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.LASER_TRANSFORMER.getBlock())
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.REFINED_RAGI_STEEL)
            .define('B', Items.PISTON)
            .define('C', Items.END_CRYSTAL)
            .define('D', RagiumBlocks.Casings.ELITE)
            .unlockedBy("has_refined_ragi_steel", has(HTTagPrefix.INGOT, RagiumMaterials.REFINED_RAGI_STEEL))
            .savePrefixed(output)
        // Multi Smelter
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.MULTI_SMELTER.getBlock())
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.REFINED_RAGI_STEEL)
            .define('B', HTMachineTier.ELITE.getCircuitTag())
            .define('C', RagiumMachineKeys.BLAST_FURNACE.getBlock())
            .define('D', RagiumBlocks.Casings.ELITE)
            .unlockedBy("has_circuit", has(HTMachineTier.ELITE.getCircuitTag()))
            .savePrefixed(output)
        // Resource Plant
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.RESOURCE_PLANT.getBlock())
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.REFINED_RAGI_STEEL)
            .define('B', RagiumBlocks.SHAFT)
            .define('C', RagiumMachineKeys.EXTRACTOR.getBlock())
            .define('D', RagiumBlocks.Casings.ELITE)
            .unlockedBy("has_circuit", has(HTMachineTier.ELITE.getCircuitTag()))
            .savePrefixed(output)
    }

    private fun ultimateMachines(output: RecipeOutput) {
        // Bedrock Miner
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.BEDROCK_MINER.getBlock())
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGIUM)
            .define('B', RagiumBlocks.SHAFT)
            .define('C', Items.NETHER_STAR)
            .define('D', RagiumBlocks.Casings.ULTIMATE)
            .unlockedBy("has_star", has(Items.NETHER_STAR))
            .savePrefixed(output)
    }
}
