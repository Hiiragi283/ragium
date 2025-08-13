package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCombineItemToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTItemWithCatalystToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumPressingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Plastic Plate
        HTItemWithCatalystToItemRecipeBuilder
            .pressing(
                HTIngredientHelper.item(RagiumModTags.Items.POLYMER_RESIN),
                null,
                HTResultHelper.item(RagiumModTags.Items.PLASTICS),
            ).save(output)
        // Synthetic Fiber
        HTItemWithCatalystToItemRecipeBuilder
            .pressing(
                HTIngredientHelper.item(RagiumModTags.Items.POLYMER_RESIN),
                HTIngredientHelper.item(Tags.Items.STRINGS),
                HTResultHelper.item(RagiumItems.SYNTHETIC_FIBER, 2),
            ).save(output)
        // Synthetic Leather
        HTItemWithCatalystToItemRecipeBuilder
            .pressing(
                HTIngredientHelper.item(RagiumModTags.Items.POLYMER_RESIN),
                HTIngredientHelper.item(Tags.Items.LEATHERS),
                HTResultHelper.item(RagiumItems.SYNTHETIC_LEATHER, 2),
            ).save(output)

        // Blaze Rod
        HTItemWithCatalystToItemRecipeBuilder
            .pressing(
                HTIngredientHelper.item(Items.BLAZE_POWDER, 4),
                HTIngredientHelper.item(Tags.Items.RODS_WOODEN),
                HTResultHelper.item(Items.BLAZE_ROD),
            ).save(output)
        // Breeze Rod
        HTItemWithCatalystToItemRecipeBuilder
            .pressing(
                HTIngredientHelper.item(Items.WIND_CHARGE, 6),
                HTIngredientHelper.item(Tags.Items.RODS_WOODEN),
                HTResultHelper.item(Items.BREEZE_ROD),
            ).save(output)

        circuits()
        redStones()
        diode()
    }

    private fun circuits() {
        // Basic
        HTShapedRecipeBuilder(RagiumItems.Circuits.BASIC)
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', Tags.Items.INGOTS_COPPER)
            .define('B', RagiumCommonTags.Items.DUSTS_RAGINITE)
            .define('C', Tags.Items.INGOTS_IRON)
            .save(output)
        // Advanced
        HTShapedRecipeBuilder(RagiumItems.Circuits.ADVANCED)
            .cross4()
            .define('A', Tags.Items.INGOTS_GOLD)
            .define('B', Tags.Items.DUSTS_GLOWSTONE)
            .define('C', RagiumCommonTags.Items.CIRCUITS_BASIC)
            .save(output)

        // Circuit Board
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTIngredientHelper.item(RagiumModTags.Items.PLASTICS),
                HTIngredientHelper.item(Tags.Items.DUSTS_REDSTONE),
                HTResultHelper.item(RagiumItems.CIRCUIT_BOARD),
            ).save(output)

        for (circuit: RagiumItems.Circuits in RagiumItems.Circuits.entries) {
            val input: TagKey<Item> = when (circuit) {
                RagiumItems.Circuits.BASIC -> Tags.Items.INGOTS_COPPER
                RagiumItems.Circuits.ADVANCED -> Tags.Items.INGOTS_GOLD
                RagiumItems.Circuits.ELITE -> RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL
                RagiumItems.Circuits.ULTIMATE -> RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL
            }

            HTItemWithCatalystToItemRecipeBuilder
                .pressing(
                    HTIngredientHelper.item(input),
                    HTIngredientHelper.item(RagiumItems.CIRCUIT_BOARD),
                    HTResultHelper.item(circuit),
                ).save(output)
        }
    }

    private fun redStones() {
        // Redstone Board
        HTItemWithCatalystToItemRecipeBuilder
            .pressing(
                HTIngredientHelper.item(Tags.Items.DUSTS_REDSTONE),
                HTIngredientHelper.item(Items.SMOOTH_STONE_SLAB),
                HTResultHelper.item(RagiumItems.REDSTONE_BOARD, 4),
            ).save(output)
        // Repeater
        HTItemWithCatalystToItemRecipeBuilder
            .pressing(
                HTIngredientHelper.item(Items.REDSTONE_TORCH, 2),
                HTIngredientHelper.item(RagiumItems.REDSTONE_BOARD),
                HTResultHelper.item(Items.REPEATER, 2),
            ).save(output)
        // Comparator
        HTItemWithCatalystToItemRecipeBuilder
            .pressing(
                HTIngredientHelper.item(Items.REDSTONE_TORCH),
                HTIngredientHelper.item(Items.REPEATER),
                HTResultHelper.item(Items.COMPARATOR),
            ).save(output)
    }

    private fun diode() {
        // LED
        HTItemWithCatalystToItemRecipeBuilder
            .pressing(
                HTIngredientHelper.item(RagiumItems.LUMINOUS_PASTE),
                HTIngredientHelper.item(Tags.Items.INGOTS_COPPER),
                HTResultHelper.item(RagiumItems.LED, 4),
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
        HTShapedRecipeBuilder(RagiumItems.SOLAR_PANEL, 3)
            .pattern(
                "AAA",
                "BBB",
                "CCC",
            ).define('A', Tags.Items.GLASS_BLOCKS)
            .define('B', RagiumItems.LUMINOUS_PASTE)
            .define('C', RagiumModTags.Items.PLASTICS)
            .save(output)
    }
}
