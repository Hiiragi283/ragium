package hiiragi283.ragium.data

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFeatures
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.block.Blocks
import net.minecraft.registry.RegistryEntryLookup
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.structure.rule.BlockMatchRuleTest
import net.minecraft.structure.rule.TagMatchRuleTest
import net.minecraft.world.gen.YOffset
import net.minecraft.world.gen.feature.*
import net.minecraft.world.gen.placementmodifier.*
import java.util.concurrent.CompletableFuture

class RagiumDynamicRegistryProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricDynamicRegistryProvider(output, registriesFuture) {
    override fun getName(): String = "Dynamic"

    override fun configure(registries: RegistryWrapper.WrapperLookup, entries: Entries) {
        // val enchantmentLookup: RegistryEntryLookup<Enchantment> = entries.getLookup(RegistryKeys.ENCHANTMENT)
        // val itemLookup: RegistryEntryLookup<Item> = entries.getLookup(RegistryKeys.ITEM)

        // Bootstraps.registerEnchantments(entries::add, enchantmentLookup, itemLookup)
        Bootstraps.registerConfigured(entries::add)

        registerPlaced(entries)
    }

    //    Placed Features    //

    private fun registerPlaced(entries: Entries) {
        val lookup: RegistryEntryLookup<ConfiguredFeature<*, *>> = entries.getLookup(RegistryKeys.CONFIGURED_FEATURE)
        registerFeature(
            entries,
            lookup,
            RagiumFeatures.ORE_ASPHALT,
            CountPlacementModifier.of(2),
            HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(0)),
        )
        registerFeature(
            entries,
            lookup,
            RagiumFeatures.ORE_GYPSUM,
            CountPlacementModifier.of(2),
            HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(0)),
        )
        registerFeature(
            entries,
            lookup,
            RagiumFeatures.ORE_SLATE,
            CountPlacementModifier.of(2),
            HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.getTop()),
        )
        registerOre(
            entries,
            lookup,
            RagiumFeatures.ORE_RAGINITE,
            HeightRangePlacementModifier.trapezoid(
                YOffset.fixed(-48),
                YOffset.fixed(48),
            ),
        )
        registerOre(
            entries,
            lookup,
            RagiumFeatures.ORE_NETHER_RAGINITE,
            PlacedFeatures.TEN_ABOVE_AND_BELOW_RANGE,
        )
        registerOre(
            entries,
            lookup,
            RagiumFeatures.ORE_END_RAGI_CRYSTAL,
            PlacedFeatures.TEN_ABOVE_AND_BELOW_RANGE,
        )
    }

    private fun registerOre(
        entries: Entries,
        lookup: RegistryEntryLookup<ConfiguredFeature<*, *>>,
        data: RagiumFeatures.Data,
        vararg modifiers: PlacementModifier,
    ) {
        registerFeature(
            entries,
            lookup,
            data,
            CountPlacementModifier.of(8),
            BiomePlacementModifier.of(),
            SquarePlacementModifier.of(),
            *modifiers,
        )
    }

    private fun registerFeature(
        entries: Entries,
        lookup: RegistryEntryLookup<ConfiguredFeature<*, *>>,
        data: RagiumFeatures.Data,
        vararg modifiers: PlacementModifier,
    ) {
        entries.add(
            data.featureKey,
            PlacedFeature(
                data.getConfiguredEntry(lookup),
                listOf(*modifiers),
            ),
        )
    }

    //    Bootstraps    //

    data object Bootstraps {
        //    Enchantment    //

        /*@JvmStatic
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
                .addEffect(
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
                )
            )
        }*/

        //    Configured Features    //

        @JvmStatic
        fun registerConfigured(register: (RegistryKey<ConfiguredFeature<*, *>>, ConfiguredFeature<*, *>) -> Unit) {
            registerOre(
                register,
                RagiumFeatures.ORE_ASPHALT,
                listOf(
                    OreFeatureConfig.createTarget(
                        TagMatchRuleTest(BlockTags.BASE_STONE_OVERWORLD),
                        RagiumBlocks.ASPHALT.defaultState,
                    ),
                ),
                64,
            )
            registerOre(
                register,
                RagiumFeatures.ORE_GYPSUM,
                listOf(
                    OreFeatureConfig.createTarget(
                        TagMatchRuleTest(BlockTags.BASE_STONE_OVERWORLD),
                        RagiumBlocks.GYPSUM.defaultState,
                    ),
                ),
                64,
            )
            registerOre(
                register,
                RagiumFeatures.ORE_SLATE,
                listOf(
                    OreFeatureConfig.createTarget(
                        TagMatchRuleTest(BlockTags.BASE_STONE_OVERWORLD),
                        RagiumBlocks.SLATE.defaultState,
                    ),
                ),
                64,
            )
            registerOre(
                register,
                RagiumFeatures.ORE_RAGINITE,
                listOf(
                    OreFeatureConfig.createTarget(
                        TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES),
                        RagiumContents.Ores.CRUDE_RAGINITE.value.defaultState,
                    ),
                    OreFeatureConfig.createTarget(
                        TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES),
                        RagiumContents.Ores.DEEP_RAGINITE.value.defaultState,
                    ),
                ),
            )
            registerOre(
                register,
                RagiumFeatures.ORE_NETHER_RAGINITE,
                listOf(
                    OreFeatureConfig.createTarget(
                        BlockMatchRuleTest(Blocks.NETHERRACK),
                        RagiumContents.Ores.NETHER_RAGINITE.value.defaultState,
                    ),
                ),
            )
            registerOre(
                register,
                RagiumFeatures.ORE_END_RAGI_CRYSTAL,
                listOf(
                    OreFeatureConfig.createTarget(
                        BlockMatchRuleTest(Blocks.END_STONE),
                        RagiumContents.Ores.END_RAGI_CRYSTAL.value.defaultState,
                    ),
                ),
            )
        }

        @JvmStatic
        private fun registerOre(
            register: (RegistryKey<ConfiguredFeature<*, *>>, ConfiguredFeature<*, *>) -> Unit,
            data: RagiumFeatures.Data,
            targets: List<OreFeatureConfig.Target>,
            size: Int = 16,
        ) {
            register(register, data, Feature.ORE, OreFeatureConfig(targets, size))
        }

        @JvmStatic
        private fun <FC : FeatureConfig, F : Feature<FC>> register(
            register: (RegistryKey<ConfiguredFeature<*, *>>, ConfiguredFeature<*, *>) -> Unit,
            data: RagiumFeatures.Data,
            feature: F,
            config: FC,
        ) {
            register(data.configuredKey, ConfiguredFeature(feature, config))
        }
    }
}
