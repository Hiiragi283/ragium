package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.recipe.chance.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.impl.recipe.HTCrushingRecipe
import hiiragi283.ragium.impl.recipe.HTCuttingRecipe

class HTItemToChancedItemRecipeBuilder(prefix: String, private val factory: Factory<*>, val ingredient: HTItemIngredient) :
    HTChancedItemRecipeBuilder<HTItemToChancedItemRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun crushing(ingredient: HTItemIngredient): HTItemToChancedItemRecipeBuilder =
            HTItemToChancedItemRecipeBuilder(RagiumConst.CRUSHING, ::HTCrushingRecipe, ingredient)

        @JvmStatic
        fun cutting(ingredient: HTItemIngredient): HTItemToChancedItemRecipeBuilder =
            HTItemToChancedItemRecipeBuilder(RagiumConst.CUTTING, ::HTCuttingRecipe, ingredient)
    }

    override fun createRecipe(): HTItemToChancedItemRecipe = factory.create(ingredient, results)

    fun interface Factory<RECIPE : HTItemToChancedItemRecipe> {
        fun create(ingredient: HTItemIngredient, results: List<HTItemResultWithChance>): RECIPE
    }
}
