package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.inventory.HTContainerItemSlot
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.predicate.HTItemPredicates
import hiiragi283.ragium.api.storage.value.HTValueInput
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import java.util.function.BiPredicate
import java.util.function.Predicate
import kotlin.math.min

/**
 * @see [mekanism.common.inventory.slot.BasicInventorySlot]
 */
open class HTItemStackSlot protected constructor(
    private val limit: Long,
    private val canExtract: BiPredicate<ItemStack, HTStorageAccess>,
    private val canInsert: BiPredicate<ItemStack, HTStorageAccess>,
    private val filter: Predicate<ItemStack>,
    private val listener: HTContentListener?,
    private val x: Int,
    private val y: Int,
    private val slotType: HTContainerItemSlot.Type,
) : HTItemSlot.Mutable {
    companion object {
        @JvmField
        val ALWAYS_TRUE: BiPredicate<ItemStack, HTStorageAccess> =
            BiPredicate { _: ItemStack, _: HTStorageAccess -> true }

        @JvmField
        val MANUAL_ONLY: BiPredicate<ItemStack, HTStorageAccess> =
            BiPredicate { _, access: HTStorageAccess -> access == HTStorageAccess.MANUAL }

        @JvmStatic
        fun create(
            listener: HTContentListener?,
            x: Int,
            y: Int,
            limit: Long = HTItemSlot.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: BiPredicate<ItemStack, HTStorageAccess> = ALWAYS_TRUE,
            canInsert: BiPredicate<ItemStack, HTStorageAccess> = ALWAYS_TRUE,
            filter: Predicate<ItemStack> = HTItemPredicates.TRUE,
        ): HTItemStackSlot = create(listener, x, y, limit, canExtract, canInsert, filter, HTContainerItemSlot.Type.BOTH)

        @JvmStatic
        private fun create(
            listener: HTContentListener?,
            x: Int,
            y: Int,
            limit: Long = HTItemSlot.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: BiPredicate<ItemStack, HTStorageAccess> = ALWAYS_TRUE,
            canInsert: BiPredicate<ItemStack, HTStorageAccess> = ALWAYS_TRUE,
            filter: Predicate<ItemStack> = HTItemPredicates.TRUE,
            slotType: HTContainerItemSlot.Type,
        ): HTItemStackSlot = HTItemStackSlot(limit, canExtract, canInsert, filter, listener, x, y, slotType)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            x: Int,
            y: Int,
            limit: Long = HTItemSlot.ABSOLUTE_MAX_STACK_SIZE,
            canInsert: Predicate<ItemStack> = HTItemPredicates.TRUE,
            filter: Predicate<ItemStack> = canInsert,
        ): HTItemStackSlot = create(
            listener,
            x,
            y,
            limit,
            { _: ItemStack, access: HTStorageAccess -> access != HTStorageAccess.EXTERNAL },
            { stack: ItemStack, _: HTStorageAccess -> canInsert.test(stack) },
            filter,
            HTContainerItemSlot.Type.INPUT,
        )

        @JvmStatic
        fun output(
            listener: HTContentListener?,
            x: Int,
            y: Int,
            limit: Long = HTItemSlot.ABSOLUTE_MAX_STACK_SIZE,
        ): HTItemStackSlot = create(
            listener,
            x,
            y,
            limit,
            ALWAYS_TRUE,
            { _: ItemStack, access: HTStorageAccess -> access == HTStorageAccess.INTERNAl },
            slotType = HTContainerItemSlot.Type.OUTPUT,
        )
    }

    protected constructor(
        limit: Long,
        canExtract: Predicate<ItemStack>,
        canInsert: Predicate<ItemStack>,
        filter: Predicate<ItemStack>,
        listener: HTContentListener?,
        x: Int,
        y: Int,
        slotType: HTContainerItemSlot.Type,
    ) : this(
        limit,
        { stack: ItemStack, access: HTStorageAccess -> access == HTStorageAccess.MANUAL || canExtract.test(stack) },
        { stack: ItemStack, _: HTStorageAccess -> canInsert.test(stack) },
        filter,
        listener,
        x,
        y,
        slotType,
    )

    private var stack: ItemStack = ItemStack.EMPTY

    override fun getStack(): ItemStack = stack

    override fun getCapacityAsLong(stack: ItemStack): Long = if (stack.isEmpty) limit else min(limit, stack.maxStackSize.toLong())

    override fun isValid(stack: ItemStack): Boolean = filter.test(stack)

    override fun isItemValidForInsert(stack: ItemStack, access: HTStorageAccess): Boolean =
        super.isItemValidForInsert(stack, access) && canInsert.test(stack, access)

    override fun canItemExtract(stack: ItemStack, access: HTStorageAccess): Boolean = canExtract.test(stack, access)

    override fun createContainerSlot(): Slot? = HTContainerItemSlot(this, x, y, ::setStackUnchecked, slotType)

    override fun deserialize(input: HTValueInput) {
        input.read(RagiumConst.ITEM, BiCodecs.itemStack(true))?.let(::setStackUnchecked)
    }

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }

    override fun setStack(stack: ItemStack) {
        setStackUnchecked(stack, true)
    }

    fun setStackUnchecked(stack: ItemStack, validate: Boolean = false) {
        if (stack.isEmpty) {
            if (getStack().isEmpty) return
            this.stack = ItemStack.EMPTY
        } else if (!validate || isValid(stack)) {
            this.stack = stack.copy()
        } else {
            error("Invalid stack for slot: $stack ${stack.componentsPatch}")
        }
        onContentsChanged()
    }
}
