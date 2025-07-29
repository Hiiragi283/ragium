package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.block.entity.HTBlockEntityExtension
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.properties.DirectionProperty

/**
 * 水平方向の回転を行える[HTEntityBlock]
 */
class HTHorizontalEntityBlock<BE>(type: HTDeferredBlockEntityType<BE>, properties: Properties) :
    HTDirectionalEntityBlock<BE>(type, properties) where BE : BlockEntity, BE : HTBlockEntityExtension {
    companion object {
        @JvmStatic
        fun <BE> create(
            type: HTDeferredBlockEntityType<BE>,
        ): (Properties) -> HTHorizontalEntityBlock<*> where BE : BlockEntity, BE : HTBlockEntityExtension =
            { prop: Properties -> HTHorizontalEntityBlock(type, prop) }
    }

    override val directionProperty: DirectionProperty get() = HTBlockStateProperties.HORIZONTAL

    override fun placedFront(context: BlockPlaceContext): Direction = context.horizontalDirection.opposite
}
