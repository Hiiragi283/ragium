package hiiragi283.ragium.api.recipe.result

import net.minecraft.core.HolderLookup
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [FluidStack]向けの[HTRecipeResult]の実装
 */
interface HTFluidResult : HTRecipeResult<FluidStack> {
    override fun getOrEmpty(provider: HolderLookup.Provider?): FluidStack = getStackResult(provider).result().orElse(FluidStack.EMPTY)
}
