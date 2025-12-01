package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.recipe.chance.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.impl.data.recipe.base.HTChancedItemRecipeBuilder
import hiiragi283.ragium.impl.recipe.HTCrushingRecipe
import hiiragi283.ragium.impl.recipe.HTCuttingRecipe
import hiiragi283.ragium.impl.recipe.HTPulverizingRecipe
import org.apache.commons.lang3.math.Fraction

class HTItemToChancedItemRecipeBuilder(prefix: String, private val factory: Factory<*>, val ingredient: HTItemIngredient) :
    HTChancedItemRecipeBuilder<HTItemToChancedItemRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun crushing(ingredient: HTItemIngredient): HTItemToChancedItemRecipeBuilder = HTItemToChancedItemRecipeBuilder(
            RagiumConst.CRUSHING,
            { ingredient: HTItemIngredient, results: List<HTItemResultWithChance> ->
                if (results.size == 1 && results[0].chance == Fraction.ONE) {
                    HTPulverizingRecipe(ingredient, results[0].base)
                } else {
                    HTCrushingRecipe(ingredient, results)
                }
            },
            ingredient,
        )

        @JvmStatic
        fun cutting(ingredient: HTItemIngredient): HTItemToChancedItemRecipeBuilder =
            HTItemToChancedItemRecipeBuilder(RagiumConst.CUTTING, ::HTCuttingRecipe, ingredient)
    }

    override fun createRecipe(): HTItemToChancedItemRecipe = factory.create(ingredient, results)

    fun interface Factory<RECIPE : HTItemToChancedItemRecipe> {
        fun create(ingredient: HTItemIngredient, results: List<HTItemResultWithChance>): RECIPE
    }
}
