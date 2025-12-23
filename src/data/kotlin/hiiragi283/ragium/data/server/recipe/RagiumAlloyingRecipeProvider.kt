package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTAlloyingRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterial

object RagiumAlloyingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Raginite + Copper -> Ragi-Alloy
        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.INGOT, RagiumMaterial.RAGI_ALLOY),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(HCMaterial.Metals.COPPER),
                ),
                itemCreator.fromTagKey(HCMaterialPrefixes.DUST, RagiumMaterial.RAGINITE, 2),
            ).save(output)
        // Ragi-Alloy + Glowstone -> Adv Ragi-Alloy
        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.INGOT, RagiumMaterial.ADVANCED_RAGI_ALLOY),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(RagiumMaterial.RAGI_ALLOY),
                ),
                itemCreator.fromTagKey(HCMaterialPrefixes.DUST, HCMaterial.Minerals.GLOWSTONE, 2),
            ).save(output)
        // Raginite + Diamond -> Ragi-Crystal
        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.GEM, RagiumMaterial.RAGI_CRYSTAL),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.GEM),
                    listOf(HCMaterial.Gems.DIAMOND),
                ),
                itemCreator.fromTagKey(HCMaterialPrefixes.DUST, RagiumMaterial.RAGINITE, 6),
            ).save(output)

        // Amethyst + Lapis -> Azure Shard
        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.GEM, HCMaterial.Gems.AZURE, 2),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.GEM),
                    listOf(HCMaterial.Gems.AMETHYST),
                ),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.GEM),
                    listOf(HCMaterial.Gems.LAPIS),
                ),
            ).save(output)
        // Azure Shard + Iron -> Azure Steel
        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.INGOT, HCMaterial.Alloys.AZURE_STEEL),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(HCMaterial.Metals.IRON),
                ),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.INGOT),
                    listOf(HCMaterial.Gems.AZURE),
                    2,
                ),
            ).save(output)
    }
}
