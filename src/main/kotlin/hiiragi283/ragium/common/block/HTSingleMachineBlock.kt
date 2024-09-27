package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.block.entity.machine.HTSingleMachineBlockEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class HTSingleMachineBlock(machineType: HTMachineType<*>, tier: HTMachineTier) : HTBaseMachineBlock(machineType, tier) {
    init {
        RagiumBlockEntityTypes.SINGLE_MACHINE.addSupportedBlock(this)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
        HTSingleMachineBlockEntity(pos, state, machineType, tier)
}
