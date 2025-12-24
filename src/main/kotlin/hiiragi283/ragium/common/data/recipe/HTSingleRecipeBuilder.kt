package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTRecipeResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

class HTSingleRecipeBuilder<ING : HTIngredient<*, *>, RES : HTRecipeResult<*>>(
    prefix: String,
    private val factory: Factory<ING, RES, *>,
    private val ingredient: ING,
    private val result: RES,
) : HTProcessingRecipeBuilder<HTSingleRecipeBuilder<ING, RES>>(prefix) {
    companion object {
        @JvmStatic
        fun melting(ingredient: HTItemIngredient, result: HTFluidResult): HTSingleRecipeBuilder<HTItemIngredient, HTFluidResult> =
            HTSingleRecipeBuilder(RagiumConst.MELTING, ::HTMeltingRecipe, ingredient, result)
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTProcessingRecipe = factory.create(ingredient, result, time, exp)

    fun interface Factory<ING : HTIngredient<*, *>, RES : HTRecipeResult<*>, RECIPE : HTProcessingRecipe> {
        fun create(
            ingredient: ING,
            result: RES,
            time: Int,
            exp: Fraction,
        ): RECIPE
    }
}
