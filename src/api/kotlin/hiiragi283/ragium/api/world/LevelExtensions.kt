package hiiragi283.ragium.api.world

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

//    Position    //

fun Vec3.getRangedAABB(radius: Number): AABB = AABB(this, this).inflate(radius.toDouble())

//    Level    //

inline fun <reified BE : BlockEntity> BlockGetter.getTypedBlockEntity(pos: BlockPos): BE? = this.getBlockEntity(pos) as? BE

fun Level.sendBlockUpdated(pos: BlockPos) {
    val state: BlockState = this.getBlockState(pos)
    this.sendBlockUpdated(pos, state, state, 3)
}
