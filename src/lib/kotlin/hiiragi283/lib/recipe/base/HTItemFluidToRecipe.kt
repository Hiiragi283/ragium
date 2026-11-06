package hiiragi283.lib.recipe.base

import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import net.minecraft.world.item.ItemInstance
import net.neoforged.neoforge.fluids.FluidInstance

/**
 * 1種類のアイテムと液体を入力として受け取るレシピを表すインターフェースです。
 * @see HTItemAndFluidToRecipe
 * @see HTItemOrFluidToRecipe
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
interface HTItemFluidToRecipe<OUTPUT : Any> : HTRecipeFactories.DoubleInput<HTItemAndFluidRecipeInput, ItemInstance, FluidInstance, OUTPUT> {
    override fun matches(input: HTItemAndFluidRecipeInput): Boolean {
        val (item: ItemInstance, fluid: FluidInstance) = input
        return test(item, fluid)
    }

    override fun produce(input: HTItemAndFluidRecipeInput): OUTPUT = apply(input.item, input.fluid)

    override fun getProgressData(input: HTItemAndFluidRecipeInput): HTProgressData = getProgressData(input.item, input.fluid)
}
