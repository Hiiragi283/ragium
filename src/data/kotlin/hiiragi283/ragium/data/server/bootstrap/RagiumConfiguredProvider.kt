package hiiragi283.ragium.data.server.bootstrap

import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature

object RagiumConfiguredProvider : RegistrySetBuilder.RegistryBootstrap<ConfiguredFeature<*, *>> {
    override fun run(context: BootstrapContext<ConfiguredFeature<*, *>>) {
        // Ore
        /*register(
            context,
            RagiumWorldGenData.ORE_RAGINITE,
            Feature.ORE,
            OreConfiguration(
                listOf(
                    TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES) to HTOreVariant.OVERWORLD,
                    TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES) to HTOreVariant.DEEPSLATE,
                ).map { (target: RuleTest, variant: HTOreVariant) ->
                    OreConfiguration.target(target, RagiumBlocks.RAGINITE_ORES[variant].get().defaultBlockState())
                },
                16,
            ),
        )*/
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

    /*private fun <FC : FeatureConfiguration> register(
        context: BootstrapContext<ConfiguredFeature<*, *>>,
        data: HTWorldGenData,
        feature: Feature<FC>,
        config: FC,
    ) {
        data.configuredHolder = context.register(data.configuredKey, ConfiguredFeature(feature, config))
    }*/
}
