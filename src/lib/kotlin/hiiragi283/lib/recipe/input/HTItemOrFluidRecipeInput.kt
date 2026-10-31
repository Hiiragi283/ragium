package hiiragi283.lib.recipe.input

import hiiragi283.lib.util.Either
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
@JvmRecord
data class HTItemOrFluidRecipeInput(val content: Either<ItemStack, FluidStack>) : HTFluidRecipeInput {
    override val fluidSize: Int get() = if (content.isRight()) 1 else 0

    override fun getFluid(index: Int): FluidStack = content.getOrNull() ?: FluidStack.EMPTY

    override fun getItem(index: Int): ItemStack = content.leftOrNull() ?: ItemStack.EMPTY

    override fun size(): Int = if (content.isLeft()) 1 else 0
}
