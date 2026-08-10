package hiiragi283.lib.recipe.input

import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 液体に対応した[RecipeInput]の拡張インターフェースです。
 *
 * 参照 : [Mekanism - FluidRecipeInput](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/recipes/vanilla_input/FluidRecipeInput.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTFluidRecipeInput : RecipeInput {
    /**
     * 液体の種類数
     * @since 26.1.2
     */
    val fluidSize: Int

    /**
     * 有効な液体のインデックスの範囲
     * @since 26.1.2
     */
    val fluidIndices: IntRange get() = (0..<this.fluidSize)

    /**
     * 保持しているすべての液体が空かどうか
     * @since 26.1.2
     */
    val isFluidEmpty: Boolean get() = this.fluidIndices.map(::getFluid).all(FluidStack::isEmpty)

    /**
     * 指定した[インデックス][index]液体を取得します。
     */
    fun getFluid(index: Int): FluidStack

    /**
     * 液体の[List]に変換します。
     * @since 26.1.2
     */
    fun asFluidList(): List<FluidStack> = object : AbstractList<FluidStack>() {
        override val size: Int get() = this@HTFluidRecipeInput.fluidSize

        override fun get(index: Int): FluidStack = this@HTFluidRecipeInput.getFluid(index)

        override fun isEmpty(): Boolean = this@HTFluidRecipeInput.isFluidEmpty
    }

    override fun isEmpty(): Boolean = super.isEmpty() && isFluidEmpty
}
