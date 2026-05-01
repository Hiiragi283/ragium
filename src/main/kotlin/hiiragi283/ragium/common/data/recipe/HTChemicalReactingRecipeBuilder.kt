package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.holder.HTIngredientHolder
import hiiragi283.core.api.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.recipe.result.HTListFluidResult
import hiiragi283.core.api.util.toIorOrThrow
import hiiragi283.core.api.util.wrapOptional
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTChemicalReactingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTChemicalReactingRecipeBuilder : HTProgressRecipeBuilder(RagiumConst.CHEMICAL_REACTING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTChemicalReactingRecipeBuilder.() -> Unit) {
            HTChemicalReactingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    val ingredients: MutableList<HTFluidIngredient> = mutableListOf()
    val catalyst: HTIngredientHolder.Single = HTIngredientHolder.Single()
    val fluidResults: MutableList<HTFluidResult> = mutableListOf()
    var itemResult: HTItemResult? = null

    override fun getPrimalId(): ResourceLocation = fluidResults[0].getId()

    override fun createRecipe(): HTChemicalReactingRecipe = HTChemicalReactingRecipe(
        ingredients[0],
        (ingredients.getOrNull(1) to catalyst.getOrNull()).toIorOrThrow(),
        HTListFluidResult(fluidResults),
        itemResult.wrapOptional(),
        progressData,
    )
}
