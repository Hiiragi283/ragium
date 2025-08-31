package hiiragi283.ragium.common.storage.item

import com.google.common.base.Predicates
import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.data.BiCodecs
import hiiragi283.ragium.api.inventory.slot.HTContainerItemSlot
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.util.function.BiPredicate
import java.util.function.Predicate
import kotlin.math.min

/**
 * @see [mekanism.common.inventory.slot.BasicInventorySlot]
 */
open class HTItemStackSlot protected constructor(
    private val limit: Int,
    private val canExtract: BiPredicate<ItemStack, HTStorageAccess>,
    private val canInsert: BiPredicate<ItemStack, HTStorageAccess>,
    private val filter: Predicate<ItemStack>,
    private val listener: HTContentListener?,
    private val x: Int,
    private val y: Int,
) : HTItemSlot {
    companion object {
        @JvmField
        val ALWAYS_TRUE: BiPredicate<ItemStack, HTStorageAccess> =
            BiPredicate { _: ItemStack, _: HTStorageAccess -> true }

        @JvmField
        val SIMPLE_CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemSlot> = BiCodec
            .composite(
                BiCodecs.itemStack(true).fieldOf("stack"),
                HTItemStackSlot::stack,
                BiCodec.INT.fieldOf("x"),
                HTItemStackSlot::x,
                BiCodec.INT.fieldOf("y"),
                HTItemStackSlot::y,
            ) { stack: ItemStack, x: Int, y: Int ->
                val slot: HTItemStackSlot = at(null, x, y)
                slot.setStack(stack)
                slot
            }.let(BiCodec.Companion::downCast)

        @JvmStatic
        fun atManualOut(
            listener: HTContentListener?,
            x: Int,
            y: Int,
            limit: Int = Item.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: Predicate<ItemStack> = Predicates.alwaysTrue(),
            canInsert: Predicate<ItemStack> = Predicates.alwaysTrue(),
            filter: Predicate<ItemStack> = Predicates.alwaysTrue(),
        ): HTItemStackSlot = HTItemStackSlot(limit, canExtract, canInsert, filter, listener, x, y)

        @JvmStatic
        fun at(
            listener: HTContentListener?,
            x: Int,
            y: Int,
            limit: Int = Item.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: BiPredicate<ItemStack, HTStorageAccess> = ALWAYS_TRUE,
            canInsert: BiPredicate<ItemStack, HTStorageAccess> = ALWAYS_TRUE,
            filter: Predicate<ItemStack> = Predicates.alwaysTrue(),
        ): HTItemStackSlot = HTItemStackSlot(limit, canExtract, canInsert, filter, listener, x, y)
    }

    protected constructor(
        limit: Int,
        canExtract: Predicate<ItemStack>,
        canInsert: Predicate<ItemStack>,
        filter: Predicate<ItemStack>,
        listener: HTContentListener?,
        x: Int,
        y: Int,
    ) : this(
        limit,
        { stack: ItemStack, access: HTStorageAccess -> access == HTStorageAccess.MANUAL && canExtract.test(stack) },
        { stack: ItemStack, _: HTStorageAccess -> canInsert.test(stack) },
        filter,
        listener,
        x,
        y,
    )

    private var stack: ItemStack = ItemStack.EMPTY

    override fun getStack(): ItemStack = stack

    override fun setStack(stack: ItemStack) {
        setStackUnchecked(stack, true)
    }

    fun setStackUnchecked(stack: ItemStack, validate: Boolean = false) {
        if (stack.isEmpty) {
            if (getStack().isEmpty) return
            this.stack = ItemStack.EMPTY
        } else if (!validate || isItemValid(stack)) {
            this.stack = stack.copy()
        } else {
            error("Invalid stack for slot: $stack ${stack.componentsPatch}")
        }
        onContentsChanged()
    }

    override fun getLimit(stack: ItemStack): Int = if (stack.isEmpty) limit else min(limit, stack.maxStackSize)

    override fun isItemValid(stack: ItemStack): Boolean = filter.test(stack)

    override fun isItemValidForInsert(stack: ItemStack, access: HTStorageAccess): Boolean =
        super.isItemValidForInsert(stack, access) && canInsert.test(stack, access)

    override fun canItemExtract(stack: ItemStack, access: HTStorageAccess): Boolean = canExtract.test(stack, access)

    override fun createContainerSlot(): Slot? = HTContainerItemSlot(this, x, y, ::setStackUnchecked)

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        setStackUnchecked(ItemStack.parseOptional(provider, nbt.getCompound(RagiumConst.ITEM)), false)
    }

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }
}
