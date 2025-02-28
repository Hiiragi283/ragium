package hiiragi283.ragium.api.event

import hiiragi283.ragium.api.machine.HTMachineAccess
import hiiragi283.ragium.api.storage.fluid.HTFluidVariant
import net.neoforged.bus.api.ICancellableEvent

/**
 * 液体系発電機における燃料の液体量を変えるイベント
 */
class HTGeneratorFuelTimeEvent(machine: HTMachineAccess, val variant: HTFluidVariant, var fuelTime: Int) :
    HTMachineEvent(machine),
    ICancellableEvent
