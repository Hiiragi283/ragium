package hiiragi283.ragium.impl.storage.capability

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStackView
import hiiragi283.ragium.api.storage.capability.HTMultiCapability
import hiiragi283.ragium.api.storage.capability.HTViewCapability
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.storage.capability.slotRange
import hiiragi283.ragium.api.storage.capability.tankRange
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.item.HTItemHandler
import net.minecraft.core.Direction
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import net.neoforged.neoforge.items.IItemHandler

class RagiumCapabilitiesImpl : RagiumCapabilities {
    override val energy: HTMultiCapability<IEnergyStorage, IEnergyStorage> = HTMultiCapabilityBase(
        Capabilities.EnergyStorage.BLOCK,
        Capabilities.EnergyStorage.ITEM,
    )

    override val fluid: HTViewCapability<IFluidHandler, IFluidHandlerItem, ImmutableFluidStack> = HTViewCapabilityBase(
        Capabilities.FluidHandler.BLOCK,
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

    override val item: HTViewCapability<IItemHandler, IItemHandler, ImmutableItemStack> = HTViewCapabilityBase(
        Capabilities.ItemHandler.BLOCK,
        Capabilities.ItemHandler.ITEM,
    ) { handler: IItemHandler, side: Direction? ->
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
    }
}
