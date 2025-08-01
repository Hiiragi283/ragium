package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTItemToObjRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType

abstract class HTItemToItemRecipe(recipeType: RecipeType<*>, ingredient: HTItemIngredient, result: HTItemResult) :
    HTItemToObjRecipe<HTItemResult>(recipeType, ingredient, result) {
    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.get()
}
