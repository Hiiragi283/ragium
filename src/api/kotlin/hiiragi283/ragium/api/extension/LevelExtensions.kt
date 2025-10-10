package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.recipe.manager.HTRecipeAccess
import hiiragi283.ragium.api.storage.HTMultiCapability
import net.minecraft.core.BlockPos
import net.minecraft.core.Position
import net.minecraft.world.Containers
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper

//    Position    //

fun BlockPos.toVec3(): Vec3 = Vec3(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())

fun BlockPos.toCenterVec3(): Vec3 = toVec3().add(0.5, 0.0, 0.5)

fun BlockPos.getRangedAABB(radius: Number): AABB = toCenterVec3().getRangedAABB(radius)

fun Vec3.getRangedAABB(radius: Number): AABB = AABB.ofSize(this, radius.toDouble(), radius.toDouble(), radius.toDouble())

//    Level    //

/**
 * 指定した[stack]を[entity]のインベントリに入れるか，足元にドロップします
 */
fun giveOrDropStack(entity: Entity, stack: ItemStack, offset: Float = 0f) {
    if (entity is Player) {
        giveStackTo(entity, stack)
    } else {
        val remainStack: ItemStack = HTMultiCapability.ITEM.getCapability(entity, null)?.let { handler: IItemHandler ->
            ItemHandlerHelper.insertItem(handler, stack, false)
        } ?: stack
        dropStackAt(entity, remainStack, offset)
    }
}

/**
 * 指定した[stack]を[player]のインベントリに入れます。
 */
fun giveStackTo(player: Player, stack: ItemStack) {
    if (player.isFakePlayer) {
        dropStackAt(player, stack)
    } else {
        ItemHandlerHelper.giveItemToPlayer(player, stack)
    }
}

/**
 * 指定した[stack]を[entity]の足元にドロップします。
 */
fun dropStackAt(entity: Entity, stack: ItemStack, offset: Float = 0f) {
    entity.spawnAtLocation(stack, offset)
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

val Level.recipeAccess: HTRecipeAccess get() = RagiumPlatform.INSTANCE.wrapRecipeManager(this.recipeManager)
