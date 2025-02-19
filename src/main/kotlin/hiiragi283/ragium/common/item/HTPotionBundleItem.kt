package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.item.MutableItemContainerContents
import hiiragi283.ragium.common.init.RagiumComponentTypes
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
import net.minecraft.world.item.component.ItemContainerContents
import net.minecraft.world.level.Level

class HTPotionBundleItem(properties: Properties) : Item(properties.stacksTo(1)) {
    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        val player: Player? = livingEntity as? Player
        if (player is ServerPlayer) {
            CriteriaTriggers.USING_ITEM.trigger(player, stack)
        }
        if (!level.isClientSide) {
            val content: MutableItemContainerContents =
                stack
                    .getOrDefault(RagiumComponentTypes.ITEM_CONTENT, ItemContainerContents.EMPTY)
                    .let(MutableItemContainerContents::of)
            for (slot: Int in content.getSlotRange()) {
                val stackIn: ItemStack = content.getItem(slot)
                if (stackIn.`is`(Items.POTION)) {
                    val usedStack: ItemStack = stackIn.finishUsingItem(level, livingEntity)
                    content.setItem(slot, usedStack)
                    break
                }
            }
            stack.set(RagiumComponentTypes.ITEM_CONTENT, content.toImmutable())
        }
        player?.awardStat(Stats.ITEM_USED.get(this))
        return stack
    }

    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int = 32

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.DRINK

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> =
        ItemUtils.startUsingInstantly(level, player, usedHand)

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        val allEffects: List<MobEffectInstance> = stack
            .get(RagiumComponentTypes.ITEM_CONTENT)
            ?.nonEmptyItemsCopy()
            ?.mapNotNull { it.get(DataComponents.POTION_CONTENTS) }
            ?.flatMap(PotionContents::getAllEffects)
            ?: return
        PotionContents.addPotionTooltip(
            allEffects.toSet(),
            tooltips::add,
            1f,
            context.tickRate(),
        )
    }
}
