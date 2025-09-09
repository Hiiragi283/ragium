package hiiragi283.ragium.data.server.recipe.compat

import appeng.core.definitions.AEBlocks
import appeng.core.definitions.AEItems
import appeng.core.definitions.BlockDefinition
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemWithCatalystToItemRecipeBuilder
import hiiragi283.ragium.api.material.HTItemMaterialVariant
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.material.HTCommonMaterialTypes
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
                HTIngredientHelper.item(AEBlocks.QUARTZ_BLOCK),
                HTIngredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 8000),
                HTResultHelper.INSTANCE.item(AEBlocks.FLAWLESS_BUDDING_QUARTZ),
            ).save(output)
        // Fluix Crystal
        combineWithRedstone(
            HTResultHelper.INSTANCE.item(HTItemMaterialVariant.GEM, HTCommonMaterialTypes.getGem("fluix"), 2),
            HTIngredientHelper.gemOrDust(HTCommonMaterialTypes.getGem("certus_quartz")),
            HTIngredientHelper.gemOrDust(HTVanillaMaterialType.QUARTZ),
        )
        // Sky Stone
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(AEBlocks.SKY_STONE_BLOCK),
                HTResultHelper.INSTANCE.item(AEItems.SKY_DUST),
            ).save(output)

        // Processor
        combineWithRedstone(
            HTResultHelper.INSTANCE.item(AEItems.LOGIC_PROCESSOR),
            HTIngredientHelper.item(AEItems.LOGIC_PROCESSOR_PRINT),
            HTIngredientHelper.item(AEItems.SILICON_PRINT),
        )
        combineWithRedstone(
            HTResultHelper.INSTANCE.item(AEItems.CALCULATION_PROCESSOR),
            HTIngredientHelper.item(AEItems.CALCULATION_PROCESSOR_PRINT),
            HTIngredientHelper.item(AEItems.SILICON_PRINT),
        )
        combineWithRedstone(
            HTResultHelper.INSTANCE.item(AEItems.ENGINEERING_PROCESSOR),
            HTIngredientHelper.item(AEItems.ENGINEERING_PROCESSOR_PRINT),
            HTIngredientHelper.item(AEItems.SILICON_PRINT),
        )
    }

    @JvmStatic
    private fun certusBudding(budding: BlockDefinition<*>, count: Int) {
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                null,
                HTIngredientHelper.item(budding),
                HTResultHelper.INSTANCE.item(AEItems.CERTUS_QUARTZ_CRYSTAL, count),
            ).saveSuffixed(output, "_from_${budding.id().path.removeSuffix("_budding_quartz")}")
    }

    @JvmStatic
    private fun combineWithRedstone(result: HTItemResult, left: HTItemIngredient, right: HTItemIngredient) {
        HTCombineItemToObjRecipeBuilder
            .alloying(
                result,
                left,
                HTIngredientHelper.item(HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE),
                right,
            ).save(output)
    }
}
