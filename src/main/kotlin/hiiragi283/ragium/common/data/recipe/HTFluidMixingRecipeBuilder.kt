package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.util.wrapOptional
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTFluidMixingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTFluidMixingRecipeBuilder : HTProcessingRecipeBuilder(RagiumConst.FLUID_MIXING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTFluidMixingRecipeBuilder.() -> Unit) {
            // HTFluidMixingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    var itemIngredient: HTItemIngredient? = null
    val fluidIngredients: MutableList<HTFluidIngredient> = mutableListOf()

    val results: MutableList<HTFluidResult> = mutableListOf()

    override fun getPrimalId(): ResourceLocation = results.first().getId()

    override fun createRecipe(): HTFluidMixingRecipe = HTFluidMixingRecipe(itemIngredient.wrapOptional(), fluidIngredients, results, time)
}
