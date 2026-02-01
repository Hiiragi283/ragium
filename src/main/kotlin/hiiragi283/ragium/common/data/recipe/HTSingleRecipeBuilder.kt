package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.HTViewProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTSingleRecipeBuilder<ING : Any, RES : HTIdLike>(prefix: String, private val factory: Factory<ING, RES, *>) :
    HTProcessingRecipeBuilder(prefix) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun melting(output: RecipeOutput, builderAction: HTSingleRecipeBuilder<HTItemIngredient, HTFluidResult>.() -> Unit) {
            HTSingleRecipeBuilder(RagiumConst.MELTING, ::HTMeltingRecipe).apply(builderAction).save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun pressing(
            output: RecipeOutput,
            builderAction: HTSingleRecipeBuilder<Pair<HTItemIngredient, HTItemIngredient>, HTItemResult>.() -> Unit,
        ) {
            HTSingleRecipeBuilder(RagiumConst.PRESSING, ::HTPressingRecipe).apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: ING
    lateinit var result: RES

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTViewProcessingRecipe = factory.create(ingredient, result, subParameters())

    //    Factory    //

    fun interface Factory<ING : Any, RES : HTIdLike, RECIPE : HTViewProcessingRecipe> {
        fun create(ingredient: ING, result: RES, parameters: HTProcessingRecipe.SubParameters): RECIPE
    }
}
