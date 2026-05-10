package hiiragi283.ragium.api.recipe.input

import hiiragi283.core.api.recipe.input.HTFluidRecipeInput
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class HTDoubleFluidRecipeInput(val first: FluidStack, val second: FluidStack) : HTFluidRecipeInput {
    override fun getFluid(index: Int): FluidStack = when (index) {
        0 -> first
        1 -> second
        else -> error("No fluid for index $index")
    }

    override fun getFluidSize(): Int = 2

    override fun getItem(index: Int): ItemStack = error("No item for index $index")

    override fun size(): Int = 0
}
