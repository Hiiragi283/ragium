package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.base.HTItemToChancedRecipe
import net.minecraft.data.recipes.RecipeOutput

class HTItemToChancedRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTChancedRecipeBuilder(prefix) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun crushing(output: RecipeOutput, builderAction: HTItemToChancedRecipeBuilder.() -> Unit) {
            HTItemToChancedRecipeBuilder(RagiumConst.CRUSHING, ::HTCrushingRecipe).apply(builderAction).save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun cutting(output: RecipeOutput, builderAction: HTItemToChancedRecipeBuilder.() -> Unit) {
            HTItemToChancedRecipeBuilder(RagiumConst.CUTTING, ::HTCuttingRecipe).apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: HTItemIngredient

    init {
        time /= 2
    }

    override fun createRecipe(): HTItemToChancedRecipe = factory.create(ingredient, result, chancedResults.results, subParameters())

    //    Factory    //

    fun interface Factory<RECIPE : HTItemToChancedRecipe> {
        fun create(
            ingredient: HTItemIngredient,
            result: HTItemResult,
            extraResults: List<HTChancedItemResult>,
            parameters: HTProcessingRecipe.SubParameters,
        ): RECIPE
    }
}
