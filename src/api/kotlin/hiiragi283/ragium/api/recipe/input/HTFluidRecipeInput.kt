package hiiragi283.ragium.api.recipe.input

import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

interface HTFluidRecipeInput : RecipeInput {
    fun getFluid(index: Int): FluidStack

    abstract override fun isEmpty(): Boolean
}
