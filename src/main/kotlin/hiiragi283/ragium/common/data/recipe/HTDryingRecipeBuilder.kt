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
import hiiragi283.ragium.common.recipe.HTDryingRecipe
import net.minecraft.resources.ResourceLocation

class HTDryingRecipeBuilder(private val ingredient: Either<HTItemIngredient, HTFluidIngredient>) :
    HTProcessingRecipeBuilder<HTDryingRecipeBuilder>(RagiumConst.DRYING) {
    companion object {
        @JvmStatic
        fun createItem(ingredient: HTItemIngredient): HTDryingRecipeBuilder = HTDryingRecipeBuilder(Either.left(ingredient))

        @JvmStatic
        fun createFluid(ingredient: HTFluidIngredient): HTDryingRecipeBuilder = HTDryingRecipeBuilder(Either.right(ingredient))
    }

    private var itemResult: HTItemResult? = null
    private var fluidResult: HTFluidResult? = null

    fun setResult(result: HTItemResult?): HTDryingRecipeBuilder = apply {
        check(this.itemResult == null) { "Item result already initialized!" }
        this.itemResult = result
    }

    fun setResult(result: HTFluidResult?): HTDryingRecipeBuilder = apply {
        check(this.fluidResult == null) { "Fluid result already initialized!" }
        this.fluidResult = result
    }

    private fun toIorResult(): HTComplexResult = (itemResult to fluidResult).toComplex()

    override fun getPrimalId(): ResourceLocation = toIorResult().map(HTItemResult::getId, HTFluidResult::getId)

    override fun createRecipe(): HTDryingRecipe = HTDryingRecipe(ingredient, toIorResult(), time, exp)
}
