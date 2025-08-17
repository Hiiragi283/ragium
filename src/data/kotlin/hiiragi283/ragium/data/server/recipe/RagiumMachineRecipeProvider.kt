package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.impl.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTSmithingRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.HTVanillaMaterialType
import hiiragi283.ragium.util.material.RagiumMaterialType
import hiiragi283.ragium.util.material.RagiumTierType
import hiiragi283.ragium.util.variant.HTDrumVariant
import hiiragi283.ragium.util.variant.HTMachineVariant
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
            .define('C', RagiumModTags.Items.TOOLS_HAMMER)
            .save(output)
        // Stone
        HTShapedRecipeBuilder(RagiumBlocks.Casings.STONE)
            .casing()
            .define('A', Tags.Items.COBBLESTONES_NORMAL)
            .define('B', RagiumModTags.Items.TOOLS_HAMMER)
            .define('C', Items.SMOOTH_STONE)
            .save(output)

        HTShapedRecipeBuilder(RagiumBlocks.Casings.REINFORCED_STONE)
            .casing()
            .define('A', Items.BASALT)
            .define('B', RagiumModTags.Items.TOOLS_HAMMER)
            .define('C', Items.SMOOTH_STONE)
            .save(output)
        // Machine
        HTShapedRecipeBuilder(RagiumBlocks.Frames.BASIC, 2)
            .hollow8()
            .define('A', HTMaterialVariant.INGOT, HTVanillaMaterialType.IRON)
            .define('B', RagiumModTags.Items.TOOLS_HAMMER)
            .save(output)
        // Advanced Machine
        HTShapedRecipeBuilder(RagiumBlocks.Frames.ADVANCED, 2)
            .hollow8()
            .define('A', HTMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('B', RagiumModTags.Items.TOOLS_HAMMER)
            .save(output)
        // Elite Machine
        HTShapedRecipeBuilder(RagiumBlocks.Frames.ELITE, 4)
            .hollow8()
            .define('A', HTMaterialVariant.INGOT, HTVanillaMaterialType.NETHERITE)
            .define('B', RagiumModTags.Items.TOOLS_HAMMER)
            .save(output)
        // Device
        HTShapedRecipeBuilder(RagiumBlocks.Casings.DEVICE)
            .cross8()
            .define('A', Items.BLACK_CONCRETE)
            .define('B', HTMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('C', RagiumModTags.Items.TOOLS_HAMMER)
            .save(output)

        HTShapedRecipeBuilder(RagiumBlocks.Casings.DEVICE, 4)
            .cross8()
            .define('A', Tags.Items.OBSIDIANS_NORMAL)
            .define('B', HTMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('C', RagiumModTags.Items.TOOLS_HAMMER)
            .saveSuffixed(output, "_with_obsidian")
    }

    //    Machines    //

    private fun generators() {
    }

    private fun machines() {
        // Basic
        basicMachine(HTMachineVariant.BLOCK_BREAKER, Ingredient.of(Tags.Items.GEMS_DIAMOND))
        basicMachine(HTMachineVariant.COMPRESSOR, Ingredient.of(Items.PISTON))
        basicMachine(HTMachineVariant.ENGRAVER, Ingredient.of(Items.STONECUTTER))
        basicMachine(HTMachineVariant.EXTRACTOR, Ingredient.of(Items.HOPPER))

        HTShapedRecipeBuilder(HTMachineVariant.PULVERIZER)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', HTMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)
            .define('B', Items.FLINT)
            .define('C', HTMaterialVariant.CIRCUIT, RagiumTierType.BASIC)
            .define('D', Items.BRICKS)
            .save(output)

        HTShapedRecipeBuilder(HTMachineVariant.SMELTER)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', HTMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)
            .define('B', HTMaterialVariant.COIL, RagiumMaterialType.RAGI_ALLOY)
            .define('C', Items.FURNACE)
            .define('D', Items.BRICKS)
            .save(output)
        // Advanced
        HTShapedRecipeBuilder(HTMachineVariant.ALLOY_SMELTER)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', HTMaterialVariant.INGOT, RagiumMaterialType.ADVANCED_RAGI_ALLOY)
            .define('B', HTMaterialVariant.COIL, RagiumMaterialType.ADVANCED_RAGI_ALLOY)
            .define('C', Items.BLAST_FURNACE)
            .define('D', Items.NETHER_BRICKS)
            .save(output)

        HTShapedRecipeBuilder(HTMachineVariant.CRUSHER)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', HTMaterialVariant.INGOT, RagiumMaterialType.ADVANCED_RAGI_ALLOY)
            .define('B', Tags.Items.GEMS_DIAMOND)
            .define('C', HTMaterialVariant.CIRCUIT, RagiumTierType.ADVANCED)
            .define('D', Items.NETHER_BRICKS)
            .save(output)

        advMachine(HTMachineVariant.INFUSER, Ingredient.of(Items.HOPPER))
        advMachine(HTMachineVariant.MELTER, Ingredient.of(Items.BLAST_FURNACE))
        advMachine(HTMachineVariant.MIXER, Ingredient.of(Items.CAULDRON))
        advMachine(HTMachineVariant.REFINERY, HTMaterialVariant.GLASS_BLOCK.toIngredient(HTVanillaMaterialType.QUARTZ))
        advMachine(HTMachineVariant.SOLIDIFIER, Ingredient.of(Items.IRON_BARS))
    }

    private fun basicMachine(variant: HTMachineVariant, side: Ingredient) {
        HTShapedRecipeBuilder(variant)
            .crossLayered()
            .define('A', HTMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)
            .define('B', HTMaterialVariant.CIRCUIT, RagiumTierType.BASIC)
            .define('C', side)
            .define('D', RagiumBlocks.Casings.STONE)
            .save(output)
    }

    private fun advMachine(variant: HTMachineVariant, side: Ingredient) {
        HTShapedRecipeBuilder(variant)
            .crossLayered()
            .define('A', HTMaterialVariant.INGOT, RagiumMaterialType.ADVANCED_RAGI_ALLOY)
            .define('B', HTMaterialVariant.CIRCUIT, RagiumTierType.ADVANCED)
            .define('C', side)
            .define('D', RagiumBlocks.Casings.REINFORCED_STONE)
            .save(output)
    }

    //    Devices    //

    private fun devices() {
        // Milk Drain
        HTShapedRecipeBuilder(RagiumBlocks.Devices.MILK_DRAIN)
            .pattern("A", "B", "C")
            .define('A', HTMaterialVariant.INGOT, HTVanillaMaterialType.COPPER)
            .define('B', Tags.Items.BARRELS_WOODEN)
            .define('C', RagiumBlocks.Casings.STONE)
            .save(output)

        // Basic
        basicDevice(RagiumBlocks.Devices.ITEM_BUFFER, Ingredient.of(Tags.Items.CHESTS))
        basicDevice(RagiumBlocks.Devices.SPRINKLER, Ingredient.of(Tags.Items.STORAGE_BLOCKS_BONE_MEAL))
        basicDevice(RagiumBlocks.Devices.WATER_COLLECTOR, Ingredient.of(Tags.Items.BUCKETS_WATER))
        // Advanced
        advancedDevice(RagiumBlocks.Devices.ENI, HTMaterialVariant.GEM.toIngredient(HTVanillaMaterialType.DIAMOND))
        advancedDevice(RagiumBlocks.Devices.EXP_COLLECTOR, Ingredient.of(Items.HOPPER))
        advancedDevice(RagiumBlocks.Devices.LAVA_COLLECTOR, Ingredient.of(Tags.Items.BUCKETS_LAVA))
        advancedDevice(RagiumBlocks.Devices.DIM_ANCHOR, HTMaterialVariant.STORAGE_BLOCK.toIngredient(RagiumMaterialType.WARPED_CRYSTAL))
    }

    private fun basicDevice(device: ItemLike, input: Ingredient) {
        HTSmithingRecipeBuilder(device)
            .addIngredient(HTMaterialVariant.CIRCUIT, RagiumTierType.BASIC)
            .addIngredient(RagiumBlocks.Casings.DEVICE)
            .addIngredient(input)
            .save(output)
    }

    private fun advancedDevice(device: ItemLike, input: Ingredient) {
        HTSmithingRecipeBuilder(device)
            .addIngredient(HTMaterialVariant.CIRCUIT, RagiumTierType.ADVANCED)
            .addIngredient(RagiumBlocks.Casings.DEVICE)
            .addIngredient(input)
            .save(output)
    }

    private fun drums() {
        for ((variant: HTDrumVariant, drum: ItemLike) in RagiumBlocks.DRUMS) {
            val pair: Pair<HTMaterialVariant, HTVanillaMaterialType> = when (variant) {
                HTDrumVariant.SMALL -> HTMaterialVariant.INGOT to HTVanillaMaterialType.COPPER
                HTDrumVariant.MEDIUM -> HTMaterialVariant.INGOT to HTVanillaMaterialType.GOLD
                HTDrumVariant.LARGE -> HTMaterialVariant.GEM to HTVanillaMaterialType.DIAMOND
                HTDrumVariant.HUGE -> continue
            }

            HTShapedRecipeBuilder(drum)
                .pattern(
                    "ABA",
                    "ACA",
                    "ABA",
                ).define('A', pair.first, pair.second)
                .define('B', Items.SMOOTH_STONE_SLAB)
                .define('C', Tags.Items.BUCKETS_EMPTY)
                .save(output)
        }
        // Huge
        createNetheriteUpgrade(HTDrumVariant.HUGE, HTDrumVariant.LARGE)
            .save(output)
        // Upgrades
        for (variant: HTDrumVariant in RagiumBlocks.DRUMS.keys) {
            val upgrade: ItemLike = when (variant) {
                HTDrumVariant.SMALL -> continue
                HTDrumVariant.MEDIUM -> RagiumItems.MEDIUM_DRUM_UPGRADE
                HTDrumVariant.LARGE -> RagiumItems.LARGE_DRUM_UPGRADE
                HTDrumVariant.HUGE -> RagiumItems.HUGE_DRUM_UPGRADE
            }
            val pair: Pair<HTMaterialVariant, HTVanillaMaterialType> = when (variant) {
                HTDrumVariant.SMALL -> continue
                HTDrumVariant.MEDIUM -> HTMaterialVariant.INGOT to HTVanillaMaterialType.GOLD
                HTDrumVariant.LARGE -> HTMaterialVariant.GEM to HTVanillaMaterialType.DIAMOND
                HTDrumVariant.HUGE -> HTMaterialVariant.INGOT to HTVanillaMaterialType.NETHERITE
            }

            HTShapedRecipeBuilder(upgrade)
                .pattern(
                    "ABA",
                    "C C",
                    "ABA",
                ).define('A', pair.first, pair.second)
                .define('B', Items.SMOOTH_STONE_SLAB)
                .define('C', Tags.Items.GLASS_BLOCKS)
                .save(output)
        }
    }
}
