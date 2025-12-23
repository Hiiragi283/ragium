package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import net.minecraft.resources.ResourceLocation

class HTAlloyingRecipeBuilder(
    private val firstIngredient: HTItemIngredient,
    private val secondIngredient: HTItemIngredient,
    private val thirdIngredient: HTItemIngredient?,
    private val result: HTItemResult,
) : HTProcessingRecipeBuilder<HTAlloyingRecipeBuilder>(RagiumConst.ALLOYING) {
    companion object {
        @JvmStatic
        fun create(
            result: HTItemResult,
            firstIngredient: HTItemIngredient,
            secondIngredient: HTItemIngredient,
            thirdIngredient: HTItemIngredient? = null,
        ): HTAlloyingRecipeBuilder = HTAlloyingRecipeBuilder(firstIngredient, secondIngredient, thirdIngredient, result)
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTAlloyingRecipe = HTAlloyingRecipe(firstIngredient, secondIngredient, thirdIngredient, result, time, exp)
}
