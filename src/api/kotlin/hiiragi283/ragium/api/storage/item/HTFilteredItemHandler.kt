package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.storage.HTTransferIO
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler

class HTFilteredItemHandler(
    private val delegate: IItemHandler,
    private val inputSlots: IntArray,
    private val outputSlots: IntArray,
    private val direction: Direction?,
    private val provider: HTTransferIO.Provider,
) : IItemHandler {
    constructor(delegate: HTItemHandler, direction: Direction?, provider: HTTransferIO.Provider) : this(
        delegate,
        delegate.inputSlots,
        delegate.outputSlots,
        direction,
        provider,
    )

    //    IItemHandler    //

    override fun getSlots(): Int = delegate.slots

    override fun getStackInSlot(slot: Int): ItemStack = delegate.getStackInSlot(slot)

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        // 指定したスロットが搬入でなければパス
        if (slot !in inputSlots) return stack
        // 指定された向きのモードが搬入できない場合はパス
        if (direction != null) {
            if (!provider[direction].canInsert) return stack
        }
        return delegate.insertItem(slot, stack, simulate)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        // 指定したスロットが搬出でなければパス
        if (slot !in outputSlots) return ItemStack.EMPTY
        // 指定された向きのモードが搬出できない場合はパス
        if (direction != null) {
            if (!provider[direction].canExtract) return ItemStack.EMPTY
        }
        return delegate.extractItem(slot, amount, simulate)
    }

    override fun getSlotLimit(slot: Int): Int = delegate.getSlotLimit(slot)

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = delegate.isItemValid(slot, stack)
}
