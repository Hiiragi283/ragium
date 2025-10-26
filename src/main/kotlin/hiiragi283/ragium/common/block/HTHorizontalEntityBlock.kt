package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty

/**
 * 水平方向の回転を行える[HTEntityBlock]
 */
open class HTHorizontalEntityBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) :
    HTDirectionalEntityBlock(type, properties) {
    final override val directionProperty: DirectionProperty get() = BlockStateProperties.HORIZONTAL_FACING

    final override fun placedFront(context: BlockPlaceContext): Direction = context.horizontalDirection.opposite
}
