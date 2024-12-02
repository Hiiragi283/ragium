package hiiragi283.ragium.common.block.transfer

import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.api.util.HTCrossDirection
import hiiragi283.ragium.api.util.HTPipeType
import hiiragi283.ragium.common.block.HTBlockWithEntity
import hiiragi283.ragium.common.init.RagiumBlockProperties
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.state.StateManager
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

class HTCrossPipeBlock(private val type: HTPipeType) : HTBlockWithEntity(blockSettings().solid().nonOpaque().strength(2f, 6f)) {
    companion object {
        @JvmField
        val SHAPE: VoxelShape = createCuboidShape(4.0, 4.0, 4.0, 12.0, 12.0, 12.0)
    }

    init {
        defaultState = stateManager.defaultState
            .with(RagiumBlockProperties.CROSS_DIRECTION, HTCrossDirection.UP_NORTH_EAST)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext,
    ): VoxelShape = SHAPE

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(RagiumBlockProperties.CROSS_DIRECTION)
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        val beforeDir: HTCrossDirection = state.get(RagiumBlockProperties.CROSS_DIRECTION)
        val afterDir: HTCrossDirection = when (beforeDir) {
            HTCrossDirection.DOWN_WEST_NORTH -> HTCrossDirection.UP_NORTH_EAST
            else -> HTCrossDirection.entries[beforeDir.ordinal + 1]
        }
        return state.with(RagiumBlockProperties.CROSS_DIRECTION, afterDir)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTCrossPipeBlockEntity(pos, state, type)
}
