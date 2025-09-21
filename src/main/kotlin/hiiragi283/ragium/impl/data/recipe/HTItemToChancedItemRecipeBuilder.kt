package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.impl.recipe.HTCrushingRecipe

class HTItemToChancedItemRecipeBuilder<RECIPE : HTItemToChancedItemRecipe>(
    prefix: String,
    private val factory: Factory<RECIPE>,
    val ingredient: HTItemIngredient,
) : HTChancedItemRecipeBuilder<RECIPE>(prefix) {
    companion object {
        @JvmStatic
        fun crushing(ingredient: HTItemIngredient): HTItemToChancedItemRecipeBuilder<HTCrushingRecipe> =
            HTItemToChancedItemRecipeBuilder(RagiumConst.CRUSHING, ::HTCrushingRecipe, ingredient)
    }

    override fun createRecipe(): RECIPE = factory.create(ingredient, results)

    fun interface Factory<RECIPE : HTItemToChancedItemRecipe> {
        fun create(ingredient: HTItemIngredient, results: List<HTChancedItemRecipe.ChancedResult>): RECIPE
    }
}
