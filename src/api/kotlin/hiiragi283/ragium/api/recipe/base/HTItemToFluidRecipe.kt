package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTFluidRecipe
import hiiragi283.ragium.api.recipe.HTItemToObjRecipe
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack

interface HTItemToFluidRecipe :
    HTItemToObjRecipe<HTFluidResult>,
    HTFluidRecipe<SingleRecipeInput> {
    override fun assembleFluid(input: SingleRecipeInput, registries: HolderLookup.Provider): FluidStack =
        getFluidResult(input, registries, result)

    override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY
}
