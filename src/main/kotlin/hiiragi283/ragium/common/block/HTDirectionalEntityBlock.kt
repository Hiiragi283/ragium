package hiiragi283.ragium.common.block

import hiiragi283.lib.registry.HTDeferredBlockEntityType
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.EnumProperty

open class HTDirectionalEntityBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) : HTBasicEntityBlock(type, properties) {
    companion object {
        @JvmField
        val FACING: EnumProperty<Direction> = DirectionalBlock.FACING
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? =
        this.defaultBlockState().setValue(FACING, context.clickedFace.opposite)

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun rotate(state: BlockState, rotation: Rotation): BlockState = state.setValue(FACING, rotation.rotate(state.getValue(FACING)))

    @Suppress("DEPRECATION")
    override fun mirror(state: BlockState, mirror: Mirror): BlockState = state.rotate(mirror.getRotation(state.getValue(FACING)))
}
