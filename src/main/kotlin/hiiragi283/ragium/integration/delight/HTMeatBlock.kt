package hiiragi283.ragium.integration.delight

import hiiragi283.ragium.api.block.HTBlockStateProperties
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import vectorwing.farmersdelight.common.block.FeastBlock

class HTMeatBlock(properties: Properties) : FeastBlock(properties, RagiumItems.COOKED_MEAT_INGOT::get, false) {
    override fun getServingsProperty(): IntegerProperty = HTBlockStateProperties.MEAT_SERVINGS

    override fun getMaxServings(): Int = 8

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = Shapes.block()

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        builder.add(FACING, HTBlockStateProperties.MEAT_SERVINGS)
    }
}
