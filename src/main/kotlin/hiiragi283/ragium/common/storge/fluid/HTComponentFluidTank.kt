package hiiragi283.ragium.common.storge.fluid

import hiiragi283.core.api.stack.ImmutableFluidStack
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.attachments.HTAttachedFluids
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storge.attachment.HTComponentHandler
import hiiragi283.ragium.common.storge.attachment.HTComponentSlot
import net.minecraft.world.item.ItemStack
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * @see mekanism.common.attachments.containers.fluid.ComponentBackedFluidTank
 */
class HTComponentFluidTank(
    attachedTo: ItemStack,
    size: Int,
    slot: Int,
    capacity: Int,
    canExtract: BiPredicate<ImmutableFluidStack, HTStorageAccess>,
    canInsert: BiPredicate<ImmutableFluidStack, HTStorageAccess>,
    filter: Predicate<ImmutableFluidStack>,
) : HTComponentSlot<ImmutableFluidStack, HTAttachedFluids>(attachedTo, size, slot, capacity, canExtract, canInsert, filter),
    HTFluidTank {
    companion object {
        @JvmStatic
        fun create(
            context: HTComponentHandler.ContainerContext,
            capacity: Int,
            canExtract: BiPredicate<ImmutableFluidStack, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            canInsert: BiPredicate<ImmutableFluidStack, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            filter: Predicate<ImmutableFluidStack> = HTStoragePredicates.alwaysTrue(),
        ): HTComponentFluidTank =
            HTComponentFluidTank(context.attachedTo, context.size, context.index, capacity, canExtract, canInsert, filter)
    }

    override fun capabilityCodec(): HTCapabilityCodec<*, HTAttachedFluids> = HTCapabilityCodec.FLUID
}
