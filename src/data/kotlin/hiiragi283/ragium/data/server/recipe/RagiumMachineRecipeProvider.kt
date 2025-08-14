package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSmithingRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumMachineRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        casings()

        machines()
        devices()

        drums()
    }

    private fun casings() {
        // Wooden
        HTShapedRecipeBuilder(RagiumBlocks.Casings.WOODEN, 4)
            .cross8()
            .define('A', ItemTags.LOGS)
            .define('B', ItemTags.PLANKS)
            .define('C', RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
            .save(output)
        // Stone
        HTShapedRecipeBuilder(RagiumBlocks.Casings.STONE, 4)
            .casing()
            .define('A', Tags.Items.COBBLESTONES_NORMAL)
            .define('B', RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
            .define('C', Items.SMOOTH_STONE)
            .save(output)
        // Machine
        HTShapedRecipeBuilder(RagiumBlocks.Frames.BASIC, 2)
            .hollow8()
            .define('A', Tags.Items.INGOTS_IRON)
            .define('B', RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
            .save(output)
        // Advanced Machine
        HTShapedRecipeBuilder(RagiumBlocks.Frames.ADVANCED, 2)
            .hollow8()
            .define('A', RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .define('B', RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
            .save(output)
        // Elite Machine
        HTShapedRecipeBuilder(RagiumBlocks.Frames.ELITE, 4)
            .hollow8()
            .define('A', Tags.Items.INGOTS_NETHERITE)
            .define('B', RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
            .save(output)
        // Device
        HTShapedRecipeBuilder(RagiumBlocks.Casings.DEVICE)
            .cross8()
            .define('A', Items.BLACK_CONCRETE)
            .define('B', RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .define('C', RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
            .save(output)

        HTShapedRecipeBuilder(RagiumBlocks.Casings.DEVICE, 4)
            .cross8()
            .define('A', Tags.Items.OBSIDIANS_NORMAL)
            .define('B', RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .define('C', RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
            .saveSuffixed(output, "_with_obsidian")
    }

    //    Machines    //

    private fun machines() {
        // Basic
        basicMachine(RagiumBlocks.Machines.CRUSHER, Ingredient.of(Items.FLINT))
        basicMachine(RagiumBlocks.Machines.BLOCK_BREAKER, Ingredient.of(ItemTags.PICKAXES))
        basicMachine(RagiumBlocks.Machines.COMPRESSOR, Ingredient.of(Items.PISTON))
        basicMachine(RagiumBlocks.Machines.ENGRAVER, Ingredient.of(Items.STONECUTTER))
        basicMachine(RagiumBlocks.Machines.EXTRACTOR, Ingredient.of(Items.DISPENSER))
        // Advanced
        advancedMachine(RagiumBlocks.Machines.ALLOY_SMELTER, Ingredient.of(Items.FURNACE), Items.NETHER_BRICKS)
        advancedMachine(RagiumBlocks.Machines.MELTER, Ingredient.of(Items.BLAST_FURNACE), Items.POLISHED_BLACKSTONE_BRICKS)
        advancedMachine(
            RagiumBlocks.Machines.REFINERY,
            Ingredient.of(RagiumCommonTags.Items.GLASS_BLOCKS_QUARTZ),
            Items.POLISHED_BLACKSTONE_BRICKS,
        )
        advancedMachine(RagiumBlocks.Machines.SOLIDIFIER, Ingredient.of(ItemTags.TRIM_TEMPLATES), Items.POLISHED_BLACKSTONE_BRICKS)
        // Elite
        eliteMachine(RagiumBlocks.Machines.INFUSER, Ingredient.of(Items.ENCHANTING_TABLE), Items.DEEPSLATE_TILES)
    }

    private fun basicMachine(machine: ItemLike, input: Ingredient) {
        HTShapedRecipeBuilder(machine)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', RagiumCommonTags.Items.INGOTS_RAGI_ALLOY)
            .define('B', input)
            .define('C', RagiumBlocks.Frames.BASIC)
            .define('D', Items.BRICKS)
            .define('E', RagiumCommonTags.Items.CIRCUITS_BASIC)
            .save(output)
    }

    private fun advancedMachine(machine: ItemLike, input: Ingredient, bottom: ItemLike) {
        HTShapedRecipeBuilder(machine)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', RagiumCommonTags.Items.INGOTS_ADVANCED_RAGI_ALLOY)
            .define('B', input)
            .define('C', RagiumBlocks.Frames.ADVANCED)
            .define('D', bottom)
            .define('E', RagiumCommonTags.Items.CIRCUITS_ADVANCED)
            .save(output)
    }

    private fun eliteMachine(machine: ItemLike, input: Ingredient, bottom: ItemLike) {
        HTShapedRecipeBuilder(machine)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', RagiumCommonTags.Items.INGOTS_DEEP_STEEL)
            .define('B', input)
            .define('C', RagiumBlocks.Frames.ELITE)
            .define('D', bottom)
            .define('E', RagiumCommonTags.Items.CIRCUITS_ELITE)
            .save(output)
    }

    //    Devices    //

    private fun devices() {
        // Milk Drain
        HTShapedRecipeBuilder(RagiumBlocks.Devices.MILK_DRAIN)
            .pattern("A", "B", "C")
            .define('A', Tags.Items.INGOTS_COPPER)
            .define('B', Tags.Items.BARRELS_WOODEN)
            .define('C', RagiumBlocks.Casings.STONE)
            .save(output)

        // Basic
        basicDevice(RagiumBlocks.Devices.ITEM_BUFFER, Ingredient.of(Tags.Items.CHESTS))
        basicDevice(RagiumBlocks.Devices.SPRINKLER, Ingredient.of(Tags.Items.STORAGE_BLOCKS_BONE_MEAL))
        basicDevice(RagiumBlocks.Devices.WATER_COLLECTOR, Ingredient.of(Tags.Items.BUCKETS_WATER))
        // Advanced
        advancedDevice(RagiumBlocks.Devices.ENI, Ingredient.of(Tags.Items.GEMS_DIAMOND))
        advancedDevice(RagiumBlocks.Devices.EXP_COLLECTOR, Ingredient.of(Items.HOPPER))
        advancedDevice(RagiumBlocks.Devices.LAVA_COLLECTOR, Ingredient.of(Tags.Items.BUCKETS_LAVA))
        advancedDevice(RagiumBlocks.Devices.DIM_ANCHOR, Ingredient.of(RagiumCommonTags.Items.STORAGE_BLOCKS_WARPED_CRYSTAL))
        // Elite
    }

    private fun basicDevice(device: ItemLike, input: Ingredient) {
        HTShapedRecipeBuilder(device)
            .pattern(
                " A ",
                "BCB",
                "DED",
            ).define('A', input)
            .define('B', Tags.Items.GLASS_BLOCKS_CHEAP)
            .define('C', RagiumBlocks.Casings.DEVICE)
            .define('D', RagiumModTags.Items.PLASTICS)
            .define('E', RagiumCommonTags.Items.CIRCUITS_BASIC)
            .save(output)
    }

    private fun advancedDevice(device: ItemLike, input: Ingredient) {
        HTShapedRecipeBuilder(device)
            .pattern(
                " A ",
                "BCB",
                "DED",
            ).define('A', input)
            .define('B', RagiumCommonTags.Items.GLASS_BLOCKS_QUARTZ)
            .define('C', RagiumBlocks.Casings.DEVICE)
            .define('D', RagiumModTags.Items.PLASTICS)
            .define('E', RagiumCommonTags.Items.CIRCUITS_ADVANCED)
            .save(output)
    }

    private fun eliteDevice(device: ItemLike, input: Ingredient) {
        HTShapedRecipeBuilder(device)
            .pattern(
                " A ",
                "BCB",
                "DED",
            ).define('A', input)
            .define('B', RagiumCommonTags.Items.GLASS_BLOCKS_OBSIDIAN)
            .define('C', RagiumBlocks.Casings.DEVICE)
            .define('D', RagiumModTags.Items.PLASTICS)
            .define('E', RagiumCommonTags.Items.CIRCUITS_ELITE)
            .save(output)
    }

    private fun drums() {
        // Small
        HTShapedRecipeBuilder(RagiumBlocks.Drums.SMALL)
            .pattern(
                "ABA",
                "ACA",
                "ABA",
            ).define('A', Tags.Items.INGOTS_COPPER)
            .define('B', Items.SMOOTH_STONE_SLAB)
            .define('C', Tags.Items.BUCKETS_EMPTY)
            .save(output)
        // Medium
        HTSmithingRecipeBuilder(RagiumBlocks.Drums.MEDIUM)
            .addIngredient(Tags.Items.INGOTS_GOLD)
            .addIngredient(RagiumBlocks.Drums.SMALL)
            .save(output)
        // Large
        HTSmithingRecipeBuilder(RagiumBlocks.Drums.LARGE)
            .addIngredient(Tags.Items.GEMS_DIAMOND)
            .addIngredient(RagiumBlocks.Drums.MEDIUM)
            .save(output)
        // Huge
        createNetheriteUpgrade(RagiumBlocks.Drums.HUGE, RagiumBlocks.Drums.LARGE)
            .save(output)
    }
}
