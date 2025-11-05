package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.getDefaultPrefix
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.registry.HTFluidContent
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
import hiiragi283.ragium.impl.data.recipe.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
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
            .define('A', CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .define('B', CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.COPPER)
            .save(output)

        HTCookingRecipeBuilder
            .smeltingAndBlasting(RagiumItems.getIngot(RagiumMaterialKeys.RAGI_ALLOY)) {
                addIngredient(RagiumItems.RAGI_ALLOY_COMPOUND)
                setExp(0.7f)
                saveSuffixed(output, "_from_compound")
            }

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY),
                itemCreator.ingotOrDust(VanillaMaterialKeys.COPPER),
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 2),
            ).save(output)

        HTShapedRecipeBuilder
            .misc(RagiumItems.RAGI_COKE)
            .hollow4()
            .define('A', CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .define('B', CommonMaterialPrefixes.FUEL, VanillaMaterialKeys.COAL)
            .save(output)
        // Advanced Ragi-Alloy
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY),
                itemCreator.ingotOrDust(VanillaMaterialKeys.GOLD),
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 4),
            ).save(output)
        // Ragi-Crystal
        HTShapedRecipeBuilder
            .misc(RagiumItems.getGem(RagiumMaterialKeys.RAGI_CRYSTAL))
            .hollow8()
            .define('A', CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .define('B', CommonMaterialPrefixes.GEM, VanillaMaterialKeys.DIAMOND)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL),
                itemCreator.gemOrDust(VanillaMaterialKeys.DIAMOND),
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 6),
            ).save(output)
    }

    @JvmStatic
    private fun azure() {
        // Azure Shard
        HTShapedRecipeBuilder
            .misc(RagiumItems.getGem(RagiumMaterialKeys.AZURE), 2)
            .mosaic4()
            .define('A', Tags.Items.GEMS_AMETHYST)
            .define('B', Tags.Items.GEMS_LAPIS)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.AZURE, 2),
                itemCreator.gemOrDust(VanillaMaterialKeys.AMETHYST),
                itemCreator.gemOrDust(VanillaMaterialKeys.LAPIS),
            ).save(output)
        // Azure Steel
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL),
                itemCreator.ingotOrDust(VanillaMaterialKeys.IRON),
                itemCreator.gemOrDust(RagiumMaterialKeys.AZURE, 2),
            ).save(output)
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

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL, 9),
                itemCreator.fromTagKey(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.CRIMSON_CRYSTAL),
                itemCreator.fromTagKey(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.WARPED_CRYSTAL),
                itemCreator.fromTagKey(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, 3),
            ).save(output)
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
        // Gildium
        HTItemToObjRecipeBuilder
            .melting(
                itemCreator.fromItem(Items.GILDED_BLACKSTONE),
                resultHelper.fluid(RagiumFluidContents.GILDED_LAVA, 1000),
            ).save(output)

        HTFluidTransformRecipeBuilder
            .refining(
                fluidCreator.fromContent(RagiumFluidContents.GILDED_LAVA, 1000),
                resultHelper.fluid(HTFluidContent.LAVA, 750),
                null,
                resultHelper.item(CommonMaterialPrefixes.NUGGET, RagiumMaterialKeys.GILDIUM),
            ).save(output)

        HTFluidTransformRecipeBuilder
            .refining(
                fluidCreator.fromContent(RagiumFluidContents.GILDED_LAVA, 1000),
                resultHelper.fluid(HTFluidContent.LAVA, 750),
                itemCreator.fromItem(RagiumItems.PLATING_CATALYST),
                resultHelper.item(CommonMaterialPrefixes.NUGGET, RagiumMaterialKeys.GILDIUM, 3),
            ).saveSuffixed(output, "_alt")
        // Iridescentium
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.IRIDESCENTIUM),
                itemCreator.gemOrDust(RagiumMaterialKeys.ELDRITCH_PEARL, 8),
                itemCreator.fromTagKey(Tags.Items.NETHER_STARS),
            ).save(output)
        // Other
        HTShapelessRecipeBuilder
            .misc(Items.GUNPOWDER, 3)
            .addIngredient(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR)
            .addIngredient(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALTPETER)
            .addIngredient(fuelOrDust(VanillaMaterialKeys.CHARCOAL))
            .saveSuffixed(output, "_with_hammer")

        HTCookingRecipeBuilder
            .smelting(RagiumItems.getMaterial(CommonMaterialPrefixes.FUEL, RagiumMaterialKeys.BAMBOO_CHARCOAL))
            .addIngredient(Items.BAMBOO)
            .setExp(0.15f)
            .save(output)
    }

    @JvmStatic
    private fun material() {
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
        HTItemToChancedItemRecipeBuilder
            .crushing(itemCreator.fromTagKey(CommonMaterialPrefixes.ORE, RagiumMaterialKeys.RAGINITE))
            .addResult(resultHelper.item(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 8))
            .addResult(resultHelper.item(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 4), 1 / 2f)
            .addResult(resultHelper.item(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL, 1), 1 / 4f)
            .saveSuffixed(output, "_from_ore")

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
            // Ragium
            RagiumMaterialKeys.RAGI_CRYSTAL to 2,
            RagiumMaterialKeys.CRIMSON_CRYSTAL to 2,
            RagiumMaterialKeys.WARPED_CRYSTAL to 2,
        ).forEach { (key: HTMaterialKey, count: Int) ->
            HTItemToChancedItemRecipeBuilder
                .crushing(itemCreator.fromTagKey(CommonMaterialPrefixes.ORE, key))
                .addResult(resultHelper.item(CommonMaterialPrefixes.GEM, key, count))
                .saveSuffixed(output, "_from_ore")
        }

        // Scraps
        mapOf(
            Tags.Items.ORES_NETHERITE_SCRAP to VanillaMaterialKeys.NETHERITE,
            RagiumCommonTags.Items.ORES_DEEP_SCRAP to RagiumMaterialKeys.DEEP_STEEL,
        ).forEach { (ore: TagKey<Item>, scrap: HTMaterialKey) ->
            HTItemToChancedItemRecipeBuilder
                .crushing(itemCreator.fromTagKey(ore))
                .addResult(resultHelper.item(CommonMaterialPrefixes.SCRAP, scrap, 2))
                .saveSuffixed(output, "_from_ore")
        }
    }

    @JvmStatic
    private fun alloying() {
        // Netherite
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(Tags.Items.INGOTS_NETHERITE, 2),
                itemCreator.ingotOrDust(VanillaMaterialKeys.GOLD, 4),
                itemCreator.fromTagKey(CommonMaterialPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 4),
            ).save(output)
        // Deep Steel
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.DEEP_STEEL, 2),
                itemCreator.ingotOrDust(RagiumMaterialKeys.AZURE_STEEL, 4),
                itemCreator.fromTagKey(CommonMaterialPrefixes.SCRAP, RagiumMaterialKeys.DEEP_STEEL, 4),
            ).save(output)

        // Steel
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.STEEL),
                itemCreator.ingotOrDust(VanillaMaterialKeys.IRON),
                itemCreator.fuelOrDust(VanillaMaterialKeys.COAL, 2),
            ).tagCondition(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.STEEL)
            .saveSuffixed(output, "_from_coal")

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.STEEL),
                itemCreator.ingotOrDust(VanillaMaterialKeys.IRON),
                itemCreator.fuelOrDust(CommonMaterialKeys.COAL_COKE),
            ).tagCondition(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.STEEL)
            .saveSuffixed(output, "_from_coke")
        // Invar
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.INVAR, 3),
                itemCreator.ingotOrDust(VanillaMaterialKeys.IRON, 2),
                itemCreator.ingotOrDust(CommonMaterialKeys.Metals.NICKEL),
            ).tagCondition(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.INVAR)
            .save(output)
        // Electrum
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.ELECTRUM, 2),
                itemCreator.ingotOrDust(VanillaMaterialKeys.GOLD),
                itemCreator.ingotOrDust(CommonMaterialKeys.Metals.SILVER),
            ).tagCondition(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.ELECTRUM)
            .save(output)
        // Bronze
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.BRONZE, 4),
                itemCreator.ingotOrDust(VanillaMaterialKeys.COPPER, 3),
                itemCreator.ingotOrDust(CommonMaterialKeys.Metals.TIN),
            ).tagCondition(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.BRONZE)
            .save(output)
        // Brass
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.BRASS, 4),
                itemCreator.ingotOrDust(VanillaMaterialKeys.COPPER, 3),
                itemCreator.ingotOrDust(CommonMaterialKeys.Metals.ZINC),
            ).tagCondition(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.BRASS)
            .save(output)
        // Constantan
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.CONSTANTAN, 2),
                itemCreator.ingotOrDust(VanillaMaterialKeys.COPPER),
                itemCreator.ingotOrDust(CommonMaterialKeys.Metals.NICKEL),
            ).tagCondition(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.CONSTANTAN)
            .save(output)

        // Adamant
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.ADAMANT, 2),
                itemCreator.ingotOrDust(CommonMaterialKeys.Metals.NICKEL),
                itemCreator.gemOrDust(VanillaMaterialKeys.DIAMOND),
            ).tagCondition(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.ADAMANT)
            .save(output)
        // Duratium
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.DURATIUM, 2),
                itemCreator.ingotOrDust(CommonMaterialKeys.Metals.PLATINUM),
                itemCreator.ingotOrDust(VanillaMaterialKeys.NETHERITE),
            ).tagCondition(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.DURATIUM)
            .save(output)
        // Energite
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.ENERGITE, 2),
                itemCreator.ingotOrDust(CommonMaterialKeys.Metals.NICKEL),
                itemCreator.gemOrDust(ModMaterialKeys.Gems.FLUXITE),
            ).tagCondition(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.ENERGITE)
            .save(output)
    }
}
