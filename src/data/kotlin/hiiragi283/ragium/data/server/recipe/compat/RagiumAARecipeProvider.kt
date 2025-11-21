package hiiragi283.ragium.data.server.recipe.compat

import de.ellpeck.actuallyadditions.api.ActuallyTags
import de.ellpeck.actuallyadditions.mod.fluids.InitFluids
import de.ellpeck.actuallyadditions.mod.items.ActuallyItems
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.impl.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystRecipeBuilder
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
        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromItem(ActuallyItems.SOLIDIFIED_EXPERIENCE),
                null,
                null,
                resultHelper.fluid(RagiumFluidContents.EXPERIENCE, 8 * 20),
            ).saveSuffixed(output, "_from_solidified_aa")

        // Crops
        cropAndSeed(ActuallyItems.CANOLA_SEEDS, ActuallyItems.CANOLA)
        cropAndSeed(ActuallyItems.COFFEE_BEANS, ActuallyItems.COFFEE_BEANS)
        cropAndSeed(ActuallyItems.RICE_SEEDS, ActuallyItems.RICE)

        canola()
    }

    @JvmStatic
    private fun canola() {
        // Canola -> Canola Oil
        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(ActuallyTags.Items.CROPS_CANOLA),
                null,
                null,
                resultHelper.fluid(InitFluids.CANOLA_OIL, 80),
            ).save(output)
        // Canola Oil -> Refined
        HTComplexRecipeBuilder
            .refining(
                fluidCreator.from(InitFluids.CANOLA_OIL.get(), 80),
                resultHelper.fluid(InitFluids.REFINED_CANOLA_OIL, 80),
                null,
                null,
            ).save(output)
        // Refined -> Crystallized
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(ActuallyItems.CRYSTALLIZED_CANOLA_SEED))
            .addIngredient(fluidCreator.from(InitFluids.REFINED_CANOLA_OIL.get(), 1000))
            .setResult(resultHelper.fluid(InitFluids.CRYSTALLIZED_OIL, 1000))
            .save(output)
        // Crystallized -> Empowered
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(ActuallyItems.EMPOWERED_CANOLA_SEED))
            .addIngredient(fluidCreator.from(InitFluids.CRYSTALLIZED_OIL.get(), 1000))
            .setResult(resultHelper.fluid(InitFluids.EMPOWERED_OIL, 1000))
            .save(output)
    }
}
