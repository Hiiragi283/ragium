package hiiragi283.ragium.data.server.bootstrap

import hiiragi283.ragium.api.data.HTWorldGenData
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.util.HTOreSets
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest

object RagiumConfiguredProvider : RegistrySetBuilder.RegistryBootstrap<ConfiguredFeature<*, *>> {
    override fun run(context: BootstrapContext<ConfiguredFeature<*, *>>) {
        registerOres(context)
    }

    private fun register(context: BootstrapContext<ConfiguredFeature<*, *>>, data: HTWorldGenData, configured: ConfiguredFeature<*, *>) {
        data.configuredHolder = context.register(data.configuredKey, configured)
    }

    //    Ore    //

    private fun registerOres(context: BootstrapContext<ConfiguredFeature<*, *>>) {
        fun register(
            data: HTWorldGenData,
            oreSet: HTOreSets,
            size: Int,
            vararg pairs: Pair<RuleTest, HTOreVariant>,
        ) {
            register(
                context,
                data,
                ConfiguredFeature(
                    Feature.ORE,
                    OreConfiguration(
                        pairs.map { (target: RuleTest, variant: HTOreVariant) ->
                            OreConfiguration.target(target, oreSet[variant].get().defaultBlockState())
                        },
                        size,
                    ),
                ),
            )
        }

        register(
            RagiumWorldGenData.RAGINITE_ORE,
            RagiumBlocks.RAGINITE_ORES,
            16,
            TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES) to HTOreVariant.OVERWORLD,
            TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES) to HTOreVariant.DEEPSLATE,
        )
    }
}
