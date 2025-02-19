package hiiragi283.ragium.api.event

import hiiragi283.ragium.api.machine.HTMachineAccess
import net.neoforged.bus.api.ICancellableEvent
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 液体系発電機における燃料の液体量を変えるイベント
 */
class HTGeneratorFuelTimeEvent(machine: HTMachineAccess, val stack: FluidStack, var fuelTime: Int) :
    HTMachineEvent(machine),
    ICancellableEvent
