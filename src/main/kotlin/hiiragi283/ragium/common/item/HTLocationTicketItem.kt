package hiiragi283.ragium.common.item

import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.text.toText
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.util.HTItemDropHelper
import hiiragi283.ragium.api.block.entity.HTTargetedBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level

class HTLocationTicketItem(properties: Properties) : Item(properties) {
    override fun onItemUseFirst(stack: ItemStack, context: UseOnContext): InteractionResult {
        val level: Level = context.level
        val pos: BlockPos = context.clickedPos
        val player: Player = context.player ?: return InteractionResult.FAIL
        if (!level.isClientSide) {
            if (!player.isShiftKeyDown) {
                // チケットの座標をブロックに書き込む
                val location: GlobalPos = stack.get(HCDataComponents.LOCATION) ?: return InteractionResult.PASS
                (level.getBlockEntity(pos) as? HTTargetedBlockEntity)?.updateTarget(location)
            } else {
                // チケットに座標を書き込む
                val pos = GlobalPos(level.dimension(), pos)
                if (stack.count == 1 || stack.has(HCDataComponents.LOCATION)) {
                    stack.set(HCDataComponents.LOCATION, pos)
                } else {
                    val newStack: ItemStack = createItemStack(this, HCDataComponents.LOCATION, pos)
                    HTItemDropHelper.giveStackTo(player, newStack)
                    stack.consume(1, player)
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        stack
            .get(HCDataComponents.LOCATION)
            ?.let(GlobalPos::toString)
            ?.let(String::toText)
            ?.let(tooltips::add) // TODO
    }

    override fun isFoil(stack: ItemStack): Boolean = super.isFoil(stack) || stack.has(HCDataComponents.LOCATION)
}
