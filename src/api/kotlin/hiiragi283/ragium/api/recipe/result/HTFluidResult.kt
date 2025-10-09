package hiiragi283.ragium.api.recipe.result

import net.neoforged.neoforge.fluids.FluidStack

/**
 * [FluidStack]向けの[HTRecipeResult]の実装
 */
interface HTFluidResult : HTRecipeResult<FluidStack> {
    fun copyWithAmount(amount: Int): HTFluidResult
}
