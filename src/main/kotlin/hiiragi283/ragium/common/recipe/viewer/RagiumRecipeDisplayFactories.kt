package hiiragi283.ragium.common.recipe.viewer

import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.impl.recipe.HTBasicItemOrFluidRecipe
import hiiragi283.core.impl.recipe.viewer.display.HTRecipeDisplayFactories
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import hiiragi283.ragium.common.recipe.HTChemicalReactingRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.RTPlantingRecipe

/**
 * @see hiiragi283.core.common.recipe.viewer.display.HCRecipeDisplayFactories
 */
data object RagiumRecipeDisplayFactories {
    @JvmStatic
    fun itemOrFluid(holder: HTRecipeHolder<out HTBasicItemOrFluidRecipe>): HTProgressRecipeDisplay =
        HTRecipeDisplayFactories.progress(holder) {
            addInput(it.ingredient.getLeft())
            addInput(it.ingredient.getRight())
            it.result.getLeft()?.let(::addOutput)
            it.result.getRight()?.let(::addOutput)
        }

    // Machine - Basic
    @JvmStatic
    fun alloying(holder: HTRecipeHolder<HTAlloyingRecipe>): HTProgressRecipeDisplay = HTRecipeDisplayFactories.progress(holder) {
        addInput(it.primary)
        addInput(it.secondary)
        addInput(it.tertiary)
        addOutput(it.result)
    }

    @JvmStatic
    fun assembling(holder: HTRecipeHolder<HTAssemblingRecipe>): HTProgressRecipeDisplay = HTRecipeDisplayFactories.progress(holder) {
        addInput(it.primary)
        addInput(it.secondary)
        addOutput(it.result)
    }

    @JvmStatic
    fun planting(holder: HTRecipeHolder<RTPlantingRecipe>): HTProgressRecipeDisplay = HTRecipeDisplayFactories.progress(holder) {
        addInput(it.plant)
        addCatalyst(it.soil)
        it.results.forEach(::addOutput)
    }

    // Machine - Advanced
    @JvmStatic
    fun freezing(holder: HTRecipeHolder<HTFreezingRecipe>): HTProgressRecipeDisplay = HTRecipeDisplayFactories.progress(holder) {
        addInput(it.ingredient)
        addCatalyst(it.catalyst)
        addOutput(it.result)
    }

    @JvmStatic
    fun melting(holder: HTRecipeHolder<HTMeltingRecipe>): HTProgressRecipeDisplay = HTRecipeDisplayFactories.progress(holder) {
        addInput(it.ingredient)
        addOutput(it.result)
    }

    @JvmStatic
    fun washing(holder: HTRecipeHolder<HTWashingRecipe>): HTProgressRecipeDisplay = HTRecipeDisplayFactories.progress(holder) {
        addInput(it.ingredient)
        addInput(HTWashingRecipe.WATER_INGREDIENT)
        it.results.forEach(::addOutput)
    }

    // Machine - Elite
    @JvmStatic
    fun reacting(holder: HTRecipeHolder<HTChemicalReactingRecipe>): HTProgressRecipeDisplay = HTRecipeDisplayFactories.progress(holder) {
        addInput(it.primary)
        addInput(it.secondary.getLeft())
        addCatalyst(it.secondary.getRight())
        it.fluidResults.forEach(::addOutput)
        it.itemResult.ifPresent(::addOutput)
    }

    @JvmStatic
    fun mixing(holder: HTRecipeHolder<HTMixingRecipe>): HTProgressRecipeDisplay = HTRecipeDisplayFactories.progress(holder) {
        addInput(it.primary)
        addInput(it.secondary)
        addInput(it.fluidIngredient)
        it.result.getLeft()?.let(::addOutput)
        it.result.getRight()?.let(::addOutput)
    }

    // Machine - Ultimate

    // Device - Ultimate
}
