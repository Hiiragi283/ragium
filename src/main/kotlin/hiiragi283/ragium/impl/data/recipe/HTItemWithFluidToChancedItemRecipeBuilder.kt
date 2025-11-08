package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTChancedItemResult
import hiiragi283.ragium.impl.recipe.HTPlantingRecipe
import hiiragi283.ragium.impl.recipe.HTWashingRecipe

class HTItemWithFluidToChancedItemRecipeBuilder(
    prefix: String,
    private val factory: Factory<*>,
    val ingredient: HTItemIngredient,
    val fluidIngredient: HTFluidIngredient,
) : HTChancedItemRecipeBuilder<HTItemWithFluidToChancedItemRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun planting(ingredient: HTItemIngredient, fluidIngredient: HTFluidIngredient): HTItemWithFluidToChancedItemRecipeBuilder =
            HTItemWithFluidToChancedItemRecipeBuilder(RagiumConst.PLANTING, ::HTPlantingRecipe, ingredient, fluidIngredient)

        @JvmStatic
        fun washing(ingredient: HTItemIngredient, fluidIngredient: HTFluidIngredient): HTItemWithFluidToChancedItemRecipeBuilder =
            HTItemWithFluidToChancedItemRecipeBuilder(RagiumConst.WASHING, ::HTWashingRecipe, ingredient, fluidIngredient)
    }

    override fun createRecipe(): HTItemWithFluidToChancedItemRecipe = factory.create(ingredient, fluidIngredient, results)

    fun interface Factory<RECIPE : HTItemWithFluidToChancedItemRecipe> {
        fun create(ingredient: HTItemIngredient, fluidIngredient: HTFluidIngredient, results: List<HTChancedItemResult>): RECIPE
    }
}
