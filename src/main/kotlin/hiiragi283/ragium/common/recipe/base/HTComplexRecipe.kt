package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTComplexResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import org.apache.commons.lang3.math.Fraction

abstract class HTComplexRecipe(val result: HTComplexResult, time: Int, exp: Fraction) :
    HTProcessingRecipe<HTItemAndFluidRecipeInput>(time, exp) {
    abstract fun getItemIngredient(): HTItemIngredient?

    abstract fun getFluidIngredient(): HTFluidIngredient?

    fun getResultFluid(provider: HolderLookup.Provider): FluidStack = result.getRight()?.getStackOrEmpty(provider) ?: FluidStack.EMPTY

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack =
        result.getLeft()?.getStackOrEmpty(registries) ?: ItemStack.EMPTY
}
