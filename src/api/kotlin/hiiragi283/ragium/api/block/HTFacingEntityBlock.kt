package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty

/**
 * 六方位を向くことができる[HTEntityBlock]
 */
open class HTFacingEntityBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) : HTDirectionalEntityBlock(type, properties) {
    final override val directionProperty: DirectionProperty get() = BlockStateProperties.FACING

    final override fun placedFront(context: BlockPlaceContext): Direction = context.nearestLookingDirection.opposite
}
