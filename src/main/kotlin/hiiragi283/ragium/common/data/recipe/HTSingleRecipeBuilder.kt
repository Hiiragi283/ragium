package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.recipe.result.HTRecipeResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTBendingRecipe
import hiiragi283.ragium.common.recipe.HTCompressingRecipe
import hiiragi283.ragium.common.recipe.HTLathingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTSingleRecipeBuilder<ING : HTIngredient<*, *>, RES : HTRecipeResult<*>>(prefix: String, private val factory: Factory<ING, RES, *>) :
    HTProcessingRecipeBuilder(prefix) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun bending(output: RecipeOutput, builderAction: HTSingleRecipeBuilder<HTItemIngredient, HTItemResult>.() -> Unit) {
            HTSingleRecipeBuilder(RagiumConst.BENDING, ::HTBendingRecipe).apply(builderAction).save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun compressing(output: RecipeOutput, builderAction: HTSingleRecipeBuilder<HTItemIngredient, HTItemResult>.() -> Unit) {
            HTSingleRecipeBuilder(RagiumConst.COMPRESSING, ::HTCompressingRecipe).apply(builderAction).save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun lathing(output: RecipeOutput, builderAction: HTSingleRecipeBuilder<HTItemIngredient, HTItemResult>.() -> Unit) {
            HTSingleRecipeBuilder(RagiumConst.LATHING, ::HTLathingRecipe).apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: ING
    lateinit var result: RES

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTProcessingRecipe<*> = factory.create(ingredient, result, subParameters())

    //    Factory    //

    fun interface Factory<ING : HTIngredient<*, *>, RES : HTRecipeResult<*>, RECIPE : HTProcessingRecipe<*>> {
        fun create(ingredient: ING, result: RES, parameters: HTProcessingRecipe.SubParameters): RECIPE
    }
}
