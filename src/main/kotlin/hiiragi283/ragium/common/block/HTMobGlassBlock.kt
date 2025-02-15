package hiiragi283.ragium.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Mob
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.TransparentBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.EntityCollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class HTMobGlassBlock(properties: Properties) : TransparentBlock(properties) {
    override fun getCollisionShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape {
        if (context is EntityCollisionContext) {
            val entity: Entity = context.entity ?: return super.getCollisionShape(state, level, pos, context)
            if (entity is Mob) {
                return Shapes.empty()
            }
        }
        return super.getCollisionShape(state, level, pos, context)
    }
}
