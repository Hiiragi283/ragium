package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import java.util.Optional

class HTMixingRecipeBuilder : HTAbstractComplexRecipeBuilder(RagiumConst.MIXING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTMixingRecipeBuilder.() -> Unit) {
            HTMixingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    var itemIngredient: HTItemIngredient? = null
    val fluidIngredients: MutableList<HTFluidIngredient> = mutableListOf()

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTMixingRecipe = HTMixingRecipe(
        Optional.ofNullable(itemIngredient),
        fluidIngredients,
        result.build(),
        time,
        exp,
    )
}
