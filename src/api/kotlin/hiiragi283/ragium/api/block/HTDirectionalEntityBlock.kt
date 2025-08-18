package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.DirectionProperty

/**
 * @see [HTFacingEntityBlock]
 * @see [HTHorizontalEntityBlock]
 */
abstract class HTDirectionalEntityBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) : HTEntityBlock(type, properties) {
    protected abstract val directionProperty: DirectionProperty

    protected abstract fun placedFront(context: BlockPlaceContext): Direction

    fun getDirection(state: BlockState): Direction = state.getValue(directionProperty)

    final override fun initDefaultState(): BlockState = stateDefinition.any().setValue(directionProperty, Direction.NORTH)

    final override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(directionProperty)
    }

    final override fun getStateForPlacement(context: BlockPlaceContext): BlockState? = defaultBlockState()
        .setValue(directionProperty, placedFront(context))

    final override fun rotate(
        state: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        direction: Rotation,
    ): BlockState = state.setValue(directionProperty, direction.rotate(state.getValue(directionProperty)))

    final override fun mirror(state: BlockState, mirror: Mirror): BlockState =
        state.setValue(directionProperty, mirror.mirror(state.getValue(directionProperty)))
}
