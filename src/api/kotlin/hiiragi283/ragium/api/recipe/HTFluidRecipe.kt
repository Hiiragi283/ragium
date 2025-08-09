package hiiragi283.ragium.api.recipe

import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack

interface HTFluidRecipe<I : RecipeInput> : HTRecipe<I> {
    fun assembleFluid(input: SingleRecipeInput, registries: HolderLookup.Provider): FluidStack
}
