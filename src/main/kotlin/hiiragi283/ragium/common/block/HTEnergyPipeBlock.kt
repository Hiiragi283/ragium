package hiiragi283.ragium.common.block

import hiiragi283.lib.capability.HTEnergyCapabilities
import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.transfer.energy.HTEnergyNetwork
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.ScheduledTickAccess
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.PipeBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class HTEnergyPipeBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) : HTBasicEntityBlock(type, properties) {
    private val shapes = getShapeForEachState { state: BlockState ->
        var shape: VoxelShape = CORE
        for ((side, property) in PipeBlock.PROPERTY_BY_DIRECTION) {
            if (state.getValue(property)) shape = Shapes.or(shape, ARMS.getValue(side))
        }
        shape
    }

    init {
        var state: BlockState = defaultBlockState()
        for (property in PipeBlock.PROPERTY_BY_DIRECTION.values) state = state.setValue(property, false)
        registerDefaultState(state)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        for (property in PipeBlock.PROPERTY_BY_DIRECTION.values) builder.add(property)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape = shapes.apply(state)

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
        val level: ServerLevel = context.level as? ServerLevel ?: return defaultBlockState()
        return connections(defaultBlockState(), level, context.clickedPos)
    }

    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
        if (level is ServerLevel && !oldState.`is`(this)) HTEnergyNetwork.onPipeChanged(level, pos)
    }

    override fun updateShape(
        state: BlockState,
        level: LevelReader,
        ticks: ScheduledTickAccess,
        pos: BlockPos,
        directionToNeighbour: Direction,
        neighbourPos: BlockPos,
        neighbourState: BlockState,
        random: RandomSource,
    ): BlockState {
        val serverLevel: ServerLevel = level as? ServerLevel ?: return state
        return state.setValue(
            PipeBlock.PROPERTY_BY_DIRECTION.getValue(directionToNeighbour),
            connects(serverLevel, neighbourPos, directionToNeighbour.opposite, neighbourState),
        )
    }

    override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val updated: BlockState = connections(state, level, pos)
        if (updated != state) level.setBlock(pos, updated, UPDATE_CLIENTS or UPDATE_KNOWN_SHAPE)
    }

    private fun connections(state: BlockState, level: ServerLevel, pos: BlockPos): BlockState {
        var updated: BlockState = state
        for ((side, property) in PipeBlock.PROPERTY_BY_DIRECTION) {
            val neighbor: BlockPos = pos.relative(side)
            val chunk: ChunkPos = ChunkPos.containing(neighbor)
            val neighborState: BlockState = level.chunkSource.getChunkNow(chunk.x, chunk.z)?.getBlockState(neighbor) ?: continue
            updated = updated.setValue(property, connects(level, neighbor, side.opposite, neighborState))
        }
        return updated
    }

    private fun connects(level: ServerLevel, pos: BlockPos, side: Direction, state: BlockState): Boolean =
        state.block is HTEnergyPipeBlock || (level.isLoaded(pos) && HTEnergyCapabilities.getCapability(level, pos, side) != null)

    companion object {
        private val CORE: VoxelShape = cube(4.0)
        private val ARMS: Map<Direction, VoxelShape> = Shapes.rotateAll(boxZ(4.0, 0.0, 8.0))
    }
}
