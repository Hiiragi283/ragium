package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.base.HTItemToChancedRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTCuttingRecipe(
    ingredient: HTItemIngredient,
    result: HTItemResult,
    extraResults: List<HTChancedItemResult>,
    parameters: SubParameters,
) : HTItemToChancedRecipe(ingredient, result, extraResults, parameters) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CUTTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CUTTING.get()
}
