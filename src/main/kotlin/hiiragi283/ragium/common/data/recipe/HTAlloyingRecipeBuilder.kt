package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import net.minecraft.resources.ResourceLocation

class HTAlloyingRecipeBuilder(private val ingredients: List<HTItemIngredient>, private val result: HTItemResult) :
    HTProcessingRecipeBuilder<HTAlloyingRecipeBuilder>(RagiumConst.ALLOYING) {
    companion object {
        @JvmStatic
        fun create(result: HTItemResult, vararg ingredients: HTItemIngredient): HTAlloyingRecipeBuilder =
            HTAlloyingRecipeBuilder(listOf(*ingredients), result)
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTAlloyingRecipe {
        check(ingredients.size in (2..3)) { "Alloying recipe requires two or three ingredients" }
        return HTAlloyingRecipe(ingredients, result, time, exp)
    }
}
