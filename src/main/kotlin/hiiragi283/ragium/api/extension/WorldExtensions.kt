package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
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

fun BlockView.getMachineEntity(pos: BlockPos): HTMachineBlockEntityBase? = (getBlockEntity(pos) as? HTMachineBlockEntityBase)

fun BlockView.getMultiblockController(pos: BlockPos): HTMultiblockProvider? = getBlockEntity(pos) as? HTMultiblockProvider

fun <T : Any> WorldView.getEntry(registryKey: RegistryKey<Registry<T>>, key: RegistryKey<T>): RegistryEntry<T>? =
    registryManager.get(registryKey).getEntryOrNull(key)

//    World    //

/**
 * Drops [ItemStack] at [entity] position
 * @return true if [ItemEntity] has successfully spawned, or false if not
 */
fun dropStackAt(entity: Entity, item: ItemConvertible, count: Int = 1): Boolean = dropStackAt(entity, ItemStack(item, count))

/**
 * Drops [ItemStack] at [entity] position
 * @return true if [ItemEntity] has successfully spawned, or false if not
 */
fun dropStackAt(entity: Entity, stack: ItemStack): Boolean = dropStackAt(entity.world, entity.pos, stack)

/**
 * Drops [ItemStack] at [pos] in [world]
 * @return true if [ItemEntity] has successfully spawned, or false if not
 */
fun dropStackAt(
    world: World,
    pos: BlockPos,
    item: ItemConvertible,
    count: Int = 1,
): Boolean = dropStackAt(world, pos, ItemStack(item, count))

/**
 * Drops [ItemStack] at [pos] in [world]
 * @return true if [ItemEntity] has successfully spawned, or false if not
 */
fun dropStackAt(world: World, pos: BlockPos, stack: ItemStack): Boolean =
    dropStackAt(world, Vec3d(pos.x.toDouble() + 0.5, pos.y.toDouble(), pos.z.toDouble() + 0.5), stack)

/**
 * Drops [ItemStack] at [pos] in [world]
 * @return true if [ItemEntity] has successfully spawned, or false if not
 */
fun dropStackAt(world: World, pos: Vec3d, stack: ItemStack): Boolean {
    val itemEntity = ItemEntity(world, pos.x, pos.y, pos.z, stack)
    itemEntity.velocity = Vec3d.ZERO
    itemEntity.setPickupDelay(0)
    return world.spawnEntity(itemEntity)
}

//    BlockPos    //

/**
 * Create a list of [BlockPos] around [this] block pos
 */
val BlockPos.aroundPos: List<BlockPos>
    get() = Direction.entries.map(this::offset)

//    ChunkPos    //

/**
 * Run [action] for each [BlockPos] in [this] chunk within [yRange]
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

fun HitResult.onBlockHit(action: (BlockHitResult) -> Unit): HitResult = apply {
    (this as? BlockHitResult)?.let(action)
}

fun HitResult.onEntityHit(action: (EntityHitResult) -> Unit): HitResult = apply {
    (this as? EntityHitResult)?.let(action)
}

val BlockHitResult.sidedPos: BlockPos
    get() = blockPos.offset(side)
