package hiiragi283.ragium.api.recipe.input

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

data class HTItemWithFluidRecipeInput(val item: ItemStack, val fluid: FluidStack) : RecipeInput {
    override fun getItem(index: Int): ItemStack = item

    override fun size(): Int = 1

    override fun isEmpty(): Boolean = item.isEmpty && fluid.isEmpty
}
