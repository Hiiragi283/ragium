package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTCombineItemToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.HTVanillaMaterialType
import hiiragi283.ragium.util.material.RagiumMaterialType
import hiiragi283.ragium.util.material.RagiumTierType
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredItem

object RagiumEngineeringRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Plastic Plate
        HTShapelessRecipeBuilder(RagiumItems.getPlate(RagiumMaterialType.PLASTIC))
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
        coils()
        redStones()
        diode()
    }

    private fun circuits() {
        // Circuit Board
        HTShapelessRecipeBuilder(RagiumItems.CIRCUIT_BOARD)
            .addIngredient(Tags.Items.DYES_GREEN)
            .addIngredient(gemOrDust(RagiumMaterialType.AZURE))
            .addIngredient(ItemTags.PLANKS)
            .save(output)

        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.CIRCUIT_BOARD, 4),
                HTIngredientHelper.item(Tags.Items.DYES_GREEN),
                HTIngredientHelper.gemOrDust(RagiumMaterialType.AZURE),
                HTIngredientHelper.item(RagiumModTags.Items.PLASTICS),
            ).save(output)

        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.ADVANCED_CIRCUIT_BOARD),
                HTIngredientHelper.item(Tags.Items.DYES_BLACK),
                HTIngredientHelper.item(RagiumItems.BASALT_MESH),
                HTIngredientHelper.item(RagiumModTags.Items.PLASTICS),
            ).save(output)
        // Basic
        HTShapedRecipeBuilder(RagiumItems.getCircuit(RagiumTierType.BASIC))
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', Tags.Items.INGOTS_COPPER)
            .define('B', Tags.Items.DUSTS_REDSTONE)
            .define('C', RagiumModTags.Items.CIRCUIT_BOARDS)
            .save(output)
        // Advanced
        HTShapedRecipeBuilder(RagiumItems.getCircuit(RagiumTierType.ADVANCED))
            .crossLayered()
            .define('A', Tags.Items.DUSTS_GLOWSTONE)
            .define('B', Tags.Items.INGOTS_GOLD)
            .define('C', Tags.Items.GEMS_LAPIS)
            .define('D', RagiumCommonTags.Items.CIRCUITS_BASIC)
            .saveSuffixed(output, "_from_basic")

        HTShapedRecipeBuilder(RagiumItems.getCircuit(RagiumTierType.ADVANCED))
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', Tags.Items.INGOTS_GOLD)
            .define('B', gemOrDust(RagiumMaterialType.AZURE))
            .define('C', RagiumModTags.Items.CIRCUIT_BOARDS)
            .save(output)

        // Circuit by combining
        for ((tier: HTMaterialType, circuit: DeferredItem<*>) in RagiumItems.MATERIALS.row(HTMaterialVariant.CIRCUIT)) {
            val dopant: HTItemIngredient = when (tier) {
                RagiumTierType.BASIC -> HTIngredientHelper.item(Tags.Items.DUSTS_REDSTONE)
                RagiumTierType.ADVANCED -> HTIngredientHelper.gemOrDust(RagiumMaterialType.AZURE)
                RagiumTierType.ELITE -> HTIngredientHelper.item(RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL)
                RagiumTierType.ULTIMATE -> HTIngredientHelper.item(RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL)
                else -> continue
            }
            val wiring: HTItemIngredient = when (tier) {
                RagiumTierType.BASIC -> Tags.Items.INGOTS_COPPER
                RagiumTierType.ADVANCED -> Tags.Items.INGOTS_GOLD
                RagiumTierType.ELITE -> RagiumCommonTags.Items.INGOTS_ADVANCED_RAGI_ALLOY
                RagiumTierType.ULTIMATE -> Tags.Items.NETHER_STARS
                else -> continue
            }.let(HTIngredientHelper::item)
            val board: HTItemIngredient = when (tier) {
                RagiumTierType.BASIC -> HTIngredientHelper.item(RagiumModTags.Items.CIRCUIT_BOARDS)
                RagiumTierType.ADVANCED -> HTIngredientHelper.item(RagiumModTags.Items.CIRCUIT_BOARDS)
                RagiumTierType.ELITE -> HTIngredientHelper.item(RagiumItems.ADVANCED_CIRCUIT_BOARD)
                RagiumTierType.ULTIMATE -> HTIngredientHelper.item(RagiumItems.ADVANCED_CIRCUIT_BOARD)
                else -> continue
            }
            HTCombineItemToItemRecipeBuilder.alloying(HTResultHelper.item(circuit), dopant, wiring, board).save(output)
        }
    }

    private fun coils() {
        fun register(material: HTMaterialType) {
            // Shaped
            HTShapedRecipeBuilder(RagiumItems.getMaterial(HTMaterialVariant.COIL, material))
                .hollow4()
                .define('A', HTMaterialVariant.INGOT, material)
                .define('B', ingotOrRod(HTVanillaMaterialType.IRON))
                .save(output)
        }

        register(RagiumMaterialType.RAGI_ALLOY)
        register(RagiumMaterialType.ADVANCED_RAGI_ALLOY)
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
                HTIngredientHelper.gemOrDust(RagiumMaterialType.AZURE),
                HTIngredientHelper.item(RagiumItems.LUMINOUS_PASTE),
                HTIngredientHelper.item(RagiumModTags.Items.PLASTICS),
            ).save(output)
    }
}
