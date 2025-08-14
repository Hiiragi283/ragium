package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCombineItemToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.RagiumCircuitType
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredItem

object RagiumEngineeringRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Plastic Plate
        HTShapelessRecipeBuilder(RagiumItems.PLASTIC_PLATE)
            .addIngredient(RagiumModTags.Items.POLYMER_RESIN)
            .save(output)
        // Synthetic Fiber
        HTShapelessRecipeBuilder(RagiumItems.SYNTHETIC_FIBER, 2)
            .addIngredient(RagiumModTags.Items.POLYMER_RESIN)
            .addIngredient(Tags.Items.STRINGS)
            .savePrefixed(output, "2x_")

        HTShapedRecipeBuilder(RagiumItems.SYNTHETIC_FIBER, 9)
            .hollow8()
            .define('A', RagiumModTags.Items.POLYMER_RESIN)
            .define('B', Tags.Items.STRINGS)
            .savePrefixed(output, "9x_")
        // Synthetic Leather
        HTShapelessRecipeBuilder(RagiumItems.SYNTHETIC_LEATHER, 2)
            .addIngredient(RagiumModTags.Items.POLYMER_RESIN)
            .addIngredient(Tags.Items.LEATHERS)
            .savePrefixed(output, "2x_")

        HTShapedRecipeBuilder(RagiumItems.SYNTHETIC_LEATHER, 9)
            .hollow8()
            .define('A', RagiumModTags.Items.POLYMER_RESIN)
            .define('B', Tags.Items.LEATHERS)
            .savePrefixed(output, "9x_")
        // Blaze Rod
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(Items.BLAZE_ROD),
                HTIngredientHelper.item(Items.BLAZE_POWDER, 4),
                HTIngredientHelper.item(Tags.Items.RODS_WOODEN),
            ).save(output)
        // Breeze Rod
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(Items.BREEZE_ROD),
                HTIngredientHelper.item(Items.WIND_CHARGE, 6),
                HTIngredientHelper.item(Tags.Items.RODS_WOODEN),
            ).save(output)

        circuits()
        redStones()
        diode()
    }

    private fun circuits() {
        // Circuit Board
        HTShapelessRecipeBuilder(RagiumItems.CIRCUIT_BOARD)
            .addIngredient(Tags.Items.DYES_GREEN)
            .addIngredient(gemOrDust(RagiumConst.AZURE))
            .addIngredient(ItemTags.PLANKS)
            .save(output)

        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.CIRCUIT_BOARD, 4),
                HTIngredientHelper.item(Tags.Items.DYES_GREEN),
                HTIngredientHelper.gemOrDust(RagiumConst.AZURE),
                HTIngredientHelper.item(RagiumModTags.Items.PLASTICS),
            ).save(output)
        // Basic
        HTShapedRecipeBuilder(RagiumItems.getCircuit(RagiumCircuitType.BASIC))
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', Tags.Items.INGOTS_COPPER)
            .define('B', Tags.Items.DUSTS_REDSTONE)
            .define('C', RagiumModTags.Items.CIRCUIT_BOARDS)
            .save(output)
        // Advanced
        HTShapedRecipeBuilder(RagiumItems.getCircuit(RagiumCircuitType.ADVANCED))
            .pattern(
                "ABA",
                "CDC",
                "ABA",
            ).define('A', Tags.Items.DUSTS_GLOWSTONE)
            .define('B', Tags.Items.INGOTS_GOLD)
            .define('C', Tags.Items.GEMS_LAPIS)
            .define('D', RagiumCommonTags.Items.CIRCUITS_BASIC)
            .saveSuffixed(output, "_from_basic")

        HTShapedRecipeBuilder(RagiumItems.getCircuit(RagiumCircuitType.ADVANCED))
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', Tags.Items.INGOTS_GOLD)
            .define('B', gemOrDust(RagiumConst.AZURE))
            .define('C', RagiumModTags.Items.CIRCUIT_BOARDS)
            .save(output)

        // Circuit by combining
        for ((tier: HTMaterialType, circuit: DeferredItem<*>) in RagiumItems.MATERIALS.row(HTMaterialVariant.CIRCUIT)) {
            val dopant: HTItemIngredient = when (tier) {
                RagiumCircuitType.BASIC -> HTIngredientHelper.item(Tags.Items.DUSTS_REDSTONE)
                RagiumCircuitType.ADVANCED -> HTIngredientHelper.gemOrDust("lapis")
                RagiumCircuitType.ELITE -> HTIngredientHelper.item(RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL)
                RagiumCircuitType.ULTIMATE -> HTIngredientHelper.item(RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL)
                else -> continue
            }
            val wiring: TagKey<Item> = when (tier) {
                RagiumCircuitType.BASIC -> Tags.Items.INGOTS_COPPER
                RagiumCircuitType.ADVANCED -> Tags.Items.INGOTS_GOLD
                RagiumCircuitType.ELITE -> RagiumCommonTags.Items.INGOTS_ADVANCED_RAGI_ALLOY
                RagiumCircuitType.ULTIMATE -> Tags.Items.NETHER_STARS
                else -> continue
            }

            HTCombineItemToItemRecipeBuilder
                .alloying(
                    HTResultHelper.item(circuit),
                    dopant,
                    HTIngredientHelper.item(wiring),
                    HTIngredientHelper.item(RagiumModTags.Items.CIRCUIT_BOARDS),
                ).save(output)
        }
    }

    private fun redStones() {
        // Redstone Board
        HTShapedRecipeBuilder(RagiumItems.REDSTONE_BOARD, 4)
            .hollow4()
            .define('A', Items.SMOOTH_STONE_SLAB)
            .define('B', Tags.Items.DUSTS_REDSTONE)
            .save(output)

        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.REDSTONE_BOARD, 4),
                HTIngredientHelper.item(Tags.Items.DUSTS_REDSTONE),
                HTIngredientHelper.item(Items.SMOOTH_STONE_SLAB),
            ).save(output)
        // Repeater
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(Items.REPEATER, 2),
                HTIngredientHelper.item(Items.REDSTONE_TORCH),
                HTIngredientHelper.item(Tags.Items.DUSTS_REDSTONE),
                HTIngredientHelper.item(RagiumItems.REDSTONE_BOARD),
            ).save(output)
        // Comparator
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(Items.COMPARATOR, 2),
                HTIngredientHelper.item(Items.REDSTONE_TORCH),
                HTIngredientHelper.item(Tags.Items.GEMS_QUARTZ),
                HTIngredientHelper.item(Items.REPEATER),
            ).save(output)
    }

    private fun diode() {
        // LED
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.LED, 4),
                HTIngredientHelper.item(Tags.Items.INGOTS_COPPER),
                HTIngredientHelper.item(RagiumItems.LUMINOUS_PASTE),
            ).save(output)
        // LED Block
        HTShapedRecipeBuilder(RagiumBlocks.LEDBlocks.WHITE, 8)
            .hollow8()
            .define('A', Tags.Items.GLASS_BLOCKS)
            .define('B', RagiumItems.LED)
            .saveSuffixed(output, "_from_led")

        for (holderLike: RagiumBlocks.LEDBlocks in RagiumBlocks.LEDBlocks.entries) {
            HTShapedRecipeBuilder(holderLike, 8)
                .hollow8()
                .define('A', RagiumModTags.Items.LED_BLOCKS)
                .define('B', holderLike.color.tag)
                .save(output)
        }

        // Solar Panel
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.SOLAR_PANEL),
                HTIngredientHelper.gemOrDust(RagiumConst.AZURE),
                HTIngredientHelper.item(RagiumItems.LUMINOUS_PASTE),
                HTIngredientHelper.item(RagiumModTags.Items.PLASTICS),
            ).save(output)
    }
}
