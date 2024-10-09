package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.enchantment.Enchantment
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys

object RagiumEnchantments {
    @JvmField
    val SMELTING: RegistryKey<Enchantment> = create("smelting")

    @JvmField
    val SLEDGE_HAMMER: RegistryKey<Enchantment> = create("sledge_hammer")

    @JvmField
    val BUZZ_SAW: RegistryKey<Enchantment> = create("buzz_saw")

    @JvmStatic
    private fun create(name: String): RegistryKey<Enchantment> = RegistryKey.of(RegistryKeys.ENCHANTMENT, RagiumAPI.id(name))
}
