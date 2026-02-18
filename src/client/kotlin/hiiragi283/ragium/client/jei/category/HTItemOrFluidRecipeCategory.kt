package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.integration.jei.HTJeiHolderRecipeType
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.monad.toIor
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.client.jei.category.base.HTIorToIorRecipeCategory
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.base.HTItemOrFluidRecipe
import mezz.jei.api.helpers.IGuiHelper

class HTItemOrFluidRecipeCategory<RECIPE : HTItemOrFluidRecipe>(guiHelper: IGuiHelper, recipeType: HTJeiHolderRecipeType<RECIPE>) :
    HTIorToIorRecipeCategory<RECIPE>(guiHelper, recipeType) {
    companion object {
        @JvmStatic
        fun freezing(guiHelper: IGuiHelper): HTItemOrFluidRecipeCategory<HTFreezingRecipe> =
            HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.FREEZING)

        @JvmStatic
        fun melting(guiHelper: IGuiHelper): HTItemOrFluidRecipeCategory<HTMeltingRecipe> =
            HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.MELTING)
    }

    override fun getIngredients(recipe: RECIPE): Ior<HTItemIngredient, HTFluidIngredient> = recipe.ingredient

    override fun getResults(recipe: RECIPE): Ior<HTItemResult, HTFluidResult> = recipe.result.toIor()
}
