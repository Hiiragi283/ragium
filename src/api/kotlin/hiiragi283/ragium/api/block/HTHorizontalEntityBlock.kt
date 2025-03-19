package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
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
 * 水平方向の回転を行える[HTEntityBlock]
 */
abstract class HTHorizontalEntityBlock<BE : HTMachineBlockEntity>(type: HTDeferredBlockEntityType<BE>, properties: Properties) :
    HTEntityBlock<BE>(type, properties) {
    companion object {
        @JvmField
        val HORIZONTAL: DirectionProperty = HTBlockStateProperties.HORIZONTAL
    }

    fun getFront(state: BlockState): Direction = state.getValue(HORIZONTAL)

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(HORIZONTAL)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? = defaultBlockState()
        .setValue(HORIZONTAL, context.horizontalDirection.opposite)

    override fun rotate(
        state: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        direction: Rotation,
    ): BlockState = state.setValue(HORIZONTAL, direction.rotate(state.getValue(HORIZONTAL)))

    override fun mirror(state: BlockState, mirror: Mirror): BlockState =
        state.setValue(HORIZONTAL, mirror.mirror(state.getValue(HORIZONTAL)))
}
