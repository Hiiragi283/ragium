package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.base.HTSingleProcessingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTCompressingRecipe(ingredient: HTItemIngredient, result: HTItemResult, parameters: SubParameters) :
    HTSingleProcessingRecipe.ItemToItem(ingredient, result, parameters) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.COMPRESSING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.COMPRESSING.get()
}
