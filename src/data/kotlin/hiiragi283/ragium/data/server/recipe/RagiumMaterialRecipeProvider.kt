package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialDefinition
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.attribute.HTStorageBlockMaterialAttribute
import hiiragi283.ragium.api.material.get
import hiiragi283.ragium.api.material.getDefaultPrefix
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.material.CommonMaterialRecipeData
import hiiragi283.ragium.impl.data.recipe.material.CreateMaterialRecipeData
import hiiragi283.ragium.impl.data.recipe.material.EIOMaterialRecipeData
import hiiragi283.ragium.impl.data.recipe.material.OritechMaterialRecipeData
import hiiragi283.ragium.impl.data.recipe.material.RagiumMaterialRecipeData
import hiiragi283.ragium.impl.data.recipe.material.VanillaMaterialRecipeData
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumMaterialRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        raginite()
        azure()
        deepSteel()
        miscMaterials()

        blockAndNugget()
        gear()
        oreToRaw()
        alloying()
    }

    @JvmStatic
    private fun raginite() {
        // Ragi-Alloy
        HTShapedRecipeBuilder
            .create(RagiumItems.RAGI_ALLOY_COMPOUND)
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

        alloyFromData(RagiumMaterialRecipeData.RAGI_ALLOY)

        HTShapedRecipeBuilder
            .create(RagiumItems.RAGI_COKE)
            .hollow4()
            .define('A', CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .define('B', CommonMaterialPrefixes.FUEL, VanillaMaterialKeys.COAL)
            .save(output)
        // Advanced Ragi-Alloy
        alloyFromData(RagiumMaterialRecipeData.ADVANCED_RAGI_ALLOY)
        // Ragi-Crystal
        HTShapedRecipeBuilder
            .create(RagiumItems.getGem(RagiumMaterialKeys.RAGI_CRYSTAL))
            .hollow8()
            .define('A', CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .define('B', CommonMaterialPrefixes.GEM, VanillaMaterialKeys.DIAMOND)
            .save(output)

        alloyFromData(RagiumMaterialRecipeData.RAGI_CRYSTAL)
    }

    @JvmStatic
    private fun azure() {
        alloyFromData(RagiumMaterialRecipeData.AZURE_SHARD)
        // Azure Steel
        alloyFromData(RagiumMaterialRecipeData.AZURE_STEEL)
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
            .addIngredients(CommonMaterialPrefixes.SCRAP, RagiumMaterialKeys.DEEP_STEEL, 4)
            .addIngredients(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL, 4)
            .save(output)

        alloyFromData(RagiumMaterialRecipeData.DEEP_STEEL)
    }

    @JvmStatic
    private fun miscMaterials() {
        // Sawdust
        HTShapedRecipeBuilder
            .create(RagiumItems.COMPRESSED_SAWDUST)
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
        HTShapedRecipeBuilder
            .create(RagiumBlocks.SOOTY_COBBLESTONE, 8)
            .hollow8()
            .define('A', Tags.Items.COBBLESTONES_NORMAL)
            .define('B', ItemTags.COALS)
            .setCategory(CraftingBookCategory.BUILDING)
            .save(output)
        HTCookingRecipeBuilder
            .blasting(Items.BLACKSTONE)
            .addIngredient(RagiumBlocks.SOOTY_COBBLESTONE)
            .setTime(400)
            .save(output)

        alloyFromData(RagiumMaterialRecipeData.NIGHT_METAL)
        // Iridescentium
        mixFromData(RagiumMaterialRecipeData.IRIDESCENTIUM)
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
            val definition: HTMaterialDefinition = RagiumPlatform.INSTANCE.getMaterialDefinition(key)
            val basePrefix: HTMaterialPrefix = definition.getDefaultPrefix() ?: continue
            val base: ItemLike = RagiumItems.MATERIALS[basePrefix, key] ?: continue

            blockMap[key]?.let { storage: ItemLike ->
                val storageBlock: HTStorageBlockMaterialAttribute = if (basePrefix.isOf(CommonMaterialPrefixes.INGOT)) {
                    HTStorageBlockMaterialAttribute.THREE_BY_THREE
                } else {
                    definition.get<HTStorageBlockMaterialAttribute>() ?: return@let
                }
                // Block -> Base
                HTShapelessRecipeBuilder
                    .misc(base, storageBlock.baseCount)
                    .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, key)
                    .saveSuffixed(output, "_from_block")
                // Base -> Block
                HTShapedRecipeBuilder
                    .create(storage)
                    .pattern(storageBlock.pattern)
                    .define('A', basePrefix, key)
                    .define('B', base)
                    .setCategory(CraftingBookCategory.BUILDING)
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
                    .create(base)
                    .hollow8()
                    .define('A', CommonMaterialPrefixes.NUGGET, key)
                    .define('B', nugget)
                    .saveSuffixed(output, "_from_nugget")
            }
        }
    }

    @JvmStatic
    private fun gear() {
        for ((key: HTMaterialKey, gear: ItemLike) in RagiumItems.getMaterialMap(CommonMaterialPrefixes.GEAR)) {
            // Shaped
            HTShapedRecipeBuilder
                .create(gear)
                .hollow4()
                .define('A', CommonMaterialPrefixes.INGOT, key)
                .define('B', CommonMaterialPrefixes.NUGGET, VanillaMaterialKeys.IRON)
                .save(output)
            // Compressing
            HTItemWithCatalystRecipeBuilder
                .compressing(
                    itemCreator.fromTagKey(CommonMaterialPrefixes.INGOT, key, 4),
                    resultHelper.item(gear),
                ).save(output)
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
                .crushing(this.getItemIngredients(itemCreator)[0])
                .addResults(this.getItemResults().map(::HTItemResultWithChance))
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
        alloyFromData(VanillaMaterialRecipeData.NETHERITE)

        // Common
        alloyFromData(CommonMaterialRecipeData.STEEL_COAL, true)
        alloyFromData(CommonMaterialRecipeData.STEEL_COKE, true)
        alloyFromData(CommonMaterialRecipeData.INVAR, true)
        alloyFromData(CommonMaterialRecipeData.ELECTRUM, true)
        alloyFromData(CommonMaterialRecipeData.BRONZE, true)
        alloyFromData(CommonMaterialRecipeData.BRASS, true)
        alloyFromData(CommonMaterialRecipeData.CONSTANTAN, true)

        // Create
        alloyFromData(CreateMaterialRecipeData.ANDESITE_ALLOY, true)

        // EIO
        alloyFromData(EIOMaterialRecipeData.CONDUCTIVE_ALLOY, true)
        alloyFromData(EIOMaterialRecipeData.COPPER_ALLOY, true)
        alloyFromData(EIOMaterialRecipeData.DARK_STEEL, true)
        alloyFromData(EIOMaterialRecipeData.DARK_STEEL_COAL, true)
        alloyFromData(EIOMaterialRecipeData.DARK_STEEL_COKE, true)
        alloyFromData(EIOMaterialRecipeData.END_STEEL, true)
        alloyFromData(EIOMaterialRecipeData.ENERGETIC_ALLOY, true)
        alloyFromData(EIOMaterialRecipeData.PULSATING_ALLOY, true)
        alloyFromData(EIOMaterialRecipeData.REDSTONE_ALLOY, true)
        alloyFromData(EIOMaterialRecipeData.SOULARIUM, true)
        alloyFromData(EIOMaterialRecipeData.VIBRANT_ALLOY, true)

        // Oritech
        alloyFromData(OritechMaterialRecipeData.ADAMANT, true)
        alloyFromData(OritechMaterialRecipeData.BIOSTEEL, true)
        alloyFromData(OritechMaterialRecipeData.DURATIUM, true)
        alloyFromData(OritechMaterialRecipeData.ENERGITE, true)
    }

    @JvmStatic
    private fun alloyFromData(data: HTRecipeData, applyCondition: Boolean = false) {
        HTCombineItemToObjRecipeBuilder
            .alloying(
                data.getItemResults()[0].first,
                data.getItemIngredients(itemCreator),
            ).apply {
                if (applyCondition) {
                    for ((entry: Ior<Item, TagKey<Item>>) in data.itemOutputs) {
                        entry.getRight()?.let(this::tagCondition)
                    }
                }
            }.saveModified(output, data.operator)
    }
}
