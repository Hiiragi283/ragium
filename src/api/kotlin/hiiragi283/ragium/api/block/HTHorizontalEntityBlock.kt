package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.state.properties.DirectionProperty

/**
 * 水平方向の回転を行える[HTEntityBlock]
 */
class HTHorizontalEntityBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) : HTDirectionalEntityBlock(type, properties) {
    override val directionProperty: DirectionProperty get() = HTBlockStateProperties.HORIZONTAL

    override fun placedFront(context: BlockPlaceContext): Direction = context.horizontalDirection.opposite
}
