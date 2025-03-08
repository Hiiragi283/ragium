package hiiragi283.ragium.common.item

import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

abstract class HTThrowableItem(properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
        val stack: ItemStack = player.mainHandItem
        if (!level.isClientSide) {
            val projectile: Projectile = throwProjectile(level, player, stack)
            projectile.shootFromRotation(player, player.xRot, player.yRot, 0f, 1.5f, 1f)
            level.addFreshEntity(projectile)
        }
        player.awardStat(Stats.ITEM_USED.get(this))
        stack.consume(1, player)
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
    }

    abstract fun throwProjectile(level: Level, player: Player, stack: ItemStack): Projectile
}
