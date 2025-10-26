package hiiragi283.ragium.api.block.attribute

import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty

/**
 * @see mekanism.common.block.attribute.AttributeState
 */
enum class HTDirectionalBlockAttribute(val property: DirectionProperty) : HTBlockAttribute {
    HORIZONTAL(BlockStateProperties.HORIZONTAL_FACING),
    FACING(BlockStateProperties.FACING),
    ;

    fun buildBlockState(block: Block, builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(property)
    }

    fun getStateForPlacement(defaultState: BlockState, context: BlockPlaceContext): BlockState {
        var state: BlockState = defaultState
        val direction: Direction = when (this) {
            HORIZONTAL -> context.horizontalDirection
            FACING -> context.nearestLookingDirection
        }.opposite
        state = setDirection(state, direction)
        return state
    }

    fun getDirection(state: BlockState): Direction = state.getValue(property)

    fun setDirection(state: BlockState, direction: Direction): BlockState = state.setValue(property, direction)
}
