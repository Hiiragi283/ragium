package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.data.holder.HTIorHolder
import hiiragi283.ragium.common.recipe.HTElectrolyzingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTElectrolyzingRecipeBuilder : HTProcessingRecipeBuilder(RagiumConst.ELECTROLYZING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTElectrolyzingRecipeBuilder.() -> Unit) {
            HTElectrolyzingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: HTFluidIngredient
    lateinit var result: HTFluidResult
    val extraResult: HTIorHolder<HTItemResult, HTFluidResult> = HTIorHolder()

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTElectrolyzingRecipe = HTElectrolyzingRecipe(
        ingredient,
        result,
        extraResult.toIor(),
        time,
    )
}
