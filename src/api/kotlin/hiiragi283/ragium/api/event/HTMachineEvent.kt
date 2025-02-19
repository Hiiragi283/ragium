package hiiragi283.ragium.api.event

import hiiragi283.ragium.api.machine.HTMachineAccess
import net.neoforged.bus.api.Event

/**
 * [HTMachineAccess]に関連したイベントのクラス
 */
abstract class HTMachineEvent(val machine: HTMachineAccess) : Event()
