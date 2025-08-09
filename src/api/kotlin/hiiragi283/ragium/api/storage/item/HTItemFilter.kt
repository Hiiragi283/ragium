package hiiragi283.ragium.api.storage.item

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler

interface HTItemFilter {
    companion object {
        @JvmField
        val EMPTY: HTItemFilter = simple(intArrayOf(), intArrayOf())

        @JvmStatic
        fun simple(inputs: IntArray, outputs: IntArray): Simple = Simple(inputs, outputs)
    }

    fun canInsert(handler: IItemHandler, slot: Int, stack: ItemStack): Boolean

    fun canExtract(handler: IItemHandler, slot: Int, amount: Int): Boolean

    class Simple(val inputs: IntArray, val outputs: IntArray) : HTItemFilter {
        fun reverse(): Simple = Simple(outputs, inputs)

        override fun canInsert(handler: IItemHandler, slot: Int, stack: ItemStack): Boolean = slot in inputs

        override fun canExtract(handler: IItemHandler, slot: Int, amount: Int): Boolean = slot in outputs
    }
}
