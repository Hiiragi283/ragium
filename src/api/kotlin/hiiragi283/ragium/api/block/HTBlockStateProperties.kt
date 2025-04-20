package hiiragi283.ragium.api.block

import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty

object HTBlockStateProperties {
    @JvmField
    val HOPPER_FACING: DirectionProperty = BlockStateProperties.FACING_HOPPER

    @JvmField
    val HORIZONTAL: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING

    @JvmField
    val IS_ACTIVE: BooleanProperty = BooleanProperty.create("active")

    //    Delight    //

    @JvmField
    val MEAT_SERVINGS: IntegerProperty = IntegerProperty.create("servings", 0, 8)
}
