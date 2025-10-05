package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.impl.HTDeferredDataComponentRegister
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.enchantment.ConditionalEffect
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets

object RagiumEnchantmentComponents {
    @JvmField
    val REGISTER = HTDeferredDataComponentRegister(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, RagiumAPI.MOD_ID)

    @JvmField
    val CAPACITY: DataComponentType<EnchantmentValueEffect> = REGISTER.registerType("capacity", EnchantmentValueEffect.CODEC, null)

    @JvmField
    val RANGE: DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> = REGISTER.registerType(
        "range",
        ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ITEM).listOf(),
        null,
    )
}
