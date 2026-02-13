package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.base.HTSingleProcessingRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTCompressingRecipeN(ingredient: HTItemIngredient, result: HTItemResult, parameters: SubParameters) :
    HTSingleProcessingRecipe.ItemToItem(ingredient, result, parameters) {
    override fun getSerializer(): RecipeSerializer<*> {
        TODO("Not yet implemented")
    }

    override fun getType(): RecipeType<*> {
        TODO("Not yet implemented")
    }
}
