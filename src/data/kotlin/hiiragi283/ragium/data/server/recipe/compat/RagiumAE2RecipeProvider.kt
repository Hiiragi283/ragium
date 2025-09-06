package hiiragi283.ragium.data.server.recipe.compat

import appeng.core.definitions.AEBlocks
import appeng.core.definitions.AEItems
import appeng.core.definitions.BlockDefinition
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemWithCatalystToItemRecipeBuilder
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
}
