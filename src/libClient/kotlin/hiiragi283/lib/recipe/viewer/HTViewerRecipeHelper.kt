package hiiragi283.lib.recipe.viewer

import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.base.HTBasicSingleRecipe
import hiiragi283.lib.recipe.base.HTCatalysedSingleRecipe
import hiiragi283.lib.recipe.base.HTFluidSeparatingRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import hiiragi283.lib.recipe.base.HTItemOrFluidToRecipe
import hiiragi283.lib.recipe.base.HTItemSeparatingRecipe
import hiiragi283.lib.recipe.base.HTItemToRecipe
import hiiragi283.lib.recipe.base.HTSizedShapelessRecipe
import hiiragi283.lib.recipe.display.HTDisplayableContent
import hiiragi283.lib.recipe.display.display
import hiiragi283.lib.recipe.display.displayOrEmpty
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.util.toOptional

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
    fun singleToMultiItem(holder: HTRecipeHolder<HTItemToRecipe.BasicMultiItem>): HTSingleToMultiViewerRecipe {
        val (id: RecipeKey, recipe: HTItemToRecipe.BasicMultiItem) = holder
        return HTSingleToMultiViewerRecipe(
            id.identifier(),
            recipe.ingredient.display(),
            recipe.results.map(HTDisplayableContent::display),
            recipe.progressData,
        )
    }

    @JvmStatic
    fun singleToItemAndFluid(
        holder: HTRecipeHolder<HTBasicSingleRecipe<HTDisplayableContent, HTItemOrFluidResult>>,
    ): HTSingleToItemAndFluidViewerRecipe {
        val (id: RecipeKey, recipe: HTBasicSingleRecipe<HTDisplayableContent, HTItemOrFluidResult>) = holder
        val (item: HTDisplayableContent?, fluid: HTDisplayableContent?) = recipe.result.content.toPair()
        return HTSingleToItemAndFluidViewerRecipe(
            id.identifier(),
            recipe.ingredient.display(),
            item.displayOrEmpty(),
            fluid.displayOrEmpty(),
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
            recipe.result.fluids.map(HTDisplayableContent::display),
            recipe.progressData,
        )
    }

    @JvmStatic
    fun itemSeparating(holder: HTRecipeHolder<HTItemSeparatingRecipe.Basic>): HTItemSeparatingViewerRecipe {
        val (id: RecipeKey, recipe: HTItemSeparatingRecipe.Basic) = holder
        return HTItemSeparatingViewerRecipe(
            id.identifier(),
            recipe.ingredient.display(),
            recipe.result.content.mapLeft { it.map(HTDisplayableContent::display) }.mapRight(HTDisplayableContent::display),
            recipe.progressData,
        )
    }

    @JvmStatic
    fun itemOrFluid(holder: HTRecipeHolder<HTItemOrFluidToRecipe.BasicItemAndFluid>): HTItemOrFluidViewerRecipe {
        val (id: RecipeKey, recipe: HTItemOrFluidToRecipe.BasicItemAndFluid) = holder
        val (item: HTDisplayableContent?, fluid: HTDisplayableContent?) = recipe.result.content.toPair()
        return HTItemOrFluidViewerRecipe(
            id.identifier(),
            recipe.ingredient.mapLeft(HTDisplayableContent::display).leftOrNull().toOptional(),
            recipe.ingredient.map(HTDisplayableContent::display).getOrNull().toOptional(),
            item.displayOrEmpty(),
            fluid.displayOrEmpty(),
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
            recipe.ingredients.map(HTDisplayableContent::display),
            recipe.result.display(),
            recipe.progressData,
        )
    }
}
