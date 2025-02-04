package hiiragi283.ragium.api.event

import hiiragi283.ragium.api.machine.HTMachineAccess
import net.neoforged.bus.api.Event

sealed class HTMachineProcessEvent(val machine: HTMachineAccess) : Event() {
    class Success(machine: HTMachineAccess) : HTMachineProcessEvent(machine)

    class Failed(machine: HTMachineAccess) : HTMachineProcessEvent(machine)
}
