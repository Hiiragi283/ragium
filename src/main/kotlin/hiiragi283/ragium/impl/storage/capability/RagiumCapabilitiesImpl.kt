package hiiragi283.ragium.impl.storage.capability

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStackView
import hiiragi283.ragium.api.storage.capability.HTMultiCapability
import hiiragi283.ragium.api.storage.capability.HTViewCapability
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.storage.capability.slotRange
import hiiragi283.ragium.api.storage.capability.tankRange
import hiiragi283.ragium.api.storage.experience.IExperienceStorage
import hiiragi283.ragium.api.storage.experience.IExperienceStorageItem
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.item.HTItemHandler
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import net.neoforged.neoforge.items.IItemHandler

/**
 * @see mekanism.common.capabilities.Capabilities
 */
class RagiumCapabilitiesImpl : RagiumCapabilities {
    override val energy: HTMultiCapability<IEnergyStorage, IEnergyStorage> = HTMultiCapabilityBase(
        Capabilities.EnergyStorage.BLOCK,
        Capabilities.EnergyStorage.ENTITY,
        Capabilities.EnergyStorage.ITEM,
    )

    override val experience: HTMultiCapability<IExperienceStorage, IExperienceStorageItem> = HTMultiCapabilityBase.create(
        RagiumAPI.id("experience"),
    )

    override val fluid: HTViewCapability<IFluidHandler, IFluidHandlerItem, ImmutableFluidStack> = HTViewCapabilityBase(
        Capabilities.FluidHandler.BLOCK,
        Capabilities.FluidHandler.ENTITY,
        Capabilities.FluidHandler.ITEM,
    ) { handler: IFluidHandler, side: Direction? ->
        if (handler is HTFluidHandler) {
            handler.getFluidTanks(side)
        } else {
            handler.tankRange.map { tank: Int ->
                object : HTStackView<ImmutableFluidStack> {
                    override fun getStack(): ImmutableFluidStack? = handler.getFluidInTank(tank).toImmutable()

                    override fun getCapacity(stack: ImmutableFluidStack?): Int = handler.getTankCapacity(tank)
                }
            }
        }
    }

    override val item: HTViewCapability<IItemHandler, IItemHandler, ImmutableItemStack> = ItemCapability

    private data object ItemCapability : HTViewCapabilityBase<IItemHandler, IItemHandler, ImmutableItemStack>(
        Capabilities.ItemHandler.BLOCK,
        Capabilities.ItemHandler.ENTITY_AUTOMATION,
        Capabilities.ItemHandler.ITEM,
        { handler: IItemHandler, side: Direction? ->
            if (handler is HTItemHandler) {
                handler.getItemSlots(side)
            } else {
                handler.slotRange.map { slot: Int ->
                    object : HTStackView<ImmutableItemStack> {
                        override fun getStack(): ImmutableItemStack? = handler.getStackInSlot(slot).toImmutable()

                        override fun getCapacity(stack: ImmutableItemStack?): Int = handler.getSlotLimit(slot)
                    }
                }
            }
        },
    ) {
        override fun getCapability(entity: Entity, side: Direction?): IItemHandler? {
            if (side == null) {
                val handler: IItemHandler? = entity.getCapability(Capabilities.ItemHandler.ENTITY)
                if (handler != null) return handler
            }
            return super.getCapability(entity, side)
        }
    }
}
