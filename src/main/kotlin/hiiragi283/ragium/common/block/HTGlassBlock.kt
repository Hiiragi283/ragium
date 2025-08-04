package hiiragi283.ragium.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.TransparentBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.EntityCollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class HTGlassBlock(private val isTinted: Boolean, private val canPlayerThrough: Boolean, properties: Properties) :
    TransparentBlock(properties) {
    override fun getCollisionShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape {
        if (canPlayerThrough && context is EntityCollisionContext) {
            val entity: Entity = context.entity ?: return super.getCollisionShape(state, level, pos, context)
            if (entity is Player) {
                return Shapes.empty()
            }
        }
        return super.getCollisionShape(state, level, pos, context)
    }

    override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean = isTinted

    override fun getLightBlock(state: BlockState, level: BlockGetter, pos: BlockPos): Int =
        if (isTinted) level.maxLightLevel else super.getLightBlock(state, level, pos)
}
