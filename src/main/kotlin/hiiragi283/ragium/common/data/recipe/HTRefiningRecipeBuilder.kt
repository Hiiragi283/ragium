package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTRefiningRecipeBuilder : HTAbstractComplexRecipeBuilder(RagiumConst.REFINING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTRefiningRecipeBuilder.() -> Unit) {
            HTRefiningRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: HTFluidIngredient
    lateinit var primalResult: HTFluidResult

    override fun getPrimalId(): ResourceLocation = primalResult.getId()

    override fun createRecipe(): HTRefiningRecipe = HTRefiningRecipe(
        ingredient,
        primalResult,
        result.build(),
        time,
        exp,
    )
}
