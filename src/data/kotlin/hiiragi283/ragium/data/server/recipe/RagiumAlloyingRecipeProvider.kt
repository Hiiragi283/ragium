package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.HTMaterialResultHelper
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTAlloyingRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import net.minecraft.world.item.Items

object RagiumAlloyingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        common()
        hiiragiCore()
        ragium()
    }

    @JvmStatic
    private fun common() {
        // Netherite
        HTAlloyingRecipeBuilder
            .create(
                HTMaterialResultHelper.item(CommonTagPrefixes.INGOT, VanillaMaterialKeys.NETHERITE, 2),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.GOLD),
                    4,
                ),
                itemCreator.fromTagKey(CommonTagPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 4),
            ).save(output)

        // Steel from Coal
        HTAlloyingRecipeBuilder
            .create(
                HTMaterialResultHelper.item(CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.IRON),
                ),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.FUEL),
                    listOf(VanillaMaterialKeys.COAL, VanillaMaterialKeys.CHARCOAL),
                    2,
                ),
            ).saveSuffixed(output, "_from_coal")
        // Steel from Coke
        HTAlloyingRecipeBuilder
            .create(
                HTMaterialResultHelper.item(CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.IRON),
                ),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.FUEL),
                    listOf(CommonMaterialKeys.COAL_COKE),
                ),
            ).saveSuffixed(output, "_from_coke")
        // Invar
        HTAlloyingRecipeBuilder
            .create(
                HTMaterialResultHelper.item(CommonTagPrefixes.INGOT, CommonMaterialKeys.INVAR, 3),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.IRON),
                    2,
                ),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT),
                    listOf(CommonMaterialKeys.NICKEL),
                ),
            ).tagCondition(CommonTagPrefixes.INGOT, CommonMaterialKeys.INVAR)
            .save(output)
        // Electrum
        HTAlloyingRecipeBuilder
            .create(
                HTMaterialResultHelper.item(CommonTagPrefixes.INGOT, CommonMaterialKeys.ELECTRUM, 2),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.GOLD),
                ),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT),
                    listOf(CommonMaterialKeys.SILVER),
                ),
            ).tagCondition(CommonTagPrefixes.INGOT, CommonMaterialKeys.ELECTRUM)
            .save(output)
        // Bronze
        HTAlloyingRecipeBuilder
            .create(
                HTMaterialResultHelper.item(CommonTagPrefixes.INGOT, CommonMaterialKeys.BRONZE, 4),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.COPPER),
                    3,
                ),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT),
                    listOf(CommonMaterialKeys.TIN),
                ),
            ).tagCondition(CommonTagPrefixes.INGOT, CommonMaterialKeys.BRONZE)
            .save(output)
        // Brass
        HTAlloyingRecipeBuilder
            .create(
                HTMaterialResultHelper.item(CommonTagPrefixes.INGOT, CommonMaterialKeys.BRASS, 2),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.COPPER),
                ),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT),
                    listOf(CommonMaterialKeys.ZINC),
                ),
            ).tagCondition(CommonTagPrefixes.INGOT, CommonMaterialKeys.BRASS)
            .save(output)
        // Constantan
        HTAlloyingRecipeBuilder
            .create(
                HTMaterialResultHelper.item(CommonTagPrefixes.INGOT, CommonMaterialKeys.CONSTANTAN, 2),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.COPPER),
                ),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT),
                    listOf(CommonMaterialKeys.NICKEL),
                ),
            ).tagCondition(CommonTagPrefixes.INGOT, CommonMaterialKeys.CONSTANTAN)
            .save(output)
    }

    @JvmStatic
    private fun hiiragiCore() {
        // Amethyst + Lapis -> Azure Shard
        HTAlloyingRecipeBuilder
            .create(
                HTMaterialResultHelper.item(CommonTagPrefixes.GEM, HCMaterialKeys.AZURE, 2),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM),
                    listOf(VanillaMaterialKeys.AMETHYST),
                ),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM),
                    listOf(VanillaMaterialKeys.LAPIS),
                ),
            ).save(output)
        // Azure Shard + Iron -> Azure Steel
        HTAlloyingRecipeBuilder
            .create(
                HTMaterialResultHelper.item(CommonTagPrefixes.INGOT, HCMaterialKeys.AZURE_STEEL),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.IRON),
                ),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM),
                    listOf(HCMaterialKeys.AZURE),
                    2,
                ),
            ).save(output)

        // Rubber
        HTAlloyingRecipeBuilder
            .create(
                HTMaterialResultHelper.item(CommonTagPrefixes.PLATE, CommonMaterialKeys.RUBBER, 2),
                itemCreator.fromItem(HCItems.RAW_RUBBER),
                itemCreator.fromTagKey(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR),
            ).saveSuffixed(output, "_with_sulfur")

        HTAlloyingRecipeBuilder
            .create(
                HTMaterialResultHelper.item(CommonTagPrefixes.PLATE, CommonMaterialKeys.RUBBER, 4),
                itemCreator.fromItem(HCItems.RAW_RUBBER),
                itemCreator.fromTagKey(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST),
                    listOf(VanillaMaterialKeys.COAL, VanillaMaterialKeys.CHARCOAL),
                ),
            ).saveSuffixed(output, "_with_sulfur_and_coal")

        // Ambrosia
        HTAlloyingRecipeBuilder
            .create(
                itemResult.create(HCItems.AMBROSIA),
                itemCreator.fromItem(HCItems.IRIDESCENT_POWDER),
                itemCreator.fromItem(Items.HONEY_BLOCK, 64),
                itemCreator.fromItem(Items.ENCHANTED_GOLDEN_APPLE, 16),
            ).save(output)
    }

    @JvmStatic
    private fun ragium() {
        // Raginite + Copper -> Ragi-Alloy
        HTAlloyingRecipeBuilder
            .create(
                HTMaterialResultHelper.item(CommonTagPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.COPPER),
                ),
                itemCreator.fromTagKey(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 2),
            ).save(output)
        // Ragi-Alloy + Glowstone -> Adv Ragi-Alloy
        HTAlloyingRecipeBuilder
            .create(
                HTMaterialResultHelper.item(CommonTagPrefixes.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY),
                itemCreator.fromTagKeys(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT),
                    listOf(RagiumMaterialKeys.RAGI_ALLOY),
                ),
                itemCreator.fromTagKey(CommonTagPrefixes.DUST, VanillaMaterialKeys.GLOWSTONE, 2),
            ).save(output)
    }
}
