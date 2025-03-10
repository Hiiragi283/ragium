package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.HTMachineBlock
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.tile.processor.HTAssemblerBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class HTAssemblerBlock(properties: Properties) : HTMachineBlock(HTMachineType.ASSEMBLER, properties) {
    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? = HTAssemblerBlockEntity(pos, state)
}
