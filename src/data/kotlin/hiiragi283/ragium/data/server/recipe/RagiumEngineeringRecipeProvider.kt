package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.HTColorMaterial
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.recipe.HTSmithingModifyRecipe
import hiiragi283.ragium.common.tier.HTCircuitTier
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.impl.data.recipe.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
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
                itemCreator.fromTagKey(RagiumModTags.Items.POLYMER_RESIN),
                resultHelper.item(CommonMaterialPrefixes.PLATE, CommonMaterialKeys.PLASTIC),
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
        // Synthetic Book
        HTShapelessRecipeBuilder
            .misc(Items.BOOK)
            .addIngredients(Items.PAPER, count = 3)
            .addIngredient(RagiumItems.SYNTHETIC_LEATHER)
            .saveSuffixed(output, "_from_synthetic")
        // Blaze Rod
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(Items.BLAZE_ROD),
                itemCreator.fromItem(Items.BLAZE_POWDER, 4),
                itemCreator.fromTagKey(Tags.Items.RODS_WOODEN),
            ).save(output)
        // Breeze Rod
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(Items.BREEZE_ROD),
                itemCreator.fromItem(Items.WIND_CHARGE, 6),
                itemCreator.fromTagKey(Tags.Items.RODS_WOODEN),
            ).save(output)

        // Gravity-Unit
        val gravityUnit: HTSimpleDeferredItem = RagiumItems.GRAVITATIONAL_UNIT
        HTShapedRecipeBuilder
            .misc(gravityUnit)
            .cross8()
            .define('A', Items.SHULKER_SHELL)
            .define('B', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.ULTIMATE)
            .define('C', Items.END_CRYSTAL)
            .save(output)
        save(
            gravityUnit.getIdWithPrefix("smithing/"),
            HTSmithingModifyRecipe(
                Ingredient.of(gravityUnit),
                Ingredient.of(),
                DataComponentPatch
                    .builder()
                    .set(RagiumDataComponents.ANTI_GRAVITY, true)
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
        // Molds
        for ((prefix: HTMaterialPrefix, mold: HTSimpleDeferredItem) in RagiumItems.MOLDS) {
            HTShapedRecipeBuilder
                .misc(mold)
                .hollow4()
                .define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
                .define('B', prefix.createCommonTagKey(Registries.ITEM))
                .save(output)
        }
        // Polymer Catalyst
        HTShapedRecipeBuilder
            .misc(RagiumItems.POLYMER_CATALYST)
            .cross8()
            .define('A', Tags.Items.RODS_BREEZE)
            .define('B', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL)
            .define('C', Items.IRON_BARS)
            .save(output)
    }

    @JvmStatic
    private fun circuits() {
        // Circuit Boards
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumItems.CIRCUIT_BOARD, 4),
                itemCreator.fromTagKey(RagiumModTags.Items.PLASTICS),
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.QUARTZ),
            ).save(output)
        // Basic
        HTShapedRecipeBuilder
            .misc(RagiumItems.getCircuit(HTCircuitTier.BASIC))
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.COPPER)
            .define('B', CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .define('C', CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.IRON)
            .save(output)

        HTShapedRecipeBuilder
            .misc(RagiumItems.getCircuit(HTCircuitTier.BASIC), 2)
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.COPPER)
            .define('B', CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .define('C', RagiumItems.CIRCUIT_BOARD)
            .saveSuffixed(output, "_with_plastic")

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumItems.getCircuit(HTCircuitTier.BASIC), 4),
                itemCreator.fromTagKey(CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.COPPER, 2),
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE),
                itemCreator.fromItem(RagiumItems.CIRCUIT_BOARD),
            ).save(output)
        // Advanced
        HTShapedRecipeBuilder
            .misc(RagiumItems.getCircuit(HTCircuitTier.ADVANCED))
            .crossLayered()
            .define('A', Tags.Items.DUSTS_GLOWSTONE)
            .define('B', CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .define('C', CommonMaterialPrefixes.DUST, VanillaMaterialKeys.LAPIS)
            .define('D', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.BASIC)
            .saveSuffixed(output, "_from_basic")

        HTShapedRecipeBuilder
            .misc(RagiumItems.getCircuit(HTCircuitTier.ADVANCED))
            .cross8()
            .define('A', CommonMaterialPrefixes.DUST, RagiumMaterialKeys.AZURE)
            .define('B', CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.GOLD)
            .define('C', RagiumItems.CIRCUIT_BOARD)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumItems.getCircuit(HTCircuitTier.ADVANCED), 2),
                itemCreator.fromTagKey(CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.GOLD, 2),
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.AZURE),
                itemCreator.fromItem(RagiumItems.CIRCUIT_BOARD),
            ).save(output)
        // Elite
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumItems.getCircuit(HTCircuitTier.ELITE)),
                itemCreator.fromTagKey(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL),
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGI_CRYSTAL),
                itemCreator.fromItem(RagiumItems.CIRCUIT_BOARD),
            ).save(output)
        // Ultimate
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumItems.getCircuit(HTCircuitTier.ULTIMATE)),
                itemCreator.fromTagKey(CommonMaterialPrefixes.NUGGET, RagiumMaterialKeys.IRIDESCENTIUM, 3),
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.ECHO),
                itemCreator.fromItem(RagiumItems.CIRCUIT_BOARD),
            ).save(output)
    }

    @JvmStatic
    private fun components() {
        // Coil
        fun registerCoil(material: HTMaterialKey, core: HTMaterialKey) {
            val coil: ItemLike = RagiumItems.getCoil(material)
            // Item
            HTShapedRecipeBuilder
                .misc(coil, 4)
                .hollow4()
                .define('A', CommonMaterialPrefixes.INGOT, material)
                .define('B', CommonMaterialPrefixes.INGOT, core)
                .save(output)
            // Block
            HTShapedRecipeBuilder
                .building(RagiumBlocks.getCoilBlock(material))
                .hollow8()
                .define('A', coil)
                .define('B', CommonMaterialPrefixes.INGOT, core)
                .save(output)
        }
        registerCoil(RagiumMaterialKeys.RAGI_ALLOY, VanillaMaterialKeys.IRON)
        registerCoil(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY, RagiumMaterialKeys.AZURE_STEEL)

        // Component
        val basic: ItemLike = RagiumItems.getComponent(HTComponentTier.BASIC)
        HTShapedRecipeBuilder
            .misc(basic)
            .crossLayered()
            .define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            .define('B', Tags.Items.GLASS_BLOCKS)
            .define('C', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.BASIC)
            .define('D', CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
            .save(output)

        val adv: ItemLike = RagiumItems.getComponent(HTComponentTier.ADVANCED)
        HTShapedRecipeBuilder
            .misc(adv)
            .crossLayered()
            .define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)
            .define('B', CommonMaterialPrefixes.GEM, VanillaMaterialKeys.QUARTZ)
            .define('C', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.ADVANCED)
            .define('D', basic)
            .save(output)

        val elite: ItemLike = RagiumItems.getComponent(HTComponentTier.ELITE)
        HTShapedRecipeBuilder
            .misc(elite)
            .crossLayered()
            .define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL)
            .define('B', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
            .define('C', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.ELITE)
            .define('D', adv)
            .save(output)

        val ultimate: ItemLike = RagiumItems.getComponent(HTComponentTier.ULTIMATE)
        HTShapedRecipeBuilder
            .misc(ultimate)
            .crossLayered()
            .define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.DEEP_STEEL)
            .define('B', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL)
            .define('C', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.ULTIMATE)
            .define('D', elite)
            .save(output)

        val eternal: ItemLike = RagiumItems.getComponent(HTComponentTier.ETERNAL)
        HTShapedRecipeBuilder
            .misc(eternal)
            .cross8()
            .define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.IRIDESCENTIUM)
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
            .define('B', CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumItems.REDSTONE_BOARD, 4),
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE),
                itemCreator.fromItem(Items.SMOOTH_STONE_SLAB),
            ).save(output)
        // Repeater
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(Items.REPEATER, 2),
                itemCreator.fromItem(Items.REDSTONE_TORCH),
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE),
                itemCreator.fromItem(RagiumItems.REDSTONE_BOARD),
            ).save(output)
        // Comparator
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(Items.COMPARATOR, 2),
                itemCreator.fromItem(Items.REDSTONE_TORCH),
                itemCreator.fromTagKey(CommonMaterialPrefixes.GEM, VanillaMaterialKeys.QUARTZ),
                itemCreator.fromItem(Items.REPEATER),
            ).save(output)
    }

    @JvmStatic
    private fun diode() {
        // LED
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumItems.LED, 4),
                itemCreator.fromTagKey(CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.COPPER),
                itemCreator.fromItem(RagiumItems.LUMINOUS_PASTE),
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
                itemCreator.fromTagKey(RagiumModTags.Items.PLASTICS),
                itemCreator.fromItem(RagiumItems.LUMINOUS_PASTE, 2),
            ).save(output)
    }
}
