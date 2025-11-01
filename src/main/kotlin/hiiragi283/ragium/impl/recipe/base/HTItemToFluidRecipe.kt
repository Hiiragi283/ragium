package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.HTSingleInputFluidRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.SingleRecipeInput

abstract class HTItemToFluidRecipe(val ingredient: HTItemIngredient, val result: HTFluidResult) : HTSingleInputFluidRecipe {
    override fun getRequiredCount(stack: ImmutableItemStack): Int = ingredient.getRequiredAmount(stack)

    override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    final override fun isIncomplete(): Boolean = ingredient.hasNoMatchingStacks() || result.hasNoMatchingStack()

    final override fun assembleFluid(input: SingleRecipeInput, registries: HolderLookup.Provider): ImmutableFluidStack? =
        getFluidResult(input, registries, result)
}
