package hiiragi283.ragium.api.capability

import hiiragi283.ragium.api.extension.constFunction2
import hiiragi283.ragium.api.extension.mapFunction
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.wrapper.ForwardingItemHandler
import thedarkcolour.kotlinforforge.neoforge.kotlin.supply
import java.util.function.Supplier

class LimitedItemHandler(private val ioProvider: (Int) -> HTStorageIO, delegate: Supplier<IItemHandler>) :
    ForwardingItemHandler(delegate) {
    companion object {
        @JvmStatic
        fun dummy(delegate: IItemHandler): LimitedItemHandler = LimitedItemHandler(constFunction2(HTStorageIO.INTERNAL), supply(delegate))

        @JvmStatic
        fun small(delegate: IItemHandler): LimitedItemHandler = LimitedItemHandler(
            mapOf(
                0 to HTStorageIO.INPUT,
                1 to HTStorageIO.OUTPUT,
            ),
            supply(delegate),
        )

        @JvmStatic
        fun basic(delegate: IItemHandler): LimitedItemHandler = LimitedItemHandler(
            mapOf(
                0 to HTStorageIO.INPUT,
                1 to HTStorageIO.INPUT,
                2 to HTStorageIO.OUTPUT,
                3 to HTStorageIO.OUTPUT,
            ),
            supply(delegate),
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
            supply(delegate),
        )
    }

    constructor(map: Map<Int, HTStorageIO>, delegate: Supplier<IItemHandler>) : this(
        mapFunction(map, HTStorageIO.INTERNAL),
        delegate,
    )

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (!ioProvider(slot).canInsert) return stack
        return super.insertItem(slot, stack, simulate)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (!ioProvider(slot).canExtract) return ItemStack.EMPTY
        return super.extractItem(slot, amount, simulate)
    }
}
