package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import org.apache.commons.lang3.math.Fraction

class HTCrushingRecipe(
    ingredient: HTItemIngredient,
    result: HTItemResult,
    extraResults: List<HTChancedItemResult>,
    time: Int,
    exp: Fraction,
) : HTChancedRecipe(ingredient, result, extraResults, time, exp) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CRUSHING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CRUSHING.get()
}
