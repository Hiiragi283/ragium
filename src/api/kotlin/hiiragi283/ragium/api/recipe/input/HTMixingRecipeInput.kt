package hiiragi283.ragium.api.recipe.input

import hiiragi283.core.api.recipe.input.HTFluidRecipeInput
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class HTMixingRecipeInput(
    val firstItem: ItemStack,
    val secondItem: ItemStack,
    val firstFluid: FluidStack,
    val secondFluid: FluidStack,
) : HTFluidRecipeInput {
    constructor(items: List<ItemStack>, fluids: List<FluidStack>) : this(
        items.getOrNull(0) ?: ItemStack.EMPTY,
        items.getOrNull(1) ?: ItemStack.EMPTY,
        fluids.getOrNull(0) ?: FluidStack.EMPTY,
        fluids.getOrNull(1) ?: FluidStack.EMPTY,
    )

    override fun getFluid(index: Int): FluidStack = when (index) {
        0 -> firstFluid
        1 -> secondFluid
        else -> error("No fluid for index $index")
    }

    override fun getFluidSize(): Int = 2

    override fun getItem(index: Int): ItemStack = when (index) {
        0 -> firstItem
        1 -> secondItem
        else -> error("No fluid for index $index")
    }

    override fun size(): Int = 2
}
