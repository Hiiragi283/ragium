package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType

interface HTTieredMachine {
    val machineType: HTMachineType
    val tier: HTMachineTier
}
