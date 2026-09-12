package hiiragi283.ragium.data.worldgen

import hiiragi283.lib.data.worldgen.HTWorldGenData
import hiiragi283.lib.data.worldgen.HTWorldGenHelper
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTOreBlockPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.LakeFeature
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.placement.BiomeFilter
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement
import net.minecraft.world.level.levelgen.placement.InSquarePlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.minecraft.world.level.levelgen.placement.RarityFilter
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.registries.NeoForgeRegistries

data object RagiumWorldData {
    // Lake
    @JvmField
    val CRUDE_OIL_LAKE = HTWorldGenData(RagiumAPI.id("lake", "crude_oil"))

    // Ore
    @JvmField
    val SULFUR_ORE = HTWorldGenData(RagiumAPI.id("ore", "sulfur"))

    @JvmField
    val NETHER_SULFUR_ORE = HTWorldGenData(SULFUR_ORE, RagiumAPI.id("nether_ore", "sulfur"))

    @JvmField
    val NITER_ORE = HTWorldGenData(RagiumAPI.id("ore", "niter"))

    @JvmField
    val NETHER_NITER_ORE = HTWorldGenData(NITER_ORE, RagiumAPI.id("nether_ore", "niter"))

    @Suppress("DEPRECATION")
    @JvmStatic
    fun bootstrap(builder: RegistrySetBuilder) {
        builder
            .add(Registries.CONFIGURED_FEATURE) { context: BootstrapContext<ConfiguredFeature<*, *>> ->
                // Lake
                RagiumFluids.CRUDE_OIL.blockHolder?.get()?.let {
                    HTWorldGenHelper.register(
                        context,
                        CRUDE_OIL_LAKE,
                        Feature.LAKE,
                        LakeFeature.Configuration(
                            BlockStateProvider.simple(it),
                            BlockStateProvider.simple(Blocks.MUD)
                        )
                    )
                }
                // Ore
                fun oreConfiguration(material: RagiumMaterial, size: Int): OreConfiguration = RagiumBlocks.MATERIAL_ORES
                    .column(material)
                    .map { (part: HTOreBlockPart, block: HTSimpleDeferredBlockAndItem) ->
                        val state: BlockState = block.block.defaultState
                        when (part) {
                            HTOreBlockPart.STONE -> BlockTags.STONE_ORE_REPLACEABLES
                            HTOreBlockPart.DEEPSLATE -> BlockTags.DEEPSLATE_ORE_REPLACEABLES
                            HTOreBlockPart.NETHER -> Tags.Blocks.NETHERRACKS
                            HTOreBlockPart.END -> Tags.Blocks.END_STONES
                        }.let { OreConfiguration.target(TagMatchTest(it), state) }
                    }.let { OreConfiguration(it, size) }

                HTWorldGenHelper.register(
                    context,
                    SULFUR_ORE,
                    Feature.ORE,
                    oreConfiguration(RagiumMaterial.Mineral.SULFUR, 7)
                )
                HTWorldGenHelper.register(
                    context,
                    NITER_ORE,
                    Feature.ORE,
                    oreConfiguration(RagiumMaterial.Mineral.NITER, 7)
                )
            }
            .add(Registries.PLACED_FEATURE) { context: BootstrapContext<PlacedFeature> ->
                // Lake
                HTWorldGenHelper.register(
                    context,
                    CRUDE_OIL_LAKE,
                    listOf(
                        RarityFilter.onAverageOnceEvery(200),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                    )
                )
                // Ore
                fun commonOrePlacement(count: Int, range: PlacementModifier): List<PlacementModifier> = listOf(
                    CountPlacement.of(count),
                    InSquarePlacement.spread(),
                    range,
                    BiomeFilter.biome()
                )

                HTWorldGenHelper.register(
                    context,
                    SULFUR_ORE,
                    commonOrePlacement(
                        2,
                        HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.absolute(32))
                    )
                )
                HTWorldGenHelper.register(
                    context,
                    NETHER_SULFUR_ORE,
                    commonOrePlacement(
                        4,
                        HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.top())
                    )
                )
                HTWorldGenHelper.register(
                    context,
                    NITER_ORE,
                    commonOrePlacement(
                        2,
                        HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.absolute(32))
                    )
                )
                HTWorldGenHelper.register(
                    context,
                    NETHER_NITER_ORE,
                    commonOrePlacement(
                        4,
                        HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.top())
                    )
                )
            }
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS) { context: BootstrapContext<BiomeModifier> ->
                // Lake
                HTWorldGenHelper.register(
                    context,
                    CRUDE_OIL_LAKE,
                    BiomeTags.IS_OVERWORLD,
                    GenerationStep.Decoration.LAKES
                )
                // Ore
                HTWorldGenHelper.register(
                    context,
                    SULFUR_ORE,
                    BiomeTags.IS_OVERWORLD,
                    GenerationStep.Decoration.UNDERGROUND_ORES
                )
                HTWorldGenHelper.register(
                    context,
                    NETHER_SULFUR_ORE,
                    BiomeTags.IS_NETHER,
                    GenerationStep.Decoration.UNDERGROUND_ORES
                )
                HTWorldGenHelper.register(
                    context,
                    NITER_ORE,
                    BiomeTags.IS_OVERWORLD,
                    GenerationStep.Decoration.UNDERGROUND_ORES
                )
                HTWorldGenHelper.register(
                    context,
                    NETHER_NITER_ORE,
                    BiomeTags.IS_NETHER,
                    GenerationStep.Decoration.UNDERGROUND_ORES
                )
            }
    }
}
