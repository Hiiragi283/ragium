package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.multi.HTMultiItemsToItemRecipe
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.impl.recipe.HTAlloyingRecipe
import hiiragi283.ragium.impl.recipe.HTEnchantingRecipe
import net.minecraft.resources.ResourceLocation

class HTCombineItemToObjRecipeBuilder(
    prefix: String,
    private val factory: Factory<*>,
    private val ingredients: List<HTItemIngredient>,
    private val result: HTItemResult,
) : HTRecipeBuilder.Prefixed(prefix) {
    companion object {
        @JvmStatic
        fun alloying(result: HTItemResult, vararg ingredients: HTItemIngredient): HTCombineItemToObjRecipeBuilder =
            alloying(result, listOf(*ingredients))

        @JvmStatic
        fun alloying(result: HTItemResult, ingredients: List<HTItemIngredient>): HTCombineItemToObjRecipeBuilder =
            HTCombineItemToObjRecipeBuilder(RagiumConst.ALLOYING, ::HTAlloyingRecipe, ingredients, result)

        @JvmStatic
        fun enchanting(result: HTItemResult, vararg ingredients: HTItemIngredient): HTCombineItemToObjRecipeBuilder =
            HTCombineItemToObjRecipeBuilder(RagiumConst.ENCHANTING, ::HTEnchantingRecipe, listOf(*ingredients), result)
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): HTMultiItemsToItemRecipe = factory.create(ingredients, result)

    fun interface Factory<RECIPE : HTMultiItemsToItemRecipe> {
        fun create(ingredients: List<HTItemIngredient>, result: HTItemResult): RECIPE
    }
}
