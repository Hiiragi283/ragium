package hiiragi283.lib.recipe.viewer

import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.base.HTBasicSingleRecipe
import hiiragi283.lib.recipe.base.HTCatalysedSingleRecipe
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.base.HTFluidSeparatingRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import hiiragi283.lib.recipe.base.HTItemSeparatingRecipe
import hiiragi283.lib.recipe.base.HTItemToRecipe
import hiiragi283.lib.recipe.base.HTSizedShapelessRecipe
import hiiragi283.lib.recipe.display.HTDisplayableContent
import hiiragi283.lib.recipe.display.display
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
data object HTViewerRecipeHelper {
    // Single
    @JvmStatic
    fun singleToSingle(holder: HTRecipeHolder<HTBasicSingleRecipe<HTDisplayableContent, HTDisplayableContent>>): HTSingleToSingleViewerRecipe {
        val (id: RecipeKey, recipe: HTBasicSingleRecipe<HTDisplayableContent, HTDisplayableContent>) = holder
        return HTSingleToSingleViewerRecipe(id.identifier(), recipe.ingredient.display(), recipe.result.display(), recipe.progressData)
    }

    @JvmStatic
    fun singleItemToMultiItem(holder: HTRecipeHolder<HTItemToRecipe.BasicMultiItem>): HTSingleToMultiViewerRecipe {
        val (id: RecipeKey, recipe: HTItemToRecipe.BasicMultiItem) = holder
        return HTSingleToMultiViewerRecipe(
            id.identifier(),
            recipe.ingredient.display(),
            recipe.results.map(HTItemResult::display),
            recipe.progressData,
        )
    }

    @JvmStatic
    fun fluidSeparating(holder: HTRecipeHolder<HTFluidSeparatingRecipe>): HTFluidSeparatingViewerRecipe {
        val (id: RecipeKey, recipe: HTFluidSeparatingRecipe) = holder
        return HTFluidSeparatingViewerRecipe(
            id.identifier(),
            recipe.ingredient.display(),
            recipe.result.item.display(),
            recipe.result.fluids.map(HTFluidResult::display),
            recipe.progressData,
        )
    }

    @JvmStatic
    fun itemSeparating(holder: HTRecipeHolder<HTItemSeparatingRecipe.Basic>): HTItemSeparatingViewerRecipe {
        val (id: RecipeKey, recipe: HTItemSeparatingRecipe.Basic) = holder
        return HTItemSeparatingViewerRecipe(
            id.identifier(),
            recipe.ingredient.display(),
            recipe.result.items.map(HTItemResult::display),
            recipe.result.fluid.display(),
            recipe.progressData,
        )
    }

    // Double
    @JvmStatic
    fun catalysedSingleToSingle(
        holder: HTRecipeHolder<HTCatalysedSingleRecipe<HTDisplayableContent, HTDisplayableContent>>,
    ): HTCatalysedSingleToSingleViewerRecipe {
        val (id: RecipeKey, recipe: HTCatalysedSingleRecipe<HTDisplayableContent, HTDisplayableContent>) = holder
        return HTCatalysedSingleToSingleViewerRecipe(
            id.identifier(),
            recipe.ingredient.display(),
            recipe.catalyst.display(),
            recipe.result.display(),
            recipe.progressData,
        )
    }

    @JvmStatic
    fun doubleItemToItem(holder: HTRecipeHolder<HTDoubleItemToItemRecipe.Basic>): HTDoubleToSingleViewerRecipe {
        val (id: RecipeKey, recipe: HTDoubleItemToItemRecipe.Basic) = holder
        return HTDoubleToSingleViewerRecipe(
            id.identifier(),
            recipe.primary.display(),
            recipe.secondary.display(),
            recipe.result.display(),
            recipe.progressData,
        )
    }

    @JvmStatic
    fun itemAndFluidToFluid(holder: HTRecipeHolder<HTItemAndFluidToRecipe.BasicFluid>): HTDoubleToSingleViewerRecipe {
        val (id: RecipeKey, recipe: HTItemAndFluidToRecipe.BasicFluid) = holder
        return HTDoubleToSingleViewerRecipe(
            id.identifier(),
            recipe.itemIngredient.display(),
            recipe.fluidIngredient.display(),
            recipe.result.display(),
            recipe.progressData,
        )
    }

    @JvmStatic
    fun itemAndFluidToItem(holder: HTRecipeHolder<HTItemAndFluidToRecipe.BasicItem>): HTDoubleToSingleViewerRecipe {
        val (id: RecipeKey, recipe: HTItemAndFluidToRecipe.BasicItem) = holder
        return HTDoubleToSingleViewerRecipe(
            id.identifier(),
            recipe.itemIngredient.display(),
            recipe.fluidIngredient.display(),
            recipe.result.display(),
            recipe.progressData,
        )
    }

    // Multi
    @JvmStatic
    fun sizedShapeless(holder: HTRecipeHolder<HTSizedShapelessRecipe.Basic>): HTMultiToSingleViewerRecipe {
        val (id: RecipeKey, recipe: HTSizedShapelessRecipe.Basic) = holder
        return HTMultiToSingleViewerRecipe(
            id.identifier(),
            recipe.ingredients.map(HTItemIngredient::display),
            recipe.result.display(),
            recipe.progressData,
        )
    }
}
