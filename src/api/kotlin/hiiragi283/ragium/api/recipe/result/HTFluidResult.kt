package hiiragi283.ragium.api.recipe.result

import hiiragi283.ragium.api.stack.ImmutableFluidStack

/**
 * [ImmutableFluidStack]向けの[HTRecipeResult]の実装
 */
interface HTFluidResult : HTRecipeResult<ImmutableFluidStack> {
    fun copyWithAmount(amount: Int): HTFluidResult
}
