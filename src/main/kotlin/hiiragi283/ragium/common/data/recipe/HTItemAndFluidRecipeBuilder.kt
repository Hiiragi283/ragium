package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTCanningRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTItemAndFluidRecipeBuilder<RESULT : HTIdLike>(prefix: String, private val factory: Factory<RESULT, *>) :
    HTProcessingRecipeBuilder(prefix) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun canning(output: RecipeOutput, builderAction: HTItemAndFluidRecipeBuilder<HTItemResult>.() -> Unit) {
            HTItemAndFluidRecipeBuilder(RagiumConst.CANNING, ::HTCanningRecipe).apply(builderAction).save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun solidifying(output: RecipeOutput, builderAction: HTItemAndFluidRecipeBuilder<HTItemResult>.() -> Unit) {
            HTItemAndFluidRecipeBuilder(RagiumConst.SOLIDIFYING, ::HTSolidifyingRecipe).apply(builderAction).save(output)
        }
    }

    lateinit var fluidIngredient: HTFluidIngredient
    lateinit var itemIngredient: HTItemIngredient
    lateinit var result: RESULT

    init {
        time /= 2
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTProcessingRecipe<*> = factory.create(
        fluidIngredient,
        itemIngredient,
        result,
        subParameters(),
    )

    //    Factory    //

    fun interface Factory<RESULT : HTIdLike, RECIPE : HTProcessingRecipe<*>> {
        fun create(
            fluidIngredient: HTFluidIngredient,
            itemIngredient: HTItemIngredient,
            result: RESULT,
            parameters: HTProcessingRecipe.SubParameters,
        ): RECIPE
    }
}
