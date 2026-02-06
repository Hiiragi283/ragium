package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import net.minecraft.data.recipes.RecipeOutput

class HTAlloyingRecipeBuilder : HTChancedRecipeBuilder(RagiumConst.ALLOYING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTAlloyingRecipeBuilder.() -> Unit) {
            HTAlloyingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    val ingredients: MutableList<HTItemIngredient> = mutableListOf()

    override fun createRecipe(): HTAlloyingRecipe = HTAlloyingRecipe(
        ingredients,
        result,
        extraResults,
        subParameters(),
    )
}
