package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import hiiragi283.ragium.data.define
import hiiragi283.ragium.data.savePrefixed
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object HTMachineRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerComponents(output)

        registerMachines(output)
    }

    private fun registerComponents(output: RecipeOutput) {
        // Grate
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, Items.COPPER_GRATE, 4)
            .pattern("AAA")
            .pattern("ABA")
            .pattern("AAA")
            .define('A', HTTagPrefix.ROD, RagiumMaterialKeys.COPPER)
            .define('B', RagiumItems.FORGE_HAMMER)
            .unlockedBy("has_rod", has(HTTagPrefix.ROD, RagiumMaterialKeys.COPPER))
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
                .create(RagiumMachineKeys.ASSEMBLER)
                .itemInput(HTTagPrefix.ROD, grate.machineTier.getSteelMetal(), 4)
                .catalyst(grate)
                .itemOutput(grate, 4)
                .save(output)
        }
        // Casing
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
                .create(RagiumMachineKeys.ASSEMBLER)
                .itemInput(grate, 4)
                .itemInput(HTTagPrefix.GEAR, casings.machineTier.getSteelMetal())
                .itemInput(corner, 4)
                .catalyst(casings)
                .itemOutput(casings, 6)
                .save(output)
        }
        // Hull
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
                .create(RagiumMachineKeys.ASSEMBLER)
                .itemInput(HTTagPrefix.INGOT, hull.machineTier.getMainMetal(), 5)
                .itemInput(hull.machineTier.getCasing(), 3)
                .itemInput(hull.machineTier.getCircuitTag())
                .catalyst(hull)
                .itemOutput(hull, 6)
                .save(output)
        }
        // Coil
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
                .create(RagiumMachineKeys.ASSEMBLER, previousTier)
                .itemInput(HTTagPrefix.INGOT, coil.machineTier.getSubMetal(), 8)
                .itemInput(RagiumBlocks.SHAFT)
                .itemOutput(coil, 4)
                .save(output)
        }
        // Burner
        RagiumBlocks.Burners.entries.forEach { burner: RagiumBlocks.Burners ->
            val tier: HTMachineTier = burner.machineTier
            // Shaped Crafting
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, burner)
                .pattern("A A")
                .pattern("ABA")
                .pattern("CCC")
                .define('A', tier.getGrate())
                .define(
                    'B',
                    when (tier) {
                        HTMachineTier.BASIC -> Items.COAL_BLOCK
                        HTMachineTier.ADVANCED -> Items.MAGMA_BLOCK
                        HTMachineTier.ELITE -> RagiumBlocks.SOUL_MAGMA_BLOCK
                        HTMachineTier.ULTIMATE -> Items.END_CRYSTAL
                    },
                ).define('C', tier.getCoil())
                .unlockedBy("has_coil", has(tier.getCoil()))
                .savePrefixed(output)
        }

        // Drum
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

    private fun registerMachines(output: RecipeOutput) {
        // Manual Machine
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumBlocks.MANUAL_GRINDER)
            .pattern("A  ")
            .pattern("BBB")
            .pattern("CCC")
            .define('A', Tags.Items.RODS_WOODEN)
            .define('B', HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            .define('C', Items.BRICKS)
            .unlockedBy("has_ragi_alloy", has(HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_ALLOY))
            .savePrefixed(output)

        basicMachines(output)
        advancedMachines(output)
        eliteMachines(output)
    }

    private fun basicMachines(output: RecipeOutput) {
        // Blast Furnace
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.BLAST_FURNACE.getBlock())
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .define('A', HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            .define('B', RagiumBlocks.Coils.BASIC)
            .define('C', Items.BLAST_FURNACE)
            .define('D', RagiumBlocks.Casings.BASIC)
            .unlockedBy("has_circuit", has(HTMachineTier.BASIC.getCircuitTag()))
            .savePrefixed(output)
        // Compressor
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.COMPRESSOR.getBlock())
            .pattern("AAA")
            .pattern("B B")
            .pattern("CCC")
            .define('A', HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            .define('B', Items.PISTON)
            .define('C', RagiumBlocks.Casings.BASIC)
            .unlockedBy("has_ragi_alloy", has(HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_ALLOY))
            .savePrefixed(output)
        // Mixer
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.MIXER.getBlock())
            .pattern("A A")
            .pattern("A A")
            .pattern("ABA")
            .define('A', HTTagPrefix.INGOT, RagiumMaterialKeys.IRON)
            .define('B', HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.COPPER)
            .unlockedBy("has_iron", has(HTTagPrefix.INGOT, RagiumMaterialKeys.IRON))
            .savePrefixed(output)
    }

    private fun advancedMachines(output: RecipeOutput) {
        // Assembler
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.ASSEMBLER.getBlock())
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .define('A', HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_STEEL)
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
            .define('A', HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_STEEL)
            .define('B', RagiumBlocks.CHEMICAL_GLASS)
            .define('C', RagiumBlocks.Casings.ADVANCED)
            .unlockedBy("has_ragi_steel", has(HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_STEEL))
            .savePrefixed(output)
        // Cutting Machine
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.CUTTING_MACHINE.getBlock())
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .define('A', HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_STEEL)
            .define('B', HTMachineTier.ADVANCED.getCircuitTag())
            .define('C', Items.STONECUTTER)
            .define('D', RagiumBlocks.Casings.ADVANCED)
            .unlockedBy("has_circuit", has(HTMachineTier.ADVANCED.getCircuitTag()))
            .savePrefixed(output)
        // Extractor
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.EXTRACTOR.getBlock())
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .define('A', HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_STEEL)
            .define('B', Items.PISTON)
            .define('C', RagiumBlocks.CHEMICAL_GLASS)
            .define('D', RagiumBlocks.Casings.ADVANCED)
            .unlockedBy("has_ragi_steel", has(HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_STEEL))
            .savePrefixed(output)
        // Grinder
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.GRINDER.getBlock())
            .pattern("AAA")
            .pattern(" B ")
            .pattern("CCC")
            .define('A', HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_STEEL)
            .define('B', Items.GRINDSTONE)
            .define('C', RagiumBlocks.Casings.ADVANCED)
            .unlockedBy("has_ragi_steel", has(HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_STEEL))
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
            .define('A', HTTagPrefix.INGOT, RagiumMaterialKeys.REFINED_RAGI_STEEL)
            .define('B', Items.PISTON)
            .define('C', Items.END_CRYSTAL)
            .define('D', RagiumBlocks.Casings.ELITE)
            .unlockedBy("has_refined_ragi_steel", has(HTTagPrefix.INGOT, RagiumMaterialKeys.REFINED_RAGI_STEEL))
            .savePrefixed(output)
        // Multi Smelter
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumMachineKeys.MULTI_SMELTER.getBlock())
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .define('A', HTTagPrefix.INGOT, RagiumMaterialKeys.REFINED_RAGI_STEEL)
            .define('B', HTMachineTier.ELITE.getCircuitTag())
            .define('C', Items.FURNACE)
            .define('D', RagiumBlocks.Casings.ELITE)
            .unlockedBy("has_circuit", has(HTMachineTier.ELITE.getCircuitTag()))
            .savePrefixed(output)
    }
}
