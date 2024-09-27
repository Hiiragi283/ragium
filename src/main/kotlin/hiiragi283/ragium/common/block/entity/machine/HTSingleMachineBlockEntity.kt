package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class HTSingleMachineBlockEntity : HTMachineBlockEntityBase {
    @Deprecated("")
    constructor(pos: BlockPos, state: BlockState) :
        super(RagiumBlockEntityTypes.SINGLE_MACHINE, pos, state)

    constructor(pos: BlockPos, state: BlockState, machineType: HTMachineType<*>, tier: HTMachineTier) :
        super(RagiumBlockEntityTypes.SINGLE_MACHINE, pos, state, machineType, tier)
}
