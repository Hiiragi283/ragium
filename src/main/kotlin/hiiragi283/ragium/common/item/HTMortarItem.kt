package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.data.map.RagiumDataMaps
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent

/**
 * @see net.minecraft.world.item.HoneycombItem
 */
class HTMortarItem(properties: Properties) : Item(properties) {
    companion object {
        @JvmStatic
        fun getRepaired(oldState: BlockState): BlockState? = oldState.typeHolder()
            .getData(RagiumDataMaps.MORTAR_REPAIR)
            ?.withPropertiesOf(oldState)
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val level: Level = context.level
        val pos: BlockPos = context.clickedPos
        val oldState: BlockState = level.getBlockState(pos)
        return getRepaired(oldState)?.let { repairedState: BlockState ->
            val player: Player? = context.player
            val itemInHand: ItemStack = context.itemInHand
            if (player is ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, pos, itemInHand)
            }
            itemInHand.shrink(1)
            level.setBlock(pos, repairedState, 11)
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, repairedState))
            level.playLocalSound(pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1f, 1f, false)
            InteractionResult.SUCCESS
        } ?: InteractionResult.PASS
    }
}
