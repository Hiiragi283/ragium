package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.HCMaterialPrefixes
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
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.INGOT, VanillaMaterialKeys.NETHERITE, 2),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.GOLD),
                    4,
                ),
                itemCreator.fromTagKey(HCMaterialPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 4),
            ).save(output)

        // Steel from Coal
        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.INGOT, CommonMaterialKeys.STEEL),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.IRON),
                ),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.FUEL),
                    listOf(VanillaMaterialKeys.COAL, VanillaMaterialKeys.CHARCOAL),
                    2,
                ),
            ).saveSuffixed(output, "_from_coal")
        // Steel from Coke
        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.INGOT, CommonMaterialKeys.STEEL),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.IRON),
                ),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.FUEL),
                    listOf(CommonMaterialKeys.COAL_COKE),
                ),
            ).saveSuffixed(output, "_from_coke")
        // Invar
        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.INGOT, CommonMaterialKeys.INVAR, 3),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.IRON),
                    2,
                ),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(CommonMaterialKeys.NICKEL),
                ),
            ).tagCondition(HCMaterialPrefixes.INGOT, CommonMaterialKeys.INVAR)
            .save(output)
        // Electrum
        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.INGOT, CommonMaterialKeys.ELECTRUM, 2),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.GOLD),
                ),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(CommonMaterialKeys.SILVER),
                ),
            ).tagCondition(HCMaterialPrefixes.INGOT, CommonMaterialKeys.ELECTRUM)
            .save(output)
        // Bronze
        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.INGOT, CommonMaterialKeys.BRONZE, 4),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.COPPER),
                    3,
                ),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(CommonMaterialKeys.TIN),
                ),
            ).tagCondition(HCMaterialPrefixes.INGOT, CommonMaterialKeys.BRONZE)
            .save(output)
        // Brass
        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.INGOT, CommonMaterialKeys.BRASS, 2),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.COPPER),
                ),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(CommonMaterialKeys.ZINC),
                ),
            ).tagCondition(HCMaterialPrefixes.INGOT, CommonMaterialKeys.BRASS)
            .save(output)
        // Constantan
        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.INGOT, CommonMaterialKeys.CONSTANTAN, 2),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.COPPER),
                ),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(CommonMaterialKeys.NICKEL),
                ),
            ).tagCondition(HCMaterialPrefixes.INGOT, CommonMaterialKeys.CONSTANTAN)
            .save(output)
    }

    @JvmStatic
    private fun hiiragiCore() {
        // Coal Coke + Calcite -> Carbide
        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.FUEL, CommonMaterialKeys.CARBIDE),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.FUEL),
                    listOf(CommonMaterialKeys.COAL_COKE),
                ),
                itemCreator.fromItem(Items.CALCITE),
            ).save(output)

        // Amethyst + Lapis -> Azure Shard
        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.GEM, HCMaterialKeys.AZURE, 2),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.GEM),
                    listOf(VanillaMaterialKeys.AMETHYST),
                ),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.GEM),
                    listOf(VanillaMaterialKeys.LAPIS),
                ),
            ).save(output)
        // Azure Shard + Iron -> Azure Steel
        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.INGOT, HCMaterialKeys.AZURE_STEEL),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.IRON),
                ),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.GEM),
                    listOf(HCMaterialKeys.AZURE),
                    2,
                ),
            ).save(output)

        // Rubber
        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.PLATE, CommonMaterialKeys.RUBBER, 2),
                itemCreator.fromItem(HCItems.RAW_RUBBER),
                itemCreator.fromTagKey(HCMaterialPrefixes.DUST, CommonMaterialKeys.SULFUR),
            ).saveSuffixed(output, "_with_sulfur")

        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.PLATE, CommonMaterialKeys.RUBBER, 4),
                itemCreator.fromItem(HCItems.RAW_RUBBER),
                itemCreator.fromTagKey(HCMaterialPrefixes.DUST, CommonMaterialKeys.SULFUR),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST),
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
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(VanillaMaterialKeys.COPPER),
                ),
                itemCreator.fromTagKey(HCMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 2),
            ).save(output)
        // Ragi-Alloy + Glowstone -> Adv Ragi-Alloy
        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(RagiumMaterialKeys.RAGI_ALLOY),
                ),
                itemCreator.fromTagKey(HCMaterialPrefixes.DUST, VanillaMaterialKeys.GLOWSTONE, 2),
            ).save(output)
    }
}
