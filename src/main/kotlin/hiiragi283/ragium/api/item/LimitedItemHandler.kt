package hiiragi283.ragium.api.item

import hiiragi283.ragium.api.util.HTStorageIO
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.wrapper.ForwardingItemHandler
import java.util.function.Supplier

class LimitedItemHandler(val storageIO: HTStorageIO, delegate: Supplier<IItemHandler>) : ForwardingItemHandler(delegate) {
    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (!storageIO.canInsert) return stack
        return super.insertItem(slot, stack, simulate)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (!storageIO.canInsert) return ItemStack.EMPTY
        return super.extractItem(slot, amount, simulate)
    }
}
