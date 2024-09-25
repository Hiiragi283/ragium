package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.machine.HTMachineType

interface HTTypedMachineBlockEntity<T : HTMachineType> {
    val machineType: T
}
