package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.block.entity.machine.HTBlastFurnaceBlockEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.machine.HTMachineTier
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class HTBlastFurnaceBlock(tier: HTMachineTier) : HTMachineBlockBase(RagiumMachineTypes.BLAST_FURNACE, tier) {
    init {
        RagiumBlockEntityTypes.BLAST_FURNACE.addSupportedBlock(this)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTBlastFurnaceBlockEntity(pos, state, tier)
}
