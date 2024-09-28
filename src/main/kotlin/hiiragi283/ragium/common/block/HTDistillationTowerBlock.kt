package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.block.entity.machine.HTDistillationTowerBlockEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.machine.HTMachineTier
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class HTDistillationTowerBlock(tier: HTMachineTier) : HTMachineBlockBase(RagiumMachineTypes.DISTILLATION_TOWER, tier) {
    init {
        RagiumBlockEntityTypes.DISTILLATION_TOWER.addSupportedBlock(this)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTDistillationTowerBlockEntity(pos, state, tier)
}
