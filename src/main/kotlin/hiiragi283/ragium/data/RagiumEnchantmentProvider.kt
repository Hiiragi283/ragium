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

        registerEnchantments(entries::add, enchantmentLookup, itemLookup)
    }

    companion object {
        @JvmStatic
        fun registerEnchantments(
            register: (RegistryKey<Enchantment>, Enchantment) -> Unit,
            enchantmentLookup: RegistryEntryLookup<Enchantment>,
            itemLookup: RegistryEntryLookup<Item>,
        ) {
            registerModifyEnchantment(register, RagiumEnchantments.SMELTING, enchantmentLookup, itemLookup)
            registerModifyEnchantment(register, RagiumEnchantments.SLEDGE_HAMMER, enchantmentLookup, itemLookup)
            registerModifyEnchantment(register, RagiumEnchantments.BUZZ_SAW, enchantmentLookup, itemLookup)
        }

        @JvmStatic
        fun registerEnchantment(
            register: (RegistryKey<Enchantment>, Enchantment) -> Unit,
            key: RegistryKey<Enchantment>,
            builder: Enchantment.Builder,
        ) {
            register(key, builder.build(key.value))
        }

        @JvmStatic
        private fun registerModifyEnchantment(
            register: (RegistryKey<Enchantment>, Enchantment) -> Unit,
            key: RegistryKey<Enchantment>,
            enchantmentLookup: RegistryEntryLookup<Enchantment>,
            itemLookup: RegistryEntryLookup<Item>,
        ) {
            registerEnchantment(
                register,
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
                /*.addEffect(
                    EnchantmentEffectComponentTypes.LOCATION_CHANGED,
                    ReplaceDiskEnchantmentEffect(
                        Clamped(EnchantmentLevelBasedValue.linear(3.0f, 1.0f), 0.0f, 16.0f),
                        EnchantmentLevelBasedValue.constant(1.0f),
                        Vec3i(0, -1, 0),
                        Optional.of(
                            BlockPredicate.allOf(
                                BlockPredicate.matchingBlockTag(Vec3i(0, 1, 0), BlockTags.AIR),
                                BlockPredicate.matchingBlocks(Blocks.LAVA),
                                BlockPredicate.matchingFluids(Fluids.LAVA),
                                BlockPredicate.unobstructed()
                            )
                        ),
                        BlockStateProvider.of(Blocks.MAGMA_BLOCK),
                        Optional.of(GameEvent.BLOCK_PLACE)
                    ),
                    EntityPropertiesLootCondition.builder(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder.create().flags(EntityFlagsPredicate.Builder.create().onGround(true))
                    )
                )*/
            )
        }
    }
}
