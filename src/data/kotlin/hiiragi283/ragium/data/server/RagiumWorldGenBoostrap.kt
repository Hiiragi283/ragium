package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumEnchantments
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.placement.BiomeFilter
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.InSquarePlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.BiomeModifiers
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.NeoForgeRegistries

object RagiumWorldGenBoostrap {
    @JvmStatic
    fun createBuilder(): RegistrySetBuilder {
        // Overworld
        addOres(
            "overworld_raginite",
            BiomeTags.IS_OVERWORLD,
            createTarget(BlockTags.STONE_ORE_REPLACEABLES, HTOreVariant.OVERWORLD, RagiumMaterials.RAGINITE),
            createTarget(BlockTags.DEEPSLATE_ORE_REPLACEABLES, HTOreVariant.DEEPSLATE, RagiumMaterials.RAGINITE),
        )
        addOres(
            "overworld_crude_oil",
            BiomeTags.IS_OVERWORLD,
            OreConfiguration.target(
                TagMatchTest(BlockTags.BASE_STONE_OVERWORLD),
                RagiumBlocks.CRUDE_OIL.get().defaultBlockState(),
            ),
            size = 32,
            count = 1,
        )
        // Nether
        addOres(
            "nether_raginite",
            BiomeTags.IS_NETHER,
            createTarget(Tags.Blocks.NETHERRACKS, HTOreVariant.NETHER, RagiumMaterials.RAGINITE),
        )
        // End
        addOres(
            "end_raginite",
            BiomeTags.IS_END,
            createTarget(Tags.Blocks.END_STONES, HTOreVariant.END, RagiumMaterials.RAGI_CRYSTAL),
        )
        return RegistrySetBuilder()
            .add(Registries.ENCHANTMENT) { context: BootstrapContext<Enchantment> ->
                fun register(key: ResourceKey<Enchantment>, builder: Enchantment.Builder) {
                    context.register(key, builder.build(key.location()))
                }

                register(
                    RagiumEnchantments.CAPACITY,
                    Enchantment.enchantment(
                        Enchantment.definition(
                            context.lookup(Registries.ITEM).getOrThrow(RagiumItemTags.CAPACITY_ENCHANTABLE),
                            1,
                            5,
                            Enchantment.constantCost(1),
                            Enchantment.constantCost(41),
                            1,
                            EquipmentSlotGroup.ANY,
                        ),
                    ),
                )
            }.add(Registries.CONFIGURED_FEATURE) { context: BootstrapContext<ConfiguredFeature<*, *>> ->
                configuredBootstrap.forEach {
                    it.run(context)
                }
            }.add(Registries.PLACED_FEATURE) { context: BootstrapContext<PlacedFeature> ->
                placedBootstrap.forEach {
                    it.run(context)
                }
            }.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS) { context: BootstrapContext<BiomeModifier> ->
                modifierBootstrap.forEach {
                    it.run(context)
                }
            }
    }

    private val configuredBootstrap: MutableList<RegistrySetBuilder.RegistryBootstrap<ConfiguredFeature<*, *>>> =
        mutableListOf()
    private val placedBootstrap: MutableList<RegistrySetBuilder.RegistryBootstrap<PlacedFeature>> =
        mutableListOf()
    private val modifierBootstrap: MutableList<RegistrySetBuilder.RegistryBootstrap<BiomeModifier>> =
        mutableListOf()

    @JvmStatic
    private fun addOres(
        path: String,
        targetBiome: TagKey<Biome>,
        vararg targets: OreConfiguration.TargetBlockState,
        size: Int = 8,
        count: Int = 16,
    ) {
        val id: ResourceLocation = RagiumAPI.id(path)
        var configured: Holder<ConfiguredFeature<*, *>>? = null
        // Configured Feature
        configuredBootstrap.add { context: BootstrapContext<ConfiguredFeature<*, *>> ->
            configured = context.register(
                ResourceKey.create(Registries.CONFIGURED_FEATURE, id),
                ConfiguredFeature(Feature.ORE, OreConfiguration(listOf(*targets), size)),
            )
        }
        // Placed Feature
        var placed: Holder<PlacedFeature>? = null
        placedBootstrap.add { context: BootstrapContext<PlacedFeature> ->
            placed = context.register(
                ResourceKey.create(Registries.PLACED_FEATURE, id),
                PlacedFeature(
                    configured!!,
                    listOf(
                        CountPlacement.of(count),
                        InSquarePlacement.spread(),
                        PlacementUtils.RANGE_10_10,
                        BiomeFilter.biome(),
                    ),
                ),
            )
        }
        // Biome Modifier
        modifierBootstrap.add { context: BootstrapContext<BiomeModifier> ->
            val biomeGetter: HolderGetter<Biome> = context.lookup(Registries.BIOME)
            context.register(
                ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, id),
                BiomeModifiers.AddFeaturesBiomeModifier(
                    biomeGetter.getOrThrow(targetBiome),
                    HolderSet.direct(placed!!),
                    GenerationStep.Decoration.UNDERGROUND_ORES,
                ),
            )
        }
    }

    @JvmStatic
    private fun createTarget(target: TagKey<Block>, variant: HTOreVariant, key: HTMaterialKey): OreConfiguration.TargetBlockState {
        val ore: DeferredBlock<out Block> = RagiumBlocks.ORES.get(variant, key) ?: error("Unknown ore: ${key.name} found")
        return OreConfiguration.target(TagMatchTest(target), ore.get().defaultBlockState())
    }
}
