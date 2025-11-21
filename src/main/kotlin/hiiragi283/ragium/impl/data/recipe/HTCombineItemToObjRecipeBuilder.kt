package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.multi.HTShapelessInputsRecipe
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.impl.recipe.HTAlloyingRecipe
import net.minecraft.resources.ResourceLocation

class HTCombineItemToObjRecipeBuilder(
    prefix: String,
    private val factory: Factory<*>,
    private val ingredients: List<HTItemIngredient>,
    private val result: HTItemResult,
) : HTRecipeBuilder<HTCombineItemToObjRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun alloying(result: HTItemResult, vararg ingredients: HTItemIngredient): HTCombineItemToObjRecipeBuilder =
            alloying(result, listOf(*ingredients))

        @JvmStatic
        fun alloying(result: HTItemResult, ingredients: List<HTItemIngredient>): HTCombineItemToObjRecipeBuilder =
            HTCombineItemToObjRecipeBuilder(RagiumConst.ALLOYING, ::HTAlloyingRecipe, ingredients, result)
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): HTShapelessInputsRecipe = factory.create(ingredients, result)

    fun interface Factory<RECIPE : HTShapelessInputsRecipe> {
        fun create(ingredients: List<HTItemIngredient>, result: HTItemResult): RECIPE
    }
}
