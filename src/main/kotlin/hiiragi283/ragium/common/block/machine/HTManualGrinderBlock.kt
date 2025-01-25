package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.HTEntityBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class HTManualGrinderBlock(properties: Properties) : HTEntityBlock(properties) {
    init {
        registerDefaultState(
            stateDefinition
                .any()
                .setValue(BlockStateProperties.AGE_7, 0),
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.AGE_7)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTManualGrinderBlockEntity(pos, state)
}
