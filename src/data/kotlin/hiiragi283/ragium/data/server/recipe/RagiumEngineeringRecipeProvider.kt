package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
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
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredItem

object RagiumEngineeringRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Plastic Plate
        HTItemToObjRecipeBuilder
            .compressing(
                HTIngredientHelper.item(RagiumItems.getPlate(RagiumMaterialType.PLASTIC)),
                HTResultHelper.item(RagiumModTags.Items.POLYMER_RESIN),
            ).save(output)
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
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(Items.BLAZE_ROD),
                HTIngredientHelper.item(Items.BLAZE_POWDER, 4),
                HTIngredientHelper.item(Tags.Items.RODS_WOODEN),
            ).save(output)
        // Breeze Rod
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(Items.BREEZE_ROD),
                HTIngredientHelper.item(Items.WIND_CHARGE, 6),
                HTIngredientHelper.item(Tags.Items.RODS_WOODEN),
            ).save(output)

        circuits()
        components()

        redStones()
        diode()
    }

    private fun circuits() {
        // Circuit Boards
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.CIRCUIT_BOARD, 4),
                HTIngredientHelper.item(RagiumModTags.Items.PLASTICS),
                HTIngredientHelper.gemOrDust(HTVanillaMaterialType.QUARTZ),
            ).save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.ADVANCED_CIRCUIT_BOARD),
                HTIngredientHelper.item(RagiumModTags.Items.PLASTICS, 2),
                HTIngredientHelper.item(RagiumItems.BASALT_MESH),
                HTIngredientHelper.item(RagiumCommonTags.Items.SILICON),
            ).save(output)
        // Basic
        HTShapedRecipeBuilder(RagiumItems.getCircuit(RagiumTierType.BASIC))
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', HTMaterialVariant.INGOT, HTVanillaMaterialType.COPPER)
            .define('B', HTMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('C', HTMaterialVariant.INGOT, HTVanillaMaterialType.IRON)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.getCircuit(RagiumTierType.BASIC), 4)
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', HTMaterialVariant.INGOT, HTVanillaMaterialType.COPPER)
            .define('B', HTMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('C', RagiumItems.CIRCUIT_BOARD)
            .saveSuffixed(output, "_with_plastic")
        // Advanced
        HTShapedRecipeBuilder(RagiumItems.getCircuit(RagiumTierType.ADVANCED))
            .crossLayered()
            .define('A', Tags.Items.DUSTS_GLOWSTONE)
            .define('B', HTMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('C', gemOrDust(HTVanillaMaterialType.LAPIS))
            .define('D', HTMaterialVariant.CIRCUIT, RagiumTierType.BASIC)
            .saveSuffixed(output, "_from_basic")

        HTShapedRecipeBuilder(RagiumItems.getCircuit(RagiumTierType.ADVANCED))
            .cross8()
            .define('A', gemOrDust(RagiumMaterialType.AZURE))
            .define('B', HTMaterialVariant.INGOT, HTVanillaMaterialType.GOLD)
            .define('C', RagiumItems.CIRCUIT_BOARD)
            .save(output)

        // Circuit by combining
        for ((tier: HTMaterialType, circuit: DeferredItem<*>) in RagiumItems.MATERIALS.row(HTMaterialVariant.CIRCUIT)) {
            val dopant: HTItemIngredient = when (tier) {
                RagiumTierType.BASIC -> HTIngredientHelper.item(HTMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE)
                RagiumTierType.ADVANCED -> HTIngredientHelper.gemOrDust(RagiumMaterialType.AZURE)
                RagiumTierType.ELITE -> HTIngredientHelper.item(HTMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
                RagiumTierType.ULTIMATE -> HTIngredientHelper.item(
                    HTMaterialVariant.GEM,
                    RagiumMaterialType.ELDRITCH_PEARL,
                )
                else -> continue
            }
            val (variant: HTMaterialVariant, material: HTMaterialType) = when (tier) {
                RagiumTierType.BASIC -> HTMaterialVariant.INGOT to HTVanillaMaterialType.COPPER
                RagiumTierType.ADVANCED -> HTMaterialVariant.INGOT to HTVanillaMaterialType.GOLD
                RagiumTierType.ELITE -> HTMaterialVariant.INGOT to RagiumMaterialType.ADVANCED_RAGI_ALLOY
                RagiumTierType.ULTIMATE -> HTMaterialVariant.NUGGET to RagiumMaterialType.IRIDESCENTIUM
                else -> continue
            }
            val board: HTItemIngredient = when (tier) {
                RagiumTierType.BASIC -> RagiumItems.CIRCUIT_BOARD
                RagiumTierType.ADVANCED -> RagiumItems.CIRCUIT_BOARD
                RagiumTierType.ELITE -> RagiumItems.ADVANCED_CIRCUIT_BOARD
                RagiumTierType.ULTIMATE -> RagiumItems.ADVANCED_CIRCUIT_BOARD
                else -> continue
            }.let(HTIngredientHelper::item)
            HTCombineItemToObjRecipeBuilder
                .alloying(
                    HTResultHelper.item(circuit),
                    dopant,
                    HTIngredientHelper.item(variant, material),
                    board,
                ).save(output)
        }
    }

    private fun components() {
        fun register(material: HTMaterialType, core: HTMaterialType) {
            // Shaped
            HTShapedRecipeBuilder(RagiumItems.getCoil(material), 4)
                .hollow4()
                .define('A', HTMaterialVariant.INGOT, material)
                .define('B', ingotOrRod(core))
                .save(output)
        }

        register(RagiumMaterialType.RAGI_ALLOY, HTVanillaMaterialType.IRON)
        register(RagiumMaterialType.ADVANCED_RAGI_ALLOY, RagiumMaterialType.AZURE_STEEL)

        HTShapedRecipeBuilder(RagiumItems.ELDRITCH_GEAR)
            .hollow4()
            .define('A', HTMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL)
            .define('B', ingotOrRod(RagiumMaterialType.DEEP_STEEL))
            .save(output)

        for (tier: RagiumTierType in RagiumTierType.entries) {
            val (variant: HTMaterialVariant, material: RagiumMaterialType) = when (tier) {
                RagiumTierType.BASIC -> HTMaterialVariant.INGOT to RagiumMaterialType.RAGI_ALLOY
                RagiumTierType.ADVANCED -> HTMaterialVariant.INGOT to RagiumMaterialType.ADVANCED_RAGI_ALLOY
                RagiumTierType.ELITE -> HTMaterialVariant.GEM to RagiumMaterialType.RAGI_CRYSTAL
                RagiumTierType.ULTIMATE -> HTMaterialVariant.GEM to RagiumMaterialType.ELDRITCH_PEARL
            }
            val side: Ingredient = when (tier) {
                RagiumTierType.BASIC -> Ingredient.of(RagiumItems.getCoil(RagiumMaterialType.RAGI_ALLOY))
                RagiumTierType.ADVANCED -> Ingredient.of(RagiumItems.getCoil(RagiumMaterialType.ADVANCED_RAGI_ALLOY))
                RagiumTierType.ELITE -> Ingredient.of(RagiumItems.SILICON)
                RagiumTierType.ULTIMATE -> HTMaterialVariant.GEAR.toIngredient(RagiumMaterialType.ELDRITCH_PEARL)
            }
            val component: DeferredItem<*> = RagiumItems.getComponent(tier)
            // Shaped
            HTShapedRecipeBuilder(component)
                .cross8()
                .define('A', variant, material)
                .define('B', side)
                .define('C', HTMaterialVariant.CIRCUIT, tier)
                .save(output)
            // Alloying
            HTCombineItemToObjRecipeBuilder
                .alloying(
                    HTResultHelper.item(component),
                    HTIngredientHelper.item(variant, material, 2),
                    HTIngredientHelper.item(side, 2),
                    HTIngredientHelper.item(HTMaterialVariant.CIRCUIT, tier),
                ).save(output)
        }
    }

    private fun redStones() {
        // Redstone Board
        HTShapedRecipeBuilder(RagiumItems.REDSTONE_BOARD, 4)
            .hollow4()
            .define('A', Items.SMOOTH_STONE_SLAB)
            .define('B', HTMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.REDSTONE_BOARD, 4),
                HTIngredientHelper.item(HTMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE),
                HTIngredientHelper.item(Items.SMOOTH_STONE_SLAB),
            ).save(output)
        // Repeater
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(Items.REPEATER, 2),
                HTIngredientHelper.item(Items.REDSTONE_TORCH),
                HTIngredientHelper.item(HTMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE),
                HTIngredientHelper.item(RagiumItems.REDSTONE_BOARD),
            ).save(output)
        // Comparator
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(Items.COMPARATOR, 2),
                HTIngredientHelper.item(Items.REDSTONE_TORCH),
                HTIngredientHelper.item(HTMaterialVariant.GEM, HTVanillaMaterialType.QUARTZ),
                HTIngredientHelper.item(Items.REPEATER),
            ).save(output)
    }

    private fun diode() {
        // LED
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.LED, 4),
                HTIngredientHelper.item(HTMaterialVariant.INGOT, HTVanillaMaterialType.COPPER),
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
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.SOLAR_PANEL),
                HTIngredientHelper.item(RagiumModTags.Items.PLASTICS),
                HTIngredientHelper.item(RagiumItems.LUMINOUS_PASTE, 2),
            ).save(output)
    }
}
