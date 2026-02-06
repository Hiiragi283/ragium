package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.monad.toIorOrThrow
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTReactingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTReactingRecipeBuilder : HTProcessingRecipeBuilder(RagiumConst.REACTING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTReactingRecipeBuilder.() -> Unit) {
            HTReactingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    val fluidIngredients: MutableList<HTFluidIngredient> = mutableListOf()
    val itemIngredients: MutableList<HTItemIngredient> = mutableListOf()

    var catalyst: HTItemIngredient? = null

    val fluidResults: MutableList<HTFluidResult> = mutableListOf()
    val itemResults: MutableList<HTItemResult> = mutableListOf()

    override fun getPrimalId(): ResourceLocation = (itemResults.firstOrNull() ?: fluidResults.first()).getId()

    override fun createRecipe(): HTReactingRecipe = HTReactingRecipe(
        (itemIngredients to fluidIngredients).toIorOrThrow(),
        catalyst,
        (itemResults to fluidResults).toIorOrThrow(),
        subParameters(),
    )
}
