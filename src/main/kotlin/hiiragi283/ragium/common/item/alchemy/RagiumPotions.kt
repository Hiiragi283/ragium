package hiiragi283.ragium.common.item.alchemy

import hiiragi283.lib.registry.HTDeferredRegister
import hiiragi283.lib.registry.HTSimpleDeferredHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.effect.RagiumMobEffects
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.Potion
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent

data object RagiumPotions {
    @JvmField
    val REGISTER: HTDeferredRegister<Potion> = HTDeferredRegister(Registries.POTION, RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    @JvmField
    val FROSTBITE: HTSimpleDeferredHolder<Potion> = REGISTER.register("frostbite") { id: Identifier ->
        Potion(id.path, MobEffectInstance(RagiumMobEffects.FROSTBITE, 900))
    }

    @JvmField
    val LONG_FROSTBITE: HTSimpleDeferredHolder<Potion> = REGISTER.register("long_frostbite") { _ ->
        Potion("frostbite", MobEffectInstance(RagiumMobEffects.FROSTBITE, 1800))
    }

    @JvmField
    val STRONG_FROSTBITE: HTSimpleDeferredHolder<Potion> = REGISTER.register("strong_frostbite") { _ ->
        Potion("frostbite", MobEffectInstance(RagiumMobEffects.FROSTBITE, 432, 1))
    }

    @JvmStatic
    private fun registerBrewingRecipes(event: RegisterBrewingRecipesEvent) {
    }
}
