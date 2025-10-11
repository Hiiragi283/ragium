package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.inventory.HTContainerItemSlot
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageStack
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemStorageStack
import hiiragi283.ragium.api.storage.item.maxStackSize
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
    private val canExtract: BiPredicate<HTItemStorageStack, HTStorageAccess>,
    private val canInsert: BiPredicate<HTItemStorageStack, HTStorageAccess>,
    private val filter: Predicate<HTItemStorageStack>,
    private val listener: HTContentListener?,
    private val x: Int,
    private val y: Int,
    private val slotType: HTContainerItemSlot.Type,
) : HTItemSlot.Mutable() {
    companion object {
        @JvmField
        val ALWAYS_TRUE: BiPredicate<HTItemStorageStack, HTStorageAccess> =
            BiPredicate { _, _ -> true }

        @JvmField
        val MANUAL_ONLY: BiPredicate<HTItemStorageStack, HTStorageAccess> =
            BiPredicate { _, access: HTStorageAccess -> access == HTStorageAccess.MANUAL }

        @JvmStatic
        fun create(
            listener: HTContentListener?,
            x: Int,
            y: Int,
            limit: Long = HTItemSlot.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: BiPredicate<HTItemStorageStack, HTStorageAccess> = ALWAYS_TRUE,
            canInsert: BiPredicate<HTItemStorageStack, HTStorageAccess> = ALWAYS_TRUE,
            filter: Predicate<HTItemStorageStack> = HTStorageStack.alwaysTrue(),
        ): HTItemStackSlot = create(listener, x, y, limit, canExtract, canInsert, filter, HTContainerItemSlot.Type.BOTH)

        @JvmStatic
        private fun create(
            listener: HTContentListener?,
            x: Int,
            y: Int,
            limit: Long = HTItemSlot.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: BiPredicate<HTItemStorageStack, HTStorageAccess> = ALWAYS_TRUE,
            canInsert: BiPredicate<HTItemStorageStack, HTStorageAccess> = ALWAYS_TRUE,
            filter: Predicate<HTItemStorageStack> = HTStorageStack.alwaysTrue(),
            slotType: HTContainerItemSlot.Type,
        ): HTItemStackSlot = HTItemStackSlot(limit, canExtract, canInsert, filter, listener, x, y, slotType)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            x: Int,
            y: Int,
            limit: Long = HTItemSlot.ABSOLUTE_MAX_STACK_SIZE,
            canInsert: Predicate<HTItemStorageStack> = HTStorageStack.alwaysTrue(),
            filter: Predicate<HTItemStorageStack> = canInsert,
        ): HTItemStackSlot = create(
            listener,
            x,
            y,
            limit,
            { _, access: HTStorageAccess -> access != HTStorageAccess.EXTERNAL },
            { stack: HTItemStorageStack, _ -> canInsert.test(stack) },
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
            { _, access: HTStorageAccess -> access == HTStorageAccess.INTERNAL },
            slotType = HTContainerItemSlot.Type.OUTPUT,
        )
    }

    protected constructor(
        limit: Long,
        canExtract: Predicate<HTItemStorageStack>,
        canInsert: Predicate<HTItemStorageStack>,
        filter: Predicate<HTItemStorageStack>,
        listener: HTContentListener?,
        x: Int,
        y: Int,
        slotType: HTContainerItemSlot.Type,
    ) : this(
        limit,
        { stack: HTItemStorageStack, access: HTStorageAccess -> access == HTStorageAccess.MANUAL || canExtract.test(stack) },
        { stack: HTItemStorageStack, _: HTStorageAccess -> canInsert.test(stack) },
        filter,
        listener,
        x,
        y,
        slotType,
    )

    private var stack: ItemStack = ItemStack.EMPTY

    override fun getStack(): HTItemStorageStack = HTItemStorageStack.of(stack)

    override fun getCapacityAsLong(stack: HTItemStorageStack): Long =
        if (stack.isEmpty()) limit else min(limit, stack.maxStackSize().toLong())

    override fun isValid(stack: HTItemStorageStack): Boolean = filter.test(stack)

    override fun isStackValidForInsert(stack: HTItemStorageStack, access: HTStorageAccess): Boolean =
        super.isStackValidForInsert(stack, access) && canInsert.test(stack, access)

    override fun canStackExtract(stack: HTItemStorageStack, access: HTStorageAccess): Boolean =
        super.canStackExtract(stack, access) && canInsert.test(stack, access)

    override fun createContainerSlot(): Slot? = HTContainerItemSlot(this, x, y, ::setStackUnchecked, slotType)

    override fun serialize(output: HTValueOutput) {
        output.store(RagiumConst.ITEM, HTItemStorageStack.CODEC, getStack())
    }

    override fun deserialize(input: HTValueInput) {
        input.read(RagiumConst.ITEM, HTItemStorageStack.CODEC)?.let(::setStackUnchecked)
    }

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }

    override fun setStack(stack: HTItemStorageStack) {
        setStackUnchecked(stack, true)
    }

    fun setStackUnchecked(stack: ItemStack, validate: Boolean = false) {
        setStackUnchecked(HTItemStorageStack.of(stack), validate)
    }

    fun setStackUnchecked(stack: HTItemStorageStack, validate: Boolean = false) {
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
}
