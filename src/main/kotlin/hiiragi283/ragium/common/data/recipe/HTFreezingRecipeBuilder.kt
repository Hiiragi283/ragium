package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTFreezingRecipeBuilder : HTProcessingRecipeBuilder(RagiumConst.FREEZING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTFreezingRecipeBuilder.() -> Unit) {
            HTFreezingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var itemIngredient: HTItemIngredient
    lateinit var fluidIngredient: HTFluidIngredient
    lateinit var result: HTItemResult

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTFreezingRecipe = HTFreezingRecipe(
        itemIngredient,
        fluidIngredient,
        result,
        time,
    )
}
