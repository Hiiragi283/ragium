package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import net.minecraft.data.recipes.RecipeOutput

class HTWashingRecipeBuilder : HTChancedRecipeBuilder(RagiumConst.WASHING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTWashingRecipeBuilder.() -> Unit) {
            HTWashingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var itemIngredient: HTItemIngredient
    lateinit var fluidIngredient: HTFluidIngredient

    override fun createRecipe(): HTWashingRecipe = HTWashingRecipe(
        itemIngredient,
        fluidIngredient,
        result,
        chancedResults.results,
        time,
        exp,
    )
}
