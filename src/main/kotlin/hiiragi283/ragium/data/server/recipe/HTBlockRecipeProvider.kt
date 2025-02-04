package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.data.HTMultiItemRecipeBuilder
import hiiragi283.ragium.api.extension.define
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object HTBlockRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerGrates(output)
        registerCasings(output)
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
            HTMultiItemRecipeBuilder
                .assembler()
                .itemInput(grate, 4)
                .itemInput(HTTagPrefix.GEAR, casings.machineTier.getSteelMetal())
                .itemInput(corner, 4)
                .itemOutput(casings, 6)
                .save(output)
        }
    }

    /*private fun registerHulls(output: RecipeOutput) {
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
    }*/

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
            HTMultiItemRecipeBuilder
                .assembler()
                .itemInput(HTTagPrefix.INGOT, coil.machineTier.getSubMetal(), 8)
                .itemInput(RagiumBlocks.SHAFT)
                .itemOutput(coil, 4)
                .save(output)
        }
    }

    private fun registerBurners(output: RecipeOutput) {
        RagiumBlocks.Burners.entries.forEach { burner: RagiumBlocks.Burners ->
            val core: ItemLike = when (burner) {
                RagiumBlocks.Burners.ADVANCED -> Items.MAGMA_BLOCK
                RagiumBlocks.Burners.ELITE -> RagiumBlocks.SOUL_MAGMA_BLOCK
                RagiumBlocks.Burners.ULTIMATE -> RagiumBlocks.STORAGE_BLOCKS[RagiumMaterials.FIERY_COAL]!!
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
            ShapedRecipeBuilder
                .shaped(RecipeCategory.TRANSPORTATION, drum)
                .pattern("ABA")
                .pattern("ACA")
                .pattern("ABA")
                .define('A', HTTagPrefix.INGOT, drum.machineTier.getSubMetal())
                .define('B', ItemTags.SLABS)
                .define('C', Items.BUCKET)
                .unlockedBy("has_slab", has(ItemTags.SLABS))
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
            .define('A', HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .define('B', HTMachineTier.ADVANCED.getCircuitTag())
            .define('C', Tags.Items.OBSIDIANS_CRYING)
            .unlockedBy("has_circuit", has(HTMachineTier.ADVANCED.getCircuitTag()))
            .savePrefixed(output)
        // Slag Collector
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumBlocks.SLAG_COLLECTOR)
            .pattern("ABA")
            .pattern("BCB")
            .pattern("ABA")
            .define('A', HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .define('B', Tags.Items.GRAVELS)
            .define('C', Items.HOPPER)
            .unlockedBy("has_hopper", has(Items.HOPPER))
            .savePrefixed(output)
    }

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

        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumBlocks.PRIMITIVE_BLAST_FURNACE)
            .pattern("AAA")
            .pattern("ABA")
            .pattern("CCC")
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', Items.FURNACE)
            .define('C', Items.BRICKS)
            .unlockedBy("has_ragi_alloy", has(HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY))
            .savePrefixed(output)

        // Assembler
        machine3(
            output,
            RagiumMachineKeys.ASSEMBLER,
            RagiumMaterials.RAGI_STEEL,
            Items.CRAFTER,
            HTMachineTier.ADVANCED,
        )
        // Blast Furnace
        machine1(
            output,
            RagiumMachineKeys.BLAST_FURNACE,
            CommonMaterials.STEEL,
            Items.BLAST_FURNACE,
            HTMachineTier.BASIC,
        )
        // Compressor
        machine2(
            output,
            RagiumMachineKeys.COMPRESSOR,
            CommonMaterials.STEEL,
            Items.PISTON,
            HTMachineTier.BASIC,
        )
        // Extractor
        machine3(
            output,
            RagiumMachineKeys.EXTRACTOR,
            VanillaMaterials.GOLD,
            Items.HOPPER,
            HTMachineTier.ADVANCED,
        )
        // Grinder
        machine2(
            output,
            RagiumMachineKeys.GRINDER,
            RagiumMaterials.RAGI_STEEL,
            RagiumBlocks.MANUAL_GRINDER,
            HTMachineTier.ADVANCED,
        )
        // Growth Chamber
        // Infuser
        machine3(
            output,
            RagiumMachineKeys.INFUSER,
            VanillaMaterials.IRON,
            Items.HOPPER,
            HTMachineTier.ADVANCED,
        )
        // Laser Assembly
        machine3(
            output,
            RagiumMachineKeys.LASER_ASSEMBLY,
            CommonMaterials.ALUMINUM,
            Items.END_CRYSTAL,
            HTMachineTier.ULTIMATE,
        )
        // Mixer
        machine2(
            output,
            RagiumMachineKeys.MIXER,
            VanillaMaterials.GOLD,
            Items.CAULDRON,
            HTMachineTier.ADVANCED,
        )
        // Multi Smelter
        machine2(
            output,
            RagiumMachineKeys.MULTI_SMELTER,
            RagiumMaterials.DEEP_STEEL,
            Items.FURNACE,
            HTMachineTier.ELITE,
        )
        // Refinery
        machine3(
            output,
            RagiumMachineKeys.REFINERY,
            RagiumMaterials.RAGI_STEEL,
            RagiumBlocks.CHEMICAL_GLASS,
            HTMachineTier.ELITE,
        )
    }

    private fun machine1(
        output: RecipeOutput,
        machine: HTMachineKey,
        ingot: HTMaterialKey,
        core: ItemLike,
        tier: HTMachineTier,
    ) {
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, machine.getBlock())
            .pattern("AAA")
            .pattern("ABA")
            .pattern("CCC")
            .define('A', HTTagPrefix.INGOT, ingot)
            .define('B', core)
            .define('C', tier.getCasing())
            .unlockedBy("has_core", has(core))
            .savePrefixed(output)
    }

    private fun machine2(
        output: RecipeOutput,
        machine: HTMachineKey,
        ingot: HTMaterialKey,
        core: ItemLike,
        tier: HTMachineTier,
    ) {
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, machine.getBlock())
            .pattern("AAA")
            .pattern("B B")
            .pattern("CCC")
            .define('A', HTTagPrefix.INGOT, ingot)
            .define('B', core)
            .define('C', tier.getCasing())
            .unlockedBy("has_core", has(core))
            .savePrefixed(output)
    }

    private fun machine3(
        output: RecipeOutput,
        machine: HTMachineKey,
        ingot: HTMaterialKey,
        core: ItemLike,
        tier: HTMachineTier,
    ) {
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, machine.getBlock())
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .define('A', HTTagPrefix.INGOT, ingot)
            .define('B', tier.getCircuitTag())
            .define('C', core)
            .define('D', tier.getCasing())
            .unlockedBy("has_core", has(core))
            .savePrefixed(output)
    }
}
