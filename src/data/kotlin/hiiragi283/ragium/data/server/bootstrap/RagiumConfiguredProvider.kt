package hiiragi283.ragium.data.server.bootstrap

import hiiragi283.ragium.api.data.HTWorldGenData
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest

object RagiumConfiguredProvider : RegistrySetBuilder.RegistryBootstrap<ConfiguredFeature<*, *>> {
    override fun run(context: BootstrapContext<ConfiguredFeature<*, *>>) {
        // Ore
        register(
            context,
            RagiumWorldGenData.ORE_RAGINITE,
            Feature.ORE,
            OreConfiguration(
                listOf(
                    OreConfiguration.target(
                        TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES),
                        RagiumBlocks.Ores.RAGINITE
                            .get()
                            .defaultBlockState(),
                    ),
                    OreConfiguration.target(
                        TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES),
                        RagiumBlocks.DeepOres.RAGINITE
                            .get()
                            .defaultBlockState(),
                    ),
                ),
                9,
            ),
        )

        register(
            context,
            RagiumWorldGenData.ORE_RESONANT_DEBRIS,
            Feature.SCATTERED_ORE,
            OreConfiguration(
                TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES),
                RagiumBlocks.RESONANT_DEBRIS.get().defaultBlockState(),
                3,
                1f,
            ),
        )
        // Geode
        /*register(
            context,
            RagiumWorldGenData.GEODE_CRUDE_OIL,
            Feature.GEODE,
            GeodeConfiguration(
                GeodeBlockSettings(
                    BlockStateProvider.simple(RagiumFluidContents.CRUDE_OIL.getBlock()),
                    BlockStateProvider.simple(Blocks.SOUL_SOIL),
                    BlockStateProvider.simple(RagiumBlocks.STICKY_SOUL_SOIL.get()),
                    BlockStateProvider.simple(Blocks.BLACKSTONE),
                    BlockStateProvider.simple(Blocks.SMOOTH_BASALT),
                    listOf(
                        RagiumFluidContents.CRUDE_OIL.getBlock().defaultBlockState(),
                    ),
                    BlockTags.FEATURES_CANNOT_REPLACE,
                    BlockTags.GEODE_INVALID_BLOCKS,
                ),
                GeodeLayerSettings(1.7, 2.2, 3.2, 4.2),
                GeodeCrackSettings(0.95, 2.0, 2),
                0.35,
                0.083,
                true,
                UniformInt.of(4, 6),
                UniformInt.of(3, 4),
                UniformInt.of(1, 2),
                -16,
                16,
                0.05,
                1,
            ),
        )*/
    }

    private fun <FC : FeatureConfiguration> register(
        context: BootstrapContext<ConfiguredFeature<*, *>>,
        data: HTWorldGenData,
        feature: Feature<FC>,
        config: FC,
    ) {
        data.configuredHolder = context.register(data.configuredKey, ConfiguredFeature(feature, config))
    }
}
