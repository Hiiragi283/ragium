package hiiragi283.ragium.common.block

import com.mojang.serialization.MapCodec
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.tag.RagiumTags
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.LayeredCauldronBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.fluids.CauldronFluidContent

class HTTreeTapBlock(properties: Properties) : HorizontalDirectionalBlock(properties) {
    init {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH))
    }

    override fun codec(): MapCodec<HTTreeTapBlock> = throw UnsupportedOperationException()

    /**
     * @see LayeredCauldronBlock.receiveStalactiteDrip
     */
    override fun randomTick(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        random: RandomSource,
    ) {
        if (random.nextInt(5) == 0) {
            val posBelow: BlockPos = pos.below()
            val stateBelow: BlockState = level.getBlockState(posBelow)
            handleCauldron(stateBelow, level, posBelow)
        }
    }

    private fun handleCauldron(state: BlockState, level: ServerLevel, pos: BlockPos) {
        val newState: BlockState = if (state.`is`(Blocks.CAULDRON)) {
            val cauldron: CauldronFluidContent = CauldronFluidContent.getForFluid(HCFluids.LATEX.get()) ?: return
            cauldron.block.defaultBlockState()
        } else {
            val cauldron: CauldronFluidContent = CauldronFluidContent.getForBlock(state.block) ?: return
            val newLevel: Int = cauldron.currentLevel(state) + 1
            if (newLevel <= cauldron.maxLevel) {
                val property: IntegerProperty = cauldron.levelProperty ?: return
                state.setValue(property, newLevel)
            } else {
                return
            }
        }
        level.setBlockAndUpdate(pos, newState)
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState))
        level.levelEvent(1047, pos, 0)
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean =
        level.getBlockState(pos.relative(state.getValue(FACING))).`is`(RagiumTags.Blocks.LATEX_DRIPPING_LOGS)

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = super.getShape(state, level, pos, context)

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        var state: BlockState = defaultBlockState()
        val level: Level = context.level
        val pos: BlockPos = context.clickedPos

        for (direction: Direction in context.nearestLookingDirections) {
            if (direction.axis.isHorizontal) {
                state = state.setValue(FACING, direction)
                if (state.canSurvive(level, pos)) {
                    return state
                }
            }
        }
        return null
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos,
    ): BlockState = when (direction) {
        state.getValue(FACING) if !state.canSurvive(level, pos) -> Blocks.AIR.defaultBlockState()
        else -> super.updateShape(state, direction, neighborState, level, pos, neighborPos)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun isPathfindable(state: BlockState, pathComputationType: PathComputationType): Boolean = false
}
