package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.HTUpgradeType
import hiiragi283.ragium.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.variant.VanillaToolVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.component.DataComponentType
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumMachineRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        generators()
        processors()
        devices()
        upgrades()
        storages()
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
        generator(RagiumBlocks.CULINARY_GENERATOR, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) {
            define('B', CommonMaterialPrefixes.GLASS_BLOCK, VanillaMaterialKeys.QUARTZ)
            define('C', RagiumItems.getCoil(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY))
            define('D', Items.SMOKER)
        }

        generator(RagiumBlocks.MAGMATIC_GENERATOR, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) {
            define('B', CommonMaterialPrefixes.GLASS_BLOCK, VanillaMaterialKeys.QUARTZ)
            define('C', RagiumItems.getCoil(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY))
            define('D', Items.BLAST_FURNACE)
        }
        // Elite
        generator(RagiumBlocks.COMBUSTION_GENERATOR, RagiumMaterialKeys.AZURE_STEEL) {
            define('B', CommonMaterialPrefixes.GLASS_BLOCK, VanillaMaterialKeys.OBSIDIAN)
            define('C', RagiumItems.getGear(RagiumMaterialKeys.AZURE_STEEL))
            define('D', Items.PISTON)
        }

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
        HTShapedRecipeBuilder
            .create(RagiumBlocks.ELECTRIC_FURNACE)
            .pattern(
                " A ",
                "ABA",
                "ACA",
            ).define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            .define('B', RagiumCommonTags.Items.CIRCUITS_BASIC)
            .define('C', Items.FURNACE)
            .save(output)
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
        advMachine(RagiumBlocks.MIXER) {
            define('B', Tags.Items.BUCKETS_EMPTY)
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
        eliteMachine(RagiumBlocks.ADVANCED_MIXER) {
            define('B', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
            define('C', RagiumBlocks.MIXER)
        }
        eliteMachine(RagiumBlocks.BREWERY) {
            define('B', CommonMaterialPrefixes.GEAR, RagiumMaterialKeys.AZURE_STEEL)
            define('C', Items.BREWING_STAND)
        }
        eliteMachine(RagiumBlocks.MULTI_SMELTER) {
            define('B', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
            define('C', RagiumBlocks.ELECTRIC_FURNACE)
        }
        eliteMachine(RagiumBlocks.PLANTER) {
            define('B', CommonMaterialPrefixes.GEAR, RagiumMaterialKeys.AZURE_STEEL)
            define('C', Items.FLOWER_POT)
        }
        // Ultimate
        ultimateMachine(RagiumBlocks.ENCHANTER, RagiumMaterialKeys.ELDRITCH_PEARL) {
            define('B', Items.ENCHANTING_TABLE)
        }
        ultimateMachine(RagiumBlocks.MOB_CRUSHER, RagiumMaterialKeys.CRIMSON_CRYSTAL) {
            define('B', RagiumItems.getTool(VanillaToolVariant.SWORD, RagiumMaterialKeys.DEEP_STEEL))
        }
        ultimateMachine(RagiumBlocks.SIMULATOR, RagiumMaterialKeys.WARPED_CRYSTAL) {
            define('B', CommonMaterialPrefixes.GLASS_BLOCK, VanillaMaterialKeys.OBSIDIAN)
        }

        /*machineBase(RagiumBlocks.SIMULATOR, RagiumMaterialKeys.NIGHT_METAL) {
            define('B', RagiumItems.IRIDESCENT_POWDER)
            define('C', spawnerIngredient(EntityType.ALLAY))
            define('D', Items.END_STONE_BRICKS)
        }*/
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

    @JvmStatic
    private inline fun ultimateMachine(machine: ItemLike, material: HTMaterialLike, action: HTShapedRecipeBuilder.() -> Unit) {
        machineBase(machine, RagiumMaterialKeys.NIGHT_METAL) {
            define('C', CommonMaterialPrefixes.GEM, material)
            define('D', Items.END_STONE_BRICKS)
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
                define('C', CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
                setCategory(CraftingBookCategory.BUILDING)
            }

        // Basic
        HTShapelessRecipeBuilder
            .create(RagiumBlocks.FLUID_COLLECTOR)
            .addIngredient(RagiumBlocks.DEVICE_CASING)
            .addIngredient(RagiumItems.getCoil(RagiumMaterialKeys.RAGI_ALLOY))
            .addIngredient(RagiumCommonTags.Items.CIRCUITS_BASIC)
            .addIngredient(Tags.Items.BUCKETS_EMPTY)
            .save(output)

        HTShapelessRecipeBuilder
            .create(RagiumBlocks.ITEM_COLLECTOR)
            .addIngredient(RagiumBlocks.DEVICE_CASING)
            .addIngredient(RagiumItems.getCoil(RagiumMaterialKeys.RAGI_ALLOY))
            .addIngredient(RagiumCommonTags.Items.CIRCUITS_BASIC)
            .addIngredient(Tags.Items.CHESTS)
            .save(output)
        // Elite
        HTShapelessRecipeBuilder
            .create(RagiumBlocks.DIM_ANCHOR)
            .addIngredient(RagiumBlocks.DEVICE_CASING)
            .addIngredient(CommonMaterialPrefixes.GEAR, RagiumMaterialKeys.AZURE_STEEL)
            .addIngredient(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.WARPED_CRYSTAL)
            .addIngredient(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
            .save(output)
        // Ultimate
        HTShapelessRecipeBuilder
            .create(RagiumBlocks.TELEPAD)
            .addIngredient(RagiumBlocks.DEVICE_CASING)
            .addIngredient(CommonMaterialPrefixes.GEAR, RagiumMaterialKeys.DEEP_STEEL)
            .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.WARPED_CRYSTAL)
            .addIngredient(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL)
            .save(output)
    }

    //    Upgrades    //

    @JvmStatic
    private fun upgrades() {
        for (type: HTUpgradeType in HTUpgradeType.entries) {
            val builder: HTShapedRecipeBuilder = when (type.group) {
                HTUpgradeType.Group.CREATIVE -> continue
                HTUpgradeType.Group.GENERATOR -> ::generator
                HTUpgradeType.Group.PROCESSOR -> ::processor
                HTUpgradeType.Group.DEVICE -> ::device
                HTUpgradeType.Group.STORAGE -> ::storage
            }(type)
            when (type) {
                HTUpgradeType.CREATIVE -> continue
                // Processor
                HTUpgradeType.EFFICIENCY -> builder.define('C', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.WARPED_CRYSTAL)
                HTUpgradeType.SPEED -> builder.define('C', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.CRIMSON_CRYSTAL)
                HTUpgradeType.HIGH_SPEED -> builder.define('C', HTUpgradeType.SPEED)
                // Processor
                HTUpgradeType.BIO_COMPOSTING -> builder.define('C', Items.COMPOSTER)
                HTUpgradeType.BLASTING -> builder.define('C', Items.BLAST_FURNACE)
                HTUpgradeType.EFFICIENT_CRUSHING -> builder.define('C', RagiumFluidContents.LUBRICANT.bucketTag)
                HTUpgradeType.EXP_EXTRACTING -> builder.define('C', Items.GRINDSTONE)
                HTUpgradeType.EXTRA_VOIDING -> builder.define('C', Tags.Items.BUCKETS_LAVA)
                HTUpgradeType.SMOKING -> builder.define('C', Items.SMOKER)
                // Device
                HTUpgradeType.EXP_COLLECTING -> builder.define('C', Items.EXPERIENCE_BOTTLE)
                HTUpgradeType.FISHING -> builder.define('C', Tags.Items.TOOLS_FISHING_ROD)
                HTUpgradeType.MOB_CAPTURING -> builder.define('C', RagiumItems.ELDRITCH_EGG)
                // Storage
                HTUpgradeType.ENERGY_CAPACITY -> builder.define('C', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
                HTUpgradeType.FLUID_CAPACITY -> builder.define('C', Tags.Items.BUCKETS_EMPTY)
                HTUpgradeType.ITEM_CAPACITY -> builder.define('C', Tags.Items.CHESTS)
            }.save(output)
        }
    }

    @JvmStatic
    private fun generator(upgrade: ItemLike): HTShapedRecipeBuilder = HTShapedRecipeBuilder
        .create(upgrade)
        .cross8()
        .define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.DEEP_STEEL)
        .define('B', RagiumModTags.Items.PLASTICS)

    @JvmStatic
    private fun processor(upgrade: ItemLike): HTShapedRecipeBuilder = HTShapedRecipeBuilder
        .create(upgrade)
        .cross8()
        .define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL)
        .define('B', RagiumModTags.Items.PLASTICS)

    @JvmStatic
    private fun device(upgrade: ItemLike): HTShapedRecipeBuilder = HTShapedRecipeBuilder
        .create(upgrade)
        .cross8()
        .define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
        .define('B', CommonMaterialPrefixes.PLATE, CommonMaterialKeys.RUBBER)

    @JvmStatic
    private fun storage(upgrade: ItemLike): HTShapedRecipeBuilder = HTShapedRecipeBuilder
        .create(upgrade)
        .cross8()
        .define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)
        .define('B', CommonMaterialPrefixes.PLATE, CommonMaterialKeys.RUBBER)

    //    Storage    //

    @JvmStatic
    private fun storages() {
        // Battery, Crate, Tank
        val ragiCrystal: TagKey<Item> = CommonMaterialPrefixes.GEM.itemTagKey(RagiumMaterialKeys.RAGI_CRYSTAL)
        listOf(
            Triple(RagiumBlocks.BATTERY, ragiCrystal, RagiumDataComponents.ENERGY),
            Triple(RagiumBlocks.CRATE, Tags.Items.CHESTS, RagiumDataComponents.ITEM),
            Triple(RagiumBlocks.TANK, Tags.Items.BUCKETS_EMPTY, RagiumDataComponents.FLUID),
        ).forEach { (block: HTItemHolderLike, core: TagKey<Item>, component: DataComponentType<*>) ->
            HTShapedRecipeBuilder
                .create(block)
                .crossLayered()
                .define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
                .define('B', CommonMaterialPrefixes.PLATE, CommonMaterialKeys.RUBBER)
                .define('C', Tags.Items.GLASS_BLOCKS)
                .define('D', core)
                .save(output)

            resetComponent(block, component)
        }
        // Buffer
        HTShapedRecipeBuilder
            .create(RagiumBlocks.BUFFER)
            .pattern(
                "ABA",
                "CDE",
                "ABA",
            ).define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)
            .define('B', RagiumModTags.Items.PLASTICS)
            .define('C', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
            .define('D', Tags.Items.CHESTS)
            .define('E', Tags.Items.BUCKETS_EMPTY)
            .save(output)
        // Universal Chest
        craftingDyed(RagiumBlocks.UNIVERSAL_CHEST)
        HTShapedRecipeBuilder
            .create(RagiumBlocks.UNIVERSAL_CHEST)
            .hollow8()
            .define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
            .define('B', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL)
            .save(output)

        // Tank with Minecart
        HTShapelessRecipeBuilder
            .create(RagiumItems.TANK_MINECART)
            .addIngredient(RagiumBlocks.TANK)
            .addIngredient(Items.MINECART)
            .save(output)
    }
}
