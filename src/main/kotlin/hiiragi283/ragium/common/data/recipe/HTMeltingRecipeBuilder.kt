package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTMeltingRecipeBuilder : HTProgressRecipeBuilder(RagiumConst.MELTING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTMeltingRecipeBuilder.() -> Unit) {
            HTMeltingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: HTItemIngredient
    lateinit var result: HTFluidResult

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTMeltingRecipe = HTMeltingRecipe(
        ingredient,
        result,
        progressData,
    )
}
