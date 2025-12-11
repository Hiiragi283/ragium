package hiiragi283.ragium.common.storage.fluid.tank

import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.attachments.HTAttachedFluids
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storage.attachments.HTComponentHandler
import hiiragi283.ragium.common.storage.attachments.HTComponentSlot
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
            canExtract: BiPredicate<ImmutableFluidStack, HTStorageAccess> = HTPredicates.alwaysTrueBi(),
            canInsert: BiPredicate<ImmutableFluidStack, HTStorageAccess> = HTPredicates.alwaysTrueBi(),
            filter: Predicate<ImmutableFluidStack> = HTPredicates.alwaysTrue(),
        ): HTComponentFluidTank =
            HTComponentFluidTank(context.attachedTo, context.size, context.index, capacity, canExtract, canInsert, filter)
    }

    override fun capabilityCodec(): HTCapabilityCodec<*, HTAttachedFluids> = HTCapabilityCodec.FLUID
}
