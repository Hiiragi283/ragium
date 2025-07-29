package hiiragi283.ragium.api.block

import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty

object HTBlockStateProperties {
    @JvmField
    val FACING: DirectionProperty = BlockStateProperties.FACING

    @JvmField
    val HORIZONTAL: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
}
