package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntityBase
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldView

//    Views    //

/**
 * 指定した[BlockView]と[pos]から[HTMachineBlockEntityBase]を返します。
 */
fun BlockView.getMachineEntity(pos: BlockPos): HTMachineBlockEntityBase? = (getBlockEntity(pos) as? HTMachineBlockEntityBase)

/**
 * 指定した[WorldView]から[RegistryEntry]を返します。
 * @param T 値のクラス
 * @param registryKey レジストリのキー
 * @param key 値のキー
 */
fun <T : Any> WorldView.getEntry(registryKey: RegistryKey<Registry<T>>, key: RegistryKey<T>): RegistryEntry<T>? =
    registryManager.get(registryKey).getEntryOrNull(key)

//    World    //

/**
 * 指定した[item]を[entity]の足元にドロップします。
 * @return [ItemEntity]がスポーンした場合はtrue，それ以外の場合はfalse
 */
fun dropStackAt(entity: Entity, item: ItemConvertible, count: Int = 1): Boolean = dropStackAt(entity, ItemStack(item, count))

/**
 * 指定した[stack]を[entity]の足元にドロップします。
 * @return [ItemEntity]がスポーンした場合はtrue，それ以外の場合はfalse
 */
fun dropStackAt(entity: Entity, stack: ItemStack): Boolean = dropStackAt(entity.world, entity.pos, stack)

/**
 * 指定した[item]を[pos]にドロップします。
 * @return [ItemEntity]がスポーンした場合はtrue，それ以外の場合はfalse
 */
fun dropStackAt(
    world: World,
    pos: BlockPos,
    item: ItemConvertible,
    count: Int = 1,
): Boolean = dropStackAt(world, pos, ItemStack(item, count))

/**
 * 指定した[stack]を[pos]にドロップします。
 * @return [ItemEntity]がスポーンした場合はtrue，それ以外の場合はfalse
 */
fun dropStackAt(world: World, pos: BlockPos, stack: ItemStack): Boolean =
    dropStackAt(world, pos.x.toDouble() + 0.5, pos.y.toDouble(), pos.z.toDouble() + 0.5, stack)

/**
 * 指定した[stack]を[pos]にドロップします。
 * @return [ItemEntity]がスポーンした場合はtrue，それ以外の場合はfalse
 */
fun dropStackAt(world: World, pos: Vec3d, stack: ItemStack): Boolean = dropStackAt(world, pos.x, pos.y, pos.z, stack)

/**
 * 指定した[stack]を[x]，[y]，[z]にドロップします。
 * @return [ItemEntity]がスポーンした場合はtrue，それ以外の場合はfalse
 */
fun dropStackAt(
    world: World,
    x: Double,
    y: Double,
    z: Double,
    stack: ItemStack,
): Boolean {
    val itemEntity = ItemEntity(world, x, y, z, stack)
    itemEntity.velocity = Vec3d.ZERO
    itemEntity.setPickupDelay(0)
    return world.spawnEntity(itemEntity)
}

//    BlockPos    //

/**
 * 指定した[BlockPos]を各方角に1だけ移動した[BlockPos]の一覧を返します。
 */
val BlockPos.aroundPos: List<BlockPos>
    get() = Direction.entries.map(this::offset)

//    ChunkPos    //

/**
 * 指定した[yRange]の範囲で[ChunkPos]内の各[BlockPos]に対して処理を行います。
 */
fun ChunkPos.forEach(yRange: IntRange, action: (BlockPos) -> Unit) {
    (startX..endX).forEach { x: Int ->
        (startZ..endZ).forEach { z: Int ->
            yRange.sortedDescending().forEach { y: Int ->
                action(BlockPos(x, y, z))
            }
        }
    }
}

//    HitResult    //

/**
 * 指定した[HitResult]が[BlockHitResult]の場合のみに処理を行います。
 * @return 指定した[HitResult]
 */
fun HitResult.onBlockHit(action: (BlockHitResult) -> Unit): HitResult = apply {
    (this as? BlockHitResult)?.let(action)
}

/**
 * 指定した[HitResult]が[EntityHitResult]の場合のみに処理を行います。
 * @return 指定した[HitResult]
 */
fun HitResult.onEntityHit(action: (EntityHitResult) -> Unit): HitResult = apply {
    (this as? EntityHitResult)?.let(action)
}

/**
 * 指定した[BlockHitResult]の座標を[BlockHitResult.side]で1だけずらした座標を返します。
 */
val BlockHitResult.sidedPos: BlockPos
    get() = blockPos.offset(side)
