package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.tags.ItemTags

object RagiumPyrolyzingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Log -> Charcoal
        HTComplexRecipeBuilder
            .pyrolyzing(itemCreator.fromTagKey(ItemTags.LOGS_THAT_BURN, 8))
            .setResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.FUEL, HCMaterial.Fuels.CHARCOAL, 8))
            .setResult(fluidResult.create(RagiumFluids.CREOSOTE, 1000))
            .setTime(20 * 30)
            .saveSuffixed(output, "_from_log")
        // Compressed Sawdust -> Charcoal
        HTComplexRecipeBuilder
            .pyrolyzing(itemCreator.fromItem(HCItems.COMPRESSED_SAWDUST, 8))
            .setResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.FUEL, HCMaterial.Fuels.CHARCOAL, 8))
            .setResult(fluidResult.create(RagiumFluids.CREOSOTE, 1000))
            .saveSuffixed(output, "_from_sawdust")

        // Coal -> Coke + Creosote
        HTComplexRecipeBuilder
            .pyrolyzing(itemCreator.fromTagKey(HCMaterialPrefixes.FUEL, HCMaterial.Fuels.COAL, 8))
            .setResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.FUEL, HCMaterial.Fuels.COAL_COKE, 8))
            .setResult(fluidResult.create(RagiumFluids.CREOSOTE, 2000))
            .setTime(20 * 30)
            .saveSuffixed(output, "_from_coal")

        HTComplexRecipeBuilder
            .pyrolyzing(itemCreator.fromTagKey(HCMaterialPrefixes.DUST, HCMaterial.Fuels.COAL, 8))
            .setResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Fuels.COAL_COKE, 8))
            .setResult(fluidResult.create(RagiumFluids.CREOSOTE, 2000))
            .setTime(20 * 30)
            .saveSuffixed(output, "_from_coal_dust")

        HTComplexRecipeBuilder
            .pyrolyzing(itemCreator.fromTagKey(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Fuels.COAL))
            .setResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Fuels.COAL_COKE))
            .setResult(fluidResult.create(RagiumFluids.CREOSOTE, 2000))
            .setTime(20 * 30)
            .saveSuffixed(output, "_from_coal_block")

        // Crimson Stem -> Crimson Blood
        HTComplexRecipeBuilder
            .pyrolyzing(itemCreator.fromTagKey(ItemTags.CRIMSON_STEMS, 8))
            .setResult(fluidResult.create(HCFluids.CRIMSON_BLOOD, 1000))
            .setTime(20 * 30)
            .saveSuffixed(output, "_from_log")
        // Warped Stem -> Dew of the Warp
        HTComplexRecipeBuilder
            .pyrolyzing(itemCreator.fromTagKey(ItemTags.WARPED_STEMS, 8))
            .setResult(fluidResult.create(HCFluids.DEW_OF_THE_WARP, 1000))
            .setTime(20 * 30)
            .saveSuffixed(output, "_from_log")
    }
}
