package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.createKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment

object RagiumEnchantments {
    @JvmField
    val RANGE: ResourceKey<Enchantment> = create("range")

    //    Weapon    //

    @JvmField
    val NOISE_CANCELING: ResourceKey<Enchantment> = create("noise_canceling")

    @JvmField
    val STRIKE: ResourceKey<Enchantment> = create("strike")

    //    Armor    //

    @JvmField
    val SONIC_PROTECTION: ResourceKey<Enchantment> = create("sonic_protection")

    @JvmStatic
    private fun create(path: String): ResourceKey<Enchantment> = Registries.ENCHANTMENT.createKey(RagiumAPI.id(path))
}
