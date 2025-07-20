package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumEnchantments
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.EnchantmentTags
import net.minecraft.world.item.enchantment.Enchantment
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class RagiumEnchantmentTagsProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, helper: ExistingFileHelper) :
    TagsProvider<Enchantment>(
        output,
        Registries.ENCHANTMENT,
        provider,
        RagiumAPI.MOD_ID,
        helper,
    ) {
    override fun addTags(provider: HolderLookup.Provider) {
        // Vanilla
        listOf(
            tag(EnchantmentTags.NON_TREASURE),
            tag(EnchantmentTags.ON_RANDOM_LOOT),
            tag(EnchantmentTags.TRADEABLE),
        ).forEach { it.add(RagiumEnchantments.CAPACITY) }

        listOf(
            tag(EnchantmentTags.TREASURE),
            tag(EnchantmentTags.ON_RANDOM_LOOT),
            tag(EnchantmentTags.TRADEABLE),
        ).forEach { it.add(RagiumEnchantments.NOISE_CANCELING, RagiumEnchantments.SONIC_PROTECTION) }

        tag(EnchantmentTags.DAMAGE_EXCLUSIVE).add(RagiumEnchantments.NOISE_CANCELING)
        tag(EnchantmentTags.ARMOR_EXCLUSIVE).add(RagiumEnchantments.SONIC_PROTECTION)
    }
}
