package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.storage.capability.HTItemCapabilities
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.Containers
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper
import kotlin.random.Random

//    Position    //

fun Vec3.getRangedAABB(radius: Number): AABB = AABB.ofSize(this, radius.toDouble(), radius.toDouble(), radius.toDouble())

//    Level    //

/**
 * 指定した[stack]を[entity]のインベントリに入れるか，足元にドロップします
 */
fun giveOrDropStack(entity: Entity, stack: ItemStack, offset: Float = 0f) {
    if (entity is Player) {
        giveStackTo(entity, stack)
    } else {
        val remainStack: ItemStack = HTItemCapabilities.getCapability(entity, null)?.let { handler: IItemHandler ->
            ItemHandlerHelper.insertItem(handler, stack, false)
        } ?: stack
        entity.spawnAtLocation(remainStack, offset)
    }
}

/**
 * 指定した[stack]を[player]のインベントリに入れます。
 */
fun giveStackTo(player: Player, stack: ItemStack?) {
    if (stack == null || stack.isEmpty) return
    if (player.isFakePlayer) {
        player.spawnAtLocation(stack)
    } else {
        ItemHandlerHelper.giveItemToPlayer(player, stack)
    }
}

/**
 * 指定した[stack]を[pos]にドロップします。
 */
fun dropStackAt(level: Level, pos: BlockPos, stack: ItemStack) {
    Containers.dropItemStack(level, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), stack)
}

inline fun <reified BE : BlockEntity> BlockGetter.getTypedBlockEntity(pos: BlockPos): BE? = this.getBlockEntity(pos) as? BE

//    RandomSource    //

fun RandomSource.asKotlinRandom(): Random = RagiumPlatform.INSTANCE.wrapRandom(this)

val Level.randomKt: Random get() = this.random.asKotlinRandom()

val Entity.randomKt: Random get() = this.random.asKotlinRandom()
