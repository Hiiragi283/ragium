package hiiragi283.ragium.api.block.attribute

import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.block.state.properties.Property

enum class HTDirectionalBlockAttribute(val property: DirectionProperty) : HTBlockAttribute {
    HORIZONTAL(BlockStateProperties.HORIZONTAL_FACING),
    FACING(BlockStateProperties.FACING),
    ;

    fun buildBlockState(block: Block, properties: MutableList<Property<*>>) {
        properties.add(property)
    }

    fun getStateForPlacement(block: Block, context: BlockPlaceContext): BlockState {
        val state: BlockState = block.defaultBlockState()
        val direction: Direction = when (this) {
            HORIZONTAL -> context.horizontalDirection
            FACING -> context.nearestLookingDirection
        }.opposite
        state.setValue(property, direction)
        return state
    }

    fun getDirection(state: BlockState): Direction = state.getValue(property)

    fun setDirection(state: BlockState, direction: Direction): BlockState = state.setValue(property, direction)
}
