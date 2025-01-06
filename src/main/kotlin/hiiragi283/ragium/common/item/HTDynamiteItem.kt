package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.component.HTExplosionComponent
import hiiragi283.ragium.api.extension.setItemFromOwner
import hiiragi283.ragium.common.entity.HTDynamiteEntity
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Position
import net.minecraft.world.World

class HTDynamiteItem(settings: Settings) : HTThrowableItem(settings) {
    override fun hasGlint(stack: ItemStack): Boolean = super.hasGlint(stack) || stack.contains(RagiumComponentTypes.DYNAMITE)

    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType,
    ) {
        stack
            .getOrDefault(HTExplosionComponent.COMPONENT_TYPE, HTExplosionComponent.DEFAULT)
            .appendTooltip(context, tooltip::add, type)
    }

    override fun createEntity(world: World, user: LivingEntity): ThrownItemEntity = HTDynamiteEntity(world, user)
        .setItemFromOwner(user)

    override fun createEntity(
        world: World,
        pos: Position,
        stack: ItemStack,
        direction: Direction,
    ): ProjectileEntity = HTDynamiteEntity(world, pos.x, pos.y, pos.z).apply { setItem(stack) }
}
