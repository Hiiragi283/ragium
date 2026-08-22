package hiiragi283.lib.transfer.item

import hiiragi283.lib.HTConstants
import hiiragi283.lib.transfer.HTStackResourceSlot
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.HTTransferPredicates
import hiiragi283.lib.transfer.HTTransferValidators
import java.util.function.BiPredicate
import java.util.function.Predicate
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.item.ItemResource

open class HTBasicItemSlot(
    capacity: Int,
    canExtract: BiPredicate<ItemResource, HTTransferAccess>,
    canInsert: BiPredicate<ItemResource, HTTransferAccess>,
    filter: Predicate<ItemResource>,
    listener: Runnable?,
) : HTStackResourceSlot.Basic<ItemStack, ItemResource>(capacity, canExtract, canInsert, filter, listener) {
    companion object {
        @JvmStatic
        fun create(
            listener: Runnable?,
            limit: Int = Item.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: BiPredicate<ItemResource, HTTransferAccess> = HTTransferPredicates.alwaysTrueBi(),
            canInsert: BiPredicate<ItemResource, HTTransferAccess> = HTTransferPredicates.alwaysTrueBi(),
            filter: Predicate<ItemResource> = HTTransferPredicates.alwaysTrue(),
        ): HTBasicItemSlot = HTBasicItemSlot(HTTransferValidators.validateLimit(limit), canExtract, canInsert, filter, listener)

        @JvmStatic
        fun input(
            listener: Runnable?,
            limit: Int = Item.ABSOLUTE_MAX_STACK_SIZE,
            canInsert: Predicate<ItemResource> = HTTransferPredicates.alwaysTrue(),
            filter: Predicate<ItemResource> = canInsert,
        ): HTBasicItemSlot = create(
            listener,
            limit,
            HTTransferPredicates.notExternal(),
            { stack: ItemResource, _ -> canInsert.test(stack) },
            filter,
        )

        @JvmStatic
        fun output(listener: Runnable?): HTBasicItemSlot = create(
            listener,
            canInsert = HTTransferPredicates.internalOnly(),
        )
    }

    private var stackIn: ItemStack = ItemStack.EMPTY

    override var stack: ItemStack
        get() = stackIn.copy()
        set(value) {
            setStackUnchecked(value, true)
        }

    override fun setStackInternal(stack: ItemStack) {
        setStackUnchecked(stack, false)
    }

    private fun setStackUnchecked(other: ItemStack, validate: Boolean) {
        val resource: ItemResource = getResourceFrom(other)
        if (resource.isEmpty) {
            if (this.stack.isEmpty) return
            this.stackIn = ItemStack.EMPTY
        } else if (!validate || isValid(resource)) {
            this.stackIn = other
        } else {
            error("Invalid stack for slot: $other")
        }
        onRootCommit(this.stack)
    }

    final override fun getResourceFrom(stack: ItemStack): ItemResource = ItemResource.of(stack)

    final override fun getAmountFrom(stack: ItemStack): Int = stack.count()

    final override fun isSame(stack: ItemStack, resource: ItemResource): Boolean = resource.matches(stack)

    final override fun createStack(resource: ItemResource, amount: Int): ItemStack = resource.toStack(amount)

    final override fun copyStack(stack: ItemStack): ItemStack = stack.copy()

    override fun getCapacity(resource: ItemResource): Int = when (resource.isEmpty) {
        true -> capacity
        false -> minOf(resource.maxStackSize, capacity)
    }

    //    ValueIOSerializable    //

    override fun serialize(output: ValueOutput) {
        output.store(HTConstants.ITEM, ItemStack.OPTIONAL_CODEC, stack)
    }

    override fun deserialize(input: ValueInput) {
        input.read(HTConstants.ITEM, ItemStack.OPTIONAL_CODEC).ifPresent(::stack::set)
    }
}
