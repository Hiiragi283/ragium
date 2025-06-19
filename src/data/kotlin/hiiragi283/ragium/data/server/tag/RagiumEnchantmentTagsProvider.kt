package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumEnchantmentTags
import hiiragi283.ragium.setup.RagiumEnchantments
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.EnchantmentTags
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.neoforged.neoforge.common.Tags
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
        tag(EnchantmentTags.NON_TREASURE).add(RagiumEnchantments.CAPACITY)

        listOf(
            tag(EnchantmentTags.DAMAGE_EXCLUSIVE),
            tag(EnchantmentTags.TREASURE),
            tag(EnchantmentTags.ON_RANDOM_LOOT),
            tag(EnchantmentTags.TRADEABLE),
        ).forEach { it.add(RagiumEnchantments.NOISE_CANCELING) }

        // Charging
        tag(RagiumEnchantmentTags.CHARGING).add(Enchantments.CHANNELING, Enchantments.WIND_BURST)
        // Cooling
        tag(RagiumEnchantmentTags.COOLING).add(Enchantments.FROST_WALKER)
        // Efficiency
        tag(RagiumEnchantmentTags.EFFICIENCY).add(Enchantments.EFFICIENCY, Enchantments.LURE, Enchantments.QUICK_CHARGE)
        // Extra Output
        tag(RagiumEnchantmentTags.EXTRA_OUTPUT)
            .addTags(Tags.Enchantments.INCREASE_BLOCK_DROPS, Tags.Enchantments.INCREASE_ENTITY_DROPS)
            .add(Enchantments.LUCK_OF_THE_SEA)
        // Heating
        tag(RagiumEnchantmentTags.HEATING).add(Enchantments.FIRE_ASPECT, Enchantments.FLAME)
        // Power Saving
        tag(RagiumEnchantmentTags.POWER_SAVING).add(Enchantments.MENDING, Enchantments.UNBREAKING)
        // Range
        tag(RagiumEnchantmentTags.RANGE).add(Enchantments.SWEEPING_EDGE)
    }
}
