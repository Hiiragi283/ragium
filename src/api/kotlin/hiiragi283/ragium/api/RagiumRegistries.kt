package hiiragi283.ragium.api

import hiiragi283.ragium.api.component.HTConsumeEffect
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.RegistryBuilder

object RagiumRegistries {
    @JvmField
    val CONSUME_EFFECT_TYPE: Registry<HTConsumeEffect.Type<*>> =
        RegistryBuilder(Keys.CONSUME_EFFECT_TYPE).sync(true).create()

    object Keys {
        @JvmField
        val CONSUME_EFFECT_TYPE: ResourceKey<Registry<HTConsumeEffect.Type<*>>> =
            ResourceKey.createRegistryKey(RagiumAPI.id("consume_effect_type"))
    }
}
