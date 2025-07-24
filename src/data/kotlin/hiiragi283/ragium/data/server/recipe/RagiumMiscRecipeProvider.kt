package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSmithingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTTransmuteRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
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
        materials()

        casings()

        machines()
        devices()

        drums()
    }

    private fun materials() {
        // Ragi-Alloy
        HTShapedRecipeBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .hollow4()
            .define('A', RagiumCommonTags.Items.DUSTS_RAGINITE)
            .define('B', Tags.Items.INGOTS_COPPER)
            .save(output)

        HTCookingRecipeBuilder
            .blasting(RagiumItems.RAGI_ALLOY_INGOT)
            .addIngredient(RagiumItems.RAGI_ALLOY_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        createAlloying()
            .itemOutput(RagiumCommonTags.Items.INGOTS_RAGI_ALLOY)
            .itemInput(Tags.Items.INGOTS_COPPER)
            .itemInput(RagiumCommonTags.Items.DUSTS_RAGINITE, 3)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.RAGI_COKE)
            .hollow4()
            .define('A', RagiumCommonTags.Items.DUSTS_RAGINITE)
            .define('B', ItemTags.COALS)
            .save(output)
        // Advanced Ragi-Alloy
        HTShapedRecipeBuilder(RagiumItems.ADVANCED_RAGI_ALLOY_COMPOUND)
            .cross8()
            .define('A', Tags.Items.DUSTS_GLOWSTONE)
            .define('B', RagiumCommonTags.Items.DUSTS_RAGINITE)
            .define('C', Tags.Items.INGOTS_GOLD)
            .save(output)

        HTCookingRecipeBuilder
            .blasting(RagiumItems.ADVANCED_RAGI_ALLOY_INGOT)
            .addIngredient(RagiumItems.ADVANCED_RAGI_ALLOY_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        createAlloying()
            .itemOutput(RagiumCommonTags.Items.INGOTS_ADVANCED_RAGI_ALLOY)
            .itemInput(Tags.Items.INGOTS_GOLD)
            .itemInput(RagiumCommonTags.Items.DUSTS_RAGINITE, 3)
            .save(output)
        // Ragi-Crystal
        HTShapedRecipeBuilder(RagiumItems.RAGI_CRYSTAL)
            .hollow8()
            .define('A', RagiumCommonTags.Items.DUSTS_RAGINITE)
            .define('B', Tags.Items.GEMS_DIAMOND)
            .save(output)

        createAlloying()
            .itemOutput(RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL)
            .itemInput(Tags.Items.GEMS_DIAMOND)
            .itemInput(RagiumCommonTags.Items.DUSTS_RAGINITE, 6)
            .save(output)
        // Azure Steel
        HTShapedRecipeBuilder(RagiumItems.AZURE_STEEL_COMPOUND)
            .hollow4()
            .define('A', RagiumItems.AZURE_SHARD)
            .define('B', Tags.Items.INGOTS_IRON)
            .save(output)

        HTCookingRecipeBuilder
            .blasting(RagiumItems.AZURE_STEEL_INGOT)
            .addIngredient(RagiumItems.AZURE_STEEL_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        createAlloying()
            .itemOutput(RagiumItems.AZURE_SHARD, 2)
            .itemInput(Tags.Items.GEMS_AMETHYST)
            .itemInput(Tags.Items.GEMS_LAPIS)
            .save(output)

        createAlloying()
            .itemOutput(RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .itemInput(Tags.Items.INGOTS_IRON)
            .itemInput(RagiumItems.AZURE_SHARD, 3)
            .save(output)
        // Sawdust
        HTShapedRecipeBuilder(RagiumItems.COMPRESSED_SAWDUST)
            .hollow8()
            .define('A', RagiumCommonTags.Items.DUSTS_WOOD)
            .define('B', RagiumItems.SAWDUST)
            .save(output)

        HTCookingRecipeBuilder
            .blasting(Items.CHARCOAL, onlyBlasting = true)
            .addIngredient(RagiumItems.COMPRESSED_SAWDUST)
            .setExp(0.15f)
            .saveSuffixed(output, "_from_pellet")
        // Eldritch Pearl
        HTShapedRecipeBuilder(RagiumItems.ELDRITCH_ORB)
            .cross4()
            .define('A', RagiumCommonTags.Items.GEMS_CRIMSON_CRYSTAL)
            .define('B', RagiumCommonTags.Items.GEMS_WARPED_CRYSTAL)
            .define('C', RagiumModTags.Items.ELDRITCH_PEARL_BINDER)
            .save(output)

        createAlloying()
            .itemOutput(RagiumItems.ELDRITCH_ORB, 6)
            .itemInput(RagiumCommonTags.Items.STORAGE_BLOCKS_CRIMSON_CRYSTAL)
            .itemInput(RagiumCommonTags.Items.STORAGE_BLOCKS_WARPED_CRYSTAL)
            .save(output)
        // Deep Steel
        HTCookingRecipeBuilder
            .blasting(RagiumItems.DEEP_SCRAP)
            .addIngredient(RagiumCommonTags.Items.ORES_DEEP_SCRAP)
            .save(output)

        HTShapelessRecipeBuilder(RagiumItems.DEEP_STEEL_INGOT)
            .addIngredient(RagiumItems.DEEP_SCRAP)
            .addIngredient(RagiumItems.DEEP_SCRAP)
            .addIngredient(RagiumItems.DEEP_SCRAP)
            .addIngredient(RagiumItems.DEEP_SCRAP)
            .addIngredient(RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .addIngredient(RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .addIngredient(RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .addIngredient(RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .save(output)
        // Other
        HTShapelessRecipeBuilder(Items.GUNPOWDER, 3)
            .addIngredient(RagiumCommonTags.Items.DUSTS_SULFUR)
            .addIngredient(RagiumCommonTags.Items.DUSTS_SALTPETER)
            .addIngredient(Items.CHARCOAL)
            .addIngredient(RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
            .saveSuffixed(output, "_with_hammer")
    }

    private fun casings() {
        // Wooden
        HTShapedRecipeBuilder(RagiumBlocks.WOODEN_CASING, 4)
            .cross8()
            .define('A', ItemTags.LOGS)
            .define('B', ItemTags.PLANKS)
            .define('C', RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
            .save(output)
        // Stone
        HTShapedRecipeBuilder(RagiumBlocks.STONE_CASING, 4)
            .casing()
            .define('A', Tags.Items.COBBLESTONES_NORMAL)
            .define('B', RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
            .define('C', Items.SMOOTH_STONE)
            .save(output)
        // Machine
        HTShapedRecipeBuilder(RagiumBlocks.BASIC_MACHINE_FRAME, 2)
            .hollow8()
            .define('A', Tags.Items.INGOTS_IRON)
            .define('B', RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
            .save(output)
        // Advanced Machine
        HTShapedRecipeBuilder(RagiumBlocks.ADVANCED_MACHINE_FRAME, 2)
            .hollow8()
            .define('A', RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .define('B', RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
            .save(output)
        // Device
        HTShapedRecipeBuilder(RagiumBlocks.DEVICE_CASING)
            .cross8()
            .define('A', Items.BLACK_CONCRETE)
            .define('B', RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .define('C', RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
            .save(output)

        HTShapedRecipeBuilder(RagiumBlocks.DEVICE_CASING, 4)
            .cross8()
            .define('A', Tags.Items.OBSIDIANS_NORMAL)
            .define('B', RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .define('C', RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
            .saveSuffixed(output, "_with_obsidian")
    }

    private fun machines() {
        // Basic
        basicMachine(RagiumBlocks.CRUSHER, Ingredient.of(Items.FLINT))
        basicMachine(RagiumBlocks.BLOCK_BREAKER, Ingredient.of(ItemTags.PICKAXES))
        basicMachine(RagiumBlocks.EXTRACTOR, Ingredient.of(Items.DISPENSER))

        // Advanced
        advancedMachine(RagiumBlocks.ALLOY_SMELTER, Ingredient.of(Items.FURNACE), Items.NETHER_BRICKS)
        advancedMachine(RagiumBlocks.FORMING_PRESS, Ingredient.of(Items.PISTON), Items.NETHER_BRICKS)
        advancedMachine(RagiumBlocks.MELTER, Ingredient.of(Items.BLAST_FURNACE), Items.POLISHED_BLACKSTONE_BRICKS)
        advancedMachine(RagiumBlocks.REFINERY, Ingredient.of(RagiumCommonTags.Items.GLASS_BLOCKS_QUARTZ), Items.POLISHED_BLACKSTONE_BRICKS)
        advancedMachine(RagiumBlocks.SOLIDIFIER, Ingredient.of(ItemTags.TRIM_TEMPLATES), Items.POLISHED_BLACKSTONE_BRICKS)
    }

    private fun basicMachine(machine: ItemLike, input: Ingredient) {
        HTShapedRecipeBuilder(machine)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', RagiumCommonTags.Items.INGOTS_RAGI_ALLOY)
            .define('B', input)
            .define('C', RagiumBlocks.BASIC_MACHINE_FRAME)
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
            .define('C', RagiumBlocks.ADVANCED_MACHINE_FRAME)
            .define('D', bottom)
            .define('E', RagiumCommonTags.Items.CIRCUITS_ADVANCED)
            .save(output)
    }

    private fun devices() {
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
                .addIngredient(RagiumCommonTags.Items.CIRCUITS_BASIC)
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
                .addIngredient(RagiumCommonTags.Items.CIRCUITS_ADVANCED)
                .addIngredient(RagiumBlocks.DEVICE_CASING)
                .addIngredient(part)
                .save(output)
        }

        fun advDevice(device: ItemLike, part: ItemLike) {
            HTSmithingRecipeBuilder(device)
                .addIngredient(RagiumCommonTags.Items.CIRCUITS_ADVANCED)
                .addIngredient(RagiumBlocks.DEVICE_CASING)
                .addIngredient(part)
                .save(output)
        }

        advDevice(RagiumBlocks.ENI, Tags.Items.GEMS_DIAMOND)
        advDevice(RagiumBlocks.EXP_COLLECTOR, Items.HOPPER)
        advDevice(RagiumBlocks.LAVA_COLLECTOR, Tags.Items.BUCKETS_LAVA)
        advDevice(RagiumBlocks.TELEPORT_ANCHOR, RagiumCommonTags.Items.STORAGE_BLOCKS_WARPED_CRYSTAL)

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
