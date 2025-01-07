package hiiragi283.ragium.common.item

import hiiragi283.ragium.common.entity.HTEchoBulletEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Position
import net.minecraft.world.World

class HTEchoBulletItem(settings: Settings) : HTThrowableItem(settings) {
    override fun createEntity(world: World, user: LivingEntity): ProjectileEntity = HTEchoBulletEntity(world, user)

    override fun createEntity(
        world: World,
        pos: Position,
        stack: ItemStack,
        direction: Direction,
    ): ProjectileEntity = HTEchoBulletEntity(world, pos, direction)
}
