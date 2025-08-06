package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTFluidWithCatalystToObjRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import java.util.*

abstract class HTFluidWithCatalystToItemRecipe(
    recipeType: RecipeType<*>,
    ingredient: HTFluidIngredient,
    catalyst: Optional<HTItemIngredient>,
    result: HTItemResult,
) : HTFluidWithCatalystToObjRecipe<HTItemResult>(
        recipeType,
        ingredient,
        catalyst,
        result,
    ) {
    override fun getResultItem(provider: HolderLookup.Provider): ItemStack = result.get()
}
