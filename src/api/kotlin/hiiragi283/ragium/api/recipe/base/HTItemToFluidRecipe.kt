package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTItemToObjRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType

abstract class HTItemToFluidRecipe(recipeType: RecipeType<*>, ingredient: HTItemIngredient, result: HTFluidResult) :
    HTItemToObjRecipe<HTFluidResult>(recipeType, ingredient, result) {
    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY
}
