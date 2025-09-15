package hiiragi283.ragium.data.server.recipe.compat

import appeng.core.definitions.AEBlocks
import appeng.core.definitions.AEItems
import appeng.core.definitions.BlockDefinition
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.impl.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemWithCatalystToItemRecipeBuilder
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.material.HTCommonMaterialTypes
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.setup.RagiumFluidContents

object RagiumAE2RecipeProvider : HTRecipeProvider.Integration(RagiumConst.AE2) {
    override fun buildRecipeInternal() {
        // Certus Quartz
        certusBudding(AEBlocks.FLAWLESS_BUDDING_QUARTZ, 4)
        certusBudding(AEBlocks.FLAWED_BUDDING_QUARTZ, 3)
        certusBudding(AEBlocks.CHIPPED_BUDDING_QUARTZ, 2)
        certusBudding(AEBlocks.DAMAGED_BUDDING_QUARTZ, 1)

        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(AEBlocks.QUARTZ_BLOCK),
                ingredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 8000),
                resultHelper.item(AEBlocks.FLAWLESS_BUDDING_QUARTZ),
            ).save(output)
        // Fluix Crystal
        combineWithRedstone(
            resultHelper.item(HTItemMaterialVariant.GEM, HTCommonMaterialTypes.getGem("fluix"), 2),
            ingredientHelper.gemOrDust(HTCommonMaterialTypes.getGem("certus_quartz")),
            ingredientHelper.gemOrDust(HTVanillaMaterialType.QUARTZ),
        )
        // Sky Stone
        HTItemToObjRecipeBuilder
            .pulverizing(
                ingredientHelper.item(AEBlocks.SKY_STONE_BLOCK),
                resultHelper.item(AEItems.SKY_DUST),
            ).save(output)

        // Processor
        combineWithRedstone(
            resultHelper.item(AEItems.LOGIC_PROCESSOR),
            ingredientHelper.item(AEItems.LOGIC_PROCESSOR_PRINT),
            ingredientHelper.item(AEItems.SILICON_PRINT),
        )
        combineWithRedstone(
            resultHelper.item(AEItems.CALCULATION_PROCESSOR),
            ingredientHelper.item(AEItems.CALCULATION_PROCESSOR_PRINT),
            ingredientHelper.item(AEItems.SILICON_PRINT),
        )
        combineWithRedstone(
            resultHelper.item(AEItems.ENGINEERING_PROCESSOR),
            ingredientHelper.item(AEItems.ENGINEERING_PROCESSOR_PRINT),
            ingredientHelper.item(AEItems.SILICON_PRINT),
        )
    }

    @JvmStatic
    private fun certusBudding(budding: BlockDefinition<*>, count: Int) {
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                null,
                ingredientHelper.item(budding),
                resultHelper.item(AEItems.CERTUS_QUARTZ_CRYSTAL, count),
            ).saveSuffixed(output, "_from_${budding.id().path.removeSuffix("_budding_quartz")}")
    }

    @JvmStatic
    private fun combineWithRedstone(result: HTItemResult, left: HTItemIngredient, right: HTItemIngredient) {
        HTCombineItemToObjRecipeBuilder
            .alloying(
                result,
                left,
                ingredientHelper.item(HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE),
                right,
            ).save(output)
    }
}
