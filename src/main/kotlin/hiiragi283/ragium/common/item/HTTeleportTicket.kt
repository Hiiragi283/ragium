package hiiragi283.ragium.common.item

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.asServerPlayer
import hiiragi283.ragium.common.component.HTTeleportPos
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.ChatFormatting
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.portal.DimensionTransition

class HTTeleportTicket(properties: Properties) : Item(properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val level: Level = context.level
        val pos: BlockPos = context.clickedPos
        if (level.getBlockState(pos).`is`(RagiumBlocks.TELEPORT_ANCHOR)) {
            context.itemInHand.set(
                RagiumComponentTypes.TELEPORT_POS,
                HTTeleportPos(level.dimension(), pos.above()),
            )
            return InteractionResult.sidedSuccess(level.isClientSide)
        }
        return super.useOn(context)
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack: ItemStack = player.getItemInHand(usedHand)
        when (tryToTeleport(level, player, stack)) {
            true -> InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
            false -> InteractionResultHolder.fail(stack)
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
    }

    private fun tryToTeleport(level: Level, player: Player, stack: ItemStack): Boolean {
        val teleportPos: HTTeleportPos =
            stack.get(RagiumComponentTypes.TELEPORT_POS) ?: return false
        // Check can teleport
        val result: DataResult<Unit> = teleportPos.canTeleportTo()
        if (result.isError) {
            result.ifError {
                level.playSound(
                    null,
                    player.x,
                    player.y,
                    player.z,
                    SoundEvents.VILLAGER_NO,
                    SoundSource.PLAYERS,
                )
                player.displayClientMessage(
                    Component.translatable(it.message()).withStyle(ChatFormatting.RED),
                    true,
                )
            }
            return false
        }
        // Do teleport
        val transition: DimensionTransition = teleportPos.toTransition(player) ?: return false
        val serverPlayer: ServerPlayer = player.asServerPlayer() ?: return false
        if (serverPlayer.connection.isAcceptingMessages) {
            serverPlayer.changeDimension(transition)
            serverPlayer.resetFallDistance()
            serverPlayer.resetCurrentImpulseContext()
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack)
            stack.consume(1, serverPlayer)
            level.playSound(
                null,
                serverPlayer.x,
                serverPlayer.y,
                serverPlayer.z,
                SoundEvents.PLAYER_TELEPORT,
                SoundSource.PLAYERS,
            )
            return true
        }
        return false
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        stack.get(RagiumComponentTypes.TELEPORT_POS)?.addToTooltip(context, tooltips::add, flag)
    }

    override fun isFoil(stack: ItemStack): Boolean = super.isFoil(stack) || stack.has(RagiumComponentTypes.TELEPORT_POS)
}
