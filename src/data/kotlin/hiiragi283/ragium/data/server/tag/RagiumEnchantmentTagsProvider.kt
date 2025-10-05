package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.data.tag.HTTagBuilder
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.setup.RagiumEnchantments
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.EnchantmentTags
import net.minecraft.world.item.enchantment.Enchantment

class RagiumEnchantmentTagsProvider(context: HTDataGenContext) : HTTagsProvider<Enchantment>(Registries.ENCHANTMENT, context) {
    override fun addTags(builder: HTTagBuilder<Enchantment>) {
        // Vanilla
        builder.addNonTreasure(RagiumEnchantments.CAPACITY)
        builder.addNonTreasure(RagiumEnchantments.RANGE)

        builder.addTreasure(RagiumEnchantments.NOISE_CANCELING)
        builder.add(EnchantmentTags.DAMAGE_EXCLUSIVE, RagiumEnchantments.NOISE_CANCELING)

        builder.addTreasure(RagiumEnchantments.SONIC_PROTECTION)
        builder.add(EnchantmentTags.ARMOR_EXCLUSIVE, RagiumEnchantments.SONIC_PROTECTION)
    }

    //    Extensions    //

    private fun HTTagBuilder<Enchantment>.addNonTreasure(key: ResourceKey<Enchantment>) {
        add(EnchantmentTags.NON_TREASURE, key)
        add(EnchantmentTags.IN_ENCHANTING_TABLE, key)
    }

    private fun HTTagBuilder<Enchantment>.addTreasure(key: ResourceKey<Enchantment>) {
        add(EnchantmentTags.TREASURE, key)
        add(EnchantmentTags.ON_RANDOM_LOOT, key)
        add(EnchantmentTags.TRADEABLE, key)
    }
}
