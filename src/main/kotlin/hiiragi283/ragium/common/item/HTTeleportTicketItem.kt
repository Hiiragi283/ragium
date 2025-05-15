package hiiragi283.ragium.common.item

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.asServerPlayer
import hiiragi283.ragium.api.extension.globalPosText
import hiiragi283.ragium.api.extension.toCenterVec3
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumComponentTypes
import net.minecraft.ChatFormatting
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.UseAnim
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.portal.DimensionTransition

class HTTeleportTicketItem(properties: Properties) : Item(properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val level: Level = context.level
        val pos: BlockPos = context.clickedPos
        // 右クリックしたブロックがテレポートアンカーの場合，その座標を保持する
        if (level.getBlockState(pos).`is`(RagiumBlocks.TELEPORT_ANCHOR)) {
            context.itemInHand.set(
                RagiumComponentTypes.TELEPORT_POS,
                GlobalPos(level.dimension(), pos.above()),
            )
            return InteractionResult.sidedSuccess(level.isClientSide)
        }
        return super.useOn(context)
    }

    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        // サーバー側のみで実行
        if (level.isClientSide) return stack
        // 実行者がプレイヤーの場合のみ実行
        if (livingEntity is Player) {
            // テレポートに成功したらチケットを消費
            val sound: SoundEvent = when {
                tryToTeleport(livingEntity, stack) -> {
                    stack.consume(1, livingEntity)
                    SoundEvents.PLAYER_TELEPORT
                }

                else -> SoundEvents.VILLAGER_NO
            }
            // SEを鳴らす
            level.playSound(
                null,
                livingEntity.x,
                livingEntity.y,
                livingEntity.z,
                sound,
                SoundSource.PLAYERS,
            )
        }
        return stack
    }

    private fun tryToTeleport(player: Player, stack: ItemStack): Boolean {
        val globalPos: GlobalPos = stack.get(RagiumComponentTypes.TELEPORT_POS) ?: return false
        val targetLevel: ServerLevel =
            RagiumAPI.getInstance().getCurrentServer()?.getLevel(globalPos.dimension) ?: return false
        // テレポート可能か判定する
        return canTeleportTo(targetLevel, globalPos)
            .ifError {
                player.displayClientMessage(
                    Component.translatable(it.message()).withStyle(ChatFormatting.RED),
                    true,
                )
            }.map {
                // 実際にテレポートを行う
                val serverPlayer: ServerPlayer = player.asServerPlayer() ?: return@map false
                if (serverPlayer.connection.isAcceptingMessages) {
                    serverPlayer.changeDimension(toTransition(targetLevel, globalPos, player))
                    serverPlayer.resetFallDistance()
                    serverPlayer.resetCurrentImpulseContext()
                    CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack)
                    return@map true
                }
                return@map false
            }.result()
            .orElse(false)
    }

    private fun canTeleportTo(serverLevel: ServerLevel, globalPos: GlobalPos): DataResult<Unit> {
        if (!serverLevel.isLoaded(globalPos.pos)) {
            return DataResult.error(RagiumTranslationKeys::TEXT_MISSING_POS)
        }
        if (!serverLevel.getBlockState(globalPos.pos.below()).`is`(RagiumBlocks.TELEPORT_ANCHOR)) {
            return DataResult.error(RagiumTranslationKeys::TEXT_MISSING_ANCHOR)
        }
        return DataResult.success(Unit)
    }

    private fun toTransition(serverLevel: ServerLevel, globalPos: GlobalPos, target: Entity): DimensionTransition = DimensionTransition(
        serverLevel,
        globalPos.pos.toCenterVec3(),
        target.deltaMovement,
        target.yRot,
        target.xRot,
        DimensionTransition.DO_NOTHING,
    )

    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int = 20 * 2

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.BOW

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> =
        ItemUtils.startUsingInstantly(level, player, usedHand)

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        stack.get(RagiumComponentTypes.TELEPORT_POS)?.let(::globalPosText)?.let(tooltips::add)
    }

    override fun isFoil(stack: ItemStack): Boolean = super.isFoil(stack) || stack.has(RagiumComponentTypes.TELEPORT_POS)
}
