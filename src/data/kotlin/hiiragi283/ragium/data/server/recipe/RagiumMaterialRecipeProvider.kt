package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.material.HTMaterialRecipeData
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.getDefaultPrefix
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.ModMaterialKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.material.CommonMaterialRecipeData
import hiiragi283.ragium.impl.data.recipe.material.EIOMaterialRecipeData
import hiiragi283.ragium.impl.data.recipe.material.OritechMaterialRecipeData
import hiiragi283.ragium.impl.data.recipe.material.RagiumMaterialRecipeData
import hiiragi283.ragium.impl.data.recipe.material.VanillaMaterialRecipeData
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

object RagiumMaterialRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        raginite()
        azure()
        eldritch()
        deepSteel()
        miscMaterials()

        blockAndNugget()
        oreToRaw()
        alloying()
    }

    @JvmStatic
    private fun raginite() {
        // Ragi-Alloy
        HTShapedRecipeBuilder
            .misc(RagiumItems.RAGI_ALLOY_COMPOUND)
            .hollow4()
            .define('A', CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .define('B', CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.COPPER)
            .save(output)

        HTCookingRecipeBuilder
            .smeltingAndBlasting(RagiumItems.getIngot(RagiumMaterialKeys.RAGI_ALLOY)) {
                addIngredient(RagiumItems.RAGI_ALLOY_COMPOUND)
                setExp(0.7f)
                saveSuffixed(output, "_from_compound")
            }

        alloyFromData(RagiumMaterialRecipeData.RAGI_ALLOY).save(output)

        HTShapedRecipeBuilder
            .misc(RagiumItems.RAGI_COKE)
            .hollow4()
            .define('A', CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .define('B', CommonMaterialPrefixes.FUEL, VanillaMaterialKeys.COAL)
            .save(output)
        // Advanced Ragi-Alloy
        alloyFromData(RagiumMaterialRecipeData.ADVANCED_RAGI_ALLOY).save(output)
        // Ragi-Crystal
        HTShapedRecipeBuilder
            .misc(RagiumItems.getGem(RagiumMaterialKeys.RAGI_CRYSTAL))
            .hollow8()
            .define('A', CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .define('B', CommonMaterialPrefixes.GEM, VanillaMaterialKeys.DIAMOND)
            .save(output)

        alloyFromData(RagiumMaterialRecipeData.RAGI_CRYSTAL).save(output)
    }

    @JvmStatic
    private fun azure() {
        // Azure Shard
        HTShapedRecipeBuilder
            .misc(RagiumItems.getGem(RagiumMaterialKeys.AZURE), 2)
            .mosaic4()
            .define('A', CommonMaterialPrefixes.GEM, VanillaMaterialKeys.AMETHYST)
            .define('B', CommonMaterialPrefixes.GEM, VanillaMaterialKeys.LAPIS)
            .save(output)

        alloyFromData(RagiumMaterialRecipeData.AZURE_SHARD).save(output)
        // Azure Steel
        alloyFromData(RagiumMaterialRecipeData.AZURE_STEEL).save(output)
    }

    @JvmStatic
    private fun eldritch() {
        // Eldritch Pearl
        HTShapedRecipeBuilder
            .misc(RagiumItems.getGem(RagiumMaterialKeys.ELDRITCH_PEARL))
            .cross4()
            .define('A', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.CRIMSON_CRYSTAL)
            .define('B', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.WARPED_CRYSTAL)
            .define('C', RagiumModTags.Items.ELDRITCH_PEARL_BINDER)
            .save(output)

        alloyFromData(RagiumMaterialRecipeData.ELDRITCH_PEARL).save(output)
        alloyFromData(RagiumMaterialRecipeData.ELDRITCH_PEARL_BULK).saveSuffixed(output, "_alt")
    }

    @JvmStatic
    private fun deepSteel() {
        // Deep Steel
        HTCookingRecipeBuilder
            .smeltingAndBlasting(RagiumItems.getScrap(RagiumMaterialKeys.DEEP_STEEL)) {
                addIngredient(RagiumCommonTags.Items.ORES_DEEP_SCRAP)
                save(output)
            }

        HTShapelessRecipeBuilder
            .misc(RagiumItems.getIngot(RagiumMaterialKeys.DEEP_STEEL))
            .addIngredient(CommonMaterialPrefixes.SCRAP, RagiumMaterialKeys.DEEP_STEEL)
            .addIngredient(CommonMaterialPrefixes.SCRAP, RagiumMaterialKeys.DEEP_STEEL)
            .addIngredient(CommonMaterialPrefixes.SCRAP, RagiumMaterialKeys.DEEP_STEEL)
            .addIngredient(CommonMaterialPrefixes.SCRAP, RagiumMaterialKeys.DEEP_STEEL)
            .addIngredient(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL)
            .addIngredient(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL)
            .addIngredient(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL)
            .addIngredient(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL)
            .save(output)

        alloyFromData(RagiumMaterialRecipeData.DEEP_STEEL)
    }

    @JvmStatic
    private fun miscMaterials() {
        // Sawdust
        HTShapedRecipeBuilder
            .misc(RagiumItems.COMPRESSED_SAWDUST)
            .hollow8()
            .define('A', CommonMaterialPrefixes.DUST, VanillaMaterialKeys.WOOD)
            .define('B', RagiumItems.getDust(VanillaMaterialKeys.WOOD))
            .save(output)

        HTCookingRecipeBuilder
            .blasting(Items.CHARCOAL)
            .addIngredient(RagiumItems.COMPRESSED_SAWDUST)
            .setExp(0.15f)
            .saveSuffixed(output, "_from_pellet")

        // Night Metal
        alloyFromData(RagiumMaterialRecipeData.NIGHT_METAL).save(output)
        // Iridescentium
        alloyFromData(RagiumMaterialRecipeData.IRIDESCENTIUM).save(output)
        // Other
        HTShapelessRecipeBuilder
            .misc(Items.GUNPOWDER, 3)
            .addIngredient(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR)
            .addIngredient(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALTPETER)
            .addIngredient(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.CHARCOAL)
            .saveSuffixed(output, "_with_hammer")

        HTCookingRecipeBuilder
            .smelting(RagiumItems.getMaterial(CommonMaterialPrefixes.FUEL, RagiumMaterialKeys.BAMBOO_CHARCOAL))
            .addIngredient(Items.BAMBOO)
            .setExp(0.15f)
            .save(output)
    }

    @JvmStatic
    private fun blockAndNugget() {
        val blockMap: Map<HTMaterialKey, HTSimpleDeferredBlock> = RagiumBlocks.getMaterialMap(CommonMaterialPrefixes.STORAGE_BLOCK)
        val nuggetMap: Map<HTMaterialKey, HTSimpleDeferredItem> = RagiumItems.getMaterialMap(CommonMaterialPrefixes.NUGGET)

        for (key: HTMaterialKey in RagiumItems.MATERIALS.columnKeys) {
            val basePrefix: HTMaterialPrefix = RagiumPlatform.INSTANCE
                .getMaterialDefinition(key)
                .getDefaultPrefix()
                ?: continue
            val base: ItemLike = RagiumItems.MATERIALS[basePrefix, key] ?: continue

            blockMap[key]?.let { storage: ItemLike ->
                // Block -> Base
                HTShapelessRecipeBuilder
                    .misc(base, 9)
                    .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, key)
                    .saveSuffixed(output, "_from_block")
                // Base -> Block
                HTShapedRecipeBuilder
                    .building(storage)
                    .hollow8()
                    .define('A', basePrefix, key)
                    .define('B', base)
                    .saveSuffixed(output, "_from_base")
            }

            nuggetMap[key]?.let { nugget: ItemLike ->
                // Base -> Nugget
                HTShapelessRecipeBuilder
                    .misc(nugget, 9)
                    .addIngredient(basePrefix, key)
                    .saveSuffixed(output, "_from_base")
                // Nugget -> Base
                HTShapedRecipeBuilder
                    .misc(base)
                    .hollow8()
                    .define('A', CommonMaterialPrefixes.NUGGET, key)
                    .define('B', nugget)
                    .saveSuffixed(output, "_from_nugget")
            }
        }
    }

    @JvmStatic
    private fun oreToRaw() {
        // Coal
        HTItemToChancedItemRecipeBuilder
            .crushing(itemCreator.fromTagKey(CommonMaterialPrefixes.ORE, VanillaMaterialKeys.COAL))
            .addResult(resultHelper.item(CommonMaterialPrefixes.FUEL, VanillaMaterialKeys.COAL, 2))
            .addResult(resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Redstone
        HTItemToChancedItemRecipeBuilder
            .crushing(itemCreator.fromTagKey(CommonMaterialPrefixes.ORE, VanillaMaterialKeys.REDSTONE))
            .addResult(resultHelper.item(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE, 8))
            .addResult(resultHelper.item(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE, 4), 1 / 2f)
            .addResult(resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.CINNABAR, 4), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Raginite
        with(RagiumMaterialRecipeData.RAGINITE_ORE) {
            HTItemToChancedItemRecipeBuilder
                .crushing(this.getItemIngredient(0, itemCreator))
                .addResults(this.getChancedResults(resultHelper))
                .saveSuffixed(output, "_from_ore")
        }

        // Raws
        mapOf(
            VanillaMaterialKeys.COPPER to VanillaMaterialKeys.GOLD,
            VanillaMaterialKeys.IRON to CommonMaterialKeys.Metals.TIN,
            VanillaMaterialKeys.GOLD to VanillaMaterialKeys.COPPER,
            CommonMaterialKeys.Metals.TIN to CommonMaterialKeys.Metals.LEAD,
            CommonMaterialKeys.Metals.LEAD to CommonMaterialKeys.Metals.SILVER,
            CommonMaterialKeys.Metals.SILVER to CommonMaterialKeys.Metals.LEAD,
            CommonMaterialKeys.Metals.NICKEL to CommonMaterialKeys.Metals.PLATINUM,
            CommonMaterialKeys.Metals.PLATINUM to CommonMaterialKeys.Metals.NICKEL,
        ).forEach { (primary: HTMaterialLike, secondary: HTMaterialLike) ->
            HTItemToChancedItemRecipeBuilder
                .crushing(itemCreator.fromTagKey(CommonMaterialPrefixes.ORE, primary))
                .addResult(resultHelper.item(CommonMaterialPrefixes.RAW_MATERIAL, primary, 2))
                .addResult(resultHelper.item(CommonMaterialPrefixes.RAW_MATERIAL, secondary), 1 / 4f)
                .saveSuffixed(output, "_from_ore")
        }

        // Gems
        mapOf(
            // Vanilla
            VanillaMaterialKeys.LAPIS to 8,
            VanillaMaterialKeys.QUARTZ to 4,
            VanillaMaterialKeys.DIAMOND to 2,
            VanillaMaterialKeys.EMERALD to 2,
        ).forEach { (key: HTMaterialKey, count: Int) ->
            HTItemToChancedItemRecipeBuilder
                .crushing(itemCreator.fromTagKey(CommonMaterialPrefixes.ORE, key))
                .addResult(resultHelper.item(CommonMaterialPrefixes.GEM, key, count))
                .saveSuffixed(output, "_from_ore")
        }

        pulverizeFromData(RagiumMaterialRecipeData.RAGI_CRYSTAL_ORE)
        pulverizeFromData(RagiumMaterialRecipeData.CRIMSON_ORE)
        pulverizeFromData(RagiumMaterialRecipeData.WARPED_ORE)

        // Scraps
        pulverizeFromData(VanillaMaterialRecipeData.NETHERITE_SCRAP)
        pulverizeFromData(RagiumMaterialRecipeData.DEEP_SCRAP)
    }

    @JvmStatic
    private fun alloying() {
        // Vanilla
        alloyFromData(VanillaMaterialRecipeData.NETHERITE).save(output)

        // Common
        alloyFromData(CommonMaterialRecipeData.STEEL_COAL)
            .tagCondition(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.STEEL)
            .saveSuffixed(output, "_from_coal")
        alloyFromData(CommonMaterialRecipeData.STEEL_COKE)
            .tagCondition(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.STEEL)
            .saveSuffixed(output, "_from_coke")
        alloyFromData(CommonMaterialRecipeData.INVAR)
            .tagCondition(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.INVAR)
            .save(output)
        alloyFromData(CommonMaterialRecipeData.ELECTRUM)
            .tagCondition(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.ELECTRUM)
            .save(output)
        alloyFromData(CommonMaterialRecipeData.BRONZE)
            .tagCondition(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.BRONZE)
            .save(output)
        alloyFromData(CommonMaterialRecipeData.BRASS)
            .tagCondition(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.BRASS)
            .save(output)
        alloyFromData(CommonMaterialRecipeData.CONSTANTAN)
            .tagCondition(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.CONSTANTAN)
            .save(output)

        // EIO
        alloyFromData(EIOMaterialRecipeData.CONDUCTIVE_ALLOY)
            .tagCondition(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.CONDUCTIVE_ALLOY)
            .save(output)
        alloyFromData(EIOMaterialRecipeData.COPPER_ALLOY)
            .tagCondition(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.COPPER_ALLOY)
            .save(output)
        alloyFromData(EIOMaterialRecipeData.DARK_STEEL)
            .tagCondition(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.DARK_STEEL)
            .save(output)
        alloyFromData(EIOMaterialRecipeData.DARK_STEEL_COKE)
            .tagCondition(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.DARK_STEEL)
            .saveSuffixed(output, "_alt")
        alloyFromData(EIOMaterialRecipeData.END_STEEL)
            .tagCondition(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.END_STEEL)
            .save(output)
        alloyFromData(EIOMaterialRecipeData.ENERGETIC_ALLOY)
            .tagCondition(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.ENERGETIC_ALLOY)
            .save(output)
        alloyFromData(EIOMaterialRecipeData.PULSATING_ALLOY)
            .tagCondition(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.PULSATING_ALLOY)
            .save(output)
        alloyFromData(EIOMaterialRecipeData.REDSTONE_ALLOY)
            .tagCondition(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.REDSTONE_ALLOY)
            .save(output)
        alloyFromData(EIOMaterialRecipeData.SOULARIUM)
            .tagCondition(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.SOULARIUM)
            .save(output)
        alloyFromData(EIOMaterialRecipeData.VIBRANT_ALLOY)
            .tagCondition(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.VIBRANT_ALLOY)
            .save(output)

        // Oritech
        alloyFromData(OritechMaterialRecipeData.ADAMANT)
            .tagCondition(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.ADAMANT)
            .save(output)
        alloyFromData(OritechMaterialRecipeData.DURATIUM)
            .tagCondition(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.DURATIUM)
            .save(output)
        alloyFromData(OritechMaterialRecipeData.ENERGITE)
            .tagCondition(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.ENERGITE)
            .save(output)
    }

    @JvmStatic
    fun alloyFromData(data: HTMaterialRecipeData): HTCombineItemToObjRecipeBuilder<*> = HTCombineItemToObjRecipeBuilder
        .alloying(
            data.getResult(resultHelper, 0),
            data.getItemIngredients(itemCreator),
        )

    @JvmStatic
    fun pulverizeFromData(data: HTMaterialRecipeData) {
        HTItemToChancedItemRecipeBuilder
            .crushing(data.getItemIngredient(0, itemCreator))
            .addResult(data.getChancedResult(resultHelper, 0))
            .saveSuffixed(output, "_from_ore")
    }
}
