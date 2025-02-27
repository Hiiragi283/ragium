package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.storage.HTFluidTank
import hiiragi283.ragium.api.storage.HTItemSlot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 機械レシピのインプットを表すクラス
 * @param items アイテムのインプットの一覧
 * @param fluids 液体のインプットの一覧
 */
class HTMachineRecipeInput private constructor(val items: List<ItemStack>, val fluids: List<FluidStack>) : RecipeInput {
    override fun getItem(index: Int): ItemStack = items.getOrNull(index) ?: ItemStack.EMPTY

    fun getFluid(index: Int): FluidStack = fluids.getOrNull(index) ?: FluidStack.EMPTY

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean = items.isEmpty() && fluids.isEmpty()

    //    Builder    //

    class Builder {
        val items: MutableList<ItemStack> = mutableListOf()
        val fluids: MutableList<FluidStack> = mutableListOf()

        fun addItem(slot: HTItemSlot): Builder = apply {
            items.add(slot.getStack())
        }

        fun addFluid(tank: HTFluidTank): Builder = apply {
            fluids.add(tank.fluid)
        }

        fun build(): HTMachineRecipeInput = HTMachineRecipeInput(items.map(ItemStack::copy), fluids.map(FluidStack::copy))
    }
}
