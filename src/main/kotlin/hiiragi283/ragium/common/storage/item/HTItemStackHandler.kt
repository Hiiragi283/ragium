package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.storage.item.HTItemHandler
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.ItemStackHandler

open class HTItemStackHandler(size: Int = 1, private var callback: (Int) -> Unit = {}) :
    ItemStackHandler(size),
    HTItemHandler {
    constructor(size: Int = 1, callback: () -> Unit = {}) : this(size, { _: Int -> callback() })

    override fun onLoad() {
        slotRange.forEach(::onContentsChanged)
    }

    override fun onContentsChanged(slot: Int) {
        callback(slot)
    }

    //    Extensions    //

    override val isEmpty: Boolean get() = stacks.isEmpty() || stacks.all(ItemStack::isEmpty)

    override fun consumeStackInSlot(slot: Int, count: Int) {
        val stack: ItemStack = getStackInSlot(slot)
        if (stack.hasCraftingRemainingItem()) {
            setStackInSlot(slot, stack.craftingRemainingItem)
        } else {
            stack.shrink(count)
        }
    }

    override fun getStackView(): Iterable<ItemStack> = stacks.map(ItemStack::copy)
}
