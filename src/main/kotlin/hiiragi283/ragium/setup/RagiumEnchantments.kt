package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment

object RagiumEnchantments {
    @JvmField
    val CAPACITY: ResourceKey<Enchantment> = create("capacity")

    @JvmField
    val NOISE_CANCELING: ResourceKey<Enchantment> = create("noise_canceling")

    @JvmStatic
    private fun create(path: String): ResourceKey<Enchantment> = ResourceKey.create(Registries.ENCHANTMENT, RagiumAPI.id(path))
}
