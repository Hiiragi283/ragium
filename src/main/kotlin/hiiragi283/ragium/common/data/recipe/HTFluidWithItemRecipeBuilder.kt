package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTArcSmeltingRecipe
import hiiragi283.ragium.common.recipe.HTBathingRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTFluidWithItemRecipeBuilder<RESULT : HTIdLike>(prefix: String, private val factory: Factory<RESULT, *>) :
    HTProcessingRecipeBuilder(prefix) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun arcSmelting(output: RecipeOutput, builderAction: HTFluidWithItemRecipeBuilder<HTFluidResult>.() -> Unit) {
            HTFluidWithItemRecipeBuilder(RagiumConst.ARC_SMELTING, ::HTArcSmeltingRecipe).apply(builderAction).save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun bathing(output: RecipeOutput, builderAction: HTFluidWithItemRecipeBuilder<HTItemResult>.() -> Unit) {
            HTFluidWithItemRecipeBuilder(RagiumConst.BATHING, ::HTBathingRecipe).apply(builderAction).save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun solidifying(output: RecipeOutput, builderAction: HTFluidWithItemRecipeBuilder<HTItemResult>.() -> Unit) {
            HTFluidWithItemRecipeBuilder(RagiumConst.SOLIDIFYING, ::HTSolidifyingRecipe).apply(builderAction).save(output)
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
