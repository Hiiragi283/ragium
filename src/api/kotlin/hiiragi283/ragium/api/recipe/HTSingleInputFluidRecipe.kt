package hiiragi283.ragium.api.recipe

import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

interface HTSingleInputFluidRecipe :
    HTSingleInputRecipe,
    HTFluidRecipe<SingleRecipeInput> {
    override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY
}
