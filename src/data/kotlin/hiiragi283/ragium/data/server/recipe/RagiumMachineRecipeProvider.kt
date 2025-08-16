package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.impl.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTSmithingRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.variant.HTDrumVariant
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem

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
            .define('C', RagiumModTags.Items.TOOLS_HAMMER)
            .save(output)
        // Stone
        HTShapedRecipeBuilder(RagiumBlocks.Casings.STONE, 4)
            .casing()
            .define('A', Tags.Items.COBBLESTONES_NORMAL)
            .define('B', RagiumModTags.Items.TOOLS_HAMMER)
            .define('C', Items.SMOOTH_STONE)
            .save(output)
        // Machine
        HTShapedRecipeBuilder(RagiumBlocks.Frames.BASIC, 2)
            .hollow8()
            .define('A', Tags.Items.INGOTS_IRON)
            .define('B', RagiumModTags.Items.TOOLS_HAMMER)
            .save(output)
        // Advanced Machine
        HTShapedRecipeBuilder(RagiumBlocks.Frames.ADVANCED, 2)
            .hollow8()
            .define('A', RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .define('B', RagiumModTags.Items.TOOLS_HAMMER)
            .save(output)
        // Elite Machine
        HTShapedRecipeBuilder(RagiumBlocks.Frames.ELITE, 4)
            .hollow8()
            .define('A', Tags.Items.INGOTS_NETHERITE)
            .define('B', RagiumModTags.Items.TOOLS_HAMMER)
            .save(output)
        // Device
        HTShapedRecipeBuilder(RagiumBlocks.Casings.DEVICE)
            .cross8()
            .define('A', Items.BLACK_CONCRETE)
            .define('B', RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .define('C', RagiumModTags.Items.TOOLS_HAMMER)
            .save(output)

        HTShapedRecipeBuilder(RagiumBlocks.Casings.DEVICE, 4)
            .cross8()
            .define('A', Tags.Items.OBSIDIANS_NORMAL)
            .define('B', RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .define('C', RagiumModTags.Items.TOOLS_HAMMER)
            .saveSuffixed(output, "_with_obsidian")
    }

    //    Machines    //

    private fun generators() {
    }

    private fun machines() {
        // Basic
        basicMachine(RagiumBlocks.Machines.BLOCK_BREAKER, Ingredient.of(Tags.Items.GEMS_DIAMOND))
        basicMachine(RagiumBlocks.Machines.COMPRESSOR, Ingredient.of(Items.PISTON))
        basicMachine(RagiumBlocks.Machines.ENGRAVER, Ingredient.of(Items.STONECUTTER))
        basicMachine(RagiumBlocks.Machines.EXTRACTOR, Ingredient.of(Items.HOPPER))
        basicMachine(RagiumBlocks.Machines.PULVERIZER, Ingredient.of(Items.FLINT))
        // Advanced
        advMachine(
            RagiumBlocks.Machines.ALLOY_SMELTER,
            Ingredient.of(Items.FURNACE),
            Ingredient.of(RagiumBlocks.Casings.STONE),
        )
        advMachine(
            RagiumBlocks.Machines.CRUSHER,
            Ingredient.of(RagiumModTags.Items.TOOLS_HAMMER),
            Ingredient.of(RagiumBlocks.Machines.PULVERIZER),
        )
        advMachine(
            RagiumBlocks.Machines.INFUSER,
            Ingredient.of(Items.HOPPER),
            Ingredient.of(Items.CAULDRON),
        )
        advMachine(
            RagiumBlocks.Machines.MELTER,
            Ingredient.of(Items.BLAST_FURNACE),
            Ingredient.of(Items.CAULDRON),
        )
        advMachine(
            RagiumBlocks.Machines.MIXER,
            Ingredient.of(RagiumCommonTags.Items.GLASS_BLOCKS_QUARTZ),
            Ingredient.of(Items.CAULDRON),
        )
        advMachine(
            RagiumBlocks.Machines.REFINERY,
            Ingredient.of(RagiumCommonTags.Items.GLASS_BLOCKS_QUARTZ),
            Ingredient.of(Items.HOPPER),
        )
        advMachine(
            RagiumBlocks.Machines.SOLIDIFIER,
            Ingredient.of(Items.IRON_BARS),
            Ingredient.of(Items.CAULDRON),
        )
    }

    private fun basicMachine(machine: ItemLike, side: Ingredient) {
        HTShapedRecipeBuilder(machine)
            .crossLayered()
            .define('A', RagiumCommonTags.Items.INGOTS_RAGI_ALLOY)
            .define('B', RagiumCommonTags.Items.CIRCUITS_BASIC)
            .define('C', side)
            .define('D', RagiumBlocks.Casings.STONE)
            .save(output)
    }

    private fun advMachine(machine: ItemLike, side: Ingredient, core: Ingredient) {
        HTShapedRecipeBuilder(machine)
            .crossLayered()
            .define('A', RagiumCommonTags.Items.INGOTS_ADVANCED_RAGI_ALLOY)
            .define('B', RagiumCommonTags.Items.CIRCUITS_ADVANCED)
            .define('C', side)
            .define('D', core)
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
    }

    private fun basicDevice(device: ItemLike, input: Ingredient) {
        HTSmithingRecipeBuilder(device)
            .addIngredient(RagiumCommonTags.Items.CIRCUITS_BASIC)
            .addIngredient(RagiumBlocks.Casings.DEVICE)
            .addIngredient(input)
            .save(output)
    }

    private fun advancedDevice(device: ItemLike, input: Ingredient) {
        HTSmithingRecipeBuilder(device)
            .addIngredient(RagiumCommonTags.Items.CIRCUITS_ADVANCED)
            .addIngredient(RagiumBlocks.Casings.DEVICE)
            .addIngredient(input)
            .save(output)
    }

    private fun drums() {
        for ((variant: HTDrumVariant, drum: DeferredBlock<Block>) in RagiumBlocks.DRUMS) {
            val material: TagKey<Item> = when (variant) {
                HTDrumVariant.SMALL -> Tags.Items.INGOTS_COPPER
                HTDrumVariant.MEDIUM -> Tags.Items.INGOTS_GOLD
                HTDrumVariant.LARGE -> Tags.Items.GEMS_DIAMOND
                HTDrumVariant.HUGE -> continue
            }

            HTShapedRecipeBuilder(drum)
                .pattern(
                    "ABA",
                    "ACA",
                    "ABA",
                ).define('A', material)
                .define('B', Items.SMOOTH_STONE_SLAB)
                .define('C', Tags.Items.BUCKETS_EMPTY)
                .save(output)
        }
        // Huge
        createNetheriteUpgrade(RagiumBlocks.getDrum(HTDrumVariant.HUGE), RagiumBlocks.getDrum(HTDrumVariant.LARGE))
            .save(output)
        // Upgrades
        for (variant: HTDrumVariant in RagiumBlocks.DRUMS.keys) {
            val upgrade: DeferredItem<*> = when (variant) {
                HTDrumVariant.SMALL -> continue
                HTDrumVariant.MEDIUM -> RagiumItems.MEDIUM_DRUM_UPGRADE
                HTDrumVariant.LARGE -> RagiumItems.LARGE_DRUM_UPGRADE
                HTDrumVariant.HUGE -> RagiumItems.HUGE_DRUM_UPGRADE
            }
            val material: TagKey<Item> = when (variant) {
                HTDrumVariant.SMALL -> continue
                HTDrumVariant.MEDIUM -> Tags.Items.INGOTS_GOLD
                HTDrumVariant.LARGE -> Tags.Items.GEMS_DIAMOND
                HTDrumVariant.HUGE -> Tags.Items.INGOTS_NETHERITE
            }

            HTShapedRecipeBuilder(upgrade)
                .pattern(
                    "ABA",
                    "C C",
                    "ABA",
                ).define('A', material)
                .define('B', Items.SMOOTH_STONE_SLAB)
                .define('C', Tags.Items.GLASS_BLOCKS)
                .save(output)
        }
    }
}
