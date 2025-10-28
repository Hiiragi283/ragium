package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.inventory.HTContainerItemSlot
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * @see [mekanism.common.inventory.slot.BasicInventorySlot]
 */
open class HTItemStackSlot protected constructor(
    private val limit: Int,
    private val canExtract: BiPredicate<ImmutableItemStack, HTStorageAccess>,
    private val canInsert: BiPredicate<ImmutableItemStack, HTStorageAccess>,
    private val filter: Predicate<ImmutableItemStack>,
    private val listener: HTContentListener?,
    private val x: Int,
    private val y: Int,
    private val slotType: HTContainerItemSlot.Type,
) : HTItemSlot.Basic() {
    companion object {
        @JvmStatic
        private fun validateLimit(limit: Int): Int {
            check(limit >= 0) { "Limit must be non negative" }
            return limit
        }

        @JvmStatic
        fun create(
            listener: HTContentListener?,
            x: Int,
            y: Int,
            limit: Int = RagiumConst.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: BiPredicate<ImmutableItemStack, HTStorageAccess> = HTPredicates.alwaysTrueBi(),
            canInsert: BiPredicate<ImmutableItemStack, HTStorageAccess> = HTPredicates.alwaysTrueBi(),
            filter: Predicate<ImmutableItemStack> = HTPredicates.alwaysTrue(),
        ): HTItemStackSlot = create(listener, x, y, limit, canExtract, canInsert, filter, HTContainerItemSlot.Type.BOTH)

        @JvmStatic
        private fun create(
            listener: HTContentListener?,
            x: Int,
            y: Int,
            limit: Int = RagiumConst.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: BiPredicate<ImmutableItemStack, HTStorageAccess> = HTPredicates.alwaysTrueBi(),
            canInsert: BiPredicate<ImmutableItemStack, HTStorageAccess> = HTPredicates.alwaysTrueBi(),
            filter: Predicate<ImmutableItemStack> = HTPredicates.alwaysTrue(),
            slotType: HTContainerItemSlot.Type,
        ): HTItemStackSlot = HTItemStackSlot(validateLimit(limit), canExtract, canInsert, filter, listener, x, y, slotType)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            x: Int,
            y: Int,
            limit: Int = RagiumConst.ABSOLUTE_MAX_STACK_SIZE,
            canInsert: Predicate<ImmutableItemStack> = HTPredicates.alwaysTrue(),
            filter: Predicate<ImmutableItemStack> = canInsert,
        ): HTItemStackSlot = create(
            listener,
            x,
            y,
            limit,
            { _, access: HTStorageAccess -> access != HTStorageAccess.EXTERNAL },
            { stack: ImmutableItemStack, _ -> canInsert.test(stack) },
            filter,
            HTContainerItemSlot.Type.INPUT,
        )
    }

    protected constructor(
        limit: Int,
        canExtract: Predicate<ImmutableItemStack>,
        canInsert: Predicate<ImmutableItemStack>,
        filter: Predicate<ImmutableItemStack>,
        listener: HTContentListener?,
        x: Int,
        y: Int,
        slotType: HTContainerItemSlot.Type,
    ) : this(
        limit,
        { stack: ImmutableItemStack, access: HTStorageAccess -> access == HTStorageAccess.MANUAL || canExtract.test(stack) },
        { stack: ImmutableItemStack, _: HTStorageAccess -> canInsert.test(stack) },
        filter,
        listener,
        x,
        y,
        slotType,
    )

    @JvmField
    protected var stack: ItemStack = ItemStack.EMPTY

    override fun getStack(): ImmutableItemStack? = stack.toImmutable()

    override fun getCapacity(stack: ImmutableItemStack?): Int = HTItemSlot.getMaxStackSize(stack, limit)

    final override fun isValid(stack: ImmutableItemStack): Boolean = filter.test(stack)

    final override fun isStackValidForInsert(stack: ImmutableItemStack, access: HTStorageAccess): Boolean =
        super.isStackValidForInsert(stack, access) && canInsert.test(stack, access)

    final override fun canStackExtract(stack: ImmutableItemStack, access: HTStorageAccess): Boolean =
        super.canStackExtract(stack, access) && canExtract.test(stack, access)

    override fun createContainerSlot(): Slot? = HTContainerItemSlot(this, x, y, ::setStackUnchecked, ::isStackValidForInsert, slotType)

    override fun serialize(output: HTValueOutput) {
        output.storeOptional(RagiumConst.ITEM, ImmutableItemStack.OPTIONAL_CODEC, getStack())
    }

    override fun deserialize(input: HTValueInput) {
        input.readOptional(RagiumConst.ITEM, ImmutableItemStack.OPTIONAL_CODEC)?.let(::setStackUnchecked)
    }

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }

    override fun setStack(stack: ImmutableItemStack?) {
        setStackUnchecked(stack, true)
    }

    fun setStackUnchecked(stack: ItemStack, validate: Boolean = false) {
        setStackUnchecked(stack.toImmutable(), validate)
    }

    fun setStackUnchecked(stack: ImmutableItemStack?, validate: Boolean = false) {
        if (stack == null) {
            if (this.getStack() == null) return
            this.stack = ItemStack.EMPTY
        } else if (!validate || isValid(stack)) {
            this.stack = stack.copy().stack
        } else {
            error("Invalid stack for slot: $stack ${stack.componentsPatch()}")
        }
        onContentsChanged()
    }

    override fun updateCount(stack: ImmutableItemStack?, amount: Int) {
        this.stack.count = amount
    }
}
