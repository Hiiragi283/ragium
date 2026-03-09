package hiiragi283.ragium.common.recipe.modifier

import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.ragium.api.recipe.HTDuplicationModifier
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.ItemStack

data object HTPotionDuplicationModifier : HTDuplicationModifier {
    override fun test(stack: ItemStack): Boolean = HTPotionHelper.getPotion(stack).allEffects.any()

    override fun calculateExtraAmount(stack: ItemStack): Int = HTPotionHelper
        .getPotion(stack)
        .allEffects
        .sumOf { instance: MobEffectInstance ->
            val effect: MobEffect = instance.effect.value()
            val modifier: Int = when (effect.category) {
                MobEffectCategory.BENEFICIAL -> 4
                MobEffectCategory.HARMFUL -> 1
                MobEffectCategory.NEUTRAL -> 2
            }
            instance.duration * (instance.amplifier + 1) * modifier
        }
}
