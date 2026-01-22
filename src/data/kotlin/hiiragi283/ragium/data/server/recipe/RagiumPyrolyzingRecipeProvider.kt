package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.tag.CommonTagPrefixes
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
                inputCreator.create(ItemTags.LOGS_THAT_BURN, 8),
                itemResult.create(CommonTagPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL, 8),
                fluidResult.create(RagiumFluids.CREOSOTE, 1000),
            ).saveSuffixed(output, "_from_log")
        // Compressed Sawdust -> Charcoal
        HTPyrolyzingRecipeBuilder
            .create(
                inputCreator.create(HCItems.COMPRESSED_SAWDUST, 8),
                itemResult.create(CommonTagPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL, 8),
                fluidResult.create(RagiumFluids.CREOSOTE, 1000),
            ).setTime(20 * 10)
            .saveSuffixed(output, "_from_sawdust")

        // Coal -> Coke + Creosote
        HTPyrolyzingRecipeBuilder
            .create(
                inputCreator.create(CommonTagPrefixes.FUEL, VanillaMaterialKeys.COAL, 8),
                itemResult.create(CommonTagPrefixes.FUEL, CommonMaterialKeys.COAL_COKE, 8),
                fluidResult.create(RagiumFluids.CREOSOTE, 2000),
            ).saveSuffixed(output, "_from_coal")

        HTPyrolyzingRecipeBuilder
            .create(
                inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL, 8),
                itemResult.create(CommonTagPrefixes.DUST, CommonMaterialKeys.COAL_COKE, 8),
                fluidResult.create(RagiumFluids.CREOSOTE, 2000),
            ).saveSuffixed(output, "_from_coal_dust")

        HTPyrolyzingRecipeBuilder
            .create(
                inputCreator.create(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.COAL),
                itemResult.create(CommonTagPrefixes.BLOCK, CommonMaterialKeys.COAL_COKE),
                fluidResult.create(RagiumFluids.CREOSOTE, 2000),
            ).saveSuffixed(output, "_from_coal_block")

        // Crimson Stem -> Crimson Blood
        HTPyrolyzingRecipeBuilder
            .create(
                inputCreator.create(ItemTags.CRIMSON_STEMS, 8),
                itemResult.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR),
                createFluidResult(HCMaterialKeys.CRIMSON_CRYSTAL, HTMaterialPropertyKeys.MOLTEN_FLUID),
            ).saveSuffixed(output, "_from_log")
        // Warped Stem -> Dew of the Warp
        HTPyrolyzingRecipeBuilder
            .create(
                inputCreator.create(ItemTags.WARPED_STEMS, 8),
                itemResult.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR),
                createFluidResult(HCMaterialKeys.WARPED_CRYSTAL, HTMaterialPropertyKeys.MOLTEN_FLUID),
            ).saveSuffixed(output, "_from_log")
    }
}
