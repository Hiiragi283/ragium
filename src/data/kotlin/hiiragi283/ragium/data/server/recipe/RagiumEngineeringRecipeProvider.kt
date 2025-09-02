package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.material.HTBlockMaterialVariant
import hiiragi283.ragium.api.material.HTItemMaterialVariant
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredBlock
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.HTTierType
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.recipe.HTEternalTicketRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.variant.HTColorMaterial
import hiiragi283.ragium.util.variant.RagiumMaterialVariants
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumEngineeringRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Polymer Catalyst
        HTShapedRecipeBuilder(RagiumItems.POLYMER_CATALYST)
            .cross8()
            .define('A', Tags.Items.RODS_BREEZE)
            .define('B', HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('C', Items.IRON_BARS)
            .save(output)
        // Plastic Plate
        HTItemToObjRecipeBuilder
            .compressing(
                HTIngredientHelper.item(RagiumModTags.Items.POLYMER_RESIN),
                HTResultHelper.item(HTItemMaterialVariant.PLATE, RagiumMaterialType.PLASTIC),
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

    @JvmStatic
    private fun circuits() {
        fun circuit(tier: HTTierType): HTDeferredItem<*> = RagiumItems.getMaterial(HTItemMaterialVariant.CIRCUIT, tier)
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
            ).save(output)
        // Basic
        HTShapedRecipeBuilder(circuit(HTTierType.BASIC))
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', HTItemMaterialVariant.INGOT, HTVanillaMaterialType.COPPER)
            .define('B', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('C', HTItemMaterialVariant.INGOT, HTVanillaMaterialType.IRON)
            .save(output)

        HTShapedRecipeBuilder(circuit(HTTierType.BASIC), 2)
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
                HTResultHelper.item(circuit(HTTierType.BASIC), 4),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.COPPER, 2),
                HTIngredientHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE),
                HTIngredientHelper.item(RagiumItems.CIRCUIT_BOARD),
            ).save(output)
        // Advanced
        HTShapedRecipeBuilder(circuit(HTTierType.ADVANCED))
            .crossLayered()
            .define('A', Tags.Items.DUSTS_GLOWSTONE)
            .define('B', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('C', gemOrDust(HTVanillaMaterialType.LAPIS))
            .define('D', HTItemMaterialVariant.CIRCUIT, HTTierType.BASIC)
            .saveSuffixed(output, "_from_basic")

        HTShapedRecipeBuilder(circuit(HTTierType.ADVANCED))
            .cross8()
            .define('A', gemOrDust(RagiumMaterialType.AZURE))
            .define('B', HTItemMaterialVariant.INGOT, HTVanillaMaterialType.GOLD)
            .define('C', RagiumItems.CIRCUIT_BOARD)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(circuit(HTTierType.ADVANCED), 2),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.GOLD, 2),
                HTIngredientHelper.gemOrDust(RagiumMaterialType.AZURE),
                HTIngredientHelper.item(RagiumItems.CIRCUIT_BOARD),
            ).save(output)
        // Elite
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(circuit(HTTierType.ELITE)),
                HTIngredientHelper.ingotOrDust(RagiumMaterialType.ADVANCED_RAGI_ALLOY, 2),
                HTIngredientHelper.gemOrDust(RagiumMaterialType.RAGI_CRYSTAL),
                HTIngredientHelper.item(RagiumItems.ADVANCED_CIRCUIT_BOARD),
            ).save(output)
        // Ultimate
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(circuit(HTTierType.ULTIMATE)),
                HTIngredientHelper.item(HTItemMaterialVariant.NUGGET, RagiumMaterialType.IRIDESCENTIUM),
                HTIngredientHelper.gemOrDust(RagiumMaterialType.ELDRITCH_PEARL),
                HTIngredientHelper.item(RagiumItems.ADVANCED_CIRCUIT_BOARD),
            ).save(output)
    }

    @JvmStatic
    private fun components() {
        // Coil
        fun registerCoil(material: HTMaterialType, core: HTMaterialType) {
            // Shaped
            HTShapedRecipeBuilder(RagiumItems.getMaterial(RagiumMaterialVariants.COIL, material), 4)
                .hollow4()
                .define('A', HTItemMaterialVariant.INGOT, material)
                .define('B', ingotOrRod(core))
                .save(output)
        }
        registerCoil(RagiumMaterialType.RAGI_ALLOY, HTVanillaMaterialType.IRON)
        registerCoil(RagiumMaterialType.ADVANCED_RAGI_ALLOY, RagiumMaterialType.AZURE_STEEL)

        // Component
        val basic: ItemLike = RagiumItems.getMaterial(RagiumMaterialVariants.COMPONENT, HTTierType.BASIC)
        HTShapedRecipeBuilder(basic)
            .crossLayered()
            .define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)
            .define('B', Tags.Items.GLASS_BLOCKS_COLORLESS)
            .define('C', HTItemMaterialVariant.CIRCUIT, HTTierType.BASIC)
            .define('D', HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE)
            .save(output)

        val adv: ItemLike = RagiumItems.getMaterial(RagiumMaterialVariants.COMPONENT, HTTierType.ADVANCED)
        HTShapedRecipeBuilder(adv)
            .crossLayered()
            .define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.ADVANCED_RAGI_ALLOY)
            .define('B', HTItemMaterialVariant.GEM, HTVanillaMaterialType.QUARTZ)
            .define('C', HTItemMaterialVariant.CIRCUIT, HTTierType.ADVANCED)
            .define('D', basic)
            .save(output)

        val elite: HTDeferredItem<*> = RagiumItems.getMaterial(RagiumMaterialVariants.COMPONENT, HTTierType.ELITE)
        HTShapedRecipeBuilder(elite)
            .crossLayered()
            .define('A', HTItemMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
            .define('B', HTBlockMaterialVariant.GLASS_BLOCK, HTVanillaMaterialType.QUARTZ)
            .define('C', HTItemMaterialVariant.CIRCUIT, HTTierType.ELITE)
            .define('D', adv)
            .save(output)

        val ultimate: ItemLike = RagiumItems.getMaterial(RagiumMaterialVariants.COMPONENT, HTTierType.ULTIMATE)
        HTShapedRecipeBuilder(ultimate)
            .crossLayered()
            .define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.DEEP_STEEL)
            .define('B', HTBlockMaterialVariant.GLASS_BLOCK, HTVanillaMaterialType.OBSIDIAN)
            .define('C', HTItemMaterialVariant.CIRCUIT, HTTierType.ULTIMATE)
            .define('D', elite)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ETERNAL_COMPONENT)
            .cross8()
            .define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.IRIDESCENTIUM)
            .define('B', Items.CLOCK)
            .define('C', ultimate)
            .save(output)
        save(
            RagiumAPI.id("shapeless/eternal_ticket"),
            HTEternalTicketRecipe(CraftingBookCategory.MISC),
        )
    }

    @JvmStatic
    private fun redStones() {
        // Redstone Board
        HTShapedRecipeBuilder(RagiumItems.REDSTONE_BOARD, 4)
            .hollow4()
            .define('A', Items.SMOOTH_STONE_SLAB)
            .define('B', HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.REDSTONE_BOARD, 4),
                HTIngredientHelper.item(HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE),
                HTIngredientHelper.item(Items.SMOOTH_STONE_SLAB),
            ).save(output)
        // Repeater
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(Items.REPEATER, 2),
                HTIngredientHelper.item(Items.REDSTONE_TORCH),
                HTIngredientHelper.item(HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE),
                HTIngredientHelper.item(RagiumItems.REDSTONE_BOARD),
            ).save(output)
        // Comparator
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(Items.COMPARATOR, 2),
                HTIngredientHelper.item(Items.REDSTONE_TORCH),
                HTIngredientHelper.item(HTItemMaterialVariant.GEM, HTVanillaMaterialType.QUARTZ),
                HTIngredientHelper.item(Items.REPEATER),
            ).save(output)
    }

    @JvmStatic
    private fun diode() {
        // LED
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.LED, 4),
                HTIngredientHelper.item(HTItemMaterialVariant.INGOT, HTVanillaMaterialType.COPPER),
                HTIngredientHelper.item(RagiumItems.LUMINOUS_PASTE),
            ).save(output)
        // LED Block
        HTShapedRecipeBuilder(RagiumBlocks.getLedBlock(HTColorMaterial.WHITE), 8)
            .hollow8()
            .define('A', Tags.Items.GLASS_BLOCKS)
            .define('B', RagiumItems.LED)
            .saveSuffixed(output, "_from_led")

        for ((color: HTColorMaterial, block: HTSimpleDeferredBlock) in RagiumBlocks.LED_BLOCKS) {
            HTShapedRecipeBuilder(block, 8)
                .hollow8()
                .define('A', RagiumModTags.Items.LED_BLOCKS)
                .define('B', color.dyeTag)
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
