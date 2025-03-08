package hiiragi283.ragium.common.item.food

import hiiragi283.ragium.api.extension.createPotionStack
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.UseAnim
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.Level

class HTPotionBundleItem(properties: Properties) : Item(properties.stacksTo(1)) {
    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        val player: Player? = livingEntity as? Player
        if (player is ServerPlayer) {
            CriteriaTriggers.USING_ITEM.trigger(player, stack)
        }
        if (!level.isClientSide) {
            val potions: List<PotionContents> = stack.getOrDefault(RagiumComponentTypes.POTION_BUNDLE_CONTENT, listOf())
            val firstPotion: PotionContents = potions.firstOrNull() ?: return stack
            val usedStack: ItemStack = createPotionStack(firstPotion).finishUsingItem(level, livingEntity)
            dropStackAt(livingEntity, usedStack)
            stack.set(
                RagiumComponentTypes.POTION_BUNDLE_CONTENT,
                potions.toMutableList().apply {
                    removeFirst()
                },
            )
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
            .get(RagiumComponentTypes.POTION_BUNDLE_CONTENT)
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
