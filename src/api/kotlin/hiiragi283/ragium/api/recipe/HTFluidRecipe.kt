package hiiragi283.ragium.api.recipe

import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

interface HTFluidRecipe<INPUT : RecipeInput> : HTRecipe<INPUT> {
    fun assembleFluid(input: INPUT, registries: HolderLookup.Provider): FluidStack
}
