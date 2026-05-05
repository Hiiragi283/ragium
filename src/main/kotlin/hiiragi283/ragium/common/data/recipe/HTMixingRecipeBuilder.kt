package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.holder.HTIorHolder
import hiiragi283.core.api.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.core.api.function.identityLeft
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTMixingRecipeBuilder : HTProgressRecipeBuilder(RagiumConst.MIXING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTMixingRecipeBuilder.() -> Unit) {
            HTMixingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    val itemIngredients: MutableList<HTItemIngredient> = mutableListOf()
    lateinit var fluidIngredient: HTFluidIngredient
    val result: HTIorHolder<HTItemResult, HTFluidResult> = HTIorHolder()

    override fun getPrimalId(): ResourceLocation = result.toIor().map(HTItemResult::getId, HTFluidResult::getId, identityLeft())

    override fun createRecipe(): HTMixingRecipe = HTMixingRecipe(itemIngredients, fluidIngredient, result.toIor(), progressData)
}
