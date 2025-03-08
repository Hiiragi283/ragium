package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTAssemblerRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object HTBlockRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerAddons(output)
        registerCasings(output)

        registerMachines(output)
        registerConsumers(output)
        registerGenerators(output)
        registerProcessors(output)
    }

    //    Machines    //

    private fun registerAddons(output: RecipeOutput) {
        // E.N.I.
        HTShapedRecipeBuilder(RagiumBlocks.ENERGY_NETWORK_INTERFACE)
            .cross8()
            .define('A', HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .define('B', RagiumItemTags.CIRCUITS_ADVANCED)
            .define('C', Tags.Items.ENDER_PEARLS)
            .save(output)
    }

    private fun registerCasings(output: RecipeOutput) {
        // Wooden
        HTShapedRecipeBuilder(RagiumBlocks.WOODEN_CASING)
            .cross8()
            .define('A', ItemTags.LOGS)
            .define('B', ItemTags.PLANKS)
            .define('C', RagiumItems.FORGE_HAMMER)
            .save(output)
        // Cobblestone
        HTShapedRecipeBuilder(RagiumBlocks.COBBLESTONE_CASING)
            .cross8()
            .define('A', Items.STONE)
            .define('B', Items.COBBLESTONE)
            .define('C', RagiumItems.FORGE_HAMMER)
            .save(output)

        // Basic
        HTShapedRecipeBuilder(RagiumBlocks.MACHINE_FRAME)
            .cross8()
            .define('A', HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .define('B', Tags.Items.GLASS_BLOCKS_COLORLESS)
            .define('C', Tags.Items.DUSTS_REDSTONE)
            .save(output)

        HTAssemblerRecipeBuilder(lookup)
            .itemInput(HTTagPrefix.INGOT, CommonMaterials.STEEL, 4)
            .itemInput(Tags.Items.GLASS_BLOCKS_COLORLESS, 4)
            .itemInput(Tags.Items.DUSTS_REDSTONE)
            .itemOutput(RagiumBlocks.MACHINE_FRAME)
            .save(output)
        // Chemical
        HTAssemblerRecipeBuilder(lookup)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterials.DEEP_STEEL, 4)
            .itemInput(RagiumItemTags.GLASS_BLOCKS_QUARTZ, 4)
            .itemInput(Tags.Items.DUSTS_GLOWSTONE)
            .itemOutput(RagiumBlocks.CHEMICAL_MACHINE_FRAME)
            .save(output)
        // Precision
        HTAssemblerRecipeBuilder(lookup)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.NETHERITE, 4)
            .itemInput(RagiumItemTags.GLASS_BLOCKS_OBSIDIAN, 4)
            .itemInput(HTTagPrefix.GEM, RagiumMaterials.WARPED_CRYSTAL)
            .itemOutput(RagiumBlocks.PRECISION_MACHINE_FRAME)
            .save(output)
    }

    private fun registerMachines(output: RecipeOutput) {
        // Manual Machine
        HTShapedRecipeBuilder(RagiumBlocks.MANUAL_GRINDER)
            .pattern("A  ")
            .pattern("BBB")
            .pattern("CCC")
            .define('A', Tags.Items.RODS_WOODEN)
            .define('B', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('C', Items.BRICKS)
            .save(output)

        HTShapedRecipeBuilder(RagiumBlocks.DISENCHANTING_TABLE)
            .pattern(" A ")
            .pattern("BCB")
            .pattern("CCC")
            .define('A', Items.GRINDSTONE)
            .define('B', HTTagPrefix.GEM, RagiumMaterials.RAGI_CRYSTAL)
            .define('C', Tags.Items.OBSIDIANS_CRYING)
            .save(output)
    }

    private fun registerConsumers(output: RecipeOutput) {
        // Fisher
        HTShapedRecipeBuilder(HTMachineType.FISHER)
            .pattern(
                "ABA",
                "CDC",
                "EFE",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', Tags.Items.TOOLS_FISHING_ROD)
            .define('C', Tags.Items.BARRELS_WOODEN)
            .define('D', RagiumBlocks.MACHINE_FRAME)
            .define('E', HTTagPrefix.GEAR, VanillaMaterials.COPPER)
            .define('F', RagiumItemTags.CIRCUITS_BASIC)
            .save(output)
    }

    private fun registerGenerators(output: RecipeOutput) {
        // Stirling Generator
        HTShapedRecipeBuilder(HTMachineType.STIRLING_GENERATOR)
            .pattern(
                "AAA",
                "ABA",
                "CDC",
            ).define('A', HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .define('B', RagiumBlocks.MACHINE_FRAME)
            .define('C', Tags.Items.DUSTS_REDSTONE)
            .define('D', Tags.Items.PLAYER_WORKSTATIONS_FURNACES)
            .save(output)

        // Combustion Generator
        HTAssemblerRecipeBuilder(lookup)
            .itemInput(RagiumBlocks.CHEMICAL_MACHINE_FRAME)
            .itemInput(RagiumItems.ENGINE, 2)
            .itemInput(RagiumItemTags.CIRCUITS_ADVANCED, 2)
            .itemOutput(HTMachineType.COMBUSTION_GENERATOR)
            .save(output)
        // Thermal Generator
        HTAssemblerRecipeBuilder(lookup)
            .itemInput(RagiumBlocks.CHEMICAL_MACHINE_FRAME)
            .itemInput(Items.MAGMA_BLOCK, 8)
            .itemInput(RagiumItemTags.CIRCUITS_ADVANCED, 2)
            .itemOutput(HTMachineType.THERMAL_GENERATOR)
            .save(output)

        // Enchantment Generator
        HTAssemblerRecipeBuilder(lookup)
            .itemInput(RagiumBlocks.PRECISION_MACHINE_FRAME)
            .itemInput(Items.ENCHANTING_TABLE)
            .itemInput(RagiumItemTags.CIRCUITS_ELITE, 4)
            .itemOutput(HTMachineType.ENCH_GENERATOR)
            .save(output)
        // Solar Generator
        HTAssemblerRecipeBuilder(lookup)
            .itemInput(RagiumBlocks.PRECISION_MACHINE_FRAME)
            .itemInput(RagiumItems.SOLAR_PANEL)
            .itemInput(RagiumItemTags.CIRCUITS_ELITE)
            .itemOutput(HTMachineType.SOLAR_GENERATOR)
            .save(output)
    }

    private fun registerProcessors(output: RecipeOutput) {
        // Alloy Furnace
        HTShapedRecipeBuilder(HTMachineType.ALLOY_FURNACE)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', Tags.Items.PLAYER_WORKSTATIONS_FURNACES)
            .define('C', RagiumBlocks.MACHINE_FRAME)
            .define('D', Items.DEEPSLATE_BRICKS)
            .save(output)
        // Assembler
        HTShapedRecipeBuilder(HTMachineType.ASSEMBLER)
            .pattern(
                "ABA",
                " C ",
                "DED",
            ).define('A', HTTagPrefix.BLOCK, RagiumMaterials.DEEP_STEEL)
            .define('B', Items.CRAFTER)
            .define('C', RagiumBlocks.MACHINE_FRAME)
            .define('D', HTTagPrefix.GEAR, RagiumMaterials.DEEP_STEEL)
            .define('E', RagiumItemTags.CIRCUITS_ADVANCED)
            .save(output)
        // Auto Chisel
        HTShapedRecipeBuilder(HTMachineType.AUTO_CHISEL)
            .pattern(
                "AAA",
                " B ",
                "CDC",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', RagiumBlocks.MACHINE_FRAME)
            .define('C', HTTagPrefix.GEAR, CommonMaterials.STEEL)
            .define('D', Items.STONECUTTER)
            .save(output)
        // Compressor
        HTShapedRecipeBuilder(HTMachineType.COMPRESSOR)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', Items.PISTON)
            .define('C', RagiumBlocks.MACHINE_FRAME)
            .define('D', HTTagPrefix.GEAR, CommonMaterials.STEEL)
            .define('E', ItemTags.ANVIL)
            .save(output)
        // Crusher
        // Electric Furnace
        HTShapedRecipeBuilder(HTMachineType.ELECTRIC_FURNACE)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', Tags.Items.PLAYER_WORKSTATIONS_FURNACES)
            .define('C', RagiumBlocks.MACHINE_FRAME)
            .define('D', HTTagPrefix.COIL, VanillaMaterials.COPPER)
            .define('E', HTTagPrefix.BLOCK, VanillaMaterials.COPPER)
            .save(output)
        // Grinder
        HTShapedRecipeBuilder(HTMachineType.GRINDER)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', Items.FLINT)
            .define('C', RagiumBlocks.MACHINE_FRAME)
            .define('D', HTTagPrefix.GEAR, CommonMaterials.STEEL)
            .define('E', Items.HOPPER)
            .save(output)

        HTShapedRecipeBuilder(HTMachineType.GRINDER)
            .pattern(
                "AAA",
                " B ",
                "CDC",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', RagiumBlocks.MANUAL_GRINDER)
            .define('C', HTTagPrefix.GEAR, CommonMaterials.STEEL)
            .define('D', Items.HOPPER)
            .save(output, RagiumAPI.id("grinder_alt"))

        // Extractor
        HTShapedRecipeBuilder(HTMachineType.EXTRACTOR)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.EMBER_ALLOY)
            .define('B', Tags.Items.BUCKETS_EMPTY)
            .define('C', RagiumBlocks.CHEMICAL_MACHINE_FRAME)
            .define('D', HTTagPrefix.COIL, VanillaMaterials.GOLD)
            .define('E', Items.HOPPER)
            .save(output)
        // Growth Chamber
        HTShapedRecipeBuilder(HTMachineType.GROWTH_CHAMBER)
            .pattern(
                "AAA",
                "BCB",
                "DEF",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.EMBER_ALLOY)
            .define('B', Tags.Items.GLASS_BLOCKS_COLORLESS)
            .define('C', RagiumBlocks.CHEMICAL_MACHINE_FRAME)
            .define('D', ItemTags.AXES)
            .define('E', Tags.Items.BUCKETS_WATER)
            .define('F', ItemTags.HOES)
            .save(output)
        // Infuser
        HTShapedRecipeBuilder(HTMachineType.INFUSER)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.EMBER_ALLOY)
            .define('B', Items.DISPENSER)
            .define('C', RagiumBlocks.CHEMICAL_MACHINE_FRAME)
            .define('D', HTTagPrefix.COIL, VanillaMaterials.GOLD)
            .define('E', Items.HOPPER)
            .save(output)
        // Mixer
        HTShapedRecipeBuilder(HTMachineType.MIXER)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.EMBER_ALLOY)
            .define('B', Tags.Items.BUCKETS_EMPTY)
            .define('C', RagiumBlocks.CHEMICAL_MACHINE_FRAME)
            .define('D', HTTagPrefix.COIL, VanillaMaterials.GOLD)
            .define('E', Items.CAULDRON)
            .save(output)
        // Refinery
        HTShapedRecipeBuilder(HTMachineType.REFINERY)
            .pattern(
                "ABA",
                "BCB",
                "DED",
            ).define('A', Items.REDSTONE_TORCH)
            .define('B', RagiumItemTags.GLASS_BLOCKS_QUARTZ)
            .define('C', HTTagPrefix.GEAR, VanillaMaterials.DIAMOND)
            .define('D', HTTagPrefix.COIL, VanillaMaterials.GOLD)
            .define('E', HTTagPrefix.BLOCK, VanillaMaterials.GOLD)
            .save(output)
        // Solidifier
        HTShapedRecipeBuilder(HTMachineType.SOLIDIFIER)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.EMBER_ALLOY)
            .define('B', RagiumItemTags.MOLDS)
            .define('C', RagiumBlocks.CHEMICAL_MACHINE_FRAME)
            .define('D', HTTagPrefix.COIL, VanillaMaterials.GOLD)
            .define('E', Items.CAULDRON)
            .save(output)

        // Brewery
        HTShapedRecipeBuilder(HTMachineType.BREWERY)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.DURALUMIN)
            .define('B', Items.BREWING_STAND)
            .define('C', RagiumBlocks.PRECISION_MACHINE_FRAME)
            .define('D', RagiumItemTags.CIRCUITS_ELITE)
            .define('E', Items.CAULDRON)
            .save(output)
        // Enchanter
        HTShapedRecipeBuilder(HTMachineType.ENCHANTER)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.DURALUMIN)
            .define('B', Items.ENCHANTED_BOOK)
            .define('C', RagiumBlocks.PRECISION_MACHINE_FRAME)
            .define('D', RagiumItemTags.CIRCUITS_ELITE)
            .define('E', Items.ENCHANTING_TABLE)
            .save(output)
        // Laser Assembly
        HTShapedRecipeBuilder(HTMachineType.LASER_ASSEMBLY)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.DURALUMIN)
            .define('B', RagiumItemTags.GLASS_BLOCKS_OBSIDIAN)
            .define('C', RagiumBlocks.PRECISION_MACHINE_FRAME)
            .define('D', RagiumItemTags.CIRCUITS_ELITE)
            .define('E', HTTagPrefix.GEM, RagiumMaterials.RAGI_CRYSTAL)
            .save(output)
        // Multi Smelter
        HTShapedRecipeBuilder(HTMachineType.MULTI_SMELTER)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.DURALUMIN)
            .define('B', Tags.Items.PLAYER_WORKSTATIONS_FURNACES)
            .define('C', RagiumBlocks.PRECISION_MACHINE_FRAME)
            .define('D', HTTagPrefix.COIL, CommonMaterials.ALUMINUM)
            .define('E', HTTagPrefix.BLOCK, CommonMaterials.ALUMINUM)
            .save(output)
    }
}
