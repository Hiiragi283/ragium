package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.holder.HTIngredientHolder
import hiiragi283.core.api.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTPrintingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTPrintingRecipeBuilder : HTProgressRecipeBuilder(RagiumConst.PRINTING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTPrintingRecipeBuilder.() -> Unit) {
            HTPrintingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: HTItemIngredient
    val press: HTIngredientHolder.Single = HTIngredientHolder.Single()
    lateinit var result: HTItemResult

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTPrintingRecipe = HTPrintingRecipe(
        ingredient,
        press.ingredient,
        result,
        progressData,
    )
}
