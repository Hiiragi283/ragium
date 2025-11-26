package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.common.variant.HTUpgradeVariant
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumMachineRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        generators()
        processors()
        devices()
        upgrades()

        crate()
        drums()
    }

    //    Generators    //

    @JvmStatic
    private fun generators() {
        // Basic
        generator(RagiumBlocks.THERMAL_GENERATOR, RagiumMaterialKeys.RAGI_ALLOY) {
            define('B', Tags.Items.GLASS_BLOCKS)
            define('C', RagiumItems.getCoil(RagiumMaterialKeys.RAGI_ALLOY))
            define('D', Items.FURNACE)
        }
        // Advanced
        generator(RagiumBlocks.COMBUSTION_GENERATOR, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) {
            define('B', CommonMaterialPrefixes.GLASS_BLOCK, VanillaMaterialKeys.QUARTZ)
            define('C', RagiumItems.getCoil(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY))
            define('D', Items.PISTON)
        }
        // Elite
        HTShapedRecipeBuilder
            .create(RagiumBlocks.SOLAR_PANEL_UNIT, 4)
            .pattern(
                "AAA",
                "BCB",
            ).define('A', RagiumItems.SOLAR_PANEL)
            .define('B', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
            .define('C', RagiumCommonTags.Items.CIRCUITS_BASIC)
            .savePrefixed(output, "basic_")

        HTShapedRecipeBuilder
            .create(RagiumBlocks.SOLAR_PANEL_UNIT, 8)
            .pattern(
                "AAA",
                "BCB",
            ).define('A', RagiumItems.SOLAR_PANEL)
            .define('B', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
            .define('C', RagiumCommonTags.Items.CIRCUITS_ADVANCED)
            .savePrefixed(output, "advanced_")

        generator(RagiumBlocks.SOLAR_PANEL_CONTROLLER, RagiumMaterialKeys.AZURE_STEEL) {
            define('B', CommonMaterialPrefixes.GLASS_BLOCK, VanillaMaterialKeys.OBSIDIAN)
            define('C', CommonMaterialPrefixes.GEAR, RagiumMaterialKeys.AZURE_STEEL)
            define('D', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
        }
        // Ultimate
        generator(RagiumBlocks.ENCHANTMENT_GENERATOR, RagiumMaterialKeys.NIGHT_METAL) {
            define('B', CommonMaterialPrefixes.GLASS_BLOCK, RagiumMaterialKeys.WARPED_CRYSTAL)
            define('C', CommonMaterialPrefixes.GEAR, RagiumMaterialKeys.NIGHT_METAL)
            define('D', Items.PISTON)
        }
    }

    @JvmStatic
    private inline fun generator(generator: ItemLike, material: HTMaterialLike, action: HTShapedRecipeBuilder.() -> Unit) {
        HTShapedRecipeBuilder
            .create(generator)
            .pattern(
                "AAA",
                " B ",
                "CDC",
            ).define('A', CommonMaterialPrefixes.INGOT, material)
            .apply(action)
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
            define('C', RagiumCommonTags.Items.CIRCUITS_BASIC)
        }
        basicMachine(RagiumBlocks.COMPRESSOR) {
            define('B', Items.PISTON)
            define('C', RagiumCommonTags.Items.CIRCUITS_BASIC)
        }
        basicMachine(RagiumBlocks.CUTTING_MACHINE) {
            define('B', Items.STONECUTTER)
            define('C', RagiumCommonTags.Items.CIRCUITS_BASIC)
        }
        basicMachine(RagiumBlocks.EXTRACTOR) {
            define('B', Items.HOPPER)
            define('C', RagiumCommonTags.Items.CIRCUITS_BASIC)
        }
        basicMachine(RagiumBlocks.PULVERIZER) {
            define('B', Items.FLINT)
            define('C', RagiumCommonTags.Items.CIRCUITS_BASIC)
        }
        // Advanced
        advMachine(RagiumBlocks.CRUSHER) {
            define('B', CommonMaterialPrefixes.GEM, VanillaMaterialKeys.DIAMOND)
            define('C', RagiumCommonTags.Items.CIRCUITS_ADVANCED)
        }
        HTShapedRecipeBuilder
            .create(RagiumBlocks.CRUSHER)
            .pattern(
                "AAA",
                " B ",
                "CCC",
            ).define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)
            .define('B', RagiumBlocks.PULVERIZER)
            .define('C', Items.NETHER_BRICKS)
            .saveSuffixed(output, "_from_pulverizer")

        advMachine(RagiumBlocks.MELTER) {
            define('B', Items.BLAST_FURNACE)
            define('C', RagiumBlocks.getCoilBlock(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY))
        }
        advMachine(RagiumBlocks.WASHER) {
            define('B', Items.IRON_TRAPDOOR)
            define('C', RagiumCommonTags.Items.CIRCUITS_ADVANCED)
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
        // Elite
        eliteMachine(RagiumBlocks.BREWERY) {
            define('B', CommonMaterialPrefixes.GEAR, RagiumMaterialKeys.AZURE_STEEL)
            define('C', Items.BREWING_STAND)
        }
        eliteMachine(RagiumBlocks.MIXER) {
            define('B', CommonMaterialPrefixes.GEAR, RagiumMaterialKeys.AZURE_STEEL)
            define('C', RagiumBlocks.REFINERY)
        }
        eliteMachine(RagiumBlocks.MULTI_SMELTER) {
            define('B', Items.FURNACE)
            define('C', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
        }
        eliteMachine(RagiumBlocks.PLANTER) {
            define('B', CommonMaterialPrefixes.GEAR, RagiumMaterialKeys.AZURE_STEEL)
            define('C', Items.FLOWER_POT)
        }
        // Ultimate
        machineBase(RagiumBlocks.ENCHANT_COPIER, RagiumMaterialKeys.NIGHT_METAL) {
            define('B', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL)
            define('C', ItemTags.ANVIL)
            define('D', RagiumCommonTags.Items.OBSIDIANS_MYSTERIOUS)
        }
        machineBase(RagiumBlocks.ENCHANTER, RagiumMaterialKeys.NIGHT_METAL) {
            define('B', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL)
            define('C', Items.ENCHANTING_TABLE)
            define('D', RagiumCommonTags.Items.OBSIDIANS_MYSTERIOUS)
        }
        machineBase(RagiumBlocks.SIMULATOR, RagiumMaterialKeys.NIGHT_METAL) {
            define('B', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL)
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
        HTShapedRecipeBuilder
            .create(RagiumBlocks.FLUID_COLLECTOR)
            .cross4()
            .define('A', RagiumItems.getCoil(RagiumMaterialKeys.RAGI_ALLOY))
            .define('B', Tags.Items.BUCKETS_EMPTY)
            .define('C', RagiumBlocks.DEVICE_CASING)
            .save(output)

        HTShapedRecipeBuilder
            .create(RagiumBlocks.ITEM_COLLECTOR)
            .cross4()
            .define('A', RagiumItems.getCoil(RagiumMaterialKeys.RAGI_ALLOY))
            .define('B', Tags.Items.CHESTS)
            .define('C', RagiumBlocks.DEVICE_CASING)
            .save(output)
        // Elite
        HTShapedRecipeBuilder
            .create(RagiumBlocks.DIM_ANCHOR)
            .cross4()
            .define('A', CommonMaterialPrefixes.GEAR, RagiumMaterialKeys.AZURE_STEEL)
            .define('B', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.WARPED_CRYSTAL)
            .define('C', RagiumBlocks.DEVICE_CASING)
            .save(output)

        HTShapedRecipeBuilder
            .create(RagiumBlocks.ENI)
            .cross4()
            .define('A', CommonMaterialPrefixes.GEAR, RagiumMaterialKeys.AZURE_STEEL)
            .define('B', CommonMaterialPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.DIAMOND)
            .define('C', RagiumBlocks.DEVICE_CASING)
            .save(output)
        // Ultimate
        HTShapedRecipeBuilder
            .create(RagiumBlocks.TELEPAD)
            .cross4()
            .define('A', CommonMaterialPrefixes.GEAR, RagiumMaterialKeys.DEEP_STEEL)
            .define('B', CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.WARPED_CRYSTAL)
            .define('C', RagiumBlocks.DEVICE_CASING)
            .save(output)
    }

    //    Upgrades    //

    @JvmStatic
    private fun upgrades() {
        // Machine
        RagiumItems.MACHINE_UPGRADES.forEach { (variant: HTUpgradeVariant, tier: HTBaseTier, item: ItemLike) ->
            val metal: HTMaterialKey = when (tier) {
                HTBaseTier.BASIC -> RagiumMaterialKeys.AZURE_STEEL
                HTBaseTier.ADVANCED -> RagiumMaterialKeys.DEEP_STEEL
                else -> return@forEach
            }
            val gem: HTMaterialKey = when (variant) {
                HTUpgradeVariant.EFFICIENCY -> RagiumMaterialKeys.WARPED_CRYSTAL
                HTUpgradeVariant.ENERGY_CAPACITY -> RagiumMaterialKeys.RAGI_CRYSTAL
                HTUpgradeVariant.SPEED -> RagiumMaterialKeys.CRIMSON_CRYSTAL
            }
            HTShapedRecipeBuilder
                .create(item)
                .hollow4()
                .define('A', CommonMaterialPrefixes.INGOT, metal)
                .define('B', CommonMaterialPrefixes.GEM, gem)
                .save(output)
        }

        // Fortune
        HTShapedRecipeBuilder
            .create(RagiumItems.FORTUNE_UPGRADE)
            .hollow4()
            .define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL)
            .define('B', CommonMaterialPrefixes.GEM, VanillaMaterialKeys.EMERALD)
            .save(output)

        // Device
        mapOf(
            RagiumItems.EXP_COLLECTOR_UPGRADE to Ingredient.of(Items.EXPERIENCE_BOTTLE),
            RagiumItems.FISHING_UPGRADE to Ingredient.of(Tags.Items.TOOLS_FISHING_ROD),
            RagiumItems.MOB_CAPTURE_UPGRADE to Ingredient.of(RagiumItems.ELDRITCH_EGG),
        ).forEach { (upgrade: ItemLike, item: Ingredient) ->
            HTShapedRecipeBuilder
                .create(upgrade)
                .hollow4()
                .define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
                .define('B', item)
                .save(output)
        }
    }

    //    Storage    //

    @JvmStatic
    private fun crate() {
        for ((tier: HTCrateTier, crate: HTItemHolderLike) in RagiumBlocks.CRATES) {
            resetComponent(crate)

            val key: HTMaterialKey = when (tier) {
                HTCrateTier.SMALL -> VanillaMaterialKeys.IRON
                HTCrateTier.MEDIUM -> VanillaMaterialKeys.GOLD
                HTCrateTier.LARGE -> VanillaMaterialKeys.DIAMOND
                HTCrateTier.HUGE -> continue
            }
            val prefix: HTMaterialPrefix = getDefaultPrefix(key) ?: continue
            HTShapedRecipeBuilder
                .create(crate)
                .pattern(
                    "ABA",
                    "ACA",
                    "ABA",
                ).define('A', prefix, key)
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

            val key: HTMaterialKey = when (tier) {
                HTDrumTier.SMALL -> VanillaMaterialKeys.COPPER
                HTDrumTier.MEDIUM -> VanillaMaterialKeys.GOLD
                HTDrumTier.LARGE -> VanillaMaterialKeys.DIAMOND
                else -> continue
            }
            val prefix: HTMaterialPrefix = getDefaultPrefix(key) ?: continue
            HTShapedRecipeBuilder
                .create(drum)
                .pattern(
                    "ABA",
                    "ACA",
                    "ABA",
                ).define('A', prefix, key)
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
