package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.inventory.HTContainerItemSlot
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.stack.maxStackSize
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
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
    private val canExtract: BiPredicate<ImmutableItemStack, HTStorageAccess>,
    private val canInsert: BiPredicate<ImmutableItemStack, HTStorageAccess>,
    private val filter: Predicate<ImmutableItemStack>,
    private val listener: HTContentListener?,
    private val x: Int,
    private val y: Int,
    private val slotType: HTContainerItemSlot.Type,
) : HTItemSlot.Mutable() {
    companion object {
        @JvmField
        val ALWAYS_TRUE: BiPredicate<ImmutableItemStack, HTStorageAccess> =
            BiPredicate { _, _ -> true }

        @JvmField
        val INTERNAL_ONLY: BiPredicate<ImmutableItemStack, HTStorageAccess> =
            BiPredicate { _, access: HTStorageAccess -> access == HTStorageAccess.INTERNAL }

        @JvmField
        val MANUAL_ONLY: BiPredicate<ImmutableItemStack, HTStorageAccess> =
            BiPredicate { _, access: HTStorageAccess -> access == HTStorageAccess.MANUAL }

        @JvmStatic
        fun create(
            listener: HTContentListener?,
            x: Int,
            y: Int,
            limit: Long = RagiumConst.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: BiPredicate<ImmutableItemStack, HTStorageAccess> = ALWAYS_TRUE,
            canInsert: BiPredicate<ImmutableItemStack, HTStorageAccess> = ALWAYS_TRUE,
            filter: Predicate<ImmutableItemStack> = ImmutableStack.alwaysTrue(),
        ): HTItemStackSlot = create(listener, x, y, limit, canExtract, canInsert, filter, HTContainerItemSlot.Type.BOTH)

        @JvmStatic
        private fun create(
            listener: HTContentListener?,
            x: Int,
            y: Int,
            limit: Long = RagiumConst.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: BiPredicate<ImmutableItemStack, HTStorageAccess> = ALWAYS_TRUE,
            canInsert: BiPredicate<ImmutableItemStack, HTStorageAccess> = ALWAYS_TRUE,
            filter: Predicate<ImmutableItemStack> = ImmutableStack.alwaysTrue(),
            slotType: HTContainerItemSlot.Type,
        ): HTItemStackSlot = HTItemStackSlot(limit, canExtract, canInsert, filter, listener, x, y, slotType)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            x: Int,
            y: Int,
            limit: Long = RagiumConst.ABSOLUTE_MAX_STACK_SIZE,
            canInsert: Predicate<ImmutableItemStack> = ImmutableStack.alwaysTrue(),
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
        limit: Long,
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

    override fun getStack(): ImmutableItemStack = stack.toImmutable()

    override fun getCapacityAsLong(stack: ImmutableItemStack): Long =
        if (stack.isEmpty()) limit else min(limit, stack.maxStackSize().toLong())

    override fun isValid(stack: ImmutableItemStack): Boolean = filter.test(stack)

    override fun isStackValidForInsert(stack: ImmutableItemStack, access: HTStorageAccess): Boolean =
        super.isStackValidForInsert(stack, access) && canInsert.test(stack, access)

    override fun canStackExtract(stack: ImmutableItemStack, access: HTStorageAccess): Boolean =
        super.canStackExtract(stack, access) && canExtract.test(stack, access)

    override fun createContainerSlot(): Slot? = HTContainerItemSlot(this, x, y, ::setStackUnchecked, ::isStackValidForInsert, slotType)

    override fun serialize(output: HTValueOutput) {
        output.store(RagiumConst.ITEM, ImmutableItemStack.CODEC, getStack())
    }

    override fun deserialize(input: HTValueInput) {
        input.read(RagiumConst.ITEM, ImmutableItemStack.CODEC)?.let(::setStackUnchecked)
    }

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }

    override fun setStack(stack: ImmutableItemStack) {
        setStackUnchecked(stack, true)
    }

    fun setStackUnchecked(stack: ItemStack, validate: Boolean = false) {
        setStackUnchecked(stack.toImmutable(), validate)
    }

    fun setStackUnchecked(stack: ImmutableItemStack, validate: Boolean = false) {
        if (stack.isEmpty()) {
            if (this.isEmpty()) return
            this.stack = ItemStack.EMPTY
        } else if (!validate || isValid(stack)) {
            this.stack = stack.copy().stack
        } else {
            error("Invalid stack for slot: $stack ${stack.componentsPatch()}")
        }
        onContentsChanged()
    }

    override fun updateCount(stack: ImmutableItemStack, amount: Int) {
        this.stack.count = amount
    }
}
