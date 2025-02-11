package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent

class HTPotionCanItem(properties: Properties) : Item(properties.durability(3)) {
    override fun getDefaultInstance(): ItemStack =
        super.getDefaultInstance().apply { set(DataComponents.POTION_CONTENTS, PotionContents(Potions.WATER)) }

    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        val player: Player? = livingEntity as? Player
        if (player is ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(player, stack)
        }
        if (!level.isClientSide) {
            val potion: PotionContents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
            potion.forEachEffect { instance: MobEffectInstance ->
                if (instance.effect.value().isInstantenous) {
                    instance.effect
                        .value()
                        .applyInstantenousEffect(player, player, livingEntity, instance.amplifier, 1.0)
                } else {
                    livingEntity.addEffect(instance)
                }
            }
        }

        player?.let { playerIn: Player ->
            playerIn.awardStat(Stats.ITEM_USED.get(this))
            val drankStack = stack.hurtAndConvertOnBreak(
                1,
                RagiumItems.ALUMINUM_CAN,
                playerIn,
                LivingEntity.getSlotForHand(playerIn.usedItemHand),
            )
            if (drankStack != stack) {
                dropStackAt(playerIn, drankStack)
            }
        }
        livingEntity.gameEvent(GameEvent.DRINK)
        return stack
    }

    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int = 32

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.DRINK

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> =
        ItemUtils.startUsingInstantly(level, player, usedHand)

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        stack.get(DataComponents.POTION_CONTENTS)?.addPotionTooltip(tooltipComponents::add, 1f, context.tickRate())
    }
}
