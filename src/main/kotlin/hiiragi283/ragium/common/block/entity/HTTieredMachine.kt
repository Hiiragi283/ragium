package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType

interface HTTieredMachine {
    val machineType: HTMachineType
    val tier: HTMachineTier
}
