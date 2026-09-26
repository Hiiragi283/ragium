package hiiragi283.ragium.common.block.storage

import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.bus.HTFluidBusBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class HTTankBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) : HTFluidBusBlock(type, properties) {
    companion object {
        @JvmField
        val SHAPE: VoxelShape = box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape =
        SHAPE
}
