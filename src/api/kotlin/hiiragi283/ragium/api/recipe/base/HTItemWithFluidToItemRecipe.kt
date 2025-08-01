package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTItemWithFluidToObjRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import java.util.Optional

abstract class HTItemWithFluidToItemRecipe(
    recipeType: RecipeType<*>,
    itemIngredient: Optional<HTItemIngredient>,
    fluidIngredient: Optional<HTFluidIngredient>,
    result: HTItemResult,
) : HTItemWithFluidToObjRecipe<HTItemResult>(recipeType, itemIngredient, fluidIngredient, result) {
    final override fun assemble(input: HTItemWithFluidRecipeInput, registries: HolderLookup.Provider): ItemStack =
        if (test(input)) getResultItem(registries) else ItemStack.EMPTY

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.get()
}
