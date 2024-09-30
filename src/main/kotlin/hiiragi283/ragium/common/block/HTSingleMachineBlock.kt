package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.block.entity.machine.HTProcessorBlockEntityBase
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.machine.HTMachineConvertible
import hiiragi283.ragium.common.machine.HTMachineTier
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class HTSingleMachineBlock(convertible: HTMachineConvertible, tier: HTMachineTier) : HTMachineBlockBase(convertible, tier) {
    init {
        RagiumBlockEntityTypes.PROCESSOR_MACHINE.addSupportedBlock(this)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
        HTProcessorBlockEntityBase.Simple(pos, state, machineType, tier)
}
