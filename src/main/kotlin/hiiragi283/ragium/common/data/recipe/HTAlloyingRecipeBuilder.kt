package hiiragi283.ragium.common.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.HTBasicAlloyingRecipe
import net.minecraft.resources.ResourceLocation

class HTAlloyingRecipeBuilder(private val ingredients: List<HTItemIngredient>, private val result: HTItemResult) :
    HTRecipeBuilder<HTAlloyingRecipeBuilder>(RagiumConst.ALLOYING) {
    companion object {
        @JvmStatic
        fun create(result: HTItemResult, vararg ingredients: HTItemIngredient): HTAlloyingRecipeBuilder =
            HTAlloyingRecipeBuilder(listOf(*ingredients), result)
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): HTBasicAlloyingRecipe = HTBasicAlloyingRecipe(ingredients, result)
}
