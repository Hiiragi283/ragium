package hiiragi283.lib.item

import hiiragi283.lib.transfer.item.ItemResourceHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.Position
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Containers
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.transfer.item.ItemUtil

data object HTItemDropHelper {
    /**
     * 指定した[stack]を[entity]のインベントリに入れるか，足元にドロップします
     */
    @JvmStatic
    fun giveOrDropStack(entity: Entity, stack: ItemStack, offset: Float = 0f) {
        val level: Level = entity.level()
        if (level !is ServerLevel) return
        if (entity is Player) {
            giveStackTo(entity, stack)
        } else {
            val remainStack: ItemStack = entity.getCapability(Capabilities.Item.ENTITY)?.let { handler: ItemResourceHandler ->
                ItemUtil.insertItemReturnRemaining(handler, stack, false, null)
            } ?: stack
            entity.spawnAtLocation(level, remainStack, offset)
        }
    }

    /**
     * 指定した[stack]を[player]のインベントリに入れます。
     */
    @JvmStatic
    fun giveStackTo(player: Player, stack: ItemStack) {
        val level: Level = player.level()
        if (level !is ServerLevel) return
        if (player.isFakePlayer) {
            player.spawnAtLocation(level, stack)
        } else {
            player.inventory.add(stack)
        }
    }

    /**
     * 指定した[stack]を[pos]にドロップします。
     */
    @JvmStatic
    fun dropStackAt(level: Level, pos: BlockPos, stack: ItemStack) {
        Containers.dropItemStack(level, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), stack)
    }

    /**
     * 指定した[stack]を[pos]にドロップします。
     */
    @JvmStatic
    fun dropStackAt(level: Level, pos: Position, stack: ItemStack) {
        ItemEntity(level, pos.x(), pos.y(), pos.z(), stack).let(level::addFreshEntity)
    }
}
