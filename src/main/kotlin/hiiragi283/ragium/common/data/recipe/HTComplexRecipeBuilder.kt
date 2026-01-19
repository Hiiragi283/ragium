package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTDryingRecipe
import hiiragi283.ragium.common.recipe.base.HTComplexResultRecipe
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

class HTComplexRecipeBuilder<INGREDIENT : Any>(
    prefix: String,
    private val factory: Factory<INGREDIENT, *>,
    private val ingredient: INGREDIENT,
) : HTAbstractComplexRecipeBuilder<HTComplexRecipeBuilder<INGREDIENT>>(prefix) {
    companion object {
        @JvmStatic
        fun drying(ingredient: HTItemIngredient): HTComplexRecipeBuilder<*> =
            HTComplexRecipeBuilder(RagiumConst.DRYING, ::HTDryingRecipe, Either.Left(ingredient))

        @JvmStatic
        fun drying(ingredient: HTFluidIngredient): HTComplexRecipeBuilder<*> =
            HTComplexRecipeBuilder(RagiumConst.DRYING, ::HTDryingRecipe, Either.Right(ingredient))
    }

    override fun getPrimalId(): ResourceLocation = toIorResult().map(HTItemResult::getId, HTFluidResult::getId)

    override fun createRecipe(): HTComplexResultRecipe.Simple = factory.create(ingredient, toIorResult(), time, exp)

    fun interface Factory<INGREDIENT : Any, RECIPE : HTComplexResultRecipe.Simple> {
        fun create(
            ingredient: INGREDIENT,
            result: HTComplexResult,
            time: Int,
            exp: Fraction,
        ): RECIPE
    }
}
