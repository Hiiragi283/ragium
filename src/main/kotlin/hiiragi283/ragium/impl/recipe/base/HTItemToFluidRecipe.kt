package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.HTSingleInputFluidRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack

abstract class HTItemToFluidRecipe internal constructor(val ingredient: HTItemIngredient, val result: HTFluidResult) :
    HTSingleInputFluidRecipe {
        override fun getIngredientCount(input: SingleRecipeInput): Int = ingredient.getRequiredAmount(input.item())

        override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

        final override fun isIncomplete(): Boolean = ingredient.hasNoMatchingStacks() || result.hasNoMatchingStack()

        final override fun assembleFluid(input: SingleRecipeInput, registries: HolderLookup.Provider): FluidStack =
            getFluidResult(input, registries, result)
    }
