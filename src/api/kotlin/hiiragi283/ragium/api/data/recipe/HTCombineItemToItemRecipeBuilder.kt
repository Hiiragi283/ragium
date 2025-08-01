package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.base.HTAlloyingRecipe
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe

class HTCombineItemToItemRecipeBuilder<R : HTCombineItemToItemRecipe>(
    prefix: String,
    private val factory: Factory<R>,
    private val ingredients: List<HTItemIngredient>,
    private val result: HTItemResult,
) : HTRecipeBuilder.Prefixed(prefix) {
    companion object {
        @JvmStatic
        fun alloying(
            first: HTItemIngredient,
            second: HTItemIngredient,
            result: HTItemResult,
        ): HTCombineItemToItemRecipeBuilder<HTAlloyingRecipe> =
            HTCombineItemToItemRecipeBuilder(RagiumConst.ALLOYING, ::HTAlloyingRecipe, listOf(first, second), result)
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): Recipe<*> = factory.create(ingredients, result)

    fun interface Factory<R : HTCombineItemToItemRecipe> {
        fun create(ingredients: List<HTItemIngredient>, result: HTItemResult): R
    }
}
