package hiiragi283.ragium.common.item.alchemy

import hiiragi283.lib.registry.HTPotionContent
import hiiragi283.lib.registry.HTPotionContentRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.effect.RagiumMobEffects
import net.neoforged.bus.api.IEventBus

data object RagiumPotions {
    @JvmField
    val REGISTER = HTPotionContentRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    @JvmField
    val FROSTBITE: HTPotionContent = REGISTER.registerHarmful("frostbite", RagiumMobEffects.FROSTBITE)
}
