package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.recipe.HTSmithingModifyRecipe
import hiiragi283.ragium.common.tier.HTCircuitTier
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.variant.HTColorMaterial
import hiiragi283.ragium.impl.data.recipe.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumEngineeringRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Plastic Plate
        HTItemToObjRecipeBuilder
            .compressing(
                ingredientHelper.item(RagiumModTags.Items.POLYMER_RESIN),
                resultHelper.item(HTItemMaterialVariant.PLATE, RagiumMaterialType.PLASTIC),
            ).save(output)
        // Synthetic Fiber / Leather
        mapOf(
            RagiumItems.SYNTHETIC_FIBER to Tags.Items.STRINGS,
            RagiumItems.SYNTHETIC_LEATHER to Tags.Items.LEATHERS,
        ).forEach { (result: ItemLike, parent: TagKey<Item>) ->
            HTShapelessRecipeBuilder
                .misc(result, 2)
                .addIngredient(RagiumModTags.Items.POLYMER_RESIN)
                .addIngredient(parent)
                .savePrefixed(output, "2x_")

            HTShapedRecipeBuilder
                .misc(result, 9)
                .hollow8()
                .define('A', RagiumModTags.Items.POLYMER_RESIN)
                .define('B', parent)
                .savePrefixed(output, "9x_")
        }
        // Blaze Rod
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(Items.BLAZE_ROD),
                ingredientHelper.item(Items.BLAZE_POWDER, 4),
                ingredientHelper.item(Tags.Items.RODS_WOODEN),
            ).save(output)
        // Breeze Rod
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(Items.BREEZE_ROD),
                ingredientHelper.item(Items.WIND_CHARGE, 6),
                ingredientHelper.item(Tags.Items.RODS_WOODEN),
            ).save(output)

        // Gravity-Unit
        val gravityUnit: HTDeferredItem<Item> = RagiumItems.GRAVITATIONAL_UNIT
        HTShapedRecipeBuilder
            .misc(gravityUnit)
            .cross8()
            .define('A', Items.SHULKER_SHELL)
            .define('B', HTItemMaterialVariant.CIRCUIT, HTCircuitTier.ULTIMATE)
            .define('C', Items.END_CRYSTAL)
            .save(output)
        save(
            gravityUnit.id.withPrefix("smithing/"),
            HTSmithingModifyRecipe(
                Ingredient.of(gravityUnit),
                Ingredient.of(),
                DataComponentPatch
                    .builder()
                    .set(RagiumDataComponents.ANTI_GRAVITY.get(), true)
                    .build(),
            ),
        )

        catalyst()
        circuits()
        components()

        redStones()
        diode()
    }

    @JvmStatic
    private fun catalyst() {
        // Plating Catalyst
        HTShapedRecipeBuilder
            .misc(RagiumItems.PLATING_CATALYST)
            .cross8()
            .define('A', Tags.Items.RODS_BLAZE)
            .define('B', HTItemMaterialVariant.INGOT, RagiumMaterialType.GILDIUM)
            .define('C', Items.IRON_BARS)
            .save(output)
        // Polymer Catalyst
        HTShapedRecipeBuilder
            .misc(RagiumItems.POLYMER_CATALYST)
            .cross8()
            .define('A', Tags.Items.RODS_BREEZE)
            .define('B', HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('C', Items.IRON_BARS)
            .save(output)
    }

    @JvmStatic
    private fun circuits() {
        // Circuit Boards
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumItems.CIRCUIT_BOARD, 4),
                ingredientHelper.item(RagiumModTags.Items.PLASTICS),
                ingredientHelper.gemOrDust(HTVanillaMaterialType.QUARTZ),
            ).save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumItems.ADVANCED_CIRCUIT_BOARD),
                ingredientHelper.item(RagiumModTags.Items.PLASTICS, 2),
                ingredientHelper.item(RagiumItems.BASALT_MESH),
            ).save(output)
        // Basic
        HTShapedRecipeBuilder
            .misc(RagiumItems.getCircuit(HTCircuitTier.BASIC))
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', HTItemMaterialVariant.INGOT, HTVanillaMaterialType.COPPER)
            .define('B', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('C', HTItemMaterialVariant.INGOT, HTVanillaMaterialType.IRON)
            .save(output)

        HTShapedRecipeBuilder
            .misc(RagiumItems.getCircuit(HTCircuitTier.BASIC), 2)
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', HTItemMaterialVariant.INGOT, HTVanillaMaterialType.COPPER)
            .define('B', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('C', RagiumItems.CIRCUIT_BOARD)
            .saveSuffixed(output, "_with_plastic")

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumItems.getCircuit(HTCircuitTier.BASIC), 4),
                ingredientHelper.ingotOrDust(HTVanillaMaterialType.COPPER, 2),
                ingredientHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE),
                ingredientHelper.item(RagiumItems.CIRCUIT_BOARD),
            ).save(output)
        // Advanced
        HTShapedRecipeBuilder
            .misc(RagiumItems.getCircuit(HTCircuitTier.ADVANCED))
            .crossLayered()
            .define('A', Tags.Items.DUSTS_GLOWSTONE)
            .define('B', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('C', gemOrDust(HTVanillaMaterialType.LAPIS))
            .define('D', HTItemMaterialVariant.CIRCUIT, HTCircuitTier.BASIC)
            .saveSuffixed(output, "_from_basic")

        HTShapedRecipeBuilder
            .misc(RagiumItems.getCircuit(HTCircuitTier.ADVANCED))
            .cross8()
            .define('A', gemOrDust(RagiumMaterialType.AZURE))
            .define('B', HTItemMaterialVariant.INGOT, HTVanillaMaterialType.GOLD)
            .define('C', RagiumItems.CIRCUIT_BOARD)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumItems.getCircuit(HTCircuitTier.ADVANCED), 2),
                ingredientHelper.ingotOrDust(HTVanillaMaterialType.GOLD, 2),
                ingredientHelper.gemOrDust(RagiumMaterialType.AZURE),
                ingredientHelper.item(RagiumItems.CIRCUIT_BOARD),
            ).save(output)
        // Elite
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumItems.getCircuit(HTCircuitTier.ELITE)),
                ingredientHelper.ingotOrDust(RagiumMaterialType.ADVANCED_RAGI_ALLOY, 2),
                ingredientHelper.gemOrDust(RagiumMaterialType.RAGI_CRYSTAL),
                ingredientHelper.item(RagiumItems.ADVANCED_CIRCUIT_BOARD),
            ).save(output)
        // Ultimate
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumItems.getCircuit(HTCircuitTier.ULTIMATE)),
                ingredientHelper.item(HTItemMaterialVariant.NUGGET, RagiumMaterialType.IRIDESCENTIUM),
                ingredientHelper.gemOrDust(HTVanillaMaterialType.ECHO),
                ingredientHelper.item(RagiumItems.ADVANCED_CIRCUIT_BOARD),
            ).save(output)
    }

    @JvmStatic
    private fun components() {
        // Coil
        fun registerCoil(material: HTMaterialType, core: HTMaterialType) {
            val coil: ItemLike = RagiumItems.getCoil(material)
            // Item
            HTShapedRecipeBuilder
                .misc(coil, 4)
                .hollow4()
                .define('A', HTItemMaterialVariant.INGOT, material)
                .define('B', ingotOrRod(core))
                .save(output)
            // Block
            HTShapedRecipeBuilder
                .building(RagiumBlocks.getCoilBlock(material))
                .hollow8()
                .define('A', coil)
                .define('B', HTItemMaterialVariant.INGOT, core)
                .save(output)
        }
        registerCoil(RagiumMaterialType.RAGI_ALLOY, HTVanillaMaterialType.IRON)
        registerCoil(RagiumMaterialType.ADVANCED_RAGI_ALLOY, RagiumMaterialType.AZURE_STEEL)

        // Component
        val basic: ItemLike = RagiumItems.getComponent(HTComponentTier.BASIC)
        HTShapedRecipeBuilder
            .misc(basic)
            .crossLayered()
            .define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)
            .define('B', Tags.Items.GLASS_BLOCKS_COLORLESS)
            .define('C', HTItemMaterialVariant.CIRCUIT, HTCircuitTier.BASIC)
            .define('D', HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE)
            .save(output)

        val adv: ItemLike = RagiumItems.getComponent(HTComponentTier.ADVANCED)
        HTShapedRecipeBuilder
            .misc(adv)
            .crossLayered()
            .define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.ADVANCED_RAGI_ALLOY)
            .define('B', HTItemMaterialVariant.GEM, HTVanillaMaterialType.QUARTZ)
            .define('C', HTItemMaterialVariant.CIRCUIT, HTCircuitTier.ADVANCED)
            .define('D', basic)
            .save(output)

        val elite: ItemLike = RagiumItems.getComponent(HTComponentTier.ELITE)
        HTShapedRecipeBuilder
            .misc(elite)
            .crossLayered()
            .define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('B', HTItemMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
            .define('C', HTItemMaterialVariant.CIRCUIT, HTCircuitTier.ELITE)
            .define('D', adv)
            .save(output)

        val ultimate: ItemLike = RagiumItems.getComponent(HTComponentTier.ULTIMATE)
        HTShapedRecipeBuilder
            .misc(ultimate)
            .crossLayered()
            .define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.DEEP_STEEL)
            .define('B', HTItemMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL)
            .define('C', HTItemMaterialVariant.CIRCUIT, HTCircuitTier.ULTIMATE)
            .define('D', elite)
            .save(output)

        val eternal: ItemLike = RagiumItems.getComponent(HTComponentTier.ETERNAL)
        HTShapedRecipeBuilder
            .misc(eternal)
            .cross8()
            .define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.IRIDESCENTIUM)
            .define('B', Items.CLOCK)
            .define('C', ultimate)
            .save(output)
        save(
            RagiumAPI.id("smithing/eternal_ticket"),
            HTSmithingModifyRecipe(
                Ingredient.of(eternal),
                Ingredient.of(),
                DataComponentPatch
                    .builder()
                    .set(DataComponents.UNBREAKABLE, Unbreakable(true))
                    .build(),
            ),
        )
    }

    @JvmStatic
    private fun redStones() {
        // Redstone Board
        HTShapedRecipeBuilder
            .redstone(RagiumItems.REDSTONE_BOARD, 4)
            .hollow4()
            .define('A', Items.SMOOTH_STONE_SLAB)
            .define('B', HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumItems.REDSTONE_BOARD, 4),
                ingredientHelper.item(HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE),
                ingredientHelper.item(Items.SMOOTH_STONE_SLAB),
            ).save(output)
        // Repeater
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(Items.REPEATER, 2),
                ingredientHelper.item(Items.REDSTONE_TORCH),
                ingredientHelper.item(HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE),
                ingredientHelper.item(RagiumItems.REDSTONE_BOARD),
            ).save(output)
        // Comparator
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(Items.COMPARATOR, 2),
                ingredientHelper.item(Items.REDSTONE_TORCH),
                ingredientHelper.item(HTItemMaterialVariant.GEM, HTVanillaMaterialType.QUARTZ),
                ingredientHelper.item(Items.REPEATER),
            ).save(output)
    }

    @JvmStatic
    private fun diode() {
        // LED
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumItems.LED, 4),
                ingredientHelper.item(HTItemMaterialVariant.INGOT, HTVanillaMaterialType.COPPER),
                ingredientHelper.item(RagiumItems.LUMINOUS_PASTE),
            ).save(output)
        // LED Block
        HTShapedRecipeBuilder
            .building(RagiumBlocks.getLedBlock(HTColorMaterial.WHITE), 8)
            .hollow8()
            .define('A', Tags.Items.GLASS_BLOCKS)
            .define('B', RagiumItems.LED)
            .saveSuffixed(output, "_from_led")

        for ((color: HTColorMaterial, block: ItemLike) in RagiumBlocks.LED_BLOCKS) {
            HTShapedRecipeBuilder
                .building(block, 8)
                .hollow8()
                .define('A', RagiumModTags.Items.LED_BLOCKS)
                .define('B', color.dyeTag)
                .save(output)
        }

        // Solar Panel
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumItems.SOLAR_PANEL),
                ingredientHelper.item(RagiumModTags.Items.PLASTICS),
                ingredientHelper.item(RagiumItems.LUMINOUS_PASTE, 2),
            ).save(output)
    }
}
