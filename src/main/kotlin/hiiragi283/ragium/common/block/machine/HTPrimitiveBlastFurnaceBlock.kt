package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.common.block.HTEntityBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTPrimitiveBlastFurnaceBlock(properties: Properties) : HTEntityBlock.Horizontal(properties) {
    override fun newBlockEntity(pos: BlockPos, state: BlockState): HTPrimitiveBlastFurnaceBlockEntity =
        HTPrimitiveBlastFurnaceBlockEntity(pos, state)
}
