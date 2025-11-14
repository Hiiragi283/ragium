package hiiragi283.ragium.data.server.recipe.compat

import appeng.core.definitions.AEBlocks
import appeng.core.definitions.AEItems
import appeng.core.definitions.BlockDefinition
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.ModMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluidContents

object RagiumAE2RecipeProvider : HTRecipeProvider.Integration(RagiumConst.AE2) {
    override fun buildRecipeInternal() {
        // Certus Quartz
        certusBudding(AEBlocks.FLAWLESS_BUDDING_QUARTZ, 4)
        certusBudding(AEBlocks.FLAWED_BUDDING_QUARTZ, 3)
        certusBudding(AEBlocks.CHIPPED_BUDDING_QUARTZ, 2)
        certusBudding(AEBlocks.DAMAGED_BUDDING_QUARTZ, 1)

        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(AEBlocks.QUARTZ_BLOCK),
                fluidCreator.fromContent(RagiumFluidContents.ELDRITCH_FLUX, 8000),
            ).addResult(resultHelper.item(AEBlocks.FLAWLESS_BUDDING_QUARTZ))
            .save(output)
        // Fluix Crystal
        combineWithRedstone(
            resultHelper.item(CommonMaterialPrefixes.GEM, ModMaterialKeys.Gems.FLUIX, 2),
            itemCreator.fromItem(AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED),
            itemCreator.multiPrefixes(VanillaMaterialKeys.QUARTZ, CommonMaterialPrefixes.DUST, CommonMaterialPrefixes.GEM),
        )
        // Sky Stone
        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromItem(AEBlocks.SKY_STONE_BLOCK),
                resultHelper.item(AEItems.SKY_DUST),
            ).save(output)

        // Processor
        combineWithRedstone(
            resultHelper.item(AEItems.LOGIC_PROCESSOR),
            itemCreator.fromItem(AEItems.LOGIC_PROCESSOR_PRINT),
            itemCreator.fromItem(AEItems.SILICON_PRINT),
        )
        combineWithRedstone(
            resultHelper.item(AEItems.CALCULATION_PROCESSOR),
            itemCreator.fromItem(AEItems.CALCULATION_PROCESSOR_PRINT),
            itemCreator.fromItem(AEItems.SILICON_PRINT),
        )
        combineWithRedstone(
            resultHelper.item(AEItems.ENGINEERING_PROCESSOR),
            itemCreator.fromItem(AEItems.ENGINEERING_PROCESSOR_PRINT),
            itemCreator.fromItem(AEItems.SILICON_PRINT),
        )
    }

    @JvmStatic
    private fun certusBudding(budding: BlockDefinition<*>, count: Int) {
        HTItemWithCatalystRecipeBuilder
            .simulating(
                null,
                itemCreator.fromItem(budding),
                resultHelper.item(AEItems.CERTUS_QUARTZ_CRYSTAL, count),
            ).saveSuffixed(output, "_from_${budding.id().path.removeSuffix("_budding_quartz")}")
    }

    @JvmStatic
    private fun combineWithRedstone(result: HTItemResult, left: HTItemIngredient, right: HTItemIngredient) {
        HTCombineItemToObjRecipeBuilder
            .alloying(
                result,
                left,
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE),
                right,
            ).save(output)
    }
}
