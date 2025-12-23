package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import net.minecraft.resources.ResourceLocation
import java.util.*

class HTAlloyingRecipeBuilder(private val ingredients: List<HTItemIngredient>, private val result: HTItemResult) :
    HTRecipeBuilder<HTAlloyingRecipeBuilder>(RagiumConst.ALLOYING) {
    companion object {
        @JvmStatic
        fun create(result: HTItemResult, vararg ingredients: HTItemIngredient): HTAlloyingRecipeBuilder =
            HTAlloyingRecipeBuilder(listOf(*ingredients), result)
    }

    private var extra: HTItemResult? = null

    fun setExtra(result: HTItemResult): HTAlloyingRecipeBuilder = apply {
        this.extra = result
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTAlloyingRecipe = HTAlloyingRecipe(ingredients, result, Optional.ofNullable(extra))
}
