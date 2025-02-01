package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.HTEntityBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class HTRobotBlock(properties: Properties) : HTEntityBlock.Horizontal(properties) {
    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTRobotBlockEntity(pos, state)
}
