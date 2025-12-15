package hiiragi283.ragium.setup

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.impl.HTDeferredDataComponentRegister
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.util.Unit
import net.minecraft.world.item.enchantment.ConditionalEffect
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets

/**
 * @see net.minecraft.world.item.enchantment.EnchantmentEffectComponents
 */
object RagiumEnchantmentComponents {
    @JvmField
    val REGISTER = HTDeferredDataComponentRegister(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    fun <T> registerConditional(name: String, codec: Codec<T>): DataComponentType<List<ConditionalEffect<T>>> = REGISTER.registerType(
        name,
        ConditionalEffect.codec(codec, LootContextParamSets.ENCHANTED_ITEM).listOf(),
        null,
    )

    @JvmField
    val RANGE: DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> =
        registerConditional("range", EnchantmentValueEffect.CODEC)

    @JvmField
    val STRIKE: DataComponentType<Unit> = REGISTER.registerType("strike", Codec.unit(Unit.INSTANCE), null)
}
