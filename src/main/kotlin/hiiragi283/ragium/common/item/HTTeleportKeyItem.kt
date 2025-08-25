package hiiragi283.ragium.common.item

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.toCenterVec3
import hiiragi283.ragium.api.item.HTFluidItem
import hiiragi283.ragium.api.item.component.HTTeleportPos
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.ChatFormatting
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.core.BlockPos
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
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.UseAnim
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.portal.DimensionTransition
import net.neoforged.neoforge.fluids.FluidStack

class HTTeleportKeyItem(properties: Properties) : HTFluidItem(properties.rarity(Rarity.RARE)) {
    private val fuelStack: FluidStack
        get() = RagiumFluidContents.DEW_OF_THE_WARP.toStack(1000)

    override fun onItemUseFirst(stack: ItemStack, context: UseOnContext): InteractionResult {
        val level: Level = context.level
        if (level.isClientSide) return InteractionResult.PASS
        val pos: BlockPos = context.clickedPos
        // 右クリックしたブロックの座標を保持する
        context.itemInHand.set(
            RagiumDataComponents.TELEPORT_POS,
            HTTeleportPos(level.dimension(), pos.above()),
        )
        return InteractionResult.sidedSuccess(false)
    }

    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        // サーバー側のみで実行
        if (level.isClientSide) return stack
        // 実行者がプレイヤーの場合のみ実行
        if (livingEntity is ServerPlayer) {
            // テレポートに成功したらチケットを消費
            val sound: SoundEvent = if (tryToTeleport(livingEntity, stack)) {
                CriteriaTriggers.CONSUME_ITEM.trigger(livingEntity, stack)
                SoundEvents.PLAYER_TELEPORT
            } else {
                SoundEvents.OMINOUS_BOTTLE_DISPOSE
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

    private fun tryToTeleport(player: ServerPlayer, stack: ItemStack): Boolean {
        val teleportPos: HTTeleportPos = stack.get(RagiumDataComponents.TELEPORT_POS) ?: return false
        val targetLevel: ServerLevel = player.server.getLevel(teleportPos.dimension) ?: return false
        // テレポート可能か判定する
        return canTeleportTo(targetLevel, teleportPos, stack)
            .ifError {
                player.displayClientMessage(
                    Component.translatable(it.message()).withStyle(ChatFormatting.RED),
                    true,
                )
            }.map {
                // 実際にテレポートを行う
                if (player.connection.isAcceptingMessages) {
                    val drain: Int = getFluidUsage(stack, fuelStack)
                    drainFluid(stack, drain, false)

                    player.changeDimension(toTransition(targetLevel, teleportPos, player))
                    player.resetFallDistance()
                    player.resetCurrentImpulseContext()
                    CriteriaTriggers.CONSUME_ITEM.trigger(player, stack)
                    return@map true
                }
                return@map false
            }.result()
            .orElse(false)
    }

    private fun canTeleportTo(serverLevel: ServerLevel, teleportPos: HTTeleportPos, stack: ItemStack): DataResult<Unit> = when {
        // 指定した座標が読み込まれていなければエラー
        !serverLevel.isLoaded(teleportPos.pos) -> DataResult.error(RagiumTranslationKeys::TOOLTIP_MISSING_POS)
        // 燃料タンクが見つからなければエラー
        !hasHandler(stack) -> DataResult.error { "Failed to get fluid handler" }
        // 燃料を消費できなければエラー
        !canConsumeFluid(stack, 1, fuelStack) -> DataResult.error { "Low fuel!" }

        else -> DataResult.success(Unit)
    }

    private fun toTransition(serverLevel: ServerLevel, teleportPos: HTTeleportPos, target: Entity): DimensionTransition =
        DimensionTransition(
            serverLevel,
            teleportPos.pos.toCenterVec3(),
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
        super.appendHoverText(stack, context, tooltips, flag)
        stack.get(RagiumDataComponents.TELEPORT_POS)?.getDescription()?.let(tooltips::add)
    }

    override fun isFoil(stack: ItemStack): Boolean = super.isFoil(stack) || stack.has(RagiumDataComponents.TELEPORT_POS)
}
