package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.monad.toIorOrThrow
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTMixingRecipeBuilder : HTProcessingRecipeBuilder(RagiumConst.MIXING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTMixingRecipeBuilder.() -> Unit) {
            HTMixingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    val fluidIngredients: MutableList<HTFluidIngredient> = mutableListOf()
    val itemIngredients: MutableList<HTItemIngredient> = mutableListOf()

    val fluidResults: MutableList<HTFluidResult> = mutableListOf()
    val itemResults: MutableList<HTItemResult> = mutableListOf()

    override fun getPrimalId(): ResourceLocation = (fluidResults.firstOrNull() ?: itemResults.first()).getId()

    override fun createRecipe(): HTMixingRecipe = HTMixingRecipe(
        (itemIngredients to fluidIngredients).toIorOrThrow(),
        (itemResults to fluidResults).toIorOrThrow(),
        subParameters(),
    )
}
