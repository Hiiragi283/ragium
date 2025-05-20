package hiiragi283.ragium.api.item

import hiiragi283.ragium.api.extension.asPlayer
import hiiragi283.ragium.api.extension.dropStackAt
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.stats.Stats
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.UseAnim
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.Level

open class HTConsumableItem(
    val consumeSeconds: Int,
    val animation: UseAnim,
    val sound: SoundEvent,
    properties: Properties,
) : Item(properties) {
    companion object {
        @JvmStatic
        fun create(
            consumeSeconds: Int = 32,
            animation: UseAnim = UseAnim.EAT,
            sound: SoundEvent = SoundEvents.GENERIC_EAT,
        ): (Properties) -> HTConsumableItem = { prop: Properties -> HTConsumableItem(consumeSeconds, animation, sound, prop) }
    }

    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        // サーバー側で固有の挙動を起こす
        if (!level.isClientSide) {
            onConsume(stack, level, livingEntity)
        }
        val remainder: ItemStack = stack.craftingRemainingItem
        // 食べ物のをプロパティを持っている場合はデフォルトの処理を行う
        if (stack.getFoodProperties(livingEntity) != null) {
            return super.finishUsingItem(stack, level, livingEntity)
        } else {
            // そうでない場合はアイテムの消費の条件を達成する
            val player: Player? = livingEntity.asPlayer()
            if (player is ServerPlayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger(player, stack)
            }
            player?.awardStat(Stats.ITEM_USED.get(this))
            stack.consume(1, player)
        }
        // 消費したアイテムが空の場合は容器を返す
        if (stack.isEmpty) {
            return remainder
        } else {
            // そうでない場合は容器を足元にドロップ
            dropStackAt(livingEntity, remainder)
            return stack
        }
    }

    open fun onConsume(stack: ItemStack, level: Level, livingEntity: LivingEntity) {}

    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int = consumeSeconds

    override fun getUseAnimation(stack: ItemStack): UseAnim = animation

    override fun getDrinkingSound(): SoundEvent = sound

    override fun getEatingSound(): SoundEvent = sound

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        val food: FoodProperties = stack.getFoodProperties(null) ?: return
        val effects: List<FoodProperties.PossibleEffect> = food.effects
        if (effects.isNotEmpty()) {
            PotionContents.addPotionTooltip(
                effects.map(FoodProperties.PossibleEffect::effect),
                tooltips::add,
                1f,
                context.tickRate(),
            )
        }
    }
}
