package hiiragi283.ragium.api.recipe.impl

import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTEnchantingRecipe(ingredients: List<HTItemIngredient>, result: HTItemResult) : HTCombineItemToItemRecipe(ingredients, result) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ENCHANTING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ENCHANTING.get()
}
