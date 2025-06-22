package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.CommonLevelAccessor
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.Vec3

fun BlockPos.toCenterVec3(): Vec3 = Vec3(this.x.toDouble(), this.y.toDouble(), this.z.toDouble()).add(0.5, 0.0, 0.5)

//    Level    //

/**
 * 指定した[Level]に対応する[ServerLevel]を返します。
 * @return 指定した[Level]が`null`，または対応する[ServerLevel]がない場合は`null`
 */
fun Level?.convertToServer(): ServerLevel? {
    if (this is ServerLevel) return this
    val dimension: ResourceKey<Level> = this?.dimension() ?: return null
    return RagiumAPI.getInstance().getCurrentServer()?.getLevel(dimension)
}

fun CommonLevelAccessor.emptyBlock(pos: BlockPos) {
    removeBlock(pos, false)
    if (!getFluidState(pos).isEmpty) {
        setBlock(pos, Blocks.AIR.defaultBlockState(), 3)
    }
}

/**
 * 指定した[item]を[entity]の足元にドロップします。
 * @return [ItemEntity]がスポーンした場合は`true`，それ以外の場合は`false`
 */
fun dropStackAt(entity: Entity, item: ItemLike, count: Int = 1): Boolean = dropStackAt(entity, ItemStack(item, count))

/**
 * 指定した[stack]を[entity]の足元にドロップします。
 * @return [ItemEntity]がスポーンした場合は`true`，それ以外の場合は`false`
 */
fun dropStackAt(entity: Entity, stack: ItemStack): Boolean = dropStackAt(entity.level(), entity.position(), stack)

/**
 * 指定した[item]を[pos]にドロップします。
 * @return [ItemEntity]がスポーンした場合は`true`，それ以外の場合は`false`
 */
fun dropStackAt(
    level: Level,
    pos: BlockPos,
    item: ItemLike,
    count: Int = 1,
): Boolean = dropStackAt(level, pos, ItemStack(item, count))

/**
 * 指定した[stack]を[pos]にドロップします。
 * @return [ItemEntity]がスポーンした場合は`true`，それ以外の場合は`false`
 */
fun dropStackAt(level: Level, pos: BlockPos, stack: ItemStack): Boolean =
    dropStackAt(level, pos.x.toDouble() + 0.5, pos.y.toDouble(), pos.z.toDouble() + 0.5, stack)

/**
 * 指定した[stack]を[pos]にドロップします。
 * @return [ItemEntity]がスポーンした場合は`true`，それ以外の場合は`false`
 */
fun dropStackAt(level: Level, pos: Vec3, stack: ItemStack): Boolean = dropStackAt(level, pos.x, pos.y, pos.z, stack)

/**
 * 指定した[stack]を[x]，[y]，[z]にドロップします。
 * @return [ItemEntity]がスポーンした場合は`true`，それ以外の場合は`false`
 */
fun dropStackAt(
    level: Level,
    x: Double,
    y: Double,
    z: Double,
    stack: ItemStack,
): Boolean {
    val itemEntity = ItemEntity(level, x, y, z, stack.copy())
    itemEntity.deltaMovement = Vec3.ZERO
    itemEntity.setPickUpDelay(0)
    return level.addFreshEntity(itemEntity)
}
