package hiiragi283.ragium.api.recipe.input

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

data class HTSingleFluidRecipeInput(val fluid: FluidStack) : RecipeInput {
    override fun getItem(index: Int): ItemStack = ItemStack.EMPTY

    override fun size(): Int = 0

    override fun isEmpty(): Boolean = fluid.isEmpty
}
