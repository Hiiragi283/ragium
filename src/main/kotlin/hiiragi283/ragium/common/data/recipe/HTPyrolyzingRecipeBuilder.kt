package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTPyrolyzingRecipeBuilder : HTAbstractComplexRecipeBuilder(RagiumConst.PYROLYZING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTPyrolyzingRecipeBuilder.() -> Unit) {
            HTPyrolyzingRecipeBuilder().apply(builderAction).save(output)
        }
    }
    
    init {
        time *= 3
    }

    lateinit var ingredient: HTItemIngredient
    lateinit var itemResult: HTItemResult
    lateinit var fluidResult: HTFluidResult

    override fun getPrimalId(): ResourceLocation = itemResult.getId()

    override fun createRecipe(): HTPyrolyzingRecipe = HTPyrolyzingRecipe(ingredient, itemResult, fluidResult, time, exp)
}
