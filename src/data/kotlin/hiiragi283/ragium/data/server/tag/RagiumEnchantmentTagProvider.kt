package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.HTTagProvider
import hiiragi283.ragium.api.extension.enchLookup
import hiiragi283.ragium.api.tag.RagiumEnchantmentTags
import hiiragi283.ragium.setup.RagiumEnchantments
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.EnchantmentTags
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class RagiumEnchantmentTagProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, helper: ExistingFileHelper) :
    HTTagProvider<Enchantment>(
        Registries.ENCHANTMENT,
        output,
        provider,
        helper,
    ) {
    override fun addTagsInternal(provider: HolderLookup.Provider) {
        val enchLookup: HolderLookup.RegistryLookup<Enchantment> = provider.enchLookup()
        // Vanilla
        val capacity: Holder.Reference<Enchantment> = enchLookup.getOrThrow(RagiumEnchantments.CAPACITY)
        add(EnchantmentTags.NON_TREASURE, capacity)

        val noiseCanceling: Holder.Reference<Enchantment> = enchLookup.getOrThrow(RagiumEnchantments.NOISE_CANCELING)
        add(EnchantmentTags.DAMAGE_EXCLUSIVE, noiseCanceling)
        add(EnchantmentTags.TREASURE, noiseCanceling)
        add(EnchantmentTags.ON_RANDOM_LOOT, noiseCanceling)
        add(EnchantmentTags.TRADEABLE, noiseCanceling)
        
        // Capacity
        add(RagiumEnchantmentTags.CAPACITY, capacity)
        // Charging
        add(RagiumEnchantmentTags.CHARGING, enchLookup.getOrThrow(Enchantments.CHANNELING))
        add(RagiumEnchantmentTags.CHARGING, enchLookup.getOrThrow(Enchantments.WIND_BURST))
        // Cooling
        add(RagiumEnchantmentTags.COOLING, enchLookup.getOrThrow(Enchantments.FROST_WALKER))
        // Efficiency
        add(RagiumEnchantmentTags.EFFICIENCY, enchLookup.getOrThrow(Enchantments.EFFICIENCY))
        add(RagiumEnchantmentTags.EFFICIENCY, enchLookup.getOrThrow(Enchantments.LURE))
        add(RagiumEnchantmentTags.EFFICIENCY, enchLookup.getOrThrow(Enchantments.QUICK_CHARGE))
        // Extra Output
        addTag(RagiumEnchantmentTags.EXTRA_OUTPUT, Tags.Enchantments.INCREASE_BLOCK_DROPS)
        addTag(RagiumEnchantmentTags.EXTRA_OUTPUT, Tags.Enchantments.INCREASE_ENTITY_DROPS)
        add(RagiumEnchantmentTags.EXTRA_OUTPUT, enchLookup.getOrThrow(Enchantments.LUCK_OF_THE_SEA))
        // Heating
        add(RagiumEnchantmentTags.HEATING, enchLookup.getOrThrow(Enchantments.FIRE_ASPECT))
        add(RagiumEnchantmentTags.HEATING, enchLookup.getOrThrow(Enchantments.FLAME))
        // Power Saving
        add(RagiumEnchantmentTags.POWER_SAVING, enchLookup.getOrThrow(Enchantments.MENDING))
        add(RagiumEnchantmentTags.POWER_SAVING, enchLookup.getOrThrow(Enchantments.UNBREAKING))
        // Range
        add(RagiumEnchantmentTags.RANGE, enchLookup.getOrThrow(Enchantments.SWEEPING_EDGE))
    }
}
