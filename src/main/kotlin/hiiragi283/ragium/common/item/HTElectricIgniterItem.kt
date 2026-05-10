package hiiragi283.ragium.common.item

import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.core.common.capability.HTEnergyCapabilities
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseFireBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent
import net.neoforged.neoforge.common.ItemAbilities
import net.neoforged.neoforge.common.ItemAbility

/**
 * @see net.minecraft.world.item.FlintAndSteelItem
 */
class HTElectricIgniterItem(properties: Properties) : HTBatteryItem(properties) {
    private fun getEnergyUsage(): Int = RagiumConfig.COMMON.electricIgniter.getUsage()

    private fun canUse(stack: ItemStack): Boolean = HTEnergyCapabilities.getBattery(stack)?.let(::canUse) ?: false

    private fun canUse(battery: HTEnergyBattery): Boolean = battery.getAmount() >= getEnergyUsage()

    private fun consumeEnergy(battery: HTEnergyBattery): Int = battery.extract(getEnergyUsage(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)

    override fun useOn(context: UseOnContext): InteractionResult {
        val stack: ItemStack = context.itemInHand
        val battery: HTEnergyBattery = HTEnergyCapabilities.getBattery(stack) ?: return InteractionResult.FAIL
        if (!canUse(battery)) return InteractionResult.FAIL

        val player: Player? = context.player
        val level: Level = context.level
        val pos: BlockPos = context.clickedPos
        val state: BlockState = level.getBlockState(pos)
        val newState: BlockState? = state.getToolModifiedState(context, ItemAbilities.FIRESTARTER_LIGHT, false)
        if (newState == null) {
            val posOffset: BlockPos = pos.relative(context.clickedFace)
            if (BaseFireBlock.canBePlacedAt(level, posOffset, context.horizontalDirection)) {
                level.playSound(player, posOffset, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS)
                level.setBlock(posOffset, BaseFireBlock.getState(level, posOffset), 11)
                level.gameEvent(player, GameEvent.BLOCK_PLACE, pos)
                if (player is ServerPlayer) {
                    CriteriaTriggers.PLACED_BLOCK.trigger(player, posOffset, stack)
                    consumeEnergy(battery)
                }
                return InteractionResult.sidedSuccess(level.isClientSide)
            } else {
                return InteractionResult.FAIL
            }
        } else {
            level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS)
            level.setBlock(pos, newState, 11)
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos)
            consumeEnergy(battery)
            return InteractionResult.sidedSuccess(level.isClientSide)
        }
    }

    override fun canPerformAction(stack: ItemStack, itemAbility: ItemAbility): Boolean = when {
        canUse(stack) -> itemAbility in ItemAbilities.DEFAULT_FLINT_ACTIONS
        else -> super.canPerformAction(stack, itemAbility)
    }
}
