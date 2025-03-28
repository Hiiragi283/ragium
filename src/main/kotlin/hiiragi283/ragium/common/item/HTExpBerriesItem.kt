package hiiragi283.ragium.common.item

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level

class HTExpBerriesItem(properties: Properties) : Item(properties) {
    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        super.finishUsingItem(stack, level, livingEntity)
        // Server Playerの場合は進捗と統計を記録する
        if (livingEntity is ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(livingEntity, stack)
            livingEntity.awardStat(Stats.ITEM_USED.get(this))
        }
        // サーバー側で経験値を増やす処理を行う
        if (!level.isClientSide && livingEntity is Player) {
            livingEntity.giveExperiencePoints(8)
        }
        // stackを消費する
        stack.consume(1, livingEntity)
        level.playSound(null, livingEntity.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS)
        return when {
            stack.isEmpty -> ItemStack.EMPTY
            else -> stack
        }
    }

    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int = 32

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.EAT

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> =
        ItemUtils.startUsingInstantly(level, player, usedHand)
}
