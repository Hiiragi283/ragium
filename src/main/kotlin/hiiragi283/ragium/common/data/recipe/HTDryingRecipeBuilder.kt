package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTDryingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTDryingRecipeBuilder : HTAbstractComplexRecipeBuilder(RagiumConst.DRYING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTDryingRecipeBuilder.() -> Unit) {
            HTDryingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: Either<HTItemIngredient, HTFluidIngredient>

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTDryingRecipe = HTDryingRecipe(ingredient, result.build(), time, exp)
}
