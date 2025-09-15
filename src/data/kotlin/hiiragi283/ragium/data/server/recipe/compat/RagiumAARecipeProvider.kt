package hiiragi283.ragium.data.server.recipe.compat

import de.ellpeck.actuallyadditions.api.ActuallyTags
import de.ellpeck.actuallyadditions.mod.fluids.InitFluids
import de.ellpeck.actuallyadditions.mod.items.ActuallyItems
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.impl.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluidContents

object RagiumAARecipeProvider : HTRecipeProvider.Integration(RagiumConst.ACTUALLY) {
    override fun buildRecipeInternal() {
        // Rice Slimeball
        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(ActuallyItems.RICE_DOUGH),
                ingredientHelper.water(250),
                resultHelper.item(ActuallyItems.RICE_SLIMEBALL),
            ).save(output)
        // Solidified Exp -> Liquid Exp
        HTItemToObjRecipeBuilder
            .melting(
                ingredientHelper.item(ActuallyItems.SOLIDIFIED_EXPERIENCE),
                resultHelper.fluid(RagiumFluidContents.EXPERIENCE, 8 * 20),
            ).saveSuffixed(output, "_from_solidified_aa")

        // Black Quartz
        HTItemToObjRecipeBuilder
            .pulverizing(
                ingredientHelper.item(ActuallyTags.Items.STORAGE_BLOCKS_BLACK_QUARTZ),
                resultHelper.item(ActuallyTags.Items.GEMS_BLACK_QUARTZ, 4),
            ).save(output)

        canola()
    }

    @JvmStatic
    private fun canola() {
        // Canola -> Canola Oil
        HTItemToObjRecipeBuilder
            .melting(
                ingredientHelper.item(ActuallyTags.Items.CROPS_CANOLA),
                resultHelper.fluid(InitFluids.CANOLA_OIL, 80),
            ).save(output)
        // Canola Oil -> Refined
        HTFluidTransformRecipeBuilder
            .refining(
                ingredientHelper.fluid(InitFluids.CANOLA_OIL.get(), 80),
                resultHelper.fluid(InitFluids.REFINED_CANOLA_OIL, 80),
                null,
                null,
            ).save(output)
        // Refined -> Crystallized
        HTFluidTransformRecipeBuilder
            .refining(
                ingredientHelper.fluid(InitFluids.REFINED_CANOLA_OIL.get(), 1000),
                resultHelper.fluid(InitFluids.CRYSTALLIZED_OIL, 1000),
                ingredientHelper.item(ActuallyItems.CRYSTALLIZED_CANOLA_SEED),
                null,
            ).save(output)
        // Crystallized -> Empowered
        HTFluidTransformRecipeBuilder
            .refining(
                ingredientHelper.fluid(InitFluids.CRYSTALLIZED_OIL.get(), 1000),
                resultHelper.fluid(InitFluids.EMPOWERED_OIL, 1000),
                ingredientHelper.item(ActuallyItems.EMPOWERED_CANOLA_SEED),
                null,
            ).save(output)
    }
}
