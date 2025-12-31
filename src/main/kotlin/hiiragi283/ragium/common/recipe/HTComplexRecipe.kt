package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.recipe.result.HTComplexResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import org.apache.commons.lang3.math.Fraction

abstract class HTComplexRecipe(val result: HTComplexResult, time: Int, exp: Fraction) : HTProcessingRecipe(time, exp) {
    fun getResultFluid(provider: HolderLookup.Provider): FluidStack = result.getRight()?.getStackOrEmpty(provider) ?: FluidStack.EMPTY

    final override fun assemble(input: HTRecipeInput, registries: HolderLookup.Provider): ItemStack =
        result.getLeft()?.getStackOrEmpty(registries) ?: ItemStack.EMPTY
}
