package hiiragi283.ragium.common

import blusunrize.immersiveengineering.api.tool.RailgunHandler
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

open class HTThrowableRailgunProjectile(private val factory: (Level, LivingEntity) -> ThrowableItemProjectile) :
    RailgunHandler.IRailgunProjectile {
    override fun getProjectile(shooter: Player?, ammo: ItemStack, defaultProjectile: Entity): Entity {
        if (shooter != null) {
            val projectile: ThrowableItemProjectile = factory(shooter.level(), shooter)
            projectile.item = ammo
            projectile.shootFromRotation(shooter, shooter.xRot, shooter.yRot, 0f, 2.5f, 0f)
            return projectile
        }
        return super.getProjectile(shooter, ammo, defaultProjectile)
    }
}
