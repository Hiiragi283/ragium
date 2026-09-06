package hiiragi283.lib.recipe.input

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 複数の液体を保持する[HTFluidRecipeInput]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
@JvmRecord
data class HTFluidListRecipeInput(val fluids: List<FluidStack>) : HTFluidRecipeInput {
    constructor(vararg fluids: FluidStack) : this(fluids.toList())

    override val fluidSize: Int get() = fluids.size

    override fun getFluid(index: Int): FluidStack = fluids[index]

    override fun getItem(index: Int): ItemStack = error("No item for index: $index")

    override fun size(): Int = 0

    override fun isEmpty(): Boolean = fluids.isEmpty() || fluids.all(FluidStack::isEmpty)
}
