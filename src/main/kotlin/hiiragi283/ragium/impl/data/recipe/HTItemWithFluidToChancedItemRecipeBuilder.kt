package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.impl.recipe.HTPlantingRecipe
import hiiragi283.ragium.impl.recipe.HTWashingRecipe

class HTItemWithFluidToChancedItemRecipeBuilder<RECIPE : HTItemWithFluidToChancedItemRecipe>(
    prefix: String,
    private val factory: Factory<RECIPE>,
    val ingredient: HTItemIngredient,
    val fluidIngredient: HTFluidIngredient,
) : HTChancedItemRecipeBuilder<RECIPE>(prefix) {
    companion object {
        @JvmStatic
        fun planting(
            ingredient: HTItemIngredient,
            fluidIngredient: HTFluidIngredient,
        ): HTItemWithFluidToChancedItemRecipeBuilder<HTPlantingRecipe> =
            HTItemWithFluidToChancedItemRecipeBuilder(RagiumConst.PLANTING, ::HTPlantingRecipe, ingredient, fluidIngredient)

        @JvmStatic
        fun washing(
            ingredient: HTItemIngredient,
            fluidIngredient: HTFluidIngredient,
        ): HTItemWithFluidToChancedItemRecipeBuilder<HTWashingRecipe> =
            HTItemWithFluidToChancedItemRecipeBuilder(RagiumConst.WASHING, ::HTWashingRecipe, ingredient, fluidIngredient)
    }

    override fun createRecipe(): RECIPE = factory.create(ingredient, fluidIngredient, results)

    fun interface Factory<RECIPE : HTItemWithFluidToChancedItemRecipe> {
        fun create(
            ingredient: HTItemIngredient,
            fluidIngredient: HTFluidIngredient,
            results: List<HTChancedItemRecipe.ChancedResult>,
        ): RECIPE
    }
}
