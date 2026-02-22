package hiiragi283.ragium.client.jei.category.base

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.client.jei.category.HTHolderRecipeCategory
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeHolder

abstract class HTProcessingRecipeCategory<RECIPE : HTProcessingRecipe<*>>(
    guiHelper: IGuiHelper,
    recipeType: HTRecipeViewerType<RecipeHolder<RECIPE>>,
) : HTHolderRecipeCategory<RECIPE>(guiHelper, recipeType) {
    final override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<RECIPE>, focuses: IFocusGroup) {
        createRecipeExtras(builder, recipe.value, focuses)
    }

    protected abstract fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup)
}
