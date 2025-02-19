package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.addFluidTooltip
import hiiragi283.ragium.api.extension.canDrain
import hiiragi283.ragium.common.entity.HTFlare
import net.minecraft.core.Direction
import net.minecraft.core.Position
import net.minecraft.network.chat.Component
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

class HTFlareLauncherItem(properties: Properties) :
    Item(properties),
    ProjectileItem {
    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        addFluidTooltip(stack, tooltips::add)
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
        val stack: ItemStack = player.mainHandItem
        val fluidHandler: IFluidHandlerItem =
            stack.getCapability(Capabilities.FluidHandler.ITEM) ?: return InteractionResultHolder.fail(stack)
        if (!level.isClientSide) {
            if (fluidHandler.canDrain(100)) {
                val flare = HTFlare(level, player)
                flare.shootFromRotation(player, player.xRot, player.yRot, 0f, 3f, 1f)
                level.addFreshEntity(flare)
                fluidHandler.drain(100, IFluidHandler.FluidAction.EXECUTE)
            }
        }
        player.awardStat(Stats.ITEM_USED.get(this))
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
    }

    override fun asProjectile(
        level: Level,
        pos: Position,
        stack: ItemStack,
        direction: Direction,
    ): Projectile = HTFlare(level, pos.x(), pos.y(), pos.z()).apply { item = stack }
}
