package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.setup.RagiumEnchantments
import net.minecraft.core.registries.Registries
import net.minecraft.tags.EnchantmentTags
import net.minecraft.world.item.enchantment.Enchantment

class RagiumEnchantmentTagsProvider(context: HTDataGenContext) : HTTagsProvider<Enchantment>(Registries.ENCHANTMENT, context) {
    override fun addTagsInternal(factory: BuilderFactory<Enchantment>) {
        factory.apply(EnchantmentTags.NON_TREASURE).add(RagiumEnchantments.RANGE)

        factory.apply(EnchantmentTags.IN_ENCHANTING_TABLE).add(RagiumEnchantments.RANGE)

        factory
            .apply(EnchantmentTags.TREASURE)
            .add(RagiumEnchantments.NOISE_CANCELING)
            .add(RagiumEnchantments.STRIKE)
            .add(RagiumEnchantments.SONIC_PROTECTION)

        factory
            .apply(EnchantmentTags.ON_RANDOM_LOOT)
            .add(RagiumEnchantments.NOISE_CANCELING)
            .add(RagiumEnchantments.STRIKE)
            .add(RagiumEnchantments.SONIC_PROTECTION)

        factory
            .apply(EnchantmentTags.TRADEABLE)
            .add(RagiumEnchantments.NOISE_CANCELING)
            .add(RagiumEnchantments.STRIKE)
            .add(RagiumEnchantments.SONIC_PROTECTION)

        factory.apply(EnchantmentTags.ARMOR_EXCLUSIVE).add(RagiumEnchantments.SONIC_PROTECTION)
        factory
            .apply(EnchantmentTags.DAMAGE_EXCLUSIVE)
            .add(RagiumEnchantments.NOISE_CANCELING)
            .add(RagiumEnchantments.STRIKE)
    }
}
