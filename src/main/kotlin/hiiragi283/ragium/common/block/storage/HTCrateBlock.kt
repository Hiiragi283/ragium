package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.util.HTCrateVariant
import hiiragi283.ragium.common.tile.storage.HTCrateBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class HTCrateBlock(val variant: HTCrateVariant, properties: Properties) : HTEntityBlock.Horizontal(properties) {
    override fun initDefaultState(): BlockState = stateDefinition.any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)

    override fun newBlockEntity(pos: BlockPos, state: BlockState): HTCrateBlockEntity = HTCrateBlockEntity(pos, state, variant)
}
