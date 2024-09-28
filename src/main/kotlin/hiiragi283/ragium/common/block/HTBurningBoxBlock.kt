package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.block.entity.generator.HTBurningBoxBlockEntity
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.machine.HTMachineTier
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

object HTBurningBoxBlock : HTMachineBlockBase(RagiumMachineTypes.BURNING_BOX, HTMachineTier.PRIMITIVE) {
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? = HTBurningBoxBlockEntity(pos, state)
}
