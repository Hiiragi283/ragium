package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.component.HTExplosionComponent
import hiiragi283.ragium.api.extension.getStackInActiveHand
import hiiragi283.ragium.common.entity.HTDynamiteEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Position
import net.minecraft.world.World

abstract class HTDynamiteItemBase(settings: Settings) : HTThrowableItem(settings) {
    override fun hasGlint(stack: ItemStack): Boolean =
        super.hasGlint(stack) || stack.get(HTExplosionComponent.COMPONENT_TYPE)?.canDestroy == true

    override fun createEntity(world: World, user: LivingEntity): ThrownItemEntity = HTDynamiteEntity(world, user)
        .apply { setItem(user.getStackInActiveHand()) }
        .setAction(::onCollision)

    abstract fun onCollision(entity: HTDynamiteEntity, hitResult: HitResult)

    //    ProjectileItem    //

    override fun createEntity(
        world: World,
        pos: Position,
        stack: ItemStack,
        direction: Direction,
    ): ProjectileEntity = HTDynamiteEntity(world, pos.x, pos.y, pos.z)
        .apply { setItem(stack) }
        .setAction(::onCollision)
}
