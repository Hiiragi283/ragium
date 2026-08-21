package hiiragi283.lib.world

import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.flatMap
import hiiragi283.lib.util.toTextResult
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

//    Position    //

/**
 * この[座標][this]を中心とし，[radius]を半径とする[三次元の範囲][AABB]を返します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun Vec3.getRangedAABB(radius: Number): AABB = AABB(this, this).inflate(radius.toDouble())

//    Level    //

/**
 * [BlockEntity]を取得します。
 * @param BE [BlockEntity]のクラス
 * @param pos [BlockEntity]を取得したい座標
 * @return 指定した[座標][pos]に[BlockEntity]がない場合，または取得した[BlockEntity]が[BE]にキャストできない場合は`null`
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <reified BE : BlockEntity> BlockGetter.getTypedBlockEntity(pos: BlockPos): BE? = this.getBlockEntity(pos) as? BE

/**
 * [BlockEntity]を取得します。
 * @param BE [BlockEntity]のクラス
 * @param pos [BlockEntity]を取得したい座標
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <reified BE : BlockEntity> BlockGetter.getBlockEntityResult(pos: BlockPos): HTTextResult<BE> = this.getBlockEntity(pos)
    .toTextResult { "Could not find block entity at $pos" }
    .flatMap { (it as? BE).toTextResult { "Failed to cast $it as ${BE::class.qualifiedName}" } }

/**
 * 指定した[レベル][this]と[座標][pos]にブロック更新を発生させます。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun Level.sendBlockUpdated(pos: BlockPos) {
    val state: BlockState = this.getBlockState(pos)
    this.sendBlockUpdated(pos, state, state, 3)
}
