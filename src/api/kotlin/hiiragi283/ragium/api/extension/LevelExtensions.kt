package hiiragi283.ragium.api.extension

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

//    Position    //

fun Vec3.getRangedAABB(radius: Number): AABB = AABB.ofSize(this, radius.toDouble(), radius.toDouble(), radius.toDouble())

//    Level    //

inline fun <reified BE : BlockEntity> BlockGetter.getTypedBlockEntity(pos: BlockPos): BE? = this.getBlockEntity(pos) as? BE
