package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.HTMaterialResultHelper
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
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
                HTMaterialResultHelper.item(CommonTagPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL, 8),
                fluidResult.create(RagiumFluids.CREOSOTE, 1000),
            ).saveSuffixed(output, "_from_log")
        // Compressed Sawdust -> Charcoal
        HTPyrolyzingRecipeBuilder
            .create(
                itemCreator.fromItem(HCItems.COMPRESSED_SAWDUST, 8),
                HTMaterialResultHelper.item(CommonTagPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL, 8),
                fluidResult.create(RagiumFluids.CREOSOTE, 1000),
            ).setTime(20 * 10)
            .saveSuffixed(output, "_from_sawdust")

        // Coal -> Coke + Creosote
        HTPyrolyzingRecipeBuilder
            .create(
                itemCreator.fromTagKey(CommonTagPrefixes.FUEL, VanillaMaterialKeys.COAL, 8),
                HTMaterialResultHelper.item(CommonTagPrefixes.FUEL, CommonMaterialKeys.COAL_COKE, 8),
                fluidResult.create(RagiumFluids.CREOSOTE, 2000),
            ).saveSuffixed(output, "_from_coal")

        HTPyrolyzingRecipeBuilder
            .create(
                itemCreator.fromTagKey(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL, 8),
                HTMaterialResultHelper.item(CommonTagPrefixes.DUST, CommonMaterialKeys.COAL_COKE, 8),
                fluidResult.create(RagiumFluids.CREOSOTE, 2000),
            ).saveSuffixed(output, "_from_coal_dust")

        HTPyrolyzingRecipeBuilder
            .create(
                itemCreator.fromTagKey(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.COAL),
                HTMaterialResultHelper.item(CommonTagPrefixes.BLOCK, CommonMaterialKeys.COAL_COKE),
                fluidResult.create(RagiumFluids.CREOSOTE, 2000),
            ).saveSuffixed(output, "_from_coal_block")

        // Crimson Stem -> Crimson Blood
        HTPyrolyzingRecipeBuilder
            .create(
                itemCreator.fromTagKey(ItemTags.CRIMSON_STEMS, 8),
                HTMaterialResultHelper.item(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR),
                createFluidResult(HCMaterialKeys.CRIMSON_CRYSTAL, HTMaterialPropertyKeys.MOLTEN_FLUID),
            ).saveSuffixed(output, "_from_log")
        // Warped Stem -> Dew of the Warp
        HTPyrolyzingRecipeBuilder
            .create(
                itemCreator.fromTagKey(ItemTags.WARPED_STEMS, 8),
                HTMaterialResultHelper.item(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR),
                createFluidResult(HCMaterialKeys.WARPED_CRYSTAL, HTMaterialPropertyKeys.MOLTEN_FLUID),
            ).saveSuffixed(output, "_from_log")
    }
}
