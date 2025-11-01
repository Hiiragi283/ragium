package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.HTSingleInputRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.SingleRecipeInput

abstract class HTItemToItemRecipe(val ingredient: HTItemIngredient, val result: HTItemResult) : HTSingleInputRecipe {
    override fun getRequiredCount(stack: ImmutableItemStack): Int = ingredient.getRequiredAmount(stack)

    override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    final override fun isIncomplete(): Boolean = ingredient.hasNoMatchingStacks() || result.hasNoMatchingStack()

    final override fun assembleItem(input: SingleRecipeInput, registries: HolderLookup.Provider): ImmutableItemStack? =
        getItemResult(input, registries, result)
}
