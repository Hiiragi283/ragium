package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.tag.HTTagBuilder
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.setup.RagiumEnchantments
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.EnchantmentTags
import net.minecraft.world.item.enchantment.Enchantment
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class RagiumEnchantmentTagsProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, helper: ExistingFileHelper) :
    HTTagsProvider<Enchantment>(
        output,
        Registries.ENCHANTMENT,
        provider,
        helper,
    ) {
    override fun addTags(builder: HTTagBuilder<Enchantment>) {
        // Vanilla
        builder.add(EnchantmentTags.NON_TREASURE, RagiumEnchantments.CAPACITY)
        builder.add(EnchantmentTags.ON_RANDOM_LOOT, RagiumEnchantments.CAPACITY)
        builder.add(EnchantmentTags.TRADEABLE, RagiumEnchantments.CAPACITY)

        builder.add(EnchantmentTags.TREASURE, RagiumEnchantments.NOISE_CANCELING)
        builder.add(EnchantmentTags.ON_RANDOM_LOOT, RagiumEnchantments.NOISE_CANCELING)
        builder.add(EnchantmentTags.TRADEABLE, RagiumEnchantments.NOISE_CANCELING)
        builder.add(EnchantmentTags.DAMAGE_EXCLUSIVE, RagiumEnchantments.NOISE_CANCELING)

        builder.add(EnchantmentTags.TREASURE, RagiumEnchantments.SONIC_PROTECTION)
        builder.add(EnchantmentTags.ON_RANDOM_LOOT, RagiumEnchantments.SONIC_PROTECTION)
        builder.add(EnchantmentTags.TRADEABLE, RagiumEnchantments.SONIC_PROTECTION)
        builder.add(EnchantmentTags.ARMOR_EXCLUSIVE, RagiumEnchantments.SONIC_PROTECTION)
    }
}
