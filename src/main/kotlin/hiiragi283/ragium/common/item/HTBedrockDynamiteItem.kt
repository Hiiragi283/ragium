package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.util.itemSettings
import hiiragi283.ragium.common.entity.HTBedrockDynamiteEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Position
import net.minecraft.world.World

object HTBedrockDynamiteItem : HTThrowableItem(itemSettings()) {
    override fun createEntity(world: World, user: LivingEntity): ThrownItemEntity = HTBedrockDynamiteEntity(world, user)

    //    ProjectileItem    //

    override fun createEntity(
        world: World,
        pos: Position,
        stack: ItemStack,
        direction: Direction,
    ): ProjectileEntity = HTBedrockDynamiteEntity(world, pos.x, pos.y, pos.z).apply { setItem(stack) }
}
