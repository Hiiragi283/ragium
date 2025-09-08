package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.impl.HTShapedRecipeBuilder
import hiiragi283.ragium.api.material.HTBlockMaterialVariant
import hiiragi283.ragium.api.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTTierType
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.common.variant.HTDrumVariant
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.common.variant.RagiumMaterialVariants
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
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

    @JvmStatic
    private fun casings() {
        // Wooden
        HTShapedRecipeBuilder(RagiumBlocks.WOODEN_CASING, 4)
            .cross8()
            .define('A', ItemTags.LOGS)
            .define('B', ItemTags.PLANKS)
            .define('C', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .save(output)
        // Stone
        HTShapedRecipeBuilder(RagiumBlocks.STONE_CASING)
            .casing()
            .define('A', Tags.Items.COBBLESTONES_NORMAL)
            .define('B', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('C', Items.SMOOTH_STONE)
            .save(output)

        HTShapedRecipeBuilder(RagiumBlocks.REINFORCED_STONE_CASING)
            .casing()
            .define('A', Items.BASALT)
            .define('B', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('C', Items.SMOOTH_STONE)
            .save(output)
        // Machine
        HTShapedRecipeBuilder(RagiumBlocks.BASIC_MACHINE_FRAME, 2)
            .hollow8()
            .define('A', HTItemMaterialVariant.INGOT, HTVanillaMaterialType.IRON)
            .define('B', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .save(output)
        // Advanced Machine
        HTShapedRecipeBuilder(RagiumBlocks.ADVANCED_MACHINE_FRAME, 2)
            .hollow8()
            .define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('B', HTItemMaterialVariant.DUST, RagiumMaterialType.AZURE)
            .save(output)
        // Elite Machine
        HTShapedRecipeBuilder(RagiumBlocks.ELITE_MACHINE_FRAME, 4)
            .hollow8()
            .define('A', HTItemMaterialVariant.INGOT, HTVanillaMaterialType.NETHERITE)
            .define('B', HTItemMaterialVariant.DUST, RagiumMaterialType.ELDRITCH_PEARL)
            .save(output)
        // Device
        HTShapedRecipeBuilder(RagiumBlocks.DEVICE_CASING)
            .cross8()
            .define('A', Items.BLACK_CONCRETE)
            .define('B', HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('C', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .save(output)

        HTShapedRecipeBuilder(RagiumBlocks.DEVICE_CASING, 4)
            .cross8()
            .define('A', Tags.Items.OBSIDIANS_NORMAL)
            .define('B', HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('C', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .saveSuffixed(output, "_with_obsidian")
    }

    //    Machines    //

    @JvmStatic
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
            ).define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)
            .define('B', Items.FLINT)
            .define('C', HTItemMaterialVariant.CIRCUIT, HTTierType.BASIC)
            .define('D', Items.BRICKS)
            .save(output)

        HTShapedRecipeBuilder(HTMachineVariant.SMELTER)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)
            .define('B', RagiumItems.getMaterial(RagiumMaterialVariants.COIL, RagiumMaterialType.RAGI_ALLOY))
            .define('C', Items.FURNACE)
            .define('D', Items.BRICKS)
            .save(output)
        // Advanced
        HTShapedRecipeBuilder(HTMachineVariant.ALLOY_SMELTER)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.ADVANCED_RAGI_ALLOY)
            .define('B', RagiumItems.getMaterial(RagiumMaterialVariants.COIL, RagiumMaterialType.ADVANCED_RAGI_ALLOY))
            .define('C', Items.BLAST_FURNACE)
            .define('D', Items.NETHER_BRICKS)
            .save(output)

        HTShapedRecipeBuilder(HTMachineVariant.CRUSHER)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.ADVANCED_RAGI_ALLOY)
            .define('B', Tags.Items.GEMS_DIAMOND)
            .define('C', HTItemMaterialVariant.CIRCUIT, HTTierType.ADVANCED)
            .define('D', Items.NETHER_BRICKS)
            .save(output)

        HTShapedRecipeBuilder(HTMachineVariant.MELTER)
            .crossLayered()
            .define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.ADVANCED_RAGI_ALLOY)
            .define('B', HTItemMaterialVariant.CIRCUIT, HTTierType.ADVANCED)
            .define('C', Items.BLAST_FURNACE)
            .define('D', RagiumBlocks.REINFORCED_STONE_CASING)
            .save(output)

        HTShapedRecipeBuilder(HTMachineVariant.REFINERY)
            .pattern(
                " A ",
                "ABA",
                "CDC",
            ).define('A', HTBlockMaterialVariant.GLASS_BLOCK, HTVanillaMaterialType.QUARTZ)
            .define('B', RagiumItems.getMaterial(RagiumMaterialVariants.COMPONENT, HTTierType.ADVANCED))
            .define('C', HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('D', RagiumItems.getMaterial(RagiumMaterialVariants.COIL, RagiumMaterialType.ADVANCED_RAGI_ALLOY))
            .save(output)

        mapOf(
            HTMachineVariant.ALLOY_SMELTER to HTMachineVariant.SMELTER,
            HTMachineVariant.CRUSHER to HTMachineVariant.PULVERIZER,
            HTMachineVariant.MELTER to HTMachineVariant.EXTRACTOR,
        ).forEach { (adv: HTMachineVariant, basic: HTMachineVariant) ->
            createComponentUpgrade(HTTierType.ADVANCED, adv, basic).save(output)
        }
        // Elite
        HTShapedRecipeBuilder(HTMachineVariant.SIMULATOR)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('B', HTBlockMaterialVariant.GLASS_BLOCK, HTVanillaMaterialType.OBSIDIAN)
            .define('C', HTItemMaterialVariant.CIRCUIT, HTTierType.ELITE)
            .define('D', Items.DEEPSLATE_TILES)
            .save(output)

        mapOf(
            HTMachineVariant.MULTI_SMELTER to HTMachineVariant.ALLOY_SMELTER,
        ).forEach { (elite: HTMachineVariant, adv: HTMachineVariant) ->
            createComponentUpgrade(HTTierType.ELITE, elite, adv).save(output)
        }
    }

    @JvmStatic
    private fun basicMachine(variant: HTMachineVariant, side: Ingredient) {
        HTShapedRecipeBuilder(variant)
            .crossLayered()
            .define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)
            .define('B', HTItemMaterialVariant.CIRCUIT, HTTierType.BASIC)
            .define('C', side)
            .define('D', RagiumBlocks.STONE_CASING)
            .save(output)
    }

    //    Devices    //

    @JvmStatic
    private fun devices() {
        // Basic
        createComponentUpgrade(HTTierType.BASIC, HTDeviceVariant.ITEM_BUFFER, RagiumBlocks.DEVICE_CASING)
            .addIngredient(Tags.Items.CHESTS)
            .save(output)
        createComponentUpgrade(HTTierType.BASIC, HTDeviceVariant.MILK_COLLECTOR, RagiumBlocks.DEVICE_CASING)
            .addIngredient(Tags.Items.BUCKETS_MILK)
            .save(output)
        createComponentUpgrade(HTTierType.BASIC, HTDeviceVariant.WATER_COLLECTOR, RagiumBlocks.DEVICE_CASING)
            .addIngredient(Tags.Items.BUCKETS_WATER)
            .save(output)

        // Advanced
        createComponentUpgrade(HTTierType.ADVANCED, HTDeviceVariant.ENI, RagiumBlocks.DEVICE_CASING)
            .addIngredient(HTItemMaterialVariant.GEM, HTVanillaMaterialType.DIAMOND)
            .save(output)
        createComponentUpgrade(HTTierType.ADVANCED, HTDeviceVariant.EXP_COLLECTOR, RagiumBlocks.DEVICE_CASING)
            .addIngredient(Items.HOPPER)
            .save(output)
        createComponentUpgrade(HTTierType.ADVANCED, HTDeviceVariant.LAVA_COLLECTOR, RagiumBlocks.DEVICE_CASING)
            .addIngredient(Tags.Items.BUCKETS_LAVA)
            .save(output)
        // Elite
        createComponentUpgrade(HTTierType.ELITE, HTDeviceVariant.DIM_ANCHOR, RagiumBlocks.DEVICE_CASING)
            .addIngredient(HTItemMaterialVariant.GEM, RagiumMaterialType.WARPED_CRYSTAL)
            .save(output)

        createComponentUpgrade(HTTierType.ELITE, HTDeviceVariant.TELEPAD, RagiumBlocks.DEVICE_CASING)
            .addIngredient(HTBlockMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.WARPED_CRYSTAL)
            .save(output)
    }

    @JvmStatic
    private fun drums() {
        for ((variant: HTDrumVariant, drum: ItemLike) in RagiumBlocks.DRUMS) {
            resetComponent(drum)

            val pair: Pair<HTItemMaterialVariant, HTVanillaMaterialType> = when (variant) {
                HTDrumVariant.SMALL -> HTItemMaterialVariant.INGOT to HTVanillaMaterialType.COPPER
                HTDrumVariant.MEDIUM -> HTItemMaterialVariant.INGOT to HTVanillaMaterialType.GOLD
                HTDrumVariant.LARGE -> HTItemMaterialVariant.GEM to HTVanillaMaterialType.DIAMOND
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
        createNetheriteUpgrade(HTDrumVariant.HUGE, HTDrumVariant.LARGE).save(output)
        // Upgrades
        for (variant: HTDrumVariant in RagiumBlocks.DRUMS.keys) {
            val upgrade: ItemLike = when (variant) {
                HTDrumVariant.SMALL -> continue
                HTDrumVariant.MEDIUM -> RagiumItems.MEDIUM_DRUM_UPGRADE
                HTDrumVariant.LARGE -> RagiumItems.LARGE_DRUM_UPGRADE
                HTDrumVariant.HUGE -> RagiumItems.HUGE_DRUM_UPGRADE
            }
            val pair: Pair<HTItemMaterialVariant, HTVanillaMaterialType> = when (variant) {
                HTDrumVariant.SMALL -> continue
                HTDrumVariant.MEDIUM -> HTItemMaterialVariant.INGOT to HTVanillaMaterialType.GOLD
                HTDrumVariant.LARGE -> HTItemMaterialVariant.GEM to HTVanillaMaterialType.DIAMOND
                HTDrumVariant.HUGE -> HTItemMaterialVariant.INGOT to HTVanillaMaterialType.NETHERITE
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
