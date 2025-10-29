package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.experience.IExperienceStorage
import hiiragi283.ragium.api.storage.experience.IExperienceStorageItem
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import net.neoforged.neoforge.items.IItemHandler

/**
 * @see mekanism.common.capabilities.Capabilities
 */
interface RagiumCapabilities {
    companion object {
        @JvmField
        val INSTANCE: RagiumCapabilities = RagiumAPI.getService()

        @JvmField
        val ENERGY: HTMultiCapability<IEnergyStorage, IEnergyStorage> = INSTANCE.energy

        @JvmField
        val EXPERIENCE: HTMultiCapability<IExperienceStorage, IExperienceStorageItem> = INSTANCE.experience

        @JvmField
        val FLUID: HTViewCapability<IFluidHandler, IFluidHandlerItem, ImmutableFluidStack> = INSTANCE.fluid

        @JvmField
        val ITEM: HTViewCapability<IItemHandler, IItemHandler, ImmutableItemStack> = INSTANCE.item
    }

    val energy: HTMultiCapability<IEnergyStorage, IEnergyStorage>
    val experience: HTMultiCapability<IExperienceStorage, IExperienceStorageItem>
    val fluid: HTViewCapability<IFluidHandler, IFluidHandlerItem, ImmutableFluidStack>
    val item: HTViewCapability<IItemHandler, IItemHandler, ImmutableItemStack>
}
