package hiiragi283.ragium.api.capability

import com.google.common.base.Functions
import com.google.common.base.Suppliers
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.wrapper.ForwardingItemHandler
import java.util.function.Function
import java.util.function.Supplier

class LimitedItemHandler(private val ioProvider: Function<Int, HTStorageIO>, delegate: Supplier<IItemHandler>) :
    ForwardingItemHandler(delegate) {
    companion object {
        @JvmStatic
        fun small(delegate: IItemHandler): LimitedItemHandler = LimitedItemHandler(
            mapOf(
                0 to HTStorageIO.INPUT,
                1 to HTStorageIO.OUTPUT,
            ),
            Suppliers.ofInstance(delegate),
        )

        @JvmStatic
        fun basic(delegate: IItemHandler): LimitedItemHandler = LimitedItemHandler(
            mapOf(
                0 to HTStorageIO.INPUT,
                1 to HTStorageIO.INPUT,
                2 to HTStorageIO.OUTPUT,
                3 to HTStorageIO.OUTPUT,
            ),
            Suppliers.ofInstance(delegate),
        )

        @JvmStatic
        fun large(delegate: IItemHandler): LimitedItemHandler = LimitedItemHandler(
            mapOf(
                0 to HTStorageIO.INPUT,
                1 to HTStorageIO.INPUT,
                2 to HTStorageIO.INPUT,
                3 to HTStorageIO.OUTPUT,
                4 to HTStorageIO.OUTPUT,
                5 to HTStorageIO.OUTPUT,
            ),
            Suppliers.ofInstance(delegate),
        )
    }

    constructor(map: Map<Int, HTStorageIO>, delegate: Supplier<IItemHandler>) : this(
        Functions.forMap(map, HTStorageIO.INTERNAL),
        delegate,
    )

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (!ioProvider.apply(slot).canInsert) return stack
        return super.insertItem(slot, stack, simulate)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (!ioProvider.apply(slot).canExtract) return ItemStack.EMPTY
        return super.extractItem(slot, amount, simulate)
    }
}
