package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.function.identityRight
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.data.holder.HTIorHolder
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

    val result: HTIorHolder<HTItemResult, HTFluidResult> = HTIorHolder()

    override fun getPrimalId(): ResourceLocation = result.toIor().map(HTItemResult::getId, HTFluidResult::getId, identityRight())

    override fun createRecipe(): HTMixingRecipe = HTMixingRecipe(
        itemIngredients,
        fluidIngredients,
        result.toIor(),
        time,
    )
}
