package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import net.minecraft.core.Direction
import net.neoforged.neoforge.capabilities.BlockCapabilityCache
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler

val IItemHandler.slotRange: IntRange get() = (0..<this.slots)

val IFluidHandler.tankRange: IntRange get() = (0..<this.tanks)

//    BlockCapabilityCache    //

fun BlockCapabilityCache<IEnergyStorage, Direction?>.getStorage(): HTEnergyBattery? {
    val storage: IEnergyStorage = this.capability ?: return null
    return HTEnergyCapabilities.wrapStorage(storage, this.context())
}
