package hiiragi283.ragium.api.block

import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty

object HTBlockStateProperties {
    @JvmField
    val HOPPER_FACING: DirectionProperty = BlockStateProperties.FACING_HOPPER

    @JvmField
    val HORIZONTAL: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING

    @JvmField
    val IS_ACTIVE: BooleanProperty = BooleanProperty.create("active")
}
