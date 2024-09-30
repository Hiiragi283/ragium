package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.block.entity.machine.HTGeneratorBlockEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.machine.HTMachineConvertible
import hiiragi283.ragium.common.machine.HTMachineTier
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class HTGeneratorMachineBlock(convertible: HTMachineConvertible, tier: HTMachineTier) : HTMachineBlockBase(convertible, tier) {
    init {
        RagiumBlockEntityTypes.GENERATOR_MACHINE.addSupportedBlock(this)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTGeneratorBlockEntity(pos, state, tier)
}
