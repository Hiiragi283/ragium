package hiiragi283.ragium.common.data.recipe

import com.mojang.datafixers.util.Either
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.recipe.result.toComplex
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTComplexRecipe
import hiiragi283.ragium.common.recipe.HTDryingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

class HTComplexRecipeBuilder(
    prefix: String,
    private val factory: Factory<*>,
    private val ingredient: Either<HTItemIngredient, HTFluidIngredient>,
) : HTProcessingRecipeBuilder<HTComplexRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun drying(ingredient: HTItemIngredient): HTComplexRecipeBuilder =
            HTComplexRecipeBuilder(RagiumConst.DRYING, ::HTDryingRecipe, Either.left(ingredient))

        @JvmStatic
        fun drying(ingredient: HTFluidIngredient): HTComplexRecipeBuilder =
            HTComplexRecipeBuilder(RagiumConst.DRYING, ::HTDryingRecipe, Either.right(ingredient))

        @JvmStatic
        fun pyrolyzing(ingredient: HTItemIngredient): HTComplexRecipeBuilder =
            HTComplexRecipeBuilder(RagiumConst.PYROLYZING, ::HTPyrolyzingRecipe, Either.left(ingredient))

        @JvmStatic
        fun pyrolyzing(ingredient: HTFluidIngredient): HTComplexRecipeBuilder =
            HTComplexRecipeBuilder(RagiumConst.PYROLYZING, ::HTPyrolyzingRecipe, Either.right(ingredient))
    }

    private var itemResult: HTItemResult? = null
    private var fluidResult: HTFluidResult? = null

    fun setResult(result: HTItemResult?): HTComplexRecipeBuilder = apply {
        check(this.itemResult == null) { "Item result already initialized!" }
        this.itemResult = result
    }

    fun setResult(result: HTFluidResult?): HTComplexRecipeBuilder = apply {
        check(this.fluidResult == null) { "Fluid result already initialized!" }
        this.fluidResult = result
    }

    private fun toIorResult(): HTComplexResult = (itemResult to fluidResult).toComplex()

    override fun getPrimalId(): ResourceLocation = toIorResult().map(HTItemResult::getId, HTFluidResult::getId)

    override fun createRecipe(): HTComplexRecipe = factory.create(ingredient, toIorResult(), time, exp)

    fun interface Factory<RECIPE : HTComplexRecipe> {
        fun create(
            ingredient: Either<HTItemIngredient, HTFluidIngredient>,
            result: HTComplexResult,
            time: Int,
            exp: Fraction,
        ): RECIPE
    }
}
