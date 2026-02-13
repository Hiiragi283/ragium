package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.monad.toIorOrThrow
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.base.HTChemicalIngredient
import hiiragi283.ragium.common.recipe.base.HTChemicalRecipe
import hiiragi283.ragium.common.recipe.base.HTChemicalResult
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTChemicalRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTProcessingRecipeBuilder(prefix) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun mixing(output: RecipeOutput, builderAction: HTChemicalRecipeBuilder.() -> Unit) {
            HTChemicalRecipeBuilder(RagiumConst.MIXING, ::HTMixingRecipe).apply(builderAction).save(output)
        }
    }

    val fluidIngredients: MutableList<HTFluidIngredient> = mutableListOf()
    val itemIngredients: MutableList<HTItemIngredient> = mutableListOf()

    val fluidResults: MutableList<HTFluidResult> = mutableListOf()
    val itemResults: MutableList<HTItemResult> = mutableListOf()

    override fun getPrimalId(): ResourceLocation = (fluidResults.firstOrNull() ?: itemResults.first()).getId()

    override fun createRecipe(): HTChemicalRecipe = factory.create(
        (itemIngredients to fluidIngredients).toIorOrThrow(),
        (itemResults to fluidResults).toIorOrThrow(),
        subParameters(),
    )

    //    Factory    //

    fun interface Factory<RECIPE : HTChemicalRecipe> {
        fun create(ingredient: HTChemicalIngredient, result: HTChemicalResult, parameters: HTProcessingRecipe.SubParameters): RECIPE
    }
}
