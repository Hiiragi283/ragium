package hiiragi283.ragium.common.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.common.data.recipe.base.HTComplexResultRecipeBuilder
import hiiragi283.ragium.common.recipe.HTBasicExtractingRecipe

class HTExtractingRecipeBuilder(private val ingredient: HTItemIngredient) :
    HTComplexResultRecipeBuilder<HTExtractingRecipeBuilder>(RagiumConst.EXTRACTING) {
    companion object {
        @JvmStatic
        fun create(ingredient: HTItemIngredient): HTExtractingRecipeBuilder = HTExtractingRecipeBuilder(ingredient)
    }

    override fun createRecipe(): HTBasicExtractingRecipe = HTBasicExtractingRecipe(ingredient, toIorResult())
}
