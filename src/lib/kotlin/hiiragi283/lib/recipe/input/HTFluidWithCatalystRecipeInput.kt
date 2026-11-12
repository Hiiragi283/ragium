package hiiragi283.lib.recipe.input

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 一種類の液体と触媒を保持する[HTFluidRecipeInput]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
@JvmRecord
data class HTFluidWithCatalystRecipeInput(val fluid: FluidStack, val catalyst: ItemStack) : HTFluidRecipeInput {
    override val fluidSize: Int get() = 1

    override fun getFluid(index: Int): FluidStack = when (index) {
        0 -> fluid
        else -> error("No fluid for index: $index")
    }

    override fun getItem(index: Int): ItemStack = error("No item for index: $index")

    override fun size(): Int = 0
}
