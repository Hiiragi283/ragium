package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.common.entity.HTRemoverDynamiteEntity
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

object HTRemoverDynamiteItem : HTThrowableItem(itemSettings()) {
    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType,
    ) {
        stack.get(RagiumComponentTypes.REMOVER_DYNAMITE)?.let {
            tooltip.add(Text.literal("- Mode: $it"))
        }
    }

    override fun createEntity(world: World, user: LivingEntity): ThrownItemEntity = HTRemoverDynamiteEntity(world, user)

    //    ProjectileItem    //

    override fun createEntity(
        world: World,
        pos: Position,
        stack: ItemStack,
        direction: Direction,
    ): ProjectileEntity = HTRemoverDynamiteEntity(world, pos.x, pos.y, pos.z).apply { setItem(stack) }
}
