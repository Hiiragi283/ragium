package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.material.HTBlockMaterialVariant
import hiiragi283.ragium.api.material.HTItemMaterialVariant
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.HTCommonMaterialTypes
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.variant.RagiumMaterialVariants
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumMaterialRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        raginite()
        azure()
        eldritch()
        deepSteel()
        miscMaterials()

        material()
        oreToRaw()
        alloying()
    }

    @JvmStatic
    private fun compound(material: HTMaterialType): ItemLike = RagiumItems.getMaterial(RagiumMaterialVariants.COMPOUND, material)

    @JvmStatic
    private fun raginite() {
        // Ragi-Alloy
        HTShapedRecipeBuilder(compound(RagiumMaterialType.RAGI_ALLOY))
            .hollow4()
            .define('A', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('B', HTItemMaterialVariant.INGOT, HTVanillaMaterialType.COPPER)
            .save(output)

        HTCookingRecipeBuilder
            .blasting(RagiumItems.getIngot(RagiumMaterialType.RAGI_ALLOY))
            .addIngredient(compound(RagiumMaterialType.RAGI_ALLOY))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTItemMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.COPPER),
                HTIngredientHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE, 2),
            ).save(output)

        HTShapedRecipeBuilder(RagiumItems.RAGI_COKE)
            .hollow4()
            .define('A', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('B', HTItemMaterialVariant.FUEL, HTVanillaMaterialType.COAL)
            .save(output)
        // Advanced Ragi-Alloy
        HTShapedRecipeBuilder(compound(RagiumMaterialType.ADVANCED_RAGI_ALLOY))
            .cross8()
            .define('A', Tags.Items.DUSTS_GLOWSTONE)
            .define('B', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('C', HTItemMaterialVariant.INGOT, HTVanillaMaterialType.GOLD)
            .save(output)

        HTCookingRecipeBuilder
            .blasting(RagiumItems.getIngot(RagiumMaterialType.ADVANCED_RAGI_ALLOY))
            .addIngredient(compound(RagiumMaterialType.ADVANCED_RAGI_ALLOY))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTItemMaterialVariant.INGOT, RagiumMaterialType.ADVANCED_RAGI_ALLOY),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.GOLD),
                HTIngredientHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE, 4),
            ).save(output)
        // Ragi-Crystal
        HTShapedRecipeBuilder(RagiumItems.getGem(RagiumMaterialType.RAGI_CRYSTAL))
            .hollow8()
            .define('A', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('B', HTItemMaterialVariant.GEM, HTVanillaMaterialType.DIAMOND)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTItemMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL),
                HTIngredientHelper.gemOrDust(HTVanillaMaterialType.DIAMOND),
                HTIngredientHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE, 6),
            ).save(output)
    }

    @JvmStatic
    private fun azure() {
        // Azure Shard
        HTShapedRecipeBuilder(RagiumItems.getGem(RagiumMaterialType.AZURE), 2)
            .mosaic4()
            .define('A', Tags.Items.GEMS_AMETHYST)
            .define('B', Tags.Items.GEMS_LAPIS)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTItemMaterialVariant.GEM, RagiumMaterialType.AZURE, 2),
                HTIngredientHelper.gemOrDust(HTVanillaMaterialType.AMETHYST),
                HTIngredientHelper.gemOrDust(HTVanillaMaterialType.LAPIS),
            ).save(output)
        // Azure Steel
        HTShapedRecipeBuilder(compound(RagiumMaterialType.AZURE_STEEL))
            .hollow4()
            .define('A', HTItemMaterialVariant.GEM, RagiumMaterialType.AZURE)
            .define('B', HTItemMaterialVariant.INGOT, HTVanillaMaterialType.IRON)
            .save(output)

        HTCookingRecipeBuilder
            .blasting(RagiumItems.getIngot(RagiumMaterialType.AZURE_STEEL))
            .addIngredient(compound(RagiumMaterialType.AZURE_STEEL))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.IRON),
                HTIngredientHelper.gemOrDust(RagiumMaterialType.AZURE, 2),
            ).save(output)
        // Azure Silicon
        HTCookingRecipeBuilder
            .blasting(RagiumItems.SILICON)
            .addIngredient(gemOrDust(RagiumMaterialType.AZURE))
            .setExp(0.7f)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.SILICON, 2),
                HTIngredientHelper.gemOrDust(RagiumMaterialType.AZURE),
                HTIngredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC),
            ).save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.SILICON, 4),
                HTIngredientHelper.gemOrDust(RagiumMaterialType.AZURE),
                HTIngredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED),
            ).saveSuffixed(output, "_alt")
    }

    @JvmStatic
    private fun eldritch() {
        // Eldritch Pearl
        HTShapedRecipeBuilder(RagiumItems.getGem(RagiumMaterialType.ELDRITCH_PEARL))
            .cross4()
            .define('A', HTItemMaterialVariant.GEM, RagiumMaterialType.CRIMSON_CRYSTAL)
            .define('B', HTItemMaterialVariant.GEM, RagiumMaterialType.WARPED_CRYSTAL)
            .define('C', RagiumModTags.Items.ELDRITCH_PEARL_BINDER)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTItemMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL, 9),
                HTIngredientHelper.item(HTBlockMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.CRIMSON_CRYSTAL),
                HTIngredientHelper.item(HTBlockMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.WARPED_CRYSTAL),
                HTIngredientHelper.item(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, 3),
            ).save(output)
    }

    @JvmStatic
    private fun deepSteel() {
        // Deep Steel
        HTCookingRecipeBuilder
            .blasting(RagiumItems.DEEP_SCRAP)
            .addIngredient(RagiumCommonTags.Items.ORES_DEEP_SCRAP)
            .save(output)

        HTShapelessRecipeBuilder(RagiumItems.getIngot(RagiumMaterialType.DEEP_STEEL))
            .addIngredient(RagiumItems.DEEP_SCRAP)
            .addIngredient(RagiumItems.DEEP_SCRAP)
            .addIngredient(RagiumItems.DEEP_SCRAP)
            .addIngredient(RagiumItems.DEEP_SCRAP)
            .addIngredient(HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .addIngredient(HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .addIngredient(HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .addIngredient(HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .save(output)

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(RagiumCommonTags.Items.ORES_DEEP_SCRAP))
            .addResult(HTResultHelper.item(RagiumItems.DEEP_SCRAP, 2))
            .saveSuffixed(output, "_from_ore")
    }

    @JvmStatic
    private fun miscMaterials() {
        // Sawdust
        HTShapedRecipeBuilder(RagiumItems.COMPRESSED_SAWDUST)
            .hollow8()
            .define('A', HTItemMaterialVariant.DUST, HTVanillaMaterialType.WOOD)
            .define('B', RagiumItems.getDust(HTVanillaMaterialType.WOOD))
            .save(output)

        HTCookingRecipeBuilder
            .blasting(Items.CHARCOAL, onlyBlasting = true)
            .addIngredient(RagiumItems.COMPRESSED_SAWDUST)
            .setExp(0.15f)
            .saveSuffixed(output, "_from_pellet")
        // Iridescentium
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTItemMaterialVariant.INGOT, RagiumMaterialType.IRIDESCENTIUM),
                HTIngredientHelper.gemOrDust(RagiumMaterialType.ELDRITCH_PEARL, 8),
                HTIngredientHelper.item(Tags.Items.NETHER_STARS),
            ).save(output)
        // Other
        HTShapelessRecipeBuilder(Items.GUNPOWDER, 3)
            .addIngredient(HTItemMaterialVariant.DUST, RagiumMaterialType.SULFUR)
            .addIngredient(HTItemMaterialVariant.DUST, RagiumMaterialType.SALTPETER)
            .addIngredient(fuelOrDust(HTVanillaMaterialType.CHARCOAL))
            .saveSuffixed(output, "_with_hammer")
    }

    @JvmStatic
    private fun material() {
        for (material: RagiumMaterialType in RagiumMaterialType.entries) {
            val baseVariant: HTMaterialVariant.ItemTag = RagiumAPI.getInstance().getBaseVariant(material) ?: continue
            val base: ItemLike = RagiumItems.MATERIALS.get(baseVariant, material) ?: continue

            RagiumBlocks.MATERIALS.get(HTBlockMaterialVariant.STORAGE_BLOCK, material)?.let { storage: ItemLike ->
                // Block -> Base
                HTShapelessRecipeBuilder(base, 9)
                    .addIngredient(HTBlockMaterialVariant.STORAGE_BLOCK, material)
                    .saveSuffixed(output, "_from_block")
                // Base -> Block
                HTShapedRecipeBuilder(storage)
                    .hollow8()
                    .define('A', baseVariant, material)
                    .define('B', base)
                    .saveSuffixed(output, "_from_base")
            }

            RagiumItems.MATERIALS.get(HTItemMaterialVariant.NUGGET, material)?.let { nugget: ItemLike ->
                // Base -> Nugget
                HTShapelessRecipeBuilder(nugget, 9)
                    .addIngredient(baseVariant, material)
                    .saveSuffixed(output, "_from_base")
                // Nugget -> Base
                HTShapedRecipeBuilder(base)
                    .hollow8()
                    .define('A', HTItemMaterialVariant.NUGGET, material)
                    .define('B', nugget)
                    .saveSuffixed(output, "_from_nugget")
            }
        }

        rawToIngot(HTVanillaMaterialType.COPPER)
        rawToIngot(HTVanillaMaterialType.IRON)
        rawToIngot(HTVanillaMaterialType.GOLD)

        HTCommonMaterialTypes.METALS.values.forEach(::rawToIngot)
    }

    @JvmStatic
    private fun oreToRaw() {
        // Coal
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(HTBlockMaterialVariant.ORE, HTVanillaMaterialType.COAL))
            .addResult(HTResultHelper.item(HTItemMaterialVariant.FUEL, HTVanillaMaterialType.COAL, 2))
            .addResult(HTResultHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.SULFUR), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Copper
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(HTBlockMaterialVariant.ORE, HTVanillaMaterialType.COPPER))
            .addResult(HTResultHelper.item(HTItemMaterialVariant.RAW_MATERIAL, HTVanillaMaterialType.COPPER, 4))
            .addResult(HTResultHelper.item(HTItemMaterialVariant.RAW_MATERIAL, HTVanillaMaterialType.GOLD), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Iron
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(HTBlockMaterialVariant.ORE, HTVanillaMaterialType.IRON))
            .addResult(HTResultHelper.item(HTItemMaterialVariant.RAW_MATERIAL, HTVanillaMaterialType.IRON, 2))
            .addResult(HTResultHelper.item(Items.FLINT), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Gold
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(HTBlockMaterialVariant.ORE, HTVanillaMaterialType.GOLD))
            .addResult(HTResultHelper.item(HTItemMaterialVariant.RAW_MATERIAL, HTVanillaMaterialType.GOLD, 2))
            .addResult(HTResultHelper.item(HTItemMaterialVariant.RAW_MATERIAL, HTVanillaMaterialType.COPPER), 1 / 4f)
            .saveSuffixed(output, "_from_ore")

        // Redstone
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(HTBlockMaterialVariant.ORE, HTVanillaMaterialType.REDSTONE))
            .addResult(HTResultHelper.item(HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE, 8))
            .addResult(HTResultHelper.item(HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE, 4), 1 / 2f)
            .addResult(HTResultHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.CINNABAR, 4), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Raginite
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(HTBlockMaterialVariant.ORE, RagiumMaterialType.RAGINITE))
            .addResult(HTResultHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE, 8))
            .addResult(HTResultHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE, 4), 1 / 2f)
            .addResult(HTResultHelper.item(HTItemMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL, 1), 1 / 4f)
            .saveSuffixed(output, "_from_ore")

        // Gems
        mapOf(
            // Vanilla
            HTVanillaMaterialType.LAPIS to 8,
            HTVanillaMaterialType.QUARTZ to 4,
            HTVanillaMaterialType.DIAMOND to 2,
            HTVanillaMaterialType.EMERALD to 2,
            // Ragium
            RagiumMaterialType.RAGI_CRYSTAL to 2,
            RagiumMaterialType.CRIMSON_CRYSTAL to 2,
            RagiumMaterialType.WARPED_CRYSTAL to 2,
        ).forEach { (material: HTMaterialType, count: Int) ->
            HTItemToChancedItemRecipeBuilder
                .crushing(HTIngredientHelper.item(HTBlockMaterialVariant.ORE, material))
                .addResult(HTResultHelper.item(HTItemMaterialVariant.GEM, material, count))
                .saveSuffixed(output, "_from_ore")
        }

        // Netherite
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.ORES_NETHERITE_SCRAP))
            .addResult(HTResultHelper.item(Items.NETHERITE_SCRAP, 2))
            .saveSuffixed(output, "_from_ore")
    }

    @JvmStatic
    @Suppress("DEPRECATION")
    private fun alloying() {
        // Netherite
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(Tags.Items.INGOTS_NETHERITE, 2),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.GOLD, 4),
                HTIngredientHelper.item(Items.NETHERITE_SCRAP, 4),
            ).save(output)
        // Deep Steel
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTItemMaterialVariant.INGOT, RagiumMaterialType.DEEP_STEEL, 2),
                HTIngredientHelper.ingotOrDust(RagiumMaterialType.AZURE_STEEL, 4),
                HTIngredientHelper.item(RagiumItems.DEEP_SCRAP, 4),
            ).save(output)

        // Steel
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("steel")),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.IRON),
                HTIngredientHelper.fuelOrDust(HTVanillaMaterialType.COAL, 2),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("steel"))
            .saveSuffixed(output, "_from_coal")

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("steel")),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.IRON),
                HTIngredientHelper.fuelOrDust(RagiumMaterialType.COAL_COKE),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("steel"))
            .saveSuffixed(output, "_from_coke")
        // Invar
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("invar"), 3),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.IRON, 2),
                HTIngredientHelper.ingotOrDust("nickel"),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("invar"))
            .save(output)
        // Electrum
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("electrum"), 2),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.GOLD),
                HTIngredientHelper.ingotOrDust("silver"),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("electrum"))
            .save(output)
        // Bronze
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("bronze"), 4),
                HTIngredientHelper.item(Tags.Items.INGOTS_COPPER, 3),
                HTIngredientHelper.ingotOrDust("tin"),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("bronze"))
            .save(output)
        // Brass
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("brass"), 4),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.COPPER, 3),
                HTIngredientHelper.ingotOrDust("zinc"),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("brass"))
            .save(output)
        // Constantan
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("constantan"), 2),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.COPPER),
                HTIngredientHelper.ingotOrDust("nickel"),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("constantan"))
            .save(output)

        // Adamant
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("adamant"), 2),
                HTIngredientHelper.ingotOrDust("nickel"),
                HTIngredientHelper.gemOrDust(HTVanillaMaterialType.DIAMOND),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("adamant"))
            .save(output)
        // Duratium
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("duratium"), 2),
                HTIngredientHelper.ingotOrDust("platinum"),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.NETHERITE),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("duratium"))
            .save(output)
        // Energite
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("energite"), 2),
                HTIngredientHelper.ingotOrDust("nickel"),
                HTIngredientHelper.gemOrDust("fluxite"),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("energite"))
            .save(output)
    }
}
