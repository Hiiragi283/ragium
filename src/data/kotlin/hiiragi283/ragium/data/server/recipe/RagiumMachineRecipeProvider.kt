package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.tier.HTCircuitTier
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumMachineRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        generators()
        processors()
        devices()

        crate()
        drums()
    }

    //    Generators    //

    @JvmStatic
    private fun generators() {
        // Basic
        generator(RagiumBlocks.THERMAL_GENERATOR) {
            define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            define('B', Tags.Items.GLASS_BLOCKS)
            define('C', RagiumItems.getCoil(RagiumMaterialKeys.RAGI_ALLOY))
            define('D', Items.FURNACE)
        }
        // Advanced
        generator(RagiumBlocks.COMBUSTION_GENERATOR) {
            define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)
            define('B', CommonMaterialPrefixes.GLASS_BLOCK, VanillaMaterialKeys.QUARTZ)
            define('C', RagiumItems.getCoil(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY))
            define('D', Items.PISTON)
        }
        // Elite
        for (tier: HTCircuitTier in HTCircuitTier.entries) {
            HTShapedRecipeBuilder
                .create(RagiumBlocks.SOLAR_PANEL_UNIT, (tier.ordinal + 1) * 4)
                .pattern(
                    "AAA",
                    "BCB",
                ).define('A', RagiumItems.SOLAR_PANEL)
                .define('B', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
                .define('C', CommonMaterialPrefixes.CIRCUIT, tier)
                .savePrefixed(output, "${tier.asMaterialName()}_")
        }

        HTShapedRecipeBuilder
            .create(RagiumBlocks.SOLAR_PANEL_CONTROLLER)
            .pattern(
                "AAA",
                " B ",
                "CCC",
            ).define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL)
            .define('B', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.ELITE)
            .define('C', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
            .save(output)
        // Ultimate
        generator(RagiumBlocks.ENCHANTMENT_GENERATOR) {
            define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
            define('B', CommonMaterialPrefixes.GLASS_BLOCK, VanillaMaterialKeys.OBSIDIAN)
            define('C', Items.GRINDSTONE)
            define('D', Items.PISTON)
        }
    }

    @JvmStatic
    private inline fun generator(generator: ItemLike, action: HTShapedRecipeBuilder.() -> Unit) {
        HTShapedRecipeBuilder
            .create(generator)
            .pattern(
                "AAA",
                " B ",
                "CDC",
            ).apply(action)
            .save(output)
    }

    //    Processors    //

    @JvmStatic
    private fun processors() {
        // Basic
        basicMachine(RagiumBlocks.ALLOY_SMELTER) {
            define('B', Items.FURNACE)
            define('C', RagiumBlocks.getCoilBlock(RagiumMaterialKeys.RAGI_ALLOY))
        }
        basicMachine(RagiumBlocks.BLOCK_BREAKER) {
            define('B', CommonMaterialPrefixes.GEM, VanillaMaterialKeys.DIAMOND)
            define('C', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.BASIC)
        }
        basicMachine(RagiumBlocks.COMPRESSOR) {
            define('B', Items.PISTON)
            define('C', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.BASIC)
        }
        basicMachine(RagiumBlocks.CUTTING_MACHINE) {
            define('B', Items.STONECUTTER)
            define('C', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.BASIC)
        }
        basicMachine(RagiumBlocks.EXTRACTOR) {
            define('B', Items.HOPPER)
            define('C', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.BASIC)
        }
        basicMachine(RagiumBlocks.PULVERIZER) {
            define('B', Items.FLINT)
            define('C', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.BASIC)
        }
        // Advanced
        advMachine(RagiumBlocks.CRUSHER) {
            define('B', CommonMaterialPrefixes.GEM, VanillaMaterialKeys.DIAMOND)
            define('C', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.ADVANCED)
        }
        advMachine(RagiumBlocks.MELTER) {
            define('B', Items.BLAST_FURNACE)
            define('C', RagiumBlocks.getCoilBlock(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY))
        }
        advMachine(RagiumBlocks.WASHER) {
            define('B', Items.IRON_TRAPDOOR)
            define('C', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.ADVANCED)
        }

        HTShapedRecipeBuilder
            .create(RagiumBlocks.REFINERY)
            .pattern(
                " A ",
                "ABA",
                "CDC",
            ).define('A', CommonMaterialPrefixes.GLASS_BLOCK, VanillaMaterialKeys.QUARTZ)
            .define('B', RagiumItems.getComponent(HTComponentTier.ADVANCED))
            .define('C', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL)
            .define('D', RagiumBlocks.getCoilBlock(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY))
            .save(output)

        createComponentUpgrade(HTComponentTier.ADVANCED, RagiumBlocks.CRUSHER, RagiumBlocks.PULVERIZER).save(output)
        // Elite
        eliteMachine(RagiumBlocks.BREWERY) {
            define('B', Items.BREWING_STAND)
            define('C', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.ELITE)
        }
        eliteMachine(RagiumBlocks.MIXER) {
            define('B', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.ELITE)
            define('C', RagiumBlocks.REFINERY)
        }
        eliteMachine(RagiumBlocks.MULTI_SMELTER) {
            define('B', Items.FURNACE)
            define('C', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
        }
        eliteMachine(RagiumBlocks.PLANTER) {
            define('B', Items.FLOWER_POT)
            define('C', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.ELITE)
        }
        // Ultimate
        machineBase(RagiumBlocks.ENCHANT_COPIER, RagiumMaterialKeys.NIGHT_METAL) {
            define('B', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.ULTIMATE)
            define('C', ItemTags.ANVIL)
            define('D', RagiumCommonTags.Items.OBSIDIANS_MYSTERIOUS)
        }
        machineBase(RagiumBlocks.ENCHANTER, RagiumMaterialKeys.NIGHT_METAL) {
            define('B', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.ULTIMATE)
            define('C', Items.ENCHANTING_TABLE)
            define('D', RagiumCommonTags.Items.OBSIDIANS_MYSTERIOUS)
        }
        machineBase(RagiumBlocks.SIMULATOR, RagiumMaterialKeys.NIGHT_METAL) {
            define('B', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.ULTIMATE)
            define('C', CommonMaterialPrefixes.GLASS_BLOCK, VanillaMaterialKeys.OBSIDIAN)
            define('D', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
        }
    }

    @JvmStatic
    private inline fun machineBase(machine: ItemLike, material: HTMaterialLike, action: HTShapedRecipeBuilder.() -> Unit) {
        HTShapedRecipeBuilder
            .create(machine)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', CommonMaterialPrefixes.INGOT, material)
            .apply(action)
            .save(output)
    }

    @JvmStatic
    private inline fun basicMachine(machine: ItemLike, action: HTShapedRecipeBuilder.() -> Unit) {
        machineBase(machine, RagiumMaterialKeys.RAGI_ALLOY) {
            define('D', Items.BRICKS)
            action()
        }
    }

    @JvmStatic
    private inline fun advMachine(machine: ItemLike, action: HTShapedRecipeBuilder.() -> Unit) {
        machineBase(machine, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) {
            define('D', Items.NETHER_BRICKS)
            action()
        }
    }

    @JvmStatic
    private inline fun eliteMachine(machine: ItemLike, action: HTShapedRecipeBuilder.() -> Unit) {
        machineBase(machine, RagiumMaterialKeys.AZURE_STEEL) {
            define('D', Items.DEEPSLATE_TILES)
            action()
        }
    }

    //    Devices    //

    @JvmStatic
    private fun devices() {
        HTShapedRecipeBuilder
            .cross8Mirrored(output, RagiumBlocks.DEVICE_CASING) {
                define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
                define('B', CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.IRON)
                define('C', CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
                setCategory(CraftingBookCategory.BUILDING)
            }

        // Basic
        createComponentUpgrade(HTComponentTier.BASIC, RagiumBlocks.ITEM_BUFFER, RagiumBlocks.DEVICE_CASING)
            .addIngredient(Tags.Items.CHESTS)
            .save(output)

        createComponentUpgrade(HTComponentTier.BASIC, RagiumBlocks.WATER_COLLECTOR, RagiumBlocks.DEVICE_CASING)
            .addIngredient(Tags.Items.BUCKETS_WATER)
            .save(output)
        // Advanced
        createComponentUpgrade(HTComponentTier.ADVANCED, RagiumBlocks.EXP_COLLECTOR, RagiumBlocks.DEVICE_CASING)
            .addIngredient(Items.HOPPER)
            .save(output)

        createComponentUpgrade(HTComponentTier.ADVANCED, RagiumBlocks.FISHER, RagiumBlocks.DEVICE_CASING)
            .addIngredient(Tags.Items.TOOLS_FISHING_ROD)
            .save(output)
        // Elite
        createComponentUpgrade(HTComponentTier.ELITE, RagiumBlocks.DIM_ANCHOR, RagiumBlocks.DEVICE_CASING)
            .addIngredient(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.WARPED_CRYSTAL)
            .save(output)

        createComponentUpgrade(HTComponentTier.ELITE, RagiumBlocks.ENI, RagiumBlocks.DEVICE_CASING)
            .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.DIAMOND)
            .save(output)
        // Ultimate
        createComponentUpgrade(HTComponentTier.ULTIMATE, RagiumBlocks.MOB_CAPTURER, RagiumBlocks.DEVICE_CASING)
            .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.ELDRITCH_PEARL)
            .save(output)

        createComponentUpgrade(HTComponentTier.ULTIMATE, RagiumBlocks.TELEPAD, RagiumBlocks.DEVICE_CASING)
            .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.WARPED_CRYSTAL)
            .save(output)
    }

    //    Storage    //

    @JvmStatic
    private fun crate() {
        for ((tier: HTCrateTier, crate: HTItemHolderLike) in RagiumBlocks.CRATES) {
            resetComponent(crate)

            val pair: Pair<CommonMaterialPrefixes, HTMaterialKey> = when (tier) {
                HTCrateTier.SMALL -> CommonMaterialPrefixes.INGOT to VanillaMaterialKeys.IRON
                HTCrateTier.MEDIUM -> CommonMaterialPrefixes.INGOT to VanillaMaterialKeys.GOLD
                HTCrateTier.LARGE -> CommonMaterialPrefixes.GEM to VanillaMaterialKeys.DIAMOND
                HTCrateTier.HUGE -> continue
            }

            HTShapedRecipeBuilder
                .create(crate)
                .pattern(
                    "ABA",
                    "ACA",
                    "ABA",
                ).define('A', pair.first, pair.second)
                .define('B', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
                .define('C', Tags.Items.CHESTS_WOODEN)
                .save(output)
        }
        // Huge
        createNetheriteUpgrade(HTCrateTier.HUGE.getBlock(), HTCrateTier.LARGE.getBlock()).save(output)
        // Open
        HTShapedRecipeBuilder
            .create(RagiumBlocks.OPEN_CRATE)
            .pattern(
                "AAA",
                "ABA",
                "A A",
            ).define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
            .define('B', Items.HOPPER)
            .save(output)
    }

    @JvmStatic
    private fun drums() {
        for ((tier: HTDrumTier, drum: HTItemHolderLike) in RagiumBlocks.DRUMS) {
            resetComponent(drum, RagiumDataComponents.FLUID_CONTENT)

            val pair: Pair<CommonMaterialPrefixes, HTMaterialKey> = when (tier) {
                HTDrumTier.SMALL -> CommonMaterialPrefixes.INGOT to VanillaMaterialKeys.COPPER
                HTDrumTier.MEDIUM -> CommonMaterialPrefixes.INGOT to VanillaMaterialKeys.GOLD
                HTDrumTier.LARGE -> CommonMaterialPrefixes.GEM to VanillaMaterialKeys.DIAMOND
                else -> continue
            }

            HTShapedRecipeBuilder
                .create(drum)
                .pattern(
                    "ABA",
                    "ACA",
                    "ABA",
                ).define('A', pair.first, pair.second)
                .define('B', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
                .define('C', Tags.Items.BUCKETS_EMPTY)
                .save(output)
        }
        // Huge
        createNetheriteUpgrade(HTDrumTier.HUGE.getBlock(), HTDrumTier.LARGE.getBlock()).save(output)
        // Exp
        HTShapedRecipeBuilder
            .create(RagiumBlocks.EXP_DRUM)
            .pattern(
                "ABA",
                "ACA",
                "ABA",
            ).define('A', CommonMaterialPrefixes.GEM, VanillaMaterialKeys.EMERALD)
            .define('B', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
            .define('C', Tags.Items.BUCKETS_EMPTY)
            .save(output)

        // Minecarts
        for (tier: HTDrumTier in HTDrumTier.entries) {
            HTShapelessRecipeBuilder
                .misc(tier.getMinecartItem())
                .addIngredient(tier.getBlock())
                .addIngredient(Items.MINECART)
                .save(output)
        }
    }
}
