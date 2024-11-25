package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.throwEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ProjectileItem
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

abstract class HTThrowableItem(settings: Settings) :
    Item(settings),
    ProjectileItem {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack: ItemStack = user.getStackInHand(hand)
        return if (throwEntity(world, user, ::createEntity)) {
            user.incrementStat(Stats.USED.getOrCreateStat(this))
            stack.decrementUnlessCreative(1, user)
            TypedActionResult.success(stack, world.isClient())
        } else {
            super.use(world, user, hand)
        }
    }

    abstract fun createEntity(world: World, user: LivingEntity): ThrownItemEntity
}
