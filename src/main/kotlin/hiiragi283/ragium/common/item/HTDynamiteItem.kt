package hiiragi283.ragium.common.item

import hiiragi283.ragium.common.entity.HTDynamite
import net.minecraft.core.Direction
import net.minecraft.core.Position
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.level.Level

class HTDynamiteItem(properties: Properties) :
    Item(properties),
    ProjectileItem {
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
        val stack: ItemStack = player.getItemInHand(usedHand)
        if (!level.isClientSide) {
            val dynamite = HTDynamite(level, player)
            dynamite.item = stack
            dynamite.shootFromRotation(player, player.xRot, player.yRot, 0f, 1.5f, 1f)
            level.addFreshEntity(dynamite)
        }
        player.awardStat(Stats.ITEM_USED.get(this))
        stack.consume(1, player)
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
    }

    override fun asProjectile(
        level: Level,
        pos: Position,
        stack: ItemStack,
        direction: Direction,
    ): Projectile = HTDynamite(level, pos.x(), pos.y(), pos.z()).apply { item = stack }
}
