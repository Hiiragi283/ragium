package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSmithingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTTransmuteRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumMiscRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        casings()
        circuits()

        machines()
        devices()

        drums()
    }

    private fun casings() {
        // Wooden
        HTShapedRecipeBuilder(RagiumBlocks.WOODEN_CASING, 4)
            .cross8()
            .define('A', ItemTags.LOGS)
            .define('B', ItemTags.PLANKS)
            .define('C', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)
        // Stone
        HTShapedRecipeBuilder(RagiumBlocks.STONE_CASING, 4)
            .casing()
            .define('A', Tags.Items.COBBLESTONES_NORMAL)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .define('C', Items.SMOOTH_STONE)
            .save(output)
        // Machine
        HTShapedRecipeBuilder(RagiumBlocks.BASIC_MACHINE_FRAME, 2)
            .hollow8()
            .define('A', Tags.Items.INGOTS_IRON)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)
        // Advanced Machine
        HTShapedRecipeBuilder(RagiumBlocks.ADVANCED_MACHINE_FRAME, 2)
            .hollow8()
            .define('A', RagiumItemTags.INGOTS_AZURE_STEEL)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)
        // Device
        HTShapedRecipeBuilder(RagiumBlocks.DEVICE_CASING)
            .cross8()
            .define('A', Tags.Items.OBSIDIANS_NORMAL)
            .define('B', RagiumItemTags.INGOTS_AZURE_STEEL)
            .define('C', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)

        HTShapedRecipeBuilder(RagiumBlocks.DEVICE_CASING, 4)
            .cross8()
            .define('A', Tags.Items.OBSIDIANS_NORMAL)
            .define('B', RagiumItemTags.INGOTS_DEEP_STEEL)
            .define('C', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .saveSuffixed(output, "_with_deep_steel")
    }

    private fun circuits() {
        // Basic Circuit
        HTShapedRecipeBuilder(RagiumItems.BASIC_CIRCUIT)
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', Tags.Items.INGOTS_COPPER)
            .define('B', Tags.Items.DUSTS_REDSTONE)
            .define('C', Tags.Items.INGOTS_IRON)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.BASIC_CIRCUIT, 2)
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', Tags.Items.INGOTS_COPPER)
            .define('B', Tags.Items.DUSTS_REDSTONE)
            .define('C', RagiumItemTags.PLASTICS)
            .savePrefixed(output, "2x_")
        // Advanced Circuit
        HTShapedRecipeBuilder(RagiumItems.ADVANCED_CIRCUIT)
            .cross8()
            .define('A', RagiumItems.AZURE_SHARD)
            .define('B', Tags.Items.DUSTS_GLOWSTONE)
            .define('C', RagiumItemTags.CIRCUITS_BASIC)
            .saveSuffixed(output, "_from_basic")

        HTShapedRecipeBuilder(RagiumItems.ADVANCED_CIRCUIT, 2)
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', RagiumItemTags.INGOTS_ADVANCED_RAGI_ALLOY)
            .define('B', Tags.Items.DUSTS_GLOWSTONE)
            .define('C', RagiumItemTags.PLASTICS)
            .save(output)
    }

    private fun machines() {
        // Basic
        basicMachine(RagiumBlocks.CRUSHER, Ingredient.of(Items.FLINT))
        basicMachine(RagiumBlocks.EXTRACTOR, Ingredient.of(Items.DISPENSER))

        // Advanced
        advancedMachine(RagiumBlocks.ALLOY_SMELTER, Ingredient.of(Items.FURNACE), Items.NETHER_BRICKS)
        advancedMachine(RagiumBlocks.MELTER, Ingredient.of(Items.BLAST_FURNACE), Items.POLISHED_BLACKSTONE_BRICKS)
        advancedMachine(RagiumBlocks.REFINERY, Ingredient.of(RagiumItemTags.GLASS_BLOCKS_QUARTZ), Items.POLISHED_BLACKSTONE_BRICKS)
        advancedMachine(RagiumBlocks.SOLIDIFIER, Ingredient.of(ItemTags.TRIM_TEMPLATES), Items.POLISHED_BLACKSTONE_BRICKS)
    }

    private fun basicMachine(machine: ItemLike, input: Ingredient) {
        HTShapedRecipeBuilder(machine)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', RagiumItemTags.INGOTS_RAGI_ALLOY)
            .define('B', input)
            .define('C', RagiumBlocks.BASIC_MACHINE_FRAME)
            .define('D', Items.BRICKS)
            .define('E', RagiumItemTags.CIRCUITS_BASIC)
            .save(output)
    }

    private fun advancedMachine(machine: ItemLike, input: Ingredient, bottom: ItemLike) {
        HTShapedRecipeBuilder(machine)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', RagiumItemTags.INGOTS_ADVANCED_RAGI_ALLOY)
            .define('B', input)
            .define('C', RagiumBlocks.ADVANCED_MACHINE_FRAME)
            .define('D', bottom)
            .define('E', RagiumItemTags.CIRCUITS_ADVANCED)
            .save(output)
    }

    private fun devices() {
        // Tree Tap
        HTShapedRecipeBuilder(RagiumBlocks.TREE_TAP)
            .pattern(
                " A ",
                "BBB",
                "B  ",
            ).define('A', Items.TRIPWIRE_HOOK)
            .define('B', Tags.Items.INGOTS_COPPER)
            .save(output)

        // Milk Drain
        HTShapedRecipeBuilder(RagiumBlocks.MILK_DRAIN)
            .pattern("A", "B", "C")
            .define('A', Tags.Items.INGOTS_COPPER)
            .define('B', Tags.Items.BARRELS_WOODEN)
            .define('C', RagiumBlocks.STONE_CASING)
            .save(output)

        // Basic
        fun basicDevice(device: ItemLike, part: Ingredient) {
            HTSmithingRecipeBuilder(device)
                .addIngredient(RagiumItemTags.CIRCUITS_BASIC)
                .addIngredient(RagiumBlocks.DEVICE_CASING)
                .addIngredient(part)
                .save(output)
        }

        basicDevice(RagiumBlocks.ITEM_COLLECTOR, Ingredient.of(Items.HOPPER))
        basicDevice(RagiumBlocks.SPRINKLER, Ingredient.of(Tags.Items.STORAGE_BLOCKS_BONE_MEAL))
        basicDevice(RagiumBlocks.WATER_COLLECTOR, Ingredient.of(Tags.Items.BUCKETS_WATER))

        // Advanced
        fun advDevice(device: ItemLike, part: TagKey<Item>) {
            HTSmithingRecipeBuilder(device)
                .addIngredient(RagiumItemTags.CIRCUITS_ADVANCED)
                .addIngredient(RagiumBlocks.DEVICE_CASING)
                .addIngredient(part)
                .save(output)
        }

        fun advDevice(device: ItemLike, part: ItemLike) {
            HTSmithingRecipeBuilder(device)
                .addIngredient(RagiumItemTags.CIRCUITS_ADVANCED)
                .addIngredient(RagiumBlocks.DEVICE_CASING)
                .addIngredient(part)
                .save(output)
        }

        advDevice(RagiumBlocks.CHARGER, RagiumItemTags.GEMS_RAGI_CRYSTAL)
        advDevice(RagiumBlocks.ENI, Tags.Items.GEMS_DIAMOND)
        advDevice(RagiumBlocks.EXP_COLLECTOR, Items.HOPPER)
        advDevice(RagiumBlocks.LAVA_COLLECTOR, Tags.Items.BUCKETS_LAVA)
        advDevice(RagiumBlocks.TELEPORT_ANCHOR, RagiumItemTags.STORAGE_BLOCKS_WARPED_CRYSTAL)

        // Elite
    }

    private fun drums() {
        // Small
        HTShapedRecipeBuilder(RagiumBlocks.SMALL_DRUM)
            .pattern(
                "ABA",
                "ACA",
                "ABA",
            ).define('A', Tags.Items.INGOTS_COPPER)
            .define('B', Items.SMOOTH_STONE_SLAB)
            .define('C', Tags.Items.BUCKETS_EMPTY)
            .save(output)
        // Medium
        HTTransmuteRecipeBuilder(RagiumBlocks.MEDIUM_DRUM)
            .addIngredient(RagiumBlocks.SMALL_DRUM)
            .addIngredient(Tags.Items.STORAGE_BLOCKS_GOLD)
            .save(output)
        // Large
        HTTransmuteRecipeBuilder(RagiumBlocks.LARGE_DRUM)
            .addIngredient(RagiumBlocks.MEDIUM_DRUM)
            .addIngredient(Tags.Items.GEMS_DIAMOND)
            .save(output)
        // Huge
        HTSmithingRecipeBuilder(RagiumBlocks.HUGE_DRUM)
            .addIngredient(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
            .addIngredient(RagiumBlocks.LARGE_DRUM)
            .addIngredient(Tags.Items.INGOTS_NETHERITE)
            .save(output)
    }
}
