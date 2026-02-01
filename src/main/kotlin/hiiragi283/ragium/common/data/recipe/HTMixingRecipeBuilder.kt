package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.monad.toIorOrThrow
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTMixingRecipeBuilder : HTAbstractComplexRecipeBuilder(RagiumConst.MIXING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTMixingRecipeBuilder.() -> Unit) {
            HTMixingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    val fluidIngredients: MutableList<HTFluidIngredient> = mutableListOf()
    val itemIngredients: MutableList<HTItemIngredient> = mutableListOf()

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTMixingRecipe = HTMixingRecipe(
        createInput(),
        result.build(),
        subParameters(),
    )

    private fun createInput(): Ior<List<HTItemIngredient>, List<HTFluidIngredient>> {
        val fluids: List<HTFluidIngredient>? = fluidIngredients.takeUnless(List<HTFluidIngredient>::isEmpty)
        val items: List<HTItemIngredient>? = itemIngredients.takeUnless(List<HTItemIngredient>::isEmpty)
        return (items to fluids).toIorOrThrow()
    }
}
