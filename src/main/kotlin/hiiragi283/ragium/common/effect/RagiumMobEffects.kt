package hiiragi283.ragium.common.effect

import hiiragi283.lib.registry.HTDeferredMobEffect
import hiiragi283.lib.registry.HTDeferredMobEffectRegister
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.world.damagesource.DamageSources
import net.minecraft.world.effect.MobEffectCategory
import net.neoforged.bus.api.IEventBus

data object RagiumMobEffects {
    @JvmField
    val REGISTER: HTDeferredMobEffectRegister = HTDeferredMobEffectRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(event: IEventBus) {
        REGISTER.register(event)
    }

    @JvmField
    val FROSTBITE: HTDeferredMobEffect<HTDamageMobEffect> = REGISTER.register("frostbite") { _ -> HTDamageMobEffect(MobEffectCategory.HARMFUL, 0x3399cc, DamageSources::freeze) }
}
