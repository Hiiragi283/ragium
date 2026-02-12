package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.monad.toIorOrThrow
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTDistillingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTDistillingRecipeBuilder : HTProcessingRecipeBuilder(RagiumConst.DISTILLING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTDistillingRecipeBuilder.() -> Unit) {
            HTDistillingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: HTFluidIngredient

    val fluidResults: MutableList<HTFluidResult> = mutableListOf()
    var itemResult: HTItemResult? = null

    override fun getPrimalId(): ResourceLocation = (fluidResults.firstOrNull() ?: itemResult)?.getId() ?: error("Required fluid results")

    override fun createRecipe(): HTDistillingRecipe = HTDistillingRecipe(
        ingredient,
        (listOfNotNull(itemResult) to fluidResults).toIorOrThrow(),
        subParameters(),
    )
}
