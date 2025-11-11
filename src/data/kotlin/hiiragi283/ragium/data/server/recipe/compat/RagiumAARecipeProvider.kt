package hiiragi283.ragium.data.server.recipe.compat

import de.ellpeck.actuallyadditions.api.ActuallyTags
import de.ellpeck.actuallyadditions.mod.fluids.InitFluids
import de.ellpeck.actuallyadditions.mod.items.ActuallyItems
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.impl.data.recipe.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluidContents

object RagiumAARecipeProvider : HTRecipeProvider.Integration(RagiumConst.ACTUALLY) {
    override fun buildRecipeInternal() {
        // Rice Slimeball
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(ActuallyItems.RICE_DOUGH),
                fluidCreator.water(250),
            ).addResult(resultHelper.item(ActuallyItems.RICE_SLIMEBALL))
            .save(output)
        // Solidified Exp -> Liquid Exp
        HTItemToObjRecipeBuilder
            .melting(
                itemCreator.fromItem(ActuallyItems.SOLIDIFIED_EXPERIENCE),
                resultHelper.fluid(RagiumFluidContents.EXPERIENCE, 8 * 20),
            ).saveSuffixed(output, "_from_solidified_aa")

        canola()
    }

    @JvmStatic
    private fun canola() {
        // Canola -> Canola Oil
        HTItemToObjRecipeBuilder
            .melting(
                itemCreator.fromTagKey(ActuallyTags.Items.CROPS_CANOLA),
                resultHelper.fluid(InitFluids.CANOLA_OIL, 80),
            ).save(output)
        // Canola Oil -> Refined
        HTFluidTransformRecipeBuilder
            .refining(
                fluidCreator.from(InitFluids.CANOLA_OIL.get(), 80),
                resultHelper.fluid(InitFluids.REFINED_CANOLA_OIL, 80),
                null,
                null,
            ).save(output)
        // Refined -> Crystallized
        HTFluidTransformRecipeBuilder
            .refining(
                fluidCreator.from(InitFluids.REFINED_CANOLA_OIL.get(), 1000),
                resultHelper.fluid(InitFluids.CRYSTALLIZED_OIL, 1000),
                itemCreator.fromItem(ActuallyItems.CRYSTALLIZED_CANOLA_SEED),
                null,
            ).save(output)
        // Crystallized -> Empowered
        HTFluidTransformRecipeBuilder
            .refining(
                fluidCreator.from(InitFluids.CRYSTALLIZED_OIL.get(), 1000),
                resultHelper.fluid(InitFluids.EMPOWERED_OIL, 1000),
                itemCreator.fromItem(ActuallyItems.EMPOWERED_CANOLA_SEED),
                null,
            ).save(output)
    }
}
