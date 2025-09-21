package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.HTSingleInputRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

abstract class HTItemToItemRecipe internal constructor(val ingredient: HTItemIngredient, val result: HTItemResult) :
    HTSingleInputRecipe {
        override fun getIngredientCount(input: SingleRecipeInput): Int = ingredient.getRequiredAmount(input.item())

        override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

        final override fun isIncomplete(): Boolean = ingredient.hasNoMatchingStacks() || result.hasNoMatchingStack()

        final override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack =
            getItemResult(input, registries, result)
    }
