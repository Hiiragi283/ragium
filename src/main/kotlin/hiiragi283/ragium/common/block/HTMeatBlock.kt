package hiiragi283.ragium.common.block

import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class HTMeatBlock(properties: Properties) : HTFeastBlock(properties, false) {
    override fun getServingsProperty(): IntegerProperty = HTBlockStateProperties.MEAT_SERVINGS

    override fun getMaxServings(): Int = 8

    override fun getServingItem(state: BlockState): ItemStack = RagiumItems.COOKED_MEAT_INGOT.toStack()

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = Shapes.block()
}
