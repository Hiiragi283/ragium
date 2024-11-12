package hiiragi283.ragium.common.item

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ProjectileItem
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

abstract class HTThrowableItem(settings: Settings) :
    Item(settings),
    ProjectileItem {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack: ItemStack = user.getStackInHand(hand)
        world.playSound(
            null,
            user.x,
            user.y,
            user.z,
            SoundEvents.ENTITY_SNOWBALL_THROW,
            SoundCategory.PLAYERS,
            0.5f,
            0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f),
        )
        if (!world.isClient) {
            createEntity(world, user).apply {
                setItem(stack)
                setVelocity(user, user.pitch, user.yaw, 0.0f, 1.5f, 1.0f)
                world.spawnEntity(this)
            }
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this))
        stack.decrementUnlessCreative(1, user)
        return TypedActionResult.success(stack, world.isClient())
    }

    abstract fun createEntity(world: World, user: LivingEntity): ThrownItemEntity
}
