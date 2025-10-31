package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.capability.HTItemCapabilities
import net.minecraft.core.BlockPos
import net.minecraft.world.Containers
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper

object HTItemDropHelper {
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
    fun giveStackTo(player: Player, stack: ItemStack) {
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

    // ImmutableItemStack

    /**
     * 指定した[stack]を[entity]のインベントリに入れるか，足元にドロップします
     */
    fun giveOrDropStack(entity: Entity, stack: ImmutableItemStack?, offset: Float = 0f) {
        if (stack == null) return
        giveOrDropStack(entity, stack.stack, offset)
    }

    /**
     * 指定した[stack]を[player]のインベントリに入れます。
     */
    fun giveStackTo(player: Player, stack: ImmutableItemStack?) {
        if (stack == null) return
        giveStackTo(player, stack.stack)
    }

    /**
     * 指定した[stack]を[pos]にドロップします。
     */
    fun dropStackAt(level: Level, pos: BlockPos, stack: ImmutableItemStack?) {
        if (stack == null) return
        dropStackAt(level, pos, stack.stack)
    }
}
