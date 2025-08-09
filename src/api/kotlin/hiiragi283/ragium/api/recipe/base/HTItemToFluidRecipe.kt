package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTFluidRecipe
import hiiragi283.ragium.api.recipe.HTItemToObjRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack

abstract class HTItemToFluidRecipe(override val ingredient: HTItemIngredient, override val result: HTFluidResult) :
    HTItemToObjRecipe<HTFluidResult>,
    HTFluidRecipe<SingleRecipeInput> {
    final override fun assembleFluid(input: SingleRecipeInput, registries: HolderLookup.Provider): FluidStack = result.get()

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY
}
