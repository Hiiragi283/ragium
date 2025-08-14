package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.impl.HTAlloyingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe

class HTCombineItemToItemRecipeBuilder<B : HTCombineItemToItemRecipe>(
    prefix: String,
    private val factory: Factory<B>,
    private val ingredients: List<HTItemIngredient>,
    private val result: HTItemResult,
) : HTRecipeBuilder.Prefixed(prefix) {
    companion object {
        @JvmStatic
        fun alloying(result: HTItemResult, vararg ingredients: HTItemIngredient): HTCombineItemToItemRecipeBuilder<HTAlloyingRecipe> =
            HTCombineItemToItemRecipeBuilder(RagiumConst.ALLOYING, ::HTAlloyingRecipe, listOf(*ingredients), result)
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): Recipe<*> = factory.create(ingredients, result)

    fun interface Factory<B : HTCombineItemToItemRecipe> {
        fun create(ingredients: List<HTItemIngredient>, result: HTItemResult): B
    }
}
