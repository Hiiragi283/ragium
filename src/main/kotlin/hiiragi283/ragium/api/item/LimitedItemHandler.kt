package hiiragi283.ragium.api.item

import com.google.common.base.Functions
import hiiragi283.ragium.api.util.HTStorageIO
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.wrapper.ForwardingItemHandler
import java.util.function.Function
import java.util.function.Supplier

class LimitedItemHandler(private val ioProvider: Function<Int, HTStorageIO>, delegate: Supplier<IItemHandler>) :
    ForwardingItemHandler(delegate) {
    constructor(map: Map<Int, HTStorageIO>, delegate: Supplier<IItemHandler>) : this(Functions.forMap(map), delegate)

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (!ioProvider.apply(slot).canInsert) return stack
        return super.insertItem(slot, stack, simulate)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (!ioProvider.apply(slot).canExtract) return ItemStack.EMPTY
        return super.extractItem(slot, amount, simulate)
    }
}
