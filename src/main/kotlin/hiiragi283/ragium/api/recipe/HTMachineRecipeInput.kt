package hiiragi283.ragium.api.recipe

import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 機械レシピのインプットを表すクラス
 * @param pos 機械レシピを実行している座標
 * @param items アイテムのインプットの一覧
 * @param fluids 液体のインプットの一覧
 */
class HTMachineRecipeInput private constructor(val pos: BlockPos, val items: List<ItemStack>, val fluids: List<FluidStack>) : RecipeInput {
    companion object {
        @JvmStatic
        fun of(pos: BlockPos, items: List<ItemStack>, fluids: List<FluidStack>): HTMachineRecipeInput =
            HTMachineRecipeInput(pos, items, fluids)

        @JvmStatic
        fun of(pos: BlockPos, item: ItemStack): HTMachineRecipeInput = HTMachineRecipeInput(pos, listOf(item), listOf())

        @JvmStatic
        fun of(pos: BlockPos, fluid: FluidStack): HTMachineRecipeInput = HTMachineRecipeInput(pos, listOf(), listOf(fluid))

        @JvmStatic
        fun of(pos: BlockPos, item: ItemStack, fluid: FluidStack): HTMachineRecipeInput =
            HTMachineRecipeInput(pos, listOf(item), listOf(fluid))
    }

    override fun getItem(index: Int): ItemStack = items.getOrNull(index) ?: ItemStack.EMPTY

    fun getFluid(index: Int): FluidStack = fluids.getOrNull(index) ?: FluidStack.EMPTY

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean = items.isEmpty() && fluids.isEmpty()
}
