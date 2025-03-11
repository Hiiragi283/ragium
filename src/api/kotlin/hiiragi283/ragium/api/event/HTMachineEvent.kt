package hiiragi283.ragium.api.event

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import net.neoforged.bus.api.Event

/**
 * [HTMachineBlockEntity]に関連したイベントのクラス
 */
abstract class HTMachineEvent(val machine: HTMachineBlockEntity) : Event()
