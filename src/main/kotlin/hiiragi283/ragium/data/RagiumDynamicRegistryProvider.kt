package hiiragi283.ragium.data

import hiiragi283.ragium.api.data.HTMultiblockExporter
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.api.tags.RagiumEnchantmentTags
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumEnchantments
import hiiragi283.ragium.common.init.RagiumFeatures
import hiiragi283.ragium.common.init.RagiumMultiblockPatterns
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.block.Blocks
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.enchantment.Enchantment
import net.minecraft.item.Item
import net.minecraft.registry.RegistryEntryLookup
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.ItemTags
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
        val enchantmentLookup: RegistryEntryLookup<Enchantment> = entries.getLookup(RegistryKeys.ENCHANTMENT)
        val itemLookup: RegistryEntryLookup<Item> = entries.getLookup(RegistryKeys.ITEM)

        Bootstraps.registerEnchantments(entries::add, enchantmentLookup, itemLookup)
        Bootstraps.registerConfigured(entries::add)

        registerPatterns(entries)
        registerPlaced(entries)
    }

    //    Multiblock Pattern    //

    private fun registerPatterns(entries: Entries) {
        // blast furnace
        registerPattern(entries, RagiumMultiblockPatterns.BLAST_FURNACE) { tier: HTMachineTier ->
            addLayer(
                -1..1,
                0,
                1..3,
                HTMultiblockComponent.of(tier.getHull()),
            ).addHollow(
                -1..1,
                1,
                1..3,
                HTMultiblockComponent.of(tier.getCoil()),
            ).addHollow(
                -1..1,
                2,
                1..3,
                HTMultiblockComponent.of(tier.getCoil()),
            ).addLayer(
                -1..1,
                3,
                1..3,
                HTMultiblockComponent.of(tier.getBaseBlock()),
            )
        }
        // distillation tower
        registerPattern(entries, RagiumMultiblockPatterns.DISTILLATION_TOWER) { tier: HTMachineTier ->
            addLayer(
                -1..1,
                -1,
                1..3,
                HTMultiblockComponent.of(tier.getBaseBlock()),
            ).addHollow(
                -1..1,
                0,
                1..3,
                HTMultiblockComponent.of(tier.getHull()),
            ).addCross4(
                -1..1,
                1,
                1..3,
                HTMultiblockComponent.of(Blocks.RED_CONCRETE),
            ).addCross4(
                -1..1,
                2,
                1..3,
                HTMultiblockComponent.of(Blocks.WHITE_CONCRETE),
            ).addCross4(
                -1..1,
                3,
                1..3,
                HTMultiblockComponent.of(Blocks.RED_CONCRETE),
            ).add(
                0,
                4,
                2,
                HTMultiblockComponent.of(Blocks.WHITE_CONCRETE),
            )
        }
        // saw mill
        registerPattern(entries, RagiumMultiblockPatterns.SAW_MILL) { tier: HTMachineTier ->
            add(-1, 0, 0, HTMultiblockComponent.of(tier.getHull()))
            add(1, 0, 0, HTMultiblockComponent.of(tier.getHull()))
            add(-1, 0, 1, HTMultiblockComponent.of(Blocks.STONE_SLAB))
            add(0, 0, 1, HTMultiblockComponent.of(Blocks.STONECUTTER))
            add(1, 0, 1, HTMultiblockComponent.of(Blocks.STONE_SLAB))
            addLayer(-1..1, 0, 2..2, HTMultiblockComponent.of(tier.getHull()))
        }

        // alchemical infuser
        entries.add(
            RagiumMultiblockPatterns.ALCHEMICAL_INFUSER,
            HTMultiblockExporter.create {
                // tiles
                add(-2, -1, -2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILES))
                add(-2, -1, 2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILES))
                add(2, -1, -2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILES))
                add(2, -1, 2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILES))
                // slabs
                add(-2, 0, -2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILE_SLAB))
                add(-2, 0, 2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILE_SLAB))
                add(2, 0, -2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILE_SLAB))
                add(2, 0, 2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILE_SLAB))
                addLayer(-1..1, -1, -2..-2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILE_SLAB))
                    .addLayer(-1..1, -1, 2..2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILE_SLAB))
                    .addLayer(-2..-2, -1, -1..1, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILE_SLAB))
                    .addLayer(2..2, -1, -1..1, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILE_SLAB))
                // obsidian
                add(-1, -1, -1, HTMultiblockComponent.of(Blocks.OBSIDIAN))
                add(-1, -1, 1, HTMultiblockComponent.of(Blocks.OBSIDIAN))
                add(1, -1, -1, HTMultiblockComponent.of(Blocks.OBSIDIAN))
                add(1, -1, 1, HTMultiblockComponent.of(Blocks.OBSIDIAN))
                // crying
                add(-1, -1, 0, HTMultiblockComponent.of(Blocks.CRYING_OBSIDIAN))
                add(0, -1, -1, HTMultiblockComponent.of(Blocks.CRYING_OBSIDIAN))
                add(0, -1, 0, HTMultiblockComponent.of(Blocks.CRYING_OBSIDIAN))
                add(0, -1, 1, HTMultiblockComponent.of(Blocks.CRYING_OBSIDIAN))
                add(1, -1, 0, HTMultiblockComponent.of(Blocks.CRYING_OBSIDIAN))
            },
        )
    }

    private fun registerPattern(
        entries: Entries,
        keyMap: Map<HTMachineTier, RegistryKey<HTMultiblockPattern>>,
        builderAction: HTMultiblockExporter.(HTMachineTier) -> Unit,
    ) {
        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            entries.add(
                keyMap[tier]!!,
                HTMultiblockExporter.create { builderAction(tier) },
            )
        }
    }

    //    Placed Features    //

    private fun registerPlaced(entries: Entries) {
        val lookup: RegistryEntryLookup<ConfiguredFeature<*, *>> = entries.getLookup(RegistryKeys.CONFIGURED_FEATURE)
        /*registerFeature(
            entries,
            lookup,
            RagiumFeatures.DISK_SALT,
            BiomePlacementModifier.of(),
            BlockFilterPlacementModifier.of(BlockPredicate.matchingFluids(Fluids.WATER)),
            PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP,
            SquarePlacementModifier.of()
        )*/
        registerOre(
            entries,
            lookup,
            RagiumFeatures.ORE_RAGINITE,
            HeightRangePlacementModifier.trapezoid(
                YOffset.fixed(-16),
                YOffset.fixed(112),
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
        /*registerFeature(
            entries,
            lookup,
            RagiumFeatures.PATCH_END_OBLIVION_CLUSTER,
            BiomePlacementModifier.of(),
            PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
            RarityFilterPlacementModifier.of(100),
            SquarePlacementModifier.of(),
        )*/
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

    //    Bootstraps    //

    data object Bootstraps {
        //    Enchantment    //

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

        //    Configured Features    //

        @JvmStatic
        fun registerConfigured(register: (RegistryKey<ConfiguredFeature<*, *>>, ConfiguredFeature<*, *>) -> Unit) {
            /*register(
                RagiumFeatures.DISK_SALT.configuredKey,
                ConfiguredFeature(
                    Feature.DISK,
                    DiskFeatureConfig(
                        PredicatedStateProvider.of(RagiumBlocks.SALT_BLOCK),
                        BlockPredicate.matchingBlocks(listOf(Blocks.DIRT, Blocks.GRASS_BLOCK)),
                        UniformIntProvider.create(2, 3),
                        1
                    )
                )
            )*/
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
            /*register(
                RagiumFeatures.PATCH_END_OBLIVION_CLUSTER.configuredKey,
                ConfiguredFeature(
                    Feature.RANDOM_PATCH,
                    ConfiguredFeatures.createRandomPatchFeatureConfig(
                        Feature.SIMPLE_BLOCK,
                        SimpleBlockFeatureConfig(
                            BlockStateProvider.of(RagiumBlocks.OBLIVION_CLUSTER),
                        ),
                    ),
                ),
            )*/
        }

        @JvmStatic
        private fun registerOre(
            register: (RegistryKey<ConfiguredFeature<*, *>>, ConfiguredFeature<*, *>) -> Unit,
            data: RagiumFeatures.Data,
            targets: List<OreFeatureConfig.Target>,
        ) {
            register(
                data.configuredKey,
                ConfiguredFeature(
                    Feature.ORE,
                    OreFeatureConfig(targets, 16),
                ),
            )
        }
    }
}
