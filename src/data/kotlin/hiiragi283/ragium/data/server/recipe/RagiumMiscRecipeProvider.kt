package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSmithingRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags

object RagiumMiscRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        casings()
        machines()

        devices()
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
        HTShapedRecipeBuilder(RagiumBlocks.MACHINE_CASING)
            .casing()
            .define('A', RagiumItemTags.INGOTS_RAGI_ALLOY)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .define('C', Blocks.DEEPSLATE_TILES)
            .save(output)
        // Advanced Machine
        HTShapedRecipeBuilder(RagiumBlocks.ADVANCED_MACHINE_CASING)
            .casing()
            .define('A', RagiumItemTags.INGOTS_ADVANCED_RAGI_ALLOY)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .define('C', RagiumBlocks.AZURE_TILE_SETS.base)
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

    private fun machines() {
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

        // Machine
        fun basicMachine(machine: ItemLike, part: Ingredient) {
            HTSmithingRecipeBuilder(machine)
                .addIngredient(RagiumItemTags.CIRCUITS_BASIC)
                .addIngredient(RagiumBlocks.MACHINE_CASING)
                .addIngredient(part)
                .save(output)
        }

        basicMachine(RagiumBlocks.CRUSHER, Ingredient.of(RagiumItemTags.TOOLS_FORGE_HAMMER))
        basicMachine(RagiumBlocks.EXTRACTOR, Ingredient.of(Items.DISPENSER))

        fun advMachine(machine: ItemLike, part: Ingredient) {
            HTSmithingRecipeBuilder(machine)
                .addIngredient(RagiumItemTags.CIRCUITS_ADVANCED)
                .addIngredient(RagiumBlocks.ADVANCED_MACHINE_CASING)
                .addIngredient(part)
                .save(output)
        }

        advMachine(RagiumBlocks.ADVANCED_CRUSHER, Ingredient.of(RagiumItemTags.TOOLS_FORGE_HAMMER))
        advMachine(RagiumBlocks.ADVANCED_EXTRACTOR, Ingredient.of(Items.DISPENSER))
        advMachine(RagiumBlocks.INFUSER, Ingredient.of(Items.HOPPER))
        advMachine(RagiumBlocks.REFINERY, Ingredient.of(RagiumItemTags.GLASS_BLOCKS_QUARTZ))

        HTSmithingRecipeBuilder(RagiumBlocks.ADVANCED_CRUSHER)
            .addIngredient(RagiumItemTags.CIRCUITS_ADVANCED)
            .addIngredient(RagiumBlocks.CRUSHER)
            .saveSuffixed(output, "_from_basic")

        HTSmithingRecipeBuilder(RagiumBlocks.ADVANCED_EXTRACTOR)
            .addIngredient(RagiumItemTags.CIRCUITS_ADVANCED)
            .addIngredient(RagiumBlocks.EXTRACTOR)
            .saveSuffixed(output, "_from_basic")
    }

    private fun devices() {
        // Milk Drain
        HTShapedRecipeBuilder(RagiumBlocks.MILK_DRAIN)
            .pattern("A", "B", "C")
            .define('A', Tags.Items.INGOTS_COPPER)
            .define('B', Tags.Items.BARRELS_WOODEN)
            .define('C', RagiumBlocks.STONE_CASING)
            .save(output)
        // Soul Spike
        HTShapedRecipeBuilder(RagiumBlocks.SOUL_SPIKE)
            .pattern(
                " A ",
                "ABA",
                "BCB",
            ).define('A', Items.POINTED_DRIPSTONE)
            .define('B', ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .define('C', Tags.Items.NETHER_STARS)
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

        advDevice(RagiumBlocks.ENI, RagiumItemTags.STORAGE_BLOCKS_RAGI_CRYSTAL)
        advDevice(RagiumBlocks.EXP_COLLECTOR, Items.HOPPER)
        advDevice(RagiumBlocks.LAVA_COLLECTOR, Tags.Items.BUCKETS_LAVA)
        advDevice(RagiumBlocks.TELEPORT_ANCHOR, RagiumItemTags.STORAGE_BLOCKS_WARPED_CRYSTAL)

        // Elite
    }
}
