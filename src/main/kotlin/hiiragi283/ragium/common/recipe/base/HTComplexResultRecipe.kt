package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTComplexResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack
import org.apache.commons.lang3.math.Fraction

abstract class HTComplexResultRecipe<INPUT : RecipeInput>(val result: HTComplexResult, time: Int, exp: Fraction) :
    HTProcessingRecipe<INPUT>(time, exp) {
    fun getResultFluid(provider: HolderLookup.Provider): FluidStack = result.getRight()?.getStackOrEmpty(provider) ?: FluidStack.EMPTY

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack =
        result.getLeft()?.getStackOrEmpty(registries) ?: ItemStack.EMPTY

    abstract class Simple(result: HTComplexResult, time: Int, exp: Fraction) :
        HTComplexResultRecipe<HTItemAndFluidRecipeInput>(result, time, exp) {
        abstract fun getItemIngredient(): HTItemIngredient?

        abstract fun getFluidIngredient(): HTFluidIngredient?
    }
}
