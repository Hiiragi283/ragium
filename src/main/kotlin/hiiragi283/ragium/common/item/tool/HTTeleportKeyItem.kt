package hiiragi283.ragium.common.item.tool

import hiiragi283.ragium.api.item.component.HTTeleportPos
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.HTFluidCapabilities
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidView
import hiiragi283.ragium.api.text.HTTextResult
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.text.levelText
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import hiiragi283.ragium.common.util.HTEnchantmentHelper
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.UseAnim
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.portal.DimensionTransition

class HTTeleportKeyItem(properties: Properties) : Item(properties.rarity(Rarity.RARE)) {
    override fun onItemUseFirst(stack: ItemStack, context: UseOnContext): InteractionResult {
        // サーバー側のみで実行
        val level: Level = context.level
        if (level.isClientSide) return InteractionResult.PASS
        val pos: BlockPos = context.clickedPos
        // 右クリックしたブロックの座標を保持する
        // すでに保存されている場合はスキップ
        return when {
            stack.has(RagiumDataComponents.TELEPORT_POS) -> InteractionResult.PASS
            else -> {
                stack.set(
                    RagiumDataComponents.TELEPORT_POS,
                    HTTeleportPos(level.dimension(), pos.above()),
                )
                InteractionResult.sidedSuccess(false)
            }
        }
    }

    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        // サーバー側のみで実行
        if (level.isClientSide) return stack
        // 実行者がプレイヤーの場合のみ実行
        if (livingEntity is ServerPlayer) {
            val player: ServerPlayer = livingEntity
            val sound: SoundEvent = teleportPlayer(stack, player).fold(
                { SoundEvents.PLAYER_TELEPORT },
                { message: Component ->
                    player.displayClientMessage(message, true)
                    SoundEvents.OMINOUS_BOTTLE_DISPOSE
                },
            )
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
        return super.finishUsingItem(stack, level, livingEntity)
    }

    private fun teleportPlayer(stack: ItemStack, player: ServerPlayer): HTTextResult<Unit> {
        val (dim: ResourceKey<Level>, pos: BlockPos) =
            stack.get(RagiumDataComponents.TELEPORT_POS)
                ?: return HTTextResult.failure(RagiumCommonTranslation.NO_DESTINATION)
        val level: ServerLevel = player.server.getLevel(dim)
            ?: return HTTextResult.failure(RagiumCommonTranslation.UNKNOWN_DIMENSION)
        // 燃料を消費できなければスキップ
        val tank: HTFluidTank = HTFluidCapabilities.getFluidView(stack, 0) as? HTFluidTank
            ?: return HTTextResult.failure(RagiumTranslation.ERROR)
        val usage: Int = player.blockPosition().distManhattan(pos) * RagiumConfig.COMMON.teleportKeyCost.asInt
        val toDrain: Int = HTEnchantmentHelper.getFixedUsage(player.serverLevel(), stack, usage)
        if (!HTStackSlotHelper.canShrinkStack(tank, toDrain, true)) {
            return HTTextResult.failure(RagiumCommonTranslation.FUEL_SHORTAGE.translate(toDrain))
        }
        // 実際にテレポートを行う
        if (player.connection.isAcceptingMessages) {
            tank.extract(toDrain, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
            val transition = DimensionTransition(
                level,
                pos.bottomCenter,
                player.deltaMovement,
                player.yRot,
                player.xRot,
                DimensionTransition.DO_NOTHING,
            )
            player.changeDimension(transition)
            player.resetFallDistance()
            player.resetCurrentImpulseContext()
            CriteriaTriggers.CONSUME_ITEM.trigger(player, stack)
            return HTTextResult.success(Unit)
        } else {
            return HTTextResult.failure(RagiumTranslation.ERROR)
        }
    }

    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int = 20 * 2

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.BOW

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> =
        ItemUtils.startUsingInstantly(level, player, usedHand)

    override fun isFoil(stack: ItemStack): Boolean = super.isFoil(stack) || stack.has(RagiumDataComponents.TELEPORT_POS)

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        if (flag.hasShiftDown()) {
            val (dimension: ResourceKey<Level>, pos: BlockPos) = stack.get(RagiumDataComponents.TELEPORT_POS) ?: return
            tooltips.add(
                RagiumTranslation.TOOLTIP_DIMENSION.translateColored(
                    ChatFormatting.GRAY,
                    ChatFormatting.WHITE,
                    levelText(dimension),
                ),
            )
            tooltips.add(
                RagiumTranslation.TOOLTIP_BLOCK_POS.translateColored(
                    ChatFormatting.GRAY,
                    ChatFormatting.WHITE,
                    pos.x,
                    ChatFormatting.WHITE,
                    pos.y,
                    ChatFormatting.WHITE,
                    pos.z,
                ),
            )
        } else {
            val view: HTFluidView = HTFluidCapabilities.getFluidView(stack, 0) ?: return
            // Fluid Name
            when (val stack: ImmutableFluidStack? = view.getStack()) {
                null -> RagiumTranslation.EMPTY.translateColored(ChatFormatting.DARK_RED)
                else -> RagiumTranslation.STORED_MB.translateColored(
                    ChatFormatting.LIGHT_PURPLE,
                    stack,
                    ChatFormatting.GRAY,
                    stack.amount(),
                )
            }.let(tooltips::add)
            // Tank Capacity
            RagiumTranslation.CAPACITY_MB
                .translateColored(
                    ChatFormatting.BLUE,
                    ChatFormatting.GRAY,
                    view.getCapacity(),
                ).let(tooltips::add)

            tooltips.add(RagiumTranslation.TOOLTIP_SHOW_DESCRIPTION.translateColored(ChatFormatting.YELLOW))
        }
    }
}
