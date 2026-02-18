package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.base.HTCombineItemRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTCombineItemRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTProcessingRecipeBuilder(prefix) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun alloying(output: RecipeOutput, builderAction: HTCombineItemRecipeBuilder.() -> Unit) {
            HTCombineItemRecipeBuilder(RagiumConst.ALLOYING, ::HTAlloyingRecipe).apply(builderAction).save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun pressing(output: RecipeOutput, builderAction: HTCombineItemRecipeBuilder.() -> Unit) {
            HTCombineItemRecipeBuilder(RagiumConst.PRESSING, ::HTPressingRecipe).apply(builderAction).save(output)
        }
    }

    val ingredients: MutableList<HTItemIngredient> = mutableListOf()
    lateinit var result: HTItemResult

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTCombineItemRecipe = factory.create(
        ingredients,
        result,
        subParameters(),
    )

    //    Factory    //

    fun interface Factory<RECIPE : HTCombineItemRecipe> {
        fun create(ingredients: List<HTItemIngredient>, result: HTItemResult, parameters: HTProcessingRecipe.SubParameters): RECIPE
    }
}
