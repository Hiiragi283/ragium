package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.define
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.machine.HTMachineKey
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
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object HTBlockRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerGrates(output)
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

    /*private fun registerCasings(output: RecipeOutput) {
        RagiumBlocks.Casings.entries.forEach { casings: RagiumBlocks.Casings ->
            val corner: Item = when (casings) {
                RagiumBlocks.Casings.BASIC -> Items.STONE
                RagiumBlocks.Casings.ADVANCED -> Items.POLISHED_DEEPSLATE
                RagiumBlocks.Casings.ELITE -> Items.QUARTZ_BLOCK
                RagiumBlocks.Casings.ULTIMATE -> Items.OBSIDIAN
            }
            val glass: Ingredient = when (casings) {
                RagiumBlocks.Casings.BASIC -> Ingredient.of(Tags.Items.GLASS_BLOCKS)
                RagiumBlocks.Casings.ADVANCED -> Ingredient.of(Tags.Items.GLASS_BLOCKS_TINTED)
                RagiumBlocks.Casings.ELITE -> Ingredient.of(RagiumBlocks.CHEMICAL_GLASS)
                RagiumBlocks.Casings.ULTIMATE -> Ingredient.of(RagiumBlocks.OBSIDIAN_GLASS)
            }
            // Shaped Crafting
            ShapedRecipeBuilder
                .shaped(RecipeCategory.BUILDING_BLOCKS, casings, 3)
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', corner)
                .define('B', glass)
                .define('C', HTTagPrefix.GEAR, casings.machineTier.getSteelMetal())
                .unlockedBy("has_gear", has(HTTagPrefix.GEAR, casings.machineTier.getSteelMetal()))
                .savePrefixed(output)
            // Assembler
            HTMultiItemRecipeBuilder
                .assembler()
                .itemInput(glass, 4)
                .itemInput(corner, 4)
                .itemInput(HTTagPrefix.GEAR, casings.machineTier.getSteelMetal())
                .itemOutput(casings, 6)
                .save(output)
        }
    }*/

    private fun registerBurners(output: RecipeOutput) {
        RagiumBlocks.Burners.entries.forEach { burner: RagiumBlocks.Burners ->
            val core: ItemLike = when (burner) {
                RagiumBlocks.Burners.ADVANCED -> Items.MAGMA_BLOCK
                RagiumBlocks.Burners.ELITE -> RagiumBlocks.SOUL_MAGMA_BLOCK
                RagiumBlocks.Burners.ULTIMATE -> RagiumBlocks.STORAGE_BLOCKS[RagiumMaterials.FIERY_COAL]!!
            }
            val base: Item = when (burner) {
                RagiumBlocks.Burners.ADVANCED -> Items.POLISHED_BLACKSTONE_BRICKS
                RagiumBlocks.Burners.ELITE -> Items.END_STONE_BRICKS
                RagiumBlocks.Burners.ULTIMATE -> Items.RED_NETHER_BRICKS
            }
            // Shaped Crafting
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, burner)
                .pattern("A A")
                .pattern("ABA")
                .pattern("CCC")
                .define('A', burner.machineTier.getGrate())
                .define('B', core)
                .define('C', base)
                .unlockedBy("has_core", has(core))
                .savePrefixed(output)
        }
    }

    private fun registerDrums(output: RecipeOutput) {
        RagiumBlocks.Drums.entries.forEach { drum: RagiumBlocks.Drums ->
            val metal: HTMaterialKey = when (drum) {
                RagiumBlocks.Drums.BASIC -> VanillaMaterials.COPPER
                RagiumBlocks.Drums.ADVANCED -> VanillaMaterials.GOLD
                RagiumBlocks.Drums.ELITE -> CommonMaterials.ALUMINUM
                RagiumBlocks.Drums.ULTIMATE -> RagiumMaterials.RAGIUM
            }
            // Shaped Crafting
            ShapedRecipeBuilder
                .shaped(RecipeCategory.TRANSPORTATION, drum)
                .pattern("ABA")
                .pattern("ACA")
                .pattern("ABA")
                .define('A', HTTagPrefix.INGOT, metal)
                .define('B', Items.SMOOTH_STONE_SLAB)
                .define('C', Items.BUCKET)
                .unlockedBy("has_slab", has(Items.SMOOTH_STONE_SLAB))
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
            .define('B', RagiumItemTags.ADVANCED_CIRCUIT)
            .define('C', Tags.Items.ENDER_PEARLS)
            .unlockedBy("has_circuit", has(RagiumItemTags.ADVANCED_CIRCUIT))
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
            .define('B', Items.BLAST_FURNACE)
            .define('C', Items.BRICKS)
            .unlockedBy("has_ragi_alloy", has(HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY))
            .savePrefixed(output)

        // Machine Casing
        fun casing(
            result: ItemLike,
            topMetal: HTMaterialKey,
            glass: Ingredient,
            gearMetal: HTMaterialKey,
        ) {
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, result)
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', HTTagPrefix.INGOT, topMetal)
                .define('B', glass)
                .define('C', HTTagPrefix.GEAR, gearMetal)
                .unlockedBy("has_gear", has(HTTagPrefix.GEAR, gearMetal))
                .savePrefixed(output)
        }

        casing(
            RagiumItems.MACHINE_CASING,
            RagiumMaterials.RAGI_ALLOY,
            Ingredient.of(Tags.Items.GLASS_BLOCKS),
            CommonMaterials.STEEL,
        )
        casing(
            RagiumItems.CHEMICAL_MACHINE_CASING,
            VanillaMaterials.GOLD,
            Ingredient.of(RagiumBlocks.CHEMICAL_GLASS),
            RagiumMaterials.DEEP_STEEL,
        )
        casing(
            RagiumItems.PRECISION_MACHINE_CASING,
            CommonMaterials.ALUMINUM,
            Ingredient.of(RagiumBlocks.OBSIDIAN_GLASS),
            VanillaMaterials.NETHERITE,
        )

        // Assembler
        registerMachine(
            output,
            RagiumMachineKeys.ASSEMBLER,
            RagiumItems.MACHINE_CASING,
            Ingredient.of(Items.CRAFTER),
            Ingredient.of(RagiumItemTags.ADVANCED_CIRCUIT),
        )
        // Blast Furnace
        registerMachine(
            output,
            RagiumMachineKeys.BLAST_FURNACE,
            RagiumItems.MACHINE_CASING,
            Ingredient.of(RagiumBlocks.PRIMITIVE_BLAST_FURNACE),
            Ingredient.of(Items.MAGMA_BLOCK),
        )
        // Compressor
        registerMachine(
            output,
            RagiumMachineKeys.COMPRESSOR,
            RagiumItems.MACHINE_CASING,
            Ingredient.of(RagiumItems.FORGE_HAMMER),
            Ingredient.of(Items.PISTON),
        )
        // Extractor
        registerMachine(
            output,
            RagiumMachineKeys.EXTRACTOR,
            RagiumItems.CHEMICAL_MACHINE_CASING,
            Ingredient.of(Items.HOPPER),
            Ingredient.of(Items.PISTON),
        )
        // Grinder
        registerMachine(
            output,
            RagiumMachineKeys.GRINDER,
            RagiumItems.MACHINE_CASING,
            Ingredient.of(RagiumBlocks.MANUAL_GRINDER),
            Ingredient.of(Items.FLINT),
        )
        // Growth Chamber
        // Infuser
        registerMachine(
            output,
            RagiumMachineKeys.INFUSER,
            RagiumItems.CHEMICAL_MACHINE_CASING,
            Ingredient.of(Items.HOPPER),
            Ingredient.of(Items.BUCKET),
        )
        // Laser Assembly
        registerMachine(
            output,
            RagiumMachineKeys.LASER_ASSEMBLY,
            RagiumItems.PRECISION_MACHINE_CASING,
            Ingredient.of(Tags.Items.NETHER_STARS),
            Ingredient.of(Items.END_CRYSTAL),
        )
        // Mixer
        registerMachine(
            output,
            RagiumMachineKeys.MIXER,
            RagiumItems.CHEMICAL_MACHINE_CASING,
            Ingredient.of(Tags.Items.GLASS_BLOCKS),
            Ingredient.of(Items.CAULDRON),
        )
        // Multi Smelter
        registerMachine(
            output,
            RagiumMachineKeys.MULTI_SMELTER,
            RagiumItems.MACHINE_CASING,
            Ingredient.of(RagiumItems.BLAZE_REAGENT),
            Ingredient.of(Items.FURNACE),
        )
        // Refinery
        registerMachine(
            output,
            RagiumMachineKeys.REFINERY,
            RagiumItems.CHEMICAL_MACHINE_CASING,
            Ingredient.of(RagiumItems.CRUDE_OIL_BUCKET),
            Ingredient.of(Tags.Items.GLASS_BLOCKS),
        )
    }

    private fun registerMachine(
        output: RecipeOutput,
        machine: HTMachineKey,
        casing: ItemLike,
        top: Ingredient,
        left: Ingredient,
        right: Ingredient = left,
    ) {
        val gearMaterial: HTMaterialKey = when (casing) {
            RagiumItems.MACHINE_CASING -> VanillaMaterials.COPPER
            RagiumItems.CHEMICAL_MACHINE_CASING -> VanillaMaterials.IRON
            RagiumItems.PRECISION_MACHINE_CASING -> VanillaMaterials.DIAMOND
            else -> return
        }
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, machine.getBlock())
            .pattern(" A ")
            .pattern("BCD")
            .pattern("EFE")
            .define('A', top)
            .define('B', left)
            .define('C', casing)
            .define('D', right)
            .define('E', HTTagPrefix.GEAR, gearMaterial)
            .define('F', RagiumItemTags.BASIC_CIRCUIT)
            .unlockedBy("has_casing", has(casing))
            .savePrefixed(output)
    }
}
