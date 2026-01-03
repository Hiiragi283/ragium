package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTPyrolyzingRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.tags.ItemTags

object RagiumPyrolyzingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Log -> Charcoal
        HTPyrolyzingRecipeBuilder
            .create(
                itemCreator.fromTagKey(ItemTags.LOGS_THAT_BURN, 8),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.FUEL, HCMaterial.Fuels.CHARCOAL, 8),
                fluidResult.create(RagiumFluids.CREOSOTE, 1000),
            ).setTime(20 * 30)
            .saveSuffixed(output, "_from_log")
        // Compressed Sawdust -> Charcoal
        HTPyrolyzingRecipeBuilder
            .create(
                itemCreator.fromItem(HCItems.COMPRESSED_SAWDUST, 8),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.FUEL, HCMaterial.Fuels.CHARCOAL, 8),
                fluidResult.create(RagiumFluids.CREOSOTE, 1000),
            ).saveSuffixed(output, "_from_sawdust")

        // Coal -> Coke + Creosote
        HTPyrolyzingRecipeBuilder
            .create(
                itemCreator.fromTagKey(HCMaterialPrefixes.FUEL, HCMaterial.Fuels.COAL, 8),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.FUEL, HCMaterial.Fuels.COAL_COKE, 8),
                fluidResult.create(RagiumFluids.CREOSOTE, 2000),
            ).setTime(20 * 30)
            .saveSuffixed(output, "_from_coal")

        HTPyrolyzingRecipeBuilder
            .create(
                itemCreator.fromTagKey(HCMaterialPrefixes.DUST, HCMaterial.Fuels.COAL, 8),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Fuels.COAL_COKE, 8),
                fluidResult.create(RagiumFluids.CREOSOTE, 2000),
            ).setTime(20 * 30)
            .saveSuffixed(output, "_from_coal_dust")

        HTPyrolyzingRecipeBuilder
            .create(
                itemCreator.fromTagKey(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Fuels.COAL),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Fuels.COAL_COKE),
                fluidResult.create(RagiumFluids.CREOSOTE, 2000),
            ).setTime(20 * 30)
            .saveSuffixed(output, "_from_coal_block")

        // Crimson Stem -> Crimson Blood
        HTPyrolyzingRecipeBuilder
            .create(
                itemCreator.fromTagKey(ItemTags.CRIMSON_STEMS, 8),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Minerals.SULFUR),
                fluidResult.create(HCFluids.MOLTEN_CRIMSON_CRYSTAL, HTConst.INGOT_AMOUNT),
            ).setTime(20 * 30)
            .saveSuffixed(output, "_from_log")
        // Warped Stem -> Dew of the Warp
        HTPyrolyzingRecipeBuilder
            .create(
                itemCreator.fromTagKey(ItemTags.WARPED_STEMS, 8),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Minerals.SULFUR),
                fluidResult.create(HCFluids.MOLTEN_WARPED_CRYSTAL, HTConst.INGOT_AMOUNT),
            ).setTime(20 * 30)
            .saveSuffixed(output, "_from_log")
    }
}
