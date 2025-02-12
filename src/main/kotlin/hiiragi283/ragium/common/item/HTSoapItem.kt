package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.data.RagiumDataMaps
import hiiragi283.ragium.api.extension.restDamage
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent

class HTSoapItem(properties: Properties) : Item(properties.stacksTo(1)) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val level: Level = context.level
        val pos: BlockPos = context.clickedPos
        val state: BlockState = level.getBlockState(pos)
        val washed: BlockState = state.blockHolder.getData(RagiumDataMaps.SOAP)?.toState() ?: return InteractionResult.FAIL
        if (washed.`is`(state.block)) return InteractionResult.FAIL
        val player: Player? = context.player
        val stack: ItemStack = context.itemInHand
        if (player is ServerPlayer) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, pos, stack)
        }
        if (player != null) {
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.hand))
        }
        level.setBlock(pos, washed, 11)
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, washed))
        level.levelEvent(player, 3003, pos, 0)
        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    override fun hasCraftingRemainingItem(stack: ItemStack): Boolean = stack.restDamage > 0

    override fun getCraftingRemainingItem(stack: ItemStack): ItemStack {
        if (hasCraftingRemainingItem(stack)) {
            val copied: ItemStack = stack.copy()
            copied.damageValue++
            return copied
        }
        return super.getCraftingRemainingItem(stack)
    }
}
