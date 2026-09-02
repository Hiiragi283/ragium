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

/**
 * アイテムのドロップ処理を補助するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTItemDropHelper {
    /**
     * アイテムをインベントリに入れるかドロップします。
     * @param entity インベントリの所有者
     * @param stack インベントリに入れられるアイテム
     * @param offset ドロップ時のオフセット
     * @return 正常にアイテムを移動できた場合は`true`
     */
    @JvmStatic
    fun giveOrDropStack(entity: Entity, stack: ItemStack, offset: Float = 0f): Boolean {
        val level: Level = entity.level()
        return when {
            level !is ServerLevel -> false

            entity is Player -> giveStackTo(entity, stack)

            else -> {
                val remainStack: ItemStack =
                    entity.getCapability(Capabilities.Item.ENTITY)?.let { handler: ItemResourceHandler ->
                        ItemUtil.insertItemReturnRemaining(handler, stack, false, null)
                    } ?: stack
                entity.spawnAtLocation(level, remainStack, offset) != null
            }
        }
    }

    /**
     * アイテムをインベントリに入れます。
     * @param player インベントリの所有者
     * @param stack インベントリに入れられるアイテム
     * @return 正常にアイテムを移動できた場合は`true`
     */
    @JvmStatic
    fun giveStackTo(player: Player, stack: ItemStack): Boolean {
        val level: Level = player.level()
        return when {
            level !is ServerLevel -> false
            player.isFakePlayer -> player.spawnAtLocation(level, stack) != null
            else -> player.inventory.add(stack)
        }
    }

    /**
     * アイテムをドロップします。
     * @param level ドロップするレベル
     * @param pos ドロップする座標
     * @param stack ドロップされるアイテム
     */
    @JvmStatic
    fun dropStackAt(level: Level, pos: BlockPos, stack: ItemStack) {
        Containers.dropItemStack(level, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), stack)
    }

    /**
     * アイテムをドロップします。
     * @param level ドロップするレベル
     * @param pos ドロップする座標
     * @param stack ドロップされるアイテム
     * @return 正常にアイテムをドロップできた場合は`true`
     */
    @JvmStatic
    fun dropStackAt(level: Level, pos: Position, stack: ItemStack): Boolean =
        ItemEntity(level, pos.x(), pos.y(), pos.z(), stack).let(level::addFreshEntity)
}
