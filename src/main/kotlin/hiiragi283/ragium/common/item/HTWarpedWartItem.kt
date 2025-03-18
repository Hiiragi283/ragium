package hiiragi283.ragium.common.item

import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class HTWarpedWartItem(properties: Properties) : Item(properties) {
    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        val targetEffect: Holder<MobEffect> = livingEntity.activeEffects
            .map(MobEffectInstance::getEffect)
            .filterNot { it.value().isBeneficial }
            .randomOrNull()
            ?: return super.finishUsingItem(stack, level, livingEntity)
        livingEntity.removeEffect(targetEffect)
        return super.finishUsingItem(stack, level, livingEntity)
    }
}
