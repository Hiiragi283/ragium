package hiiragi283.lib.transfer.item

import hiiragi283.lib.HTConstants
import hiiragi283.lib.transfer.HTSnapshotStackResourceSlot
import hiiragi283.lib.transfer.HTStackResourceHelper
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.HTTransferPredicates
import hiiragi283.lib.transfer.HTTransferValidators
import hiiragi283.lib.transfer.access.HTItemSlotItemAccess
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.item.ItemResource
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * [アイテム][ItemResource]向けの[HTSnapshotStackResourceSlot]の実装クラスです。
 *
 * 参照 : [Mekanism - BasicInventorySlot](https://github.com/mekanism/Mekanism/blob/26.3/src/main/java/mekanism/common/inventory/slot/BasicInventorySlot.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
open class HTBasicItemSlot(
    capacity: Int,
    canExtract: BiPredicate<ItemResource, HTTransferAccess>,
    canInsert: BiPredicate<ItemResource, HTTransferAccess>,
    filter: Predicate<ItemResource>,
    listener: Runnable?,
) : HTSnapshotStackResourceSlot.Basic<ItemStack, ItemResource>(capacity, canExtract, canInsert, filter, listener),
    HTItemStackSlot {
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
        fun output(listener: Runnable?): HTBasicItemSlot = create(listener, canInsert = HTTransferPredicates.internalOnly())

        /**
         * @since 26.1.12
         */
        @JvmStatic
        fun catalyst(listener: Runnable?, filter: Predicate<ItemResource> = HTTransferPredicates.alwaysTrue()): HTBasicItemSlot =
            create(listener, 1, HTTransferPredicates.manualOnly(), HTTransferPredicates.manualOnly(), filter)
    }

    private var stackIn: ItemStack = ItemStack.EMPTY

    override fun getStackCopy(): ItemStack = stackIn.copy()

    override fun setStack(stack: ItemStack) {
        setStackUnchecked(stack, true)
    }

    override fun setStackInternal(stack: ItemStack) {
        setStackUnchecked(stack, false)
    }

    private fun setStackUnchecked(other: ItemStack, validate: Boolean) {
        val resource: ItemResource = getHelper().getResourceFrom(other)
        if (resource.isEmpty) {
            if (this.stackIn.isEmpty) return
            this.stackIn = ItemStack.EMPTY
        } else if (!validate || isValid(resource)) {
            this.stackIn = other
        } else {
            error("Invalid stack for slot: $other")
        }
        onRootCommit(getStackCopy())
    }

    override fun getHelper(): HTStackResourceHelper.Item = HTStackResourceHelper.Item

    override fun getCapacity(resource: ItemResource): Int = when (resource.isEmpty) {
        true -> capacity
        false -> minOf(resource.maxStackSize, capacity)
    }

    /**
     * @since 26.1.9
     */
    val itemAccess = HTItemSlotItemAccess(this, HTTransferAccess.INTERNAL)

    //    ValueIOSerializable    //

    override fun serialize(output: ValueOutput) {
        output.store(HTConstants.ITEM, ItemStack.OPTIONAL_CODEC, getStackCopy())
    }

    override fun deserialize(input: ValueInput) {
        input.read(HTConstants.ITEM, ItemStack.OPTIONAL_CODEC).ifPresent(::setStackInternal)
    }
}
