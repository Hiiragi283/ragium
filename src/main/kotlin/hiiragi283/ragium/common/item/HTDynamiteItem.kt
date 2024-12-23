package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.component.HTExplosionComponent
import hiiragi283.ragium.common.entity.HTDynamiteEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Vec3d

class HTDynamiteItem(settings: Settings) : HTDynamiteItemBase(settings) {
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

    override fun onCollision(entity: HTDynamiteEntity, hitResult: HitResult) {
        val pos: Vec3d = hitResult.pos
        entity.stack
            .getOrDefault(HTExplosionComponent.COMPONENT_TYPE, HTExplosionComponent.DEFAULT)
            .createExplosion(entity.world, entity, pos.x, pos.y, pos.z)
    }
}
