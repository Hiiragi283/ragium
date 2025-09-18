package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.HTBlockMaterialVariant
import hiiragi283.ragium.common.material.HTCommonMaterialTypes
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.impl.data.recipe.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
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
    private fun raginite() {
        // Ragi-Alloy
        HTShapedRecipeBuilder
            .misc(RagiumItems.RAGI_ALLOY_COMPOUND)
            .hollow4()
            .define('A', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('B', HTItemMaterialVariant.INGOT, HTVanillaMaterialType.COPPER)
            .save(output)

        HTCookingRecipeBuilder
            .blasting(RagiumItems.getIngot(RagiumMaterialType.RAGI_ALLOY))
            .addIngredient(RagiumItems.RAGI_ALLOY_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(HTItemMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY),
                ingredientHelper.ingotOrDust(HTVanillaMaterialType.COPPER),
                ingredientHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE, 2),
            ).save(output)

        HTShapedRecipeBuilder
            .misc(RagiumItems.RAGI_COKE)
            .hollow4()
            .define('A', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('B', HTItemMaterialVariant.FUEL, HTVanillaMaterialType.COAL)
            .save(output)
        // Advanced Ragi-Alloy
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(HTItemMaterialVariant.INGOT, RagiumMaterialType.ADVANCED_RAGI_ALLOY),
                ingredientHelper.ingotOrDust(HTVanillaMaterialType.GOLD),
                ingredientHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE, 4),
            ).save(output)
        // Ragi-Crystal
        HTShapedRecipeBuilder
            .misc(RagiumItems.getGem(RagiumMaterialType.RAGI_CRYSTAL))
            .hollow8()
            .define('A', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('B', HTItemMaterialVariant.GEM, HTVanillaMaterialType.DIAMOND)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(HTItemMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL),
                ingredientHelper.gemOrDust(HTVanillaMaterialType.DIAMOND),
                ingredientHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE, 6),
            ).save(output)
    }

    @JvmStatic
    private fun azure() {
        // Azure Shard
        HTShapedRecipeBuilder
            .misc(RagiumItems.getGem(RagiumMaterialType.AZURE), 2)
            .mosaic4()
            .define('A', Tags.Items.GEMS_AMETHYST)
            .define('B', Tags.Items.GEMS_LAPIS)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(HTItemMaterialVariant.GEM, RagiumMaterialType.AZURE, 2),
                ingredientHelper.gemOrDust(HTVanillaMaterialType.AMETHYST),
                ingredientHelper.gemOrDust(HTVanillaMaterialType.LAPIS),
            ).save(output)
        // Azure Steel
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL),
                ingredientHelper.ingotOrDust(HTVanillaMaterialType.IRON),
                ingredientHelper.gemOrDust(RagiumMaterialType.AZURE, 2),
            ).save(output)
    }

    @JvmStatic
    private fun eldritch() {
        // Eldritch Pearl
        HTShapedRecipeBuilder
            .misc(RagiumItems.getGem(RagiumMaterialType.ELDRITCH_PEARL))
            .cross4()
            .define('A', HTItemMaterialVariant.GEM, RagiumMaterialType.CRIMSON_CRYSTAL)
            .define('B', HTItemMaterialVariant.GEM, RagiumMaterialType.WARPED_CRYSTAL)
            .define('C', RagiumModTags.Items.ELDRITCH_PEARL_BINDER)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(HTItemMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL, 9),
                ingredientHelper.item(HTBlockMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.CRIMSON_CRYSTAL),
                ingredientHelper.item(HTBlockMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.WARPED_CRYSTAL),
                ingredientHelper.item(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, 3),
            ).save(output)
    }

    @JvmStatic
    private fun deepSteel() {
        // Deep Steel
        HTCookingRecipeBuilder
            .blasting(RagiumItems.DEEP_SCRAP)
            .addIngredient(RagiumCommonTags.Items.ORES_DEEP_SCRAP)
            .save(output)

        HTShapelessRecipeBuilder
            .misc(RagiumItems.getIngot(RagiumMaterialType.DEEP_STEEL))
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
            .crushing(ingredientHelper.item(RagiumCommonTags.Items.ORES_DEEP_SCRAP))
            .addResult(resultHelper.item(RagiumItems.DEEP_SCRAP, 2))
            .saveSuffixed(output, "_from_ore")
    }

    @JvmStatic
    private fun miscMaterials() {
        // Sawdust
        HTShapedRecipeBuilder
            .misc(RagiumItems.COMPRESSED_SAWDUST)
            .hollow8()
            .define('A', HTItemMaterialVariant.DUST, HTVanillaMaterialType.WOOD)
            .define('B', RagiumItems.getDust(HTVanillaMaterialType.WOOD))
            .save(output)

        HTCookingRecipeBuilder
            .blasting(Items.CHARCOAL, onlyBlasting = true)
            .addIngredient(RagiumItems.COMPRESSED_SAWDUST)
            .setExp(0.15f)
            .saveSuffixed(output, "_from_pellet")
        // Gildium
        HTItemToObjRecipeBuilder
            .melting(
                ingredientHelper.item(Items.GILDED_BLACKSTONE),
                resultHelper.fluid(RagiumFluidContents.GILDED_LAVA, 1000),
            ).save(output)

        HTFluidTransformRecipeBuilder
            .refining(
                ingredientHelper.fluid(RagiumFluidContents.GILDED_LAVA, 1000),
                resultHelper.fluid(HTFluidContent.LAVA, 750),
                null,
                resultHelper.item(HTItemMaterialVariant.NUGGET, RagiumMaterialType.GILDIUM),
            ).save(output)

        HTFluidTransformRecipeBuilder
            .refining(
                ingredientHelper.fluid(RagiumFluidContents.GILDED_LAVA, 1000),
                resultHelper.fluid(HTFluidContent.LAVA, 750),
                ingredientHelper.item(RagiumItems.PLATING_CATALYST),
                resultHelper.item(HTItemMaterialVariant.NUGGET, RagiumMaterialType.GILDIUM, 3),
            ).saveSuffixed(output, "_alt")
        // Iridescentium
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(HTItemMaterialVariant.INGOT, RagiumMaterialType.IRIDESCENTIUM),
                ingredientHelper.gemOrDust(RagiumMaterialType.ELDRITCH_PEARL, 8),
                ingredientHelper.item(Tags.Items.NETHER_STARS),
            ).save(output)
        // Other
        HTShapelessRecipeBuilder
            .misc(Items.GUNPOWDER, 3)
            .addIngredient(HTItemMaterialVariant.DUST, RagiumMaterialType.SULFUR)
            .addIngredient(HTItemMaterialVariant.DUST, RagiumMaterialType.SALTPETER)
            .addIngredient(fuelOrDust(HTVanillaMaterialType.CHARCOAL))
            .saveSuffixed(output, "_with_hammer")

        HTCookingRecipeBuilder
            .smelting(RagiumItems.getMaterial(HTItemMaterialVariant.FUEL, RagiumMaterialType.BAMBOO_CHARCOAL))
            .addIngredient(Items.BAMBOO)
            .setExp(0.15f)
            .save(output)
    }

    @JvmStatic
    private fun material() {
        for (material: RagiumMaterialType in RagiumMaterialType.entries) {
            val baseVariant: HTMaterialVariant.ItemTag = RagiumAPI.INSTANCE.getBaseVariant(material) ?: continue
            val base: ItemLike = RagiumItems.MATERIALS.get(baseVariant, material) ?: continue

            RagiumBlocks.MATERIALS.get(HTBlockMaterialVariant.STORAGE_BLOCK, material)?.let { storage: ItemLike ->
                // Block -> Base
                HTShapelessRecipeBuilder
                    .misc(base, 9)
                    .addIngredient(HTBlockMaterialVariant.STORAGE_BLOCK, material)
                    .saveSuffixed(output, "_from_block")
                // Base -> Block
                HTShapedRecipeBuilder
                    .building(storage)
                    .hollow8()
                    .define('A', baseVariant, material)
                    .define('B', base)
                    .saveSuffixed(output, "_from_base")
            }

            RagiumItems.MATERIALS.get(HTItemMaterialVariant.NUGGET, material)?.let { nugget: ItemLike ->
                // Base -> Nugget
                HTShapelessRecipeBuilder
                    .misc(nugget, 9)
                    .addIngredient(baseVariant, material)
                    .saveSuffixed(output, "_from_base")
                // Nugget -> Base
                HTShapedRecipeBuilder
                    .misc(base)
                    .hollow8()
                    .define('A', HTItemMaterialVariant.NUGGET, material)
                    .define('B', nugget)
                    .saveSuffixed(output, "_from_nugget")
            }

            gemToChip(material)
        }

        rawToIngot(HTVanillaMaterialType.COPPER)
        rawToIngot(HTVanillaMaterialType.IRON)
        rawToIngot(HTVanillaMaterialType.GOLD)

        gemToChip(HTVanillaMaterialType.ECHO)

        HTCommonMaterialTypes.METALS.values.forEach(::rawToIngot)
    }

    @JvmStatic
    private fun gemToChip(material: HTMaterialType) {
        RagiumItems.MATERIALS.get(HTItemMaterialVariant.CHIP, material)?.let { chip: ItemLike ->
            // 3x Gem -> Chip
            HTItemToObjRecipeBuilder
                .compressing(
                    ingredientHelper.item(HTItemMaterialVariant.GEM, material, 3),
                    resultHelper.item(chip),
                ).save(output)
        }
    }

    @JvmStatic
    private fun oreToRaw() {
        // Coal
        HTItemToChancedItemRecipeBuilder
            .crushing(ingredientHelper.item(HTBlockMaterialVariant.ORE, HTVanillaMaterialType.COAL))
            .addResult(resultHelper.item(HTItemMaterialVariant.FUEL, HTVanillaMaterialType.COAL, 2))
            .addResult(resultHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.SULFUR), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Copper
        HTItemToChancedItemRecipeBuilder
            .crushing(ingredientHelper.item(HTBlockMaterialVariant.ORE, HTVanillaMaterialType.COPPER))
            .addResult(resultHelper.item(HTItemMaterialVariant.RAW_MATERIAL, HTVanillaMaterialType.COPPER, 4))
            .addResult(resultHelper.item(HTItemMaterialVariant.RAW_MATERIAL, HTVanillaMaterialType.GOLD), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Iron
        HTItemToChancedItemRecipeBuilder
            .crushing(ingredientHelper.item(HTBlockMaterialVariant.ORE, HTVanillaMaterialType.IRON))
            .addResult(resultHelper.item(HTItemMaterialVariant.RAW_MATERIAL, HTVanillaMaterialType.IRON, 2))
            .addResult(resultHelper.item(Items.FLINT), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Gold
        HTItemToChancedItemRecipeBuilder
            .crushing(ingredientHelper.item(HTBlockMaterialVariant.ORE, HTVanillaMaterialType.GOLD))
            .addResult(resultHelper.item(HTItemMaterialVariant.RAW_MATERIAL, HTVanillaMaterialType.GOLD, 2))
            .addResult(resultHelper.item(HTItemMaterialVariant.RAW_MATERIAL, HTVanillaMaterialType.COPPER), 1 / 4f)
            .saveSuffixed(output, "_from_ore")

        // Redstone
        HTItemToChancedItemRecipeBuilder
            .crushing(ingredientHelper.item(HTBlockMaterialVariant.ORE, HTVanillaMaterialType.REDSTONE))
            .addResult(resultHelper.item(HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE, 8))
            .addResult(resultHelper.item(HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE, 4), 1 / 2f)
            .addResult(resultHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.CINNABAR, 4), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Raginite
        HTItemToChancedItemRecipeBuilder
            .crushing(ingredientHelper.item(HTBlockMaterialVariant.ORE, RagiumMaterialType.RAGINITE))
            .addResult(resultHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE, 8))
            .addResult(resultHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE, 4), 1 / 2f)
            .addResult(resultHelper.item(HTItemMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL, 1), 1 / 4f)
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
                .crushing(ingredientHelper.item(HTBlockMaterialVariant.ORE, material))
                .addResult(resultHelper.item(HTItemMaterialVariant.GEM, material, count))
                .saveSuffixed(output, "_from_ore")
        }

        // Netherite
        HTItemToChancedItemRecipeBuilder
            .crushing(ingredientHelper.item(Tags.Items.ORES_NETHERITE_SCRAP))
            .addResult(resultHelper.item(Items.NETHERITE_SCRAP, 2))
            .saveSuffixed(output, "_from_ore")
    }

    @JvmStatic
    @Suppress("DEPRECATION")
    private fun alloying() {
        // Netherite
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(Tags.Items.INGOTS_NETHERITE, 2),
                ingredientHelper.ingotOrDust(HTVanillaMaterialType.GOLD, 4),
                ingredientHelper.item(Items.NETHERITE_SCRAP, 4),
            ).save(output)
        // Deep Steel
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(HTItemMaterialVariant.INGOT, RagiumMaterialType.DEEP_STEEL, 2),
                ingredientHelper.ingotOrDust(RagiumMaterialType.AZURE_STEEL, 4),
                ingredientHelper.item(RagiumItems.DEEP_SCRAP, 4),
            ).save(output)

        // Steel
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("steel")),
                ingredientHelper.ingotOrDust(HTVanillaMaterialType.IRON),
                ingredientHelper.fuelOrDust(HTVanillaMaterialType.COAL, 2),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("steel"))
            .saveSuffixed(output, "_from_coal")

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("steel")),
                ingredientHelper.ingotOrDust(HTVanillaMaterialType.IRON),
                ingredientHelper.fuelOrDust(RagiumMaterialType.COAL_COKE),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("steel"))
            .saveSuffixed(output, "_from_coke")
        // Invar
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("invar"), 3),
                ingredientHelper.ingotOrDust(HTVanillaMaterialType.IRON, 2),
                ingredientHelper.ingotOrDust("nickel"),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("invar"))
            .save(output)
        // Electrum
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("electrum"), 2),
                ingredientHelper.ingotOrDust(HTVanillaMaterialType.GOLD),
                ingredientHelper.ingotOrDust("silver"),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("electrum"))
            .save(output)
        // Bronze
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("bronze"), 4),
                ingredientHelper.item(Tags.Items.INGOTS_COPPER, 3),
                ingredientHelper.ingotOrDust("tin"),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("bronze"))
            .save(output)
        // Brass
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("brass"), 4),
                ingredientHelper.ingotOrDust(HTVanillaMaterialType.COPPER, 3),
                ingredientHelper.ingotOrDust("zinc"),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("brass"))
            .save(output)
        // Constantan
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("constantan"), 2),
                ingredientHelper.ingotOrDust(HTVanillaMaterialType.COPPER),
                ingredientHelper.ingotOrDust("nickel"),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("constantan"))
            .save(output)

        // Adamant
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("adamant"), 2),
                ingredientHelper.ingotOrDust("nickel"),
                ingredientHelper.gemOrDust(HTVanillaMaterialType.DIAMOND),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("adamant"))
            .save(output)
        // Duratium
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("duratium"), 2),
                ingredientHelper.ingotOrDust("platinum"),
                ingredientHelper.ingotOrDust(HTVanillaMaterialType.NETHERITE),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("duratium"))
            .save(output)
        // Energite
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("energite"), 2),
                ingredientHelper.ingotOrDust("nickel"),
                ingredientHelper.gemOrDust("fluxite"),
            ).tagCondition(HTItemMaterialVariant.INGOT, HTCommonMaterialTypes.getAlloy("energite"))
            .save(output)
    }
}
