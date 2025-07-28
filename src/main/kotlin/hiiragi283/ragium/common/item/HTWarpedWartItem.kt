package hiiragi283.ragium.common.item

import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemNameBlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class HTWarpedWartItem(properties: Properties) : ItemNameBlockItem(RagiumBlocks.WARPED_WART.get(), properties) {
    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        livingEntity.activeEffects
            .map(MobEffectInstance::getEffect)
            .filter { it.value().category == MobEffectCategory.HARMFUL }
            .randomOrNull()
            ?.let(livingEntity::removeEffect)
        return super.finishUsingItem(stack, level, livingEntity)
    }
}
