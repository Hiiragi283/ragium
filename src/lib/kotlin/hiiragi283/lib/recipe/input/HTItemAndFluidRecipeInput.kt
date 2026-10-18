package hiiragi283.lib.recipe.input

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 一種類のアイテムと液体を保持する[HTFluidRecipeInput]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTItemAndFluidRecipeInput(val item: ItemStack, val fluid: FluidStack) : HTFluidRecipeInput {
    override val fluidSize: Int get() = 1

    override fun getFluid(index: Int): FluidStack = fluid

    override fun getItem(index: Int): ItemStack = item

    override fun size(): Int = 1
}
