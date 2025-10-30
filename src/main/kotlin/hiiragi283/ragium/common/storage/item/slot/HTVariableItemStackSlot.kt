package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.inventory.HTContainerItemSlot
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.util.HTContentListener
import java.util.function.BiPredicate
import java.util.function.Predicate
import java.util.function.ToIntFunction

class HTVariableItemStackSlot(
    private val capacityFunction: ToIntFunction<ImmutableItemStack?>,
    canExtract: BiPredicate<ImmutableItemStack, HTStorageAccess>,
    canInsert: BiPredicate<ImmutableItemStack, HTStorageAccess>,
    filter: Predicate<ImmutableItemStack>,
    listener: HTContentListener?,
    x: Int,
    y: Int,
    slotType: HTContainerItemSlot.Type,
) : HTItemStackSlot(
        capacityFunction.applyAsInt(null),
        canExtract,
        canInsert,
        filter,
        listener,
        x,
        y,
        slotType,
    ) {
    companion object {
        @JvmStatic
        fun create(
            listener: HTContentListener?,
            capacity: ToIntFunction<ImmutableItemStack?>,
            x: Int,
            y: Int,
        ): HTVariableItemStackSlot = HTVariableItemStackSlot(
            capacity,
            HTPredicates.alwaysTrueBi(),
            HTPredicates.alwaysTrueBi(),
            HTPredicates.alwaysTrue(),
            listener,
            x,
            y,
            HTContainerItemSlot.Type.BOTH,
        )

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            capacity: ToIntFunction<ImmutableItemStack?>,
            canInsert: Predicate<ImmutableItemStack> = HTPredicates.alwaysTrue(),
            filter: Predicate<ImmutableItemStack> = canInsert,
            x: Int,
            y: Int,
        ): HTVariableItemStackSlot = HTVariableItemStackSlot(
            capacity,
            HTPredicates.notExternal(),
            { stack: ImmutableItemStack, _ -> canInsert.test(stack) },
            filter,
            listener,
            x,
            y,
            HTContainerItemSlot.Type.INPUT,
        )

        @JvmStatic
        fun output(
            listener: HTContentListener?,
            capacity: ToIntFunction<ImmutableItemStack?>,
            x: Int,
            y: Int,
        ): HTVariableItemStackSlot = HTVariableItemStackSlot(
            capacity,
            HTPredicates.alwaysTrueBi(),
            HTPredicates.internalOnly(),
            HTPredicates.alwaysTrue(),
            listener,
            x,
            y,
            HTContainerItemSlot.Type.OUTPUT,
        )
    }

    override fun getCapacity(stack: ImmutableItemStack?): Int = capacityFunction.applyAsInt(stack)
}
