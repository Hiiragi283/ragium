package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTAlloyingRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterial
import net.minecraft.world.item.Items

object RagiumAlloyingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        hiiragiCore()
        ragium()
    }

    @JvmStatic
    private fun hiiragiCore() {
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

        // Rubber
        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.PLATE, HCMaterial.Plates.RUBBER, 2),
                itemCreator.fromTagKey(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Plates.RUBBER),
                itemCreator.fromTagKey(HCMaterialPrefixes.DUST, HCMaterial.Minerals.SULFUR),
            ).saveSuffixed(output, "_with_sulfur")

        HTAlloyingRecipeBuilder
            .create(
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.PLATE, HCMaterial.Plates.RUBBER, 4),
                itemCreator.fromTagKey(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Plates.RUBBER),
                itemCreator.fromTagKey(HCMaterialPrefixes.DUST, HCMaterial.Minerals.SULFUR),
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.DUST),
                    listOf(HCMaterial.Fuels.COAL, HCMaterial.Fuels.CHARCOAL),
                ),
            ).saveSuffixed(output, "_with_sulfur_and_coal")

        // Ambrosia
        HTAlloyingRecipeBuilder
            .create(
                resultHelper.item(item = HCItems.AMBROSIA),
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
    }
}
