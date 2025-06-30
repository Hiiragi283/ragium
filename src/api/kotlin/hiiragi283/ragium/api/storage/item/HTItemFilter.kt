package hiiragi283.ragium.api.storage.item

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler

interface HTItemFilter {
    companion object {
        @JvmField
        val ALWAYS: HTItemFilter = Impl(canInsert = true, canExtract = true)

        @JvmField
        val INSERT_ONLY: HTItemFilter = Impl(canInsert = true, canExtract = false)

        @JvmField
        val EXTRACT_ONLY: HTItemFilter = Impl(canInsert = false, canExtract = true)

        @JvmField
        val VIEW_ONLY: HTItemFilter = Impl(canInsert = false, canExtract = false)
    }

    fun canInsert(handler: IItemHandler, slot: Int, stack: ItemStack): Boolean

    fun canExtract(handler: IItemHandler, slot: Int, amount: Int): Boolean

    private class Impl(private val canInsert: Boolean, private val canExtract: Boolean) : HTItemFilter {
        override fun canInsert(handler: IItemHandler, slot: Int, stack: ItemStack): Boolean = canInsert

        override fun canExtract(handler: IItemHandler, slot: Int, amount: Int): Boolean = canExtract
    }
}
