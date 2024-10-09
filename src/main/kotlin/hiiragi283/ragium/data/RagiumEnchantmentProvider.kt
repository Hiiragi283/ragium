package hiiragi283.ragium.data

import hiiragi283.ragium.api.tags.RagiumEnchantmentTags
import hiiragi283.ragium.common.init.RagiumEnchantments
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.enchantment.Enchantment
import net.minecraft.item.Item
import net.minecraft.registry.RegistryEntryLookup
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import java.util.concurrent.CompletableFuture

class RagiumEnchantmentProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricDynamicRegistryProvider(output, registriesFuture) {
    override fun getName(): String = "Enchantment"

    override fun configure(registries: RegistryWrapper.WrapperLookup, entries: Entries) {
        val enchantmentLookup: RegistryEntryLookup<Enchantment> = entries.getLookup(RegistryKeys.ENCHANTMENT)
        val itemLookup: RegistryEntryLookup<Item> = entries.getLookup(RegistryKeys.ITEM)

        registerModifyEnchantment(entries, RagiumEnchantments.SMELTING, enchantmentLookup, itemLookup)
        registerModifyEnchantment(entries, RagiumEnchantments.SLEDGE_HAMMER, enchantmentLookup, itemLookup)
        registerModifyEnchantment(entries, RagiumEnchantments.BUZZ_SAW, enchantmentLookup, itemLookup)
    }

    private fun registerEnchantment(entries: Entries, key: RegistryKey<Enchantment>, builder: Enchantment.Builder) {
        entries.add(key, builder.build(key.value))
    }

    private fun registerModifyEnchantment(
        entries: Entries,
        key: RegistryKey<Enchantment>,
        enchantmentLookup: RegistryEntryLookup<Enchantment>,
        itemLookup: RegistryEntryLookup<Item>,
    ) {
        registerEnchantment(
            entries,
            key,
            Enchantment
                .builder(
                    Enchantment.definition(
                        itemLookup.getOrThrow(ItemTags.MINING_ENCHANTABLE),
                        1,
                        1,
                        Enchantment.constantCost(15),
                        Enchantment.constantCost(65),
                        8,
                        AttributeModifierSlot.MAINHAND,
                    ),
                ).exclusiveSet(enchantmentLookup.getOrThrow(RagiumEnchantmentTags.MODIFYING_EXCLUSIVE_SET)),
        )
    }
}
