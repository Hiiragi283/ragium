package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.function.andThen
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.attachments.HTAttachedItems
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storage.attachments.HTComponentHandler
import hiiragi283.ragium.common.storage.attachments.HTComponentSlot
import net.minecraft.world.item.ItemStack
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * @see mekanism.common.attachments.containers.item.ComponentBackedInventorySlot
 */
class HTComponentItemSlot(
    attachedTo: ItemStack,
    size: Int,
    slot: Int,
    capacity: Int,
    canExtract: BiPredicate<ImmutableItemStack, HTStorageAccess>,
    canInsert: BiPredicate<ImmutableItemStack, HTStorageAccess>,
    filter: Predicate<ImmutableItemStack>,
) : HTComponentSlot<ImmutableItemStack, HTAttachedItems>(attachedTo, size, slot, capacity, canExtract, canInsert, filter),
    HTItemSlot {
    companion object {
        @JvmStatic
        fun create(
            context: HTComponentHandler.ContainerContext,
            capacity: Int,
            canExtract: BiPredicate<ImmutableItemStack, HTStorageAccess> = HTPredicates.alwaysTrueBi(),
            canInsert: BiPredicate<ImmutableItemStack, HTStorageAccess> = HTPredicates.alwaysTrueBi(),
            filter: Predicate<ImmutableItemStack> = Predicate(ImmutableItemStack::unwrap.andThen(ItemStack::canFitInsideContainerItems)),
        ): HTComponentItemSlot =
            HTComponentItemSlot(context.attachedTo, context.size, context.index, capacity, canExtract, canInsert, filter)
    }

    override fun capabilityCodec(): HTCapabilityCodec<*, HTAttachedItems> = HTCapabilityCodec.ITEM
}
