package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.function.identityLeft
import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.Ior
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.data.holder.HTIorHolder
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTItemOrFluidRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTProcessingRecipeBuilder(prefix) {
    companion object {
        @JvmStatic
        inline fun pyrolyzing(output: RecipeOutput, builderAction: HTItemOrFluidRecipeBuilder.() -> Unit) {
            HTItemOrFluidRecipeBuilder(RagiumConst.PYROLYZING, ::HTPyrolyzingRecipe)
                .apply { time *= 3 }
                .apply(builderAction)
                .save(output)
        }

        @JvmStatic
        inline fun refining(output: RecipeOutput, builderAction: HTItemOrFluidRecipeBuilder.() -> Unit) {
            HTItemOrFluidRecipeBuilder(RagiumConst.REFINING, ::HTRefiningRecipe).apply(builderAction).save(output)
        }
    }

    val ingredient: HTIorHolder<HTItemIngredient, HTFluidIngredient> = HTIorHolder()
    val result: HTIorHolder<HTItemResult, HTFluidResult> = HTIorHolder()

    override fun getPrimalId(): ResourceLocation = result.toIor().map(HTItemResult::getId, HTFluidResult::getId, identityLeft())

    override fun createRecipe(): HTSerializableRecipe<*> = factory.create(
        ingredient.toIor(),
        result.toIor(),
        time,
    )

    //    Factory    //

    fun interface Factory<RECIPE : HTSerializableRecipe<*>> {
        fun create(ingredient: Ior<HTItemIngredient, HTFluidIngredient>, result: Ior<HTItemResult, HTFluidResult>, time: Int): RECIPE
    }
}
