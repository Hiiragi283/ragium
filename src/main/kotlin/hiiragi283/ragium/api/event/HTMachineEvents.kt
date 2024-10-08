package hiiragi283.ragium.api.event

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntityBase
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory

object HTMachineEvents {
    @JvmField
    val UPDATE_PROPERTIES: Event<UpdateProperties> = EventFactory.createArrayBacked(
        UpdateProperties::class.java,
    ) { listeners: Array<UpdateProperties> ->
        UpdateProperties { machineType: HTMachineType, tier: HTMachineTier, blockEntity: HTMachineBlockEntityBase ->
            listeners.forEach { it.onUpdate(machineType, tier, blockEntity) }
        }
    }

    //    UpdateProperties    //

    fun interface UpdateProperties {
        fun onUpdate(machineType: HTMachineType, tier: HTMachineTier, blockEntity: HTMachineBlockEntityBase)
    }
}
