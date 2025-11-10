package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTChancedItemResult
import hiiragi283.ragium.impl.recipe.HTCrushingRecipe

class HTItemToChancedItemRecipeBuilder(prefix: String, private val factory: Factory<*>, val ingredient: HTItemIngredient) :
    HTChancedItemRecipeBuilder<HTItemToChancedItemRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun crushing(ingredient: HTItemIngredient): HTItemToChancedItemRecipeBuilder =
            HTItemToChancedItemRecipeBuilder(RagiumConst.CRUSHING, ::HTCrushingRecipe, ingredient)
    }

    override fun createRecipe(): HTItemToChancedItemRecipe = factory.create(ingredient, results)

    fun interface Factory<RECIPE : HTItemToChancedItemRecipe> {
        fun create(ingredient: HTItemIngredient, results: List<HTChancedItemResult>): RECIPE
    }
}
