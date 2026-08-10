package hiiragi283.lib.recipe.input

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 一種類のアイテムと液体を保持する[HTFluidRecipeInput]の実装クラスです。
 *
 * 参照 : [Mekanism - SingleFluidRecipeInput](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/recipes/vanilla_input/SingleFluidRecipeInput.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTSingleFluidRecipeInput(val fluid: FluidStack) : HTFluidRecipeInput {
    override val fluidSize: Int get() = 1

    override fun getFluid(index: Int): FluidStack = fluid

    override fun getItem(index: Int): ItemStack = ItemStack.EMPTY

    override fun size(): Int = 0
}
