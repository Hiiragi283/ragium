package hiiragi283.ragium.data.server.recipe.compat

import de.ellpeck.actuallyadditions.api.ActuallyTags
import de.ellpeck.actuallyadditions.mod.fluids.InitFluids
import de.ellpeck.actuallyadditions.mod.items.ActuallyItems
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluidContents

object RagiumAARecipeProvider : HTRecipeProvider.Integration(RagiumConst.ACTUALLY) {
    override fun buildRecipeInternal() {
        // Rice Slimeball
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(ActuallyItems.RICE_DOUGH),
                HTIngredientHelper.water(250),
                HTResultHelper.INSTANCE.item(ActuallyItems.RICE_SLIMEBALL),
            ).save(output)
        // Solidified Exp -> Liquid Exp
        HTItemToObjRecipeBuilder
            .melting(
                HTIngredientHelper.item(ActuallyItems.SOLIDIFIED_EXPERIENCE),
                HTResultHelper.INSTANCE.fluid(RagiumFluidContents.EXPERIENCE, 8 * 20),
            ).saveSuffixed(output, "_from_solidified_aa")

        // Black Quartz
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(ActuallyTags.Items.STORAGE_BLOCKS_BLACK_QUARTZ),
                HTResultHelper.INSTANCE.item(ActuallyTags.Items.GEMS_BLACK_QUARTZ, 4),
            ).save(output)

        canola()
    }

    @JvmStatic
    private fun canola() {
        // Canola -> Canola Oil
        HTItemToObjRecipeBuilder
            .melting(
                HTIngredientHelper.item(ActuallyTags.Items.CROPS_CANOLA),
                HTResultHelper.INSTANCE.fluid(InitFluids.CANOLA_OIL, 80),
            ).save(output)
        // Canola Oil -> Refined
        HTFluidTransformRecipeBuilder
            .refining(
                HTIngredientHelper.fluid(InitFluids.CANOLA_OIL.get(), 80),
                HTResultHelper.INSTANCE.fluid(InitFluids.REFINED_CANOLA_OIL, 80),
                null,
                null,
            ).save(output)
        // Refined -> Crystallized
        HTFluidTransformRecipeBuilder
            .refining(
                HTIngredientHelper.fluid(InitFluids.REFINED_CANOLA_OIL.get(), 1000),
                HTResultHelper.INSTANCE.fluid(InitFluids.CRYSTALLIZED_OIL, 1000),
                HTIngredientHelper.item(ActuallyItems.CRYSTALLIZED_CANOLA_SEED),
                null,
            ).save(output)
        // Crystallized -> Empowered
        HTFluidTransformRecipeBuilder
            .refining(
                HTIngredientHelper.fluid(InitFluids.CRYSTALLIZED_OIL.get(), 1000),
                HTResultHelper.INSTANCE.fluid(InitFluids.EMPOWERED_OIL, 1000),
                HTIngredientHelper.item(ActuallyItems.EMPOWERED_CANOLA_SEED),
                null,
            ).save(output)
    }
}
