package hiiragi283.ragium.api.extension

import net.minecraft.core.BlockPos
import net.minecraft.core.Position
import net.minecraft.world.Containers
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper

//    Position    //

fun BlockPos.toVec3(): Vec3 = Vec3(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())

fun BlockPos.toCenterVec3(): Vec3 = toVec3().add(0.5, 0.0, 0.5)

fun BlockPos.getRangedAABB(radius: Number): AABB = toCenterVec3().getRangedAABB(radius)

fun Vec3.getRangedAABB(radius: Number): AABB = AABB.ofSize(this, radius.toDouble(), radius.toDouble(), radius.toDouble())

//    Level    //

/**
 * 指定した[item]を[entity]の足元にドロップします。
 */
fun dropStackAt(entity: Entity, item: ItemLike, count: Int = 1) {
    dropStackAt(entity, ItemStack(item, count))
}

/**
 * 指定した[stack]を[entity]のインベントリに入れるか，足元にドロップします
 */
fun dropStackAt(entity: Entity, stack: ItemStack) {
    if (entity is Player) {
        if (entity.isFakePlayer) {
            dropStackAt(entity.level(), entity.position(), stack)
        } else {
            ItemHandlerHelper.giveItemToPlayer(entity, stack)
        }
    } else {
        val handler: IItemHandler? = entity.getCapability(Capabilities.ItemHandler.ENTITY)
        val remainStack: ItemStack = when {
            handler != null -> ItemHandlerHelper.insertItem(handler, stack, false)
            else -> stack
        }
        dropStackAt(entity.level(), entity.position(), remainStack)
    }
}

/**
 * 指定した[stack]を[pos]にドロップします。
 */
fun dropStackAt(level: Level, pos: BlockPos, stack: ItemStack) {
    dropStackAt(level, pos.toVec3(), stack)
}

/**
 * 指定した[stack]を[pos]にドロップします。
 */
fun dropStackAt(level: Level, pos: Position, stack: ItemStack) {
    Containers.dropItemStack(level, pos.x(), pos.y(), pos.z(), stack)
}
