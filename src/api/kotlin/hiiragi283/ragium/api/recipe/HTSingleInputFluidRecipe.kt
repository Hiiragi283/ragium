package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.SingleRecipeInput

interface HTSingleInputFluidRecipe :
    HTSingleInputRecipe,
    HTFluidRecipe<SingleRecipeInput> {
    override fun assembleItem(input: SingleRecipeInput, registries: HolderLookup.Provider): ImmutableItemStack? = null
}
