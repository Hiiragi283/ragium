package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTAssemblingRecipeBuilder : HTProcessingRecipeBuilder(RagiumConst.ASSEMBLING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTAssemblingRecipeBuilder.() -> Unit) {
            HTAssemblingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    val itemIngredients: MutableList<HTItemIngredient> = mutableListOf()
    var fluidIngredient: HTFluidIngredient? = null
    var circuit: Int = 0
    lateinit var result: HTItemResult

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTAssemblingRecipe = HTAssemblingRecipe(
        itemIngredients,
        fluidIngredient,
        circuit,
        result,
        subParameters(),
    )
}
