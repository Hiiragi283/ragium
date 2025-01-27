package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest
import net.neoforged.neoforge.common.Tags

object RagiumConfiguredFeatures {
    @JvmField
    val OVERWORLD_RAGINITE: ResourceKey<ConfiguredFeature<*, *>> = createKey("overworld_raginite")

    @JvmField
    val NETHER_RAGINITE: ResourceKey<ConfiguredFeature<*, *>> = createKey("nether_raginite")

    @JvmField
    val END_RAGINITE: ResourceKey<ConfiguredFeature<*, *>> = createKey("end_raginite")

    @JvmStatic
    private fun createKey(path: String): ResourceKey<ConfiguredFeature<*, *>> =
        ResourceKey.create(Registries.CONFIGURED_FEATURE, RagiumAPI.id(path))

    @JvmStatic
    fun boostrap(context: BootstrapContext<ConfiguredFeature<*, *>>) {
        fun createTarget(target: TagKey<Block>, ore: RagiumBlocks.Ores): OreConfiguration.TargetBlockState =
            OreConfiguration.target(TagMatchTest(target), ore.get().defaultBlockState())

        fun register(key: ResourceKey<ConfiguredFeature<*, *>>, count: Int, vararg targets: OreConfiguration.TargetBlockState) {
            context.register(
                key,
                ConfiguredFeature(Feature.ORE, OreConfiguration(listOf(*targets), count)),
            )
        }

        // Overworld
        register(
            OVERWORLD_RAGINITE,
            8,
            createTarget(BlockTags.STONE_ORE_REPLACEABLES, RagiumBlocks.Ores.CRUDE_RAGINITE),
            createTarget(BlockTags.DEEPSLATE_ORE_REPLACEABLES, RagiumBlocks.Ores.DEEPSLATE_RAGINITE),
        )
        // Deep
        // Nether
        register(
            NETHER_RAGINITE,
            8,
            createTarget(BlockTags.BASE_STONE_NETHER, RagiumBlocks.Ores.NETHER_RAGINITE),
        )
        // The End
        register(END_RAGINITE, 8, createTarget(Tags.Blocks.END_STONES, RagiumBlocks.Ores.END_RAGI_CRYSTAL))
    }
}
