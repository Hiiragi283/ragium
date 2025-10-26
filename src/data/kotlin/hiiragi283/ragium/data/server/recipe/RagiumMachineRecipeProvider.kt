package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.common.material.HTBlockMaterialVariant
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.tier.HTCircuitTier
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.common.variant.HTMachineVariant
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
            .define('B', HTBlockMaterialVariant.GLASS_BLOCK, HTVanillaMaterialType.QUARTZ)
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
            .define('B', HTBlockMaterialVariant.GLASS_BLOCK, HTVanillaMaterialType.OBSIDIAN)
            .define('C', HTItemMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL)
            .define('D', Items.GRINDSTONE)
            .save(output)
    }

    //    Processors    //

    @JvmStatic
    private fun processors() {
        // Basic
        basicMachine(
            HTMachineVariant.ALLOY_SMELTER,
            Ingredient.of(Items.FURNACE),
            Ingredient.of(RagiumBlocks.getCoilBlock(RagiumMaterialType.RAGI_ALLOY)),
        )
        basicMachine(HTMachineVariant.BLOCK_BREAKER, Ingredient.of(Tags.Items.GEMS_DIAMOND))
        basicMachine(HTMachineVariant.COMPRESSOR, Ingredient.of(Items.PISTON))
        basicMachine(HTMachineVariant.CUTTING_MACHINE, Ingredient.of(Items.STONECUTTER))
        basicMachine(HTMachineVariant.EXTRACTOR, Ingredient.of(Items.HOPPER))
        basicMachine(HTMachineVariant.PULVERIZER, Ingredient.of(Items.FLINT))
        // Advanced
        advMachine(HTMachineVariant.CRUSHER, Ingredient.of(Tags.Items.GEMS_DIAMOND))
        advMachine(
            HTMachineVariant.MELTER,
            Ingredient.of(RagiumBlocks.getCoilBlock(RagiumMaterialType.ADVANCED_RAGI_ALLOY)),
            Ingredient.of(Items.BLAST_FURNACE),
        )
        advMachine(HTMachineVariant.WASHER, Ingredient.of(Items.IRON_TRAPDOOR))

        HTShapedRecipeBuilder
            .misc(HTMachineVariant.REFINERY)
            .pattern(
                " A ",
                "ABA",
                "CDC",
            ).define('A', HTBlockMaterialVariant.GLASS_BLOCK, HTVanillaMaterialType.QUARTZ)
            .define('B', RagiumItems.getComponent(HTComponentTier.ADVANCED))
            .define('C', HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('D', RagiumBlocks.getCoilBlock(RagiumMaterialType.ADVANCED_RAGI_ALLOY))
            .save(output)

        mapOf(
            HTMachineVariant.CRUSHER to HTMachineVariant.PULVERIZER,
            HTMachineVariant.MELTER to HTMachineVariant.EXTRACTOR,
        ).forEach { (adv: HTMachineVariant, basic: HTMachineVariant) ->
            createComponentUpgrade(HTComponentTier.ADVANCED, adv, basic).save(output)
        }
        // Elite
        eliteMachine(HTMachineVariant.BREWERY, Ingredient.of(Items.BREWING_STAND))
        eliteMachine(HTMachineVariant.PLANTER, Ingredient.of(Items.FLOWER_POT))
        eliteMachine(HTMachineVariant.SIMULATOR, HTBlockMaterialVariant.GLASS_BLOCK.toIngredient(HTVanillaMaterialType.OBSIDIAN))

        mapOf(
            HTMachineVariant.MULTI_SMELTER to HTMachineVariant.ALLOY_SMELTER,
        ).forEach { (elite: HTMachineVariant, adv: HTMachineVariant) ->
            createComponentUpgrade(HTComponentTier.ELITE, elite, adv).save(output)
        }
    }

    @JvmStatic
    private fun basicMachine(
        variant: HTMachineVariant,
        side: Ingredient,
        core: Ingredient = HTItemMaterialVariant.CIRCUIT.toIngredient(HTCircuitTier.BASIC),
    ) {
        HTShapedRecipeBuilder
            .misc(variant)
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
        variant: HTMachineVariant,
        side: Ingredient,
        core: Ingredient = HTItemMaterialVariant.CIRCUIT.toIngredient(HTCircuitTier.ADVANCED),
    ) {
        HTShapedRecipeBuilder
            .misc(variant)
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
        variant: HTMachineVariant,
        side: Ingredient,
        core: Ingredient = HTItemMaterialVariant.CIRCUIT.toIngredient(HTCircuitTier.ELITE),
    ) {
        HTShapedRecipeBuilder
            .misc(variant)
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

        val storage = HTBlockMaterialVariant.STORAGE_BLOCK
        for (variant: HTDeviceVariant in HTDeviceVariant.entries) {
            val ingredient: Ingredient = when (variant) {
                // Basic
                HTDeviceVariant.ITEM_BUFFER -> Ingredient.of(Tags.Items.CHESTS)
                HTDeviceVariant.MILK_COLLECTOR -> Ingredient.of(Tags.Items.BUCKETS_MILK)
                HTDeviceVariant.WATER_COLLECTOR -> Ingredient.of(Tags.Items.BUCKETS_WATER)
                // Advanced
                HTDeviceVariant.ENI -> HTItemMaterialVariant.GEM.toIngredient(HTVanillaMaterialType.DIAMOND)
                HTDeviceVariant.EXP_COLLECTOR -> Ingredient.of(Items.HOPPER)
                HTDeviceVariant.LAVA_COLLECTOR -> Ingredient.of(Tags.Items.BUCKETS_LAVA)
                // Elite
                HTDeviceVariant.DIM_ANCHOR -> HTItemMaterialVariant.GEM.toIngredient(RagiumMaterialType.WARPED_CRYSTAL)
                HTDeviceVariant.MOB_CAPTURER -> storage.toIngredient(RagiumMaterialType.ELDRITCH_PEARL)
                // Ultimate
                HTDeviceVariant.TELEPAD -> storage.toIngredient(RagiumMaterialType.WARPED_CRYSTAL)
                // Creative
                HTDeviceVariant.CEU -> continue
            }
            val tier: HTComponentTier = HTComponentTier.from(variant.tier) ?: continue
            createComponentUpgrade(tier, variant, RagiumBlocks.DEVICE_CASING)
                .addIngredient(ingredient)
                .save(output)
        }
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
                HTDrumTier.HUGE -> continue
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
                HTDrumTier.SMALL -> continue
                HTDrumTier.MEDIUM -> RagiumItems.MEDIUM_DRUM_UPGRADE
                HTDrumTier.LARGE -> RagiumItems.LARGE_DRUM_UPGRADE
                HTDrumTier.HUGE -> RagiumItems.HUGE_DRUM_UPGRADE
            }
            val pair: Pair<HTItemMaterialVariant, HTVanillaMaterialType> = when (tier) {
                HTDrumTier.SMALL -> continue
                HTDrumTier.MEDIUM -> HTItemMaterialVariant.INGOT to HTVanillaMaterialType.GOLD
                HTDrumTier.LARGE -> HTItemMaterialVariant.GEM to HTVanillaMaterialType.DIAMOND
                HTDrumTier.HUGE -> HTItemMaterialVariant.INGOT to HTVanillaMaterialType.NETHERITE
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
