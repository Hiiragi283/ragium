package hiiragi283.ragium.common.item.tool

import hiiragi283.ragium.common.entity.HTFlare
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks

class HTRagiLanternItem(properties: Properties) : Item(properties.durability(127)) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val level: Level = context.level
        val pos: BlockPos = context.clickedPos
        val face: Direction = context.clickedFace
        val posTo: BlockPos = pos.relative(face)
        if (level.getBlockState(posTo).canBeReplaced(BlockPlaceContext(context))) {
            level.setBlockAndUpdate(posTo, Blocks.LIGHT.defaultBlockState())
            context.player?.let { player: Player ->
                context.itemInHand.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.hand))
            }
            return InteractionResult.sidedSuccess(level.isClientSide)
        }
        return super.useOn(context)
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
        val stack: ItemStack = player.mainHandItem
        if (!level.isClientSide) {
            val flare = HTFlare(player, level, player.position().x, player.position().y + 1.0, player.position().z)
            flare.shootFromRotation(player, player.xRot, player.yRot, 0f, 1.5f, 1f)
            level.addFreshEntity(flare)
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(usedHand))
        }
        player.awardStat(Stats.ITEM_USED.get(this))
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
    }
}
