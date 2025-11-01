package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.tier.HTCircuitTier
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.common.variant.HTGlassVariant
import hiiragi283.ragium.common.variant.HTItemMaterialVariant
import hiiragi283.ragium.common.variant.HTStorageMaterialVariant
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
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
        HTShapedRecipeBuilder
            .misc(RagiumBlocks.THERMAL_GENERATOR)
            .pattern(
                "AAA",
                " B ",
                "CDC",
            ).define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)
            .define('B', Tags.Items.GLASS_BLOCKS)
            .define('C', RagiumItems.getCoil(RagiumMaterialType.RAGI_ALLOY))
            .define('D', Items.FURNACE)
            .save(output)
        // Advanced
        HTShapedRecipeBuilder
            .misc(RagiumBlocks.COMBUSTION_GENERATOR)
            .pattern(
                "AAA",
                " B ",
                "CDC",
            ).define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.ADVANCED_RAGI_ALLOY)
            .define('B', HTGlassVariant.COLORLESS, HTVanillaMaterialType.QUARTZ)
            .define('C', RagiumItems.getCoil(RagiumMaterialType.ADVANCED_RAGI_ALLOY))
            .define('D', Items.BLAST_FURNACE)
            .save(output)
        // Elite
        // Ultimate
        HTShapedRecipeBuilder
            .misc(RagiumBlocks.ENCHANTMENT_GENERATOR)
            .pattern(
                "AAA",
                " B ",
                "CDC",
            ).define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.DEEP_STEEL)
            .define('B', HTGlassVariant.COLORLESS, HTVanillaMaterialType.OBSIDIAN)
            .define('C', HTItemMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL)
            .define('D', Items.GRINDSTONE)
            .save(output)
    }

    //    Processors    //

    @JvmStatic
    private fun processors() {
        // Basic
        basicMachine(
            RagiumBlocks.ALLOY_SMELTER,
            Ingredient.of(Items.FURNACE),
            Ingredient.of(RagiumBlocks.getCoilBlock(RagiumMaterialType.RAGI_ALLOY)),
        )
        basicMachine(RagiumBlocks.BLOCK_BREAKER, Ingredient.of(Tags.Items.GEMS_DIAMOND))
        basicMachine(RagiumBlocks.COMPRESSOR, Ingredient.of(Items.PISTON))
        basicMachine(RagiumBlocks.CUTTING_MACHINE, Ingredient.of(Items.STONECUTTER))
        basicMachine(RagiumBlocks.EXTRACTOR, Ingredient.of(Items.HOPPER))
        basicMachine(RagiumBlocks.PULVERIZER, Ingredient.of(Items.FLINT))
        // Advanced
        advMachine(RagiumBlocks.CRUSHER, Ingredient.of(Tags.Items.GEMS_DIAMOND))
        advMachine(
            RagiumBlocks.MELTER,
            Ingredient.of(RagiumBlocks.getCoilBlock(RagiumMaterialType.ADVANCED_RAGI_ALLOY)),
            Ingredient.of(Items.BLAST_FURNACE),
        )
        advMachine(RagiumBlocks.WASHER, Ingredient.of(Items.IRON_TRAPDOOR))

        HTShapedRecipeBuilder
            .misc(RagiumBlocks.REFINERY)
            .pattern(
                " A ",
                "ABA",
                "CDC",
            ).define('A', HTGlassVariant.COLORLESS, HTVanillaMaterialType.QUARTZ)
            .define('B', RagiumItems.getComponent(HTComponentTier.ADVANCED))
            .define('C', HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('D', RagiumBlocks.getCoilBlock(RagiumMaterialType.ADVANCED_RAGI_ALLOY))
            .save(output)

        mapOf(
            RagiumBlocks.CRUSHER to RagiumBlocks.PULVERIZER,
            RagiumBlocks.MELTER to RagiumBlocks.EXTRACTOR,
        ).forEach { (adv: ItemLike, basic: ItemLike) ->
            createComponentUpgrade(HTComponentTier.ADVANCED, adv, basic).save(output)
        }
        // Elite
        eliteMachine(RagiumBlocks.BREWERY, Ingredient.of(Items.BREWING_STAND))
        eliteMachine(RagiumBlocks.PLANTER, Ingredient.of(Items.FLOWER_POT))
        eliteMachine(RagiumBlocks.SIMULATOR, HTGlassVariant.COLORLESS.toIngredient(HTVanillaMaterialType.OBSIDIAN))

        mapOf(
            RagiumBlocks.MULTI_SMELTER to RagiumBlocks.ALLOY_SMELTER,
        ).forEach { (elite, adv) ->
            createComponentUpgrade(HTComponentTier.ELITE, elite, adv).save(output)
        }
    }

    @JvmStatic
    private fun basicMachine(
        machine: ItemLike,
        side: Ingredient,
        core: Ingredient = HTItemMaterialVariant.CIRCUIT.toIngredient(HTCircuitTier.BASIC),
    ) {
        HTShapedRecipeBuilder
            .misc(machine)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)
            .define('B', side)
            .define('C', core)
            .define('D', Items.BRICKS)
            .save(output)
    }

    @JvmStatic
    private fun advMachine(
        machine: ItemLike,
        side: Ingredient,
        core: Ingredient = HTItemMaterialVariant.CIRCUIT.toIngredient(HTCircuitTier.ADVANCED),
    ) {
        HTShapedRecipeBuilder
            .misc(machine)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.ADVANCED_RAGI_ALLOY)
            .define('B', side)
            .define('C', core)
            .define('D', Items.NETHER_BRICKS)
            .save(output)
    }

    @JvmStatic
    private fun eliteMachine(
        machine: ItemLike,
        side: Ingredient,
        core: Ingredient = HTItemMaterialVariant.CIRCUIT.toIngredient(HTCircuitTier.ELITE),
    ) {
        HTShapedRecipeBuilder
            .misc(machine)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('B', side)
            .define('C', core)
            .define('D', Items.DEEPSLATE_TILES)
            .save(output)
    }

    //    Devices    //

    @JvmStatic
    private fun devices() {
        HTShapedRecipeBuilder
            .building(RagiumBlocks.DEVICE_CASING)
            .cross8()
            .define('A', Items.BLACK_CONCRETE)
            .define('B', HTItemMaterialVariant.INGOT, HTVanillaMaterialType.IRON)
            .define('C', HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE)
            .save(output)

        HTShapedRecipeBuilder
            .building(RagiumBlocks.DEVICE_CASING, 4)
            .cross8()
            .define('A', Tags.Items.OBSIDIANS_NORMAL)
            .define('B', HTItemMaterialVariant.INGOT, HTVanillaMaterialType.IRON)
            .define('C', HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE)
            .saveSuffixed(output, "_with_obsidian")

        // Basic
        createComponentUpgrade(HTComponentTier.BASIC, RagiumBlocks.ITEM_BUFFER, RagiumBlocks.DEVICE_CASING)
            .addIngredient(Tags.Items.CHESTS)
            .save(output)

        createComponentUpgrade(HTComponentTier.BASIC, RagiumBlocks.MILK_COLLECTOR, RagiumBlocks.DEVICE_CASING)
            .addIngredient(Tags.Items.BUCKETS_MILK)
            .save(output)

        createComponentUpgrade(HTComponentTier.BASIC, RagiumBlocks.WATER_COLLECTOR, RagiumBlocks.DEVICE_CASING)
            .addIngredient(Tags.Items.BUCKETS_WATER)
            .save(output)
        // Advanced
        createComponentUpgrade(HTComponentTier.ADVANCED, RagiumBlocks.EXP_COLLECTOR, RagiumBlocks.DEVICE_CASING)
            .addIngredient(Items.HOPPER)
            .save(output)

        createComponentUpgrade(HTComponentTier.ADVANCED, RagiumBlocks.LAVA_COLLECTOR, RagiumBlocks.DEVICE_CASING)
            .addIngredient(Tags.Items.BUCKETS_LAVA)
            .save(output)
        // Elite
        createComponentUpgrade(HTComponentTier.ELITE, RagiumBlocks.DIM_ANCHOR, RagiumBlocks.DEVICE_CASING)
            .addIngredient(HTItemMaterialVariant.GEM, RagiumMaterialType.WARPED_CRYSTAL)
            .save(output)

        createComponentUpgrade(HTComponentTier.ELITE, RagiumBlocks.ENI, RagiumBlocks.DEVICE_CASING)
            .addIngredient(HTStorageMaterialVariant, HTVanillaMaterialType.DIAMOND)
            .save(output)
        // Ultimate
        createComponentUpgrade(HTComponentTier.ULTIMATE, RagiumBlocks.MOB_CAPTURER, RagiumBlocks.DEVICE_CASING)
            .addIngredient(HTStorageMaterialVariant, RagiumMaterialType.ELDRITCH_PEARL)
            .save(output)

        createComponentUpgrade(HTComponentTier.ULTIMATE, RagiumBlocks.TELEPAD, RagiumBlocks.DEVICE_CASING)
            .addIngredient(HTStorageMaterialVariant, RagiumMaterialType.WARPED_CRYSTAL)
            .save(output)
    }

    //    Storage    //

    @JvmStatic
    private fun crate() {
        for ((tier: HTCrateTier, crate: HTItemHolderLike) in RagiumBlocks.CRATES) {
            resetComponent(crate)

            val pair: Pair<HTItemMaterialVariant, HTVanillaMaterialType> = when (tier) {
                HTCrateTier.SMALL -> HTItemMaterialVariant.INGOT to HTVanillaMaterialType.IRON
                HTCrateTier.MEDIUM -> HTItemMaterialVariant.INGOT to HTVanillaMaterialType.GOLD
                HTCrateTier.LARGE -> HTItemMaterialVariant.GEM to HTVanillaMaterialType.DIAMOND
                HTCrateTier.HUGE -> continue
            }

            HTShapedRecipeBuilder
                .misc(crate)
                .pattern(
                    "ABA",
                    "ACA",
                    "ABA",
                ).define('A', pair.first, pair.second)
                .define('B', Items.SMOOTH_STONE_SLAB)
                .define('C', Tags.Items.CHESTS_WOODEN)
                .save(output)
        }
        // Huge
        createNetheriteUpgrade(HTCrateTier.HUGE.getBlock(), HTCrateTier.LARGE.getBlock()).save(output)
    }

    @JvmStatic
    private fun drums() {
        for ((tier: HTDrumTier, drum: HTItemHolderLike) in RagiumBlocks.DRUMS) {
            resetComponent(drum, RagiumDataComponents.FLUID_CONTENT)

            val pair: Pair<HTItemMaterialVariant, HTVanillaMaterialType> = when (tier) {
                HTDrumTier.SMALL -> HTItemMaterialVariant.INGOT to HTVanillaMaterialType.COPPER
                HTDrumTier.MEDIUM -> HTItemMaterialVariant.INGOT to HTVanillaMaterialType.GOLD
                HTDrumTier.LARGE -> HTItemMaterialVariant.GEM to HTVanillaMaterialType.DIAMOND
                else -> continue
            }

            HTShapedRecipeBuilder
                .misc(drum)
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
        createNetheriteUpgrade(HTDrumTier.HUGE.getBlock(), HTDrumTier.LARGE.getBlock()).save(output)
        // Upgrades
        for (tier: HTDrumTier in HTDrumTier.entries) {
            val upgrade: ItemLike = when (tier) {
                HTDrumTier.MEDIUM -> RagiumItems.MEDIUM_DRUM_UPGRADE
                HTDrumTier.LARGE -> RagiumItems.LARGE_DRUM_UPGRADE
                HTDrumTier.HUGE -> RagiumItems.HUGE_DRUM_UPGRADE
                else -> continue
            }
            val pair: Pair<HTItemMaterialVariant, HTVanillaMaterialType> = when (tier) {
                HTDrumTier.MEDIUM -> HTItemMaterialVariant.INGOT to HTVanillaMaterialType.GOLD
                HTDrumTier.LARGE -> HTItemMaterialVariant.GEM to HTVanillaMaterialType.DIAMOND
                HTDrumTier.HUGE -> HTItemMaterialVariant.INGOT to HTVanillaMaterialType.NETHERITE
                else -> continue
            }

            HTShapedRecipeBuilder
                .misc(upgrade)
                .pattern(
                    "ABA",
                    "C C",
                    "ABA",
                ).define('A', pair.first, pair.second)
                .define('B', Items.SMOOTH_STONE_SLAB)
                .define('C', Tags.Items.GLASS_BLOCKS)
                .save(output)
        }

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
