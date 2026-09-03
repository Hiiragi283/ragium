package hiiragi283.ragium.common.effect

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageSources
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity

class HTDamageMobEffect(
    category: MobEffectCategory,
    color: Int,
    private val damageProvider: (DamageSources) -> DamageSource,
    private val interval: Int = 40
) : MobEffect(category, color) {
    override fun applyEffectTick(serverLevel: ServerLevel, mob: LivingEntity, amplification: Int): Boolean {
        mob.hurtServer(serverLevel, mob.damageSources().let(damageProvider), 1f)
        return true
    }

    override fun shouldApplyEffectTickThisTick(tickCount: Int, amplification: Int): Boolean {
        val interval: Int = this.interval shr amplification
        return interval <= 0 || tickCount % interval == 0
    }
}
