package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import hiiragi283.ragium.api.recipe.single.HTSingleItemRecipe
import hiiragi283.ragium.impl.recipe.HTMeltingRecipe
import net.minecraft.resources.ResourceLocation

class HTSingleRecipeBuilder<RESULT : HTRecipeResult<*>>(
    prefix: String,
    private val factory: Factory<RESULT, *>,
    val ingredient: HTItemIngredient,
    val result: RESULT,
) : HTRecipeBuilder<HTSingleRecipeBuilder<RESULT>>(prefix) {
    companion object {
        @JvmStatic
        fun melting(ingredient: HTItemIngredient, result: HTFluidResult): HTSingleRecipeBuilder<HTFluidResult> =
            HTSingleRecipeBuilder(RagiumConst.MELTING, ::HTMeltingRecipe, ingredient, result)
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): HTSingleItemRecipe = factory.create(ingredient, result)

    fun interface Factory<RESULT : HTRecipeResult<*>, RECIPE : HTSingleItemRecipe> {
        fun create(ingredient: HTItemIngredient, result: RESULT): RECIPE
    }
}
